package dev.gradleplugins.buildscript.syntax;

import dev.gradleplugins.buildscript.ast.Node;
import dev.gradleplugins.buildscript.ast.comments.Comment;
import dev.gradleplugins.buildscript.ast.expressions.*;
import dev.gradleplugins.buildscript.ast.statements.*;
import dev.gradleplugins.buildscript.ast.type.ValType;
import dev.gradleplugins.buildscript.ast.type.VarType;
import dev.gradleplugins.buildscript.blocks.ApplyStatement;
import dev.gradleplugins.buildscript.blocks.PluginsDslBlock;
import dev.gradleplugins.buildscript.syntax.Syntax.Content;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static dev.gradleplugins.buildscript.ast.expressions.CurrentScopeExpression.current;
import static dev.gradleplugins.buildscript.ast.type.ReferenceType.stringType;
import static dev.gradleplugins.buildscript.ast.type.UnknownType.unknownType;
import static dev.gradleplugins.buildscript.syntax.Syntax.bool;
import static dev.gradleplugins.buildscript.syntax.Syntax.string;

public final class KotlinRender implements RenderableSyntax.Renderer {
    @Override
    public Content render(Node node) {
        return node.accept(new Node.Visitor<Content>() {
            @Override
            public Content visit(Statement statement) {
                return statement.accept(new Render());
            }

            @Override
            public Content visit(Expression expression) {
                return expression.accept(new Render());
            }

            @Override
            public Content visit(Comment comment) {
                throw new UnsupportedOperationException();
            }
        });
    }

    private static final class Render extends AbstractRender {
        public Syntax.Content visit(CommentedStatement<?> statement) {
            return statement.getCommentedStatement().accept(this).commented();
        }

        public Content visit(StringLiteralExpression expression) {
            return Content.of("\"" + expression.get() + "\"");
        }

        public Content visit(LiteralExpression expression) {
            return Content.of(expression.get().toString());
        }

        public Content visit(GroupStatement statement) {
            return Content.of(StreamSupport.stream(statement.spliterator(), false).map(this::render).collect(Collectors.joining("\n")));
        }

		public Content visit(KotlinDslLiteral expression) {
			final Content.Builder builder = Content.builder();
			expression.forEach(builder::add);
			return builder.build();
		}

        private String render(Statement statement) {
            return statement.accept(this).toString();
        }

        private String render(Expression expression) {
            return expression.accept(this).toString();
        }

        public Content visit(MapLiteralExpression expression) {
            if (expression.isEmpty()) {
                return Content.of("emptyMap()");
            } else {
                final List<String> entries = new ArrayList<>();
                expression.forEach((key, value) -> {
                    entries.add(key + " to " + render(value));
                });

                final StringBuilder builder = new StringBuilder();
                builder.append("mapOf(");
                builder.append(String.join(", ", entries));
                builder.append(")");

                return Content.of(builder.toString());
            }
        }

        public Content visit(TypeComparisonExpression expression) {
            switch (expression.getComparisonType()) {
                case INSTANCE_OF: return Content.of(render(expression.getExpression()) + " is " + expression.getInstanceType());
                case IS: return Content.of(render(expression.getExpression()) + " is " + expression.getInstanceType());
                case SAFE_IS: return Content.of(render(expression.getExpression()) + " is? " + expression.getInstanceType());
                default: throw invalidLanguageNode();
            }
        }

        public Content visit(BooleanLiteralExpression expression) {
            return Content.of(String.valueOf(expression.get()));
        }

        public Content visit(AssertStatement statement) {
            final StringBuilder builder = new StringBuilder();
            builder.append("assert").append("(");
            builder.append(render(statement.getCheck()));
            if (statement.getMessage() != null) {
                builder.append(", ");
                builder.append("{").append(render(statement.getMessage())).append("}");
            }
            builder.append(")");
            return Content.of(builder.toString());
        }

        public Content visit(ImportDeclaration statement) {
            switch (statement.getImportType()) {
                case STATIC: return Content.of("import static " + statement.getName());
                case TYPE: return Content.of("import " + statement.getName());
                default: throw invalidLanguageNode();
            }
        }

        public Content visit(MethodCallExpression expression) {
            return Content.of(render(expression.getObjectExpression()) + "(" + expression.getArguments().stream().map(this::render).collect(Collectors.joining(", ")) + ")");
        }

        public Content visit(ExpressionStatement statement) {
            return Content.of(render(statement.getExpression()));
        }

        public Content visit(VariableDeclarationExpression expression) {
            final StringBuilder builder = new StringBuilder();
            if (expression.getType().getClass().equals(VarType.class)) {
                builder.append("var ");
            } else if (expression.getType().getClass().equals(ValType.class)) {
                builder.append("val ");
            }
            builder.append(expression.getVariables().stream().map(this::render).collect(Collectors.joining(", ")));

            return Content.of(builder.toString());
        }

        public Content visit(VariableDeclarator expression) {
            return Content.of(expression.getVariableName() + " " + render(expression.getInitializer()));
        }

        public Content visit(AssignmentExpression expression) {
            return Content.of("= " + render(expression.getValue()));
        }

        public Content visit(DelegationSpecifier expression) {
            return Content.of("by " + render(expression.getIdentifier()));
        }

        public Content visit(ApplyStatement statement) {
            Expression argument = null;
            if (statement.getNotation() instanceof ApplyStatement.PluginNotation) {
                argument = new MapLiteralExpression(stringType(), Collections.singletonMap("plugin", ((ApplyStatement.PluginNotation) statement.getNotation()).getPluginExpression()));
            } else if (statement.getNotation() instanceof ApplyStatement.FromNotation) {
                argument = new MapLiteralExpression(stringType(), Collections.singletonMap("from", new StringLiteralExpression(((ApplyStatement.FromNotation) statement.getNotation()).getPath())));
            }

            assert argument != null;

            return visit(new ExpressionStatement(new MethodCallExpression(null /*delegate*/, "apply", Collections.singletonList(argument))));
        }

        public Content visit(EnclosedExpression expression) {
            return Content.of("(" + render(expression.getInner()) + ")");
        }

        public Content visit(FieldAccessExpression expression) {
            return Content.of(render(expression.getScope()) + "." + expression.getName());
        }

        public Content visit(QualifiedExpression expression) {
            final StringBuilder builder = new StringBuilder();
            if (!(expression.getLeftExpression() instanceof CurrentScopeExpression) && !(expression.getLeftExpression() instanceof DelegateExpression)) {
                builder.append(render(expression.getLeftExpression()));
                builder.append(".");
            }
            builder.append(render(expression.getRightExpression()));
            return Content.of(builder.toString());
        }

        public Content visit(GradleBlockStatement statement) {
            return Content.of(render(statement.getSelector()) + " " + render(statement.getBody()));
        }

        public Content visit(MultiStatement statement) {
            final Content.Builder builder = Content.builder();
            statement.forEach(it -> builder.add(it.accept(this)));
            return builder.build();
        }

        public Content visit(NullLiteralExpression expression) {
            return Content.of("null");
        }

        public Content visit(SetLiteralExpression expression) {
            if (expression.isEmpty()) {
                return Content.of("emptySet()");
            } else {
                final StringBuilder builder = new StringBuilder();

                builder.append("setOf(");
                builder.append(StreamSupport.stream(expression.getElements().spliterator(), false).map(this::render).collect(Collectors.joining(", ")));
                builder.append(")");

                return Content.of(builder.toString());
            }
        }

		public Content visit(CollectionLiteralExpression expression) {
			if (expression.getExpressions().isEmpty()) {
				return Content.of("emptyList()");
			} else {
				final StringBuilder builder = new StringBuilder();

				builder.append("listOf(");
				builder.append(StreamSupport.stream(expression.getExpressions().spliterator(), false).map(this::render).collect(Collectors.joining(", ")));
				builder.append(")");

				return Content.of(builder.toString());
			}
		}

        @Override
        public Content visit(InfixExpression expression) {
            return Content.of(render(expression.getLeftExpression()) + " " + expression.getOperator() + " " + render(expression.getRightExpression()));
        }

        public Content visit(PluginsDslBlock.IdStatement statement) {
            final StringBuilder builder = new StringBuilder();
            if (statement.shouldUseKotlinAccessor()) {
                builder.append("`").append(statement.getPluginId()).append("`");
            } else {
                builder.append(render(new MethodCallExpression(current(), "id", Collections.singletonList(string(statement.getPluginId())))));
            }

            if (statement.getVersion() != null) {
                builder.append(" ").append(render(new MethodCallExpression(current(), "version", Collections.singletonList(string(statement.getVersion())))));
            }

            if (!statement.shouldApply()) {
                builder.append(" ").append(render(new MethodCallExpression(current(), "apply", Collections.singletonList(bool(false)))));
            }
            return Content.of(builder.toString());
        }

        @Override
        public Content visit(ItExpression expression) {
            return Content.of("it");
        }

        @Override
        public Content visit(LambdaExpression expression) {
            final Content inner = expression.getBody().map(it -> it.accept(new Node.Visitor<Content>() {
                @Override
                public Content visit(Statement statement) {
                    return statement.accept(KotlinRender.Render.this);
                }

                @Override
                public Content visit(Expression expression) {
                    return expression.accept(KotlinRender.Render.this);
                }

                @Override
                public Content visit(Comment comment) {
                    throw new UnsupportedOperationException();
                }
            })).orElse(Content.empty());

            String parameters = expression.getParameters().stream().map(it -> {
                if (it.getType().equals(unknownType())) {
                    return it.getName();
                }
                return it.getName() + ": " + it.getType();
            }).collect(Collectors.joining(", "));
            if (expression.getParameters() instanceof LambdaExpression.SingleImplicitParameter) {
                parameters = "";
            } else if (expression.getParameters().count() == 0) {
                parameters = " ->";
            } else if (!parameters.isEmpty()) {
                parameters = " " + parameters + " ->";
            }

            if (inner.isEmpty()) {
                return Content.of("{}");
            } else if (inner.hasSingleLine()) {
                return Content.of("{" + parameters + " " + inner + " }");
            } else {
                return Content.builder().add("{" + parameters).add(inner.indent()).add("}").build();
            }
        }

        @Override
        public Content visit(StringInterpolationExpression expression) {
            final StringBuilder builder = new StringBuilder();
            builder.append('"');
            for (Expression e : expression) {
                if (e instanceof StringLiteralExpression) {
                    builder.append(((StringLiteralExpression) e).get());
                } else {
                    builder.append("${").append(render(e)).append("}");
                }
            }
            builder.append('"');
            return Content.of(builder.toString());
        }

        @Override
        public Content visit(SafeNavigationExpression expression) {
            return Content.of(render(expression.getObjectExpression()) + "?." + render(expression.getPropertyExpression()));
        }

        @Override
        public Content visit(CastingExpression expression) {
            switch (expression.getCastingType()) {
                case SAFE_AS: return Content.of(render(expression.getExpression()) + " as? " + expression.getType());
                case AS: return Content.of(render(expression.getExpression()) + " as " + expression.getType());
                default: throw invalidLanguageNode();
            }
        }

        @Override
        public Content visit(TernaryExpression expression) {
            return Content.of("if " + render(expression.getCondition()) + " then " + render(expression.getTrueExpression()) + " else " + render(expression.getFalseExpression()));
        }

        @Override
        public Content visit(PrefixExpression expression) {
            return Content.of(expression.getOperator() + render(expression.getExpression()));
        }

        @Override
        public Content visit(PostfixExpression expression) {
            return Content.of(render(expression.getExpression()) + expression.getOperator());
        }

		@Override
		public Content visit(ClassLiteralExpression expression) {
			return Content.of(expression.get().toString() + "::class.java");
		}
	}
}
