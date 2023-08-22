package dev.gradleplugins.buildscript.syntax;

import dev.gradleplugins.buildscript.ast.Node;
import dev.gradleplugins.buildscript.ast.comments.Comment;
import dev.gradleplugins.buildscript.ast.expressions.AssignmentExpression;
import dev.gradleplugins.buildscript.ast.expressions.BooleanLiteralExpression;
import dev.gradleplugins.buildscript.ast.expressions.CastingExpression;
import dev.gradleplugins.buildscript.ast.expressions.CurrentScopeExpression;
import dev.gradleplugins.buildscript.ast.expressions.EnclosedExpression;
import dev.gradleplugins.buildscript.ast.expressions.Expression;
import dev.gradleplugins.buildscript.ast.expressions.FieldAccessExpression;
import dev.gradleplugins.buildscript.ast.expressions.GroovyDslLiteral;
import dev.gradleplugins.buildscript.ast.expressions.InfixExpression;
import dev.gradleplugins.buildscript.ast.expressions.ItExpression;
import dev.gradleplugins.buildscript.ast.expressions.LambdaExpression;
import dev.gradleplugins.buildscript.ast.expressions.LiteralExpression;
import dev.gradleplugins.buildscript.ast.expressions.MapLiteralExpression;
import dev.gradleplugins.buildscript.ast.expressions.MethodCallExpression;
import dev.gradleplugins.buildscript.ast.expressions.NullLiteralExpression;
import dev.gradleplugins.buildscript.ast.expressions.PostfixExpression;
import dev.gradleplugins.buildscript.ast.expressions.PrefixExpression;
import dev.gradleplugins.buildscript.ast.expressions.QualifiedExpression;
import dev.gradleplugins.buildscript.ast.expressions.StringLiteralExpression;
import dev.gradleplugins.buildscript.ast.expressions.TernaryExpression;
import dev.gradleplugins.buildscript.ast.expressions.TypeComparisonExpression;
import dev.gradleplugins.buildscript.ast.expressions.TypeExpression;
import dev.gradleplugins.buildscript.ast.expressions.VariableDeclarationExpression;
import dev.gradleplugins.buildscript.ast.expressions.VariableDeclarator;
import dev.gradleplugins.buildscript.ast.statements.AssertStatement;
import dev.gradleplugins.buildscript.ast.statements.CommentedStatement;
import dev.gradleplugins.buildscript.ast.statements.ExpressionStatement;
import dev.gradleplugins.buildscript.ast.statements.GradleBlockStatement;
import dev.gradleplugins.buildscript.ast.statements.GroupStatement;
import dev.gradleplugins.buildscript.ast.statements.ImportDeclaration;
import dev.gradleplugins.buildscript.ast.statements.Statement;
import dev.gradleplugins.buildscript.blocks.ApplyStatement;
import dev.gradleplugins.buildscript.syntax.Syntax.Content;
import dev.gradleplugins.buildscript.syntax.normalizer.UseExplicitItTransformer;

import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static dev.gradleplugins.buildscript.ast.expressions.CurrentScopeExpression.current;
import static dev.gradleplugins.buildscript.ast.type.ReferenceType.stringType;
import static dev.gradleplugins.buildscript.ast.type.UnknownType.unknownType;

public final class JavaRender implements RenderableSyntax.Renderer {
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
            return Content.of(StreamSupport.stream(statement.spliterator(), false).map(it -> it.accept(this)).map(Content::toString).collect(Collectors.joining("\n")));
        }

        private String render(Expression expression) {
            return expression.accept(this).toString();
        }

        @Override
        public Content visit(ItExpression expression) {
            return Content.of("it");
        }

        public Content visit(TypeComparisonExpression expression) {
            switch (expression.getComparisonType()) {
                case INSTANCE_OF: return Content.of(render(expression.getExpression()) + " instanceof " + expression.getInstanceType());
                default: throw invalidLanguageNode();
            }
        }

        public Content visit(CastingExpression expression) {
            switch (expression.getCastingType()) {
                case C_STYLE: Content.of("(" + expression.getType() + ") " + render(expression.getExpression()));
                default: throw invalidLanguageNode();
            }
        }

        public Content visit(BooleanLiteralExpression expression) {
            return Content.of(String.valueOf(expression.get()));
        }

        public Content visit(AssertStatement statement) {
            final StringBuilder builder = new StringBuilder();
            builder.append("assert").append(" ");
            builder.append(render(statement.getCheck()));
            if (statement.getMessage() != null) {
                builder.append(" : ").append(render(statement.getMessage()));
            }
            builder.append(";");
            return Content.of(builder.toString());
        }

        @Override
        public Content visit(GroovyDslLiteral statement) {
            throw new UnsupportedOperationException("cannot render Groovy DSL");
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
            return Content.of(render(statement.getExpression()) + ";");
        }

        public Content visit(VariableDeclarationExpression expression) {
            final StringBuilder builder = new StringBuilder();
            builder.append(expression.getModifiers().stream().map(it -> it.getKeyword().toString()).collect(Collectors.joining(" ")));
            builder.append(" ");
            builder.append(expression.getType());
            builder.append(" ");
            builder.append(expression.getVariables().stream().map(this::render).collect(Collectors.joining(", ")));
            return Content.of(builder.toString().trim());
        }

        public Content visit(VariableDeclarator expression) {
            return Content.of(expression.getVariableName() + " " + render(expression.getInitializer()));
        }

        public Content visit(AssignmentExpression expression) {
            return Content.of("= " + render(expression.getValue()));
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

        @Override
        public Content visit(TypeExpression expression) {
            return Content.of(expression.getType().toString());
        }

        public Content visit(FieldAccessExpression expression) {
            return Content.of(render(expression.getScope()) + "." + expression.getName());
        }

        @Override
        public Content visit(QualifiedExpression expression) {
            final StringBuilder builder = new StringBuilder();
            if (!(expression.getLeftExpression() instanceof CurrentScopeExpression)) {
                builder.append(render(expression.getLeftExpression()));
                builder.append(".");
            }
            builder.append(render(expression.getRightExpression()));
            return Content.of(builder.toString());
        }

        public Content visit(GradleBlockStatement statement) {
            statement = (GradleBlockStatement) statement.accept(new UseExplicitItTransformer());
            LambdaExpression bodyExpression = statement.getBody();
//            if (bodyExpression.getParameters() instanceof LambdaExpressionEx.SingleImplicitParameter) {
//                bodyExpression = (LambdaExpression) new ASTTransformer() {
//                    @Override
//                    public Expression visit(ItExpression expression) {
//                        return literal("it");
//                    }
//                }.visit(new LambdaExpression(Collections.singletonList(new Parameter(unknownType(), "it")), statement.getBody().getBody()));
//            }
            if (statement.getSelector() instanceof MethodCallExpression) {
                return visit(new ExpressionStatement(new MethodCallExpression(((MethodCallExpression) statement.getSelector()).getObjectExpression(), Stream.concat(((MethodCallExpression) statement.getSelector()).getArguments().stream(), Stream.of(bodyExpression)).collect(Collectors.toList()))));
            } else { // assume literal
                return visit(new ExpressionStatement(new MethodCallExpression(current(), render(statement.getSelector()), Collections.singletonList(bodyExpression))));
            }
        }

        @Override
        public Content visit(LambdaExpression expression) {
            final Content inner = expression.getBody().map(it -> it.accept(new Node.Visitor<Content>() {
                @Override
                public Content visit(Statement statement) {
                    return statement.accept(JavaRender.Render.this);
                }

                @Override
                public Content visit(Expression expression) {
                    return expression.accept(JavaRender.Render.this);
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
                return it.getType() + " " + it.getName();
            }).collect(Collectors.joining(", "));

            if (parameters.isEmpty()) {
                if (expression.getParameters() instanceof LambdaExpression.SingleImplicitParameter) {
                    parameters = "it";
                } else {
                    parameters = "()";
                }
            } else if (expression.getParameters().count() > 1 || expression.getParameters().stream().anyMatch(it -> !it.getType().equals(unknownType()))) {
                parameters = "(" + parameters + ")";
            }

            if (inner.isEmpty()) {
                return Content.of(parameters + " -> {}");
            } else if (inner.hasSingleLine()) {
                if (expression.getBody().orElse(null) instanceof Expression) {
                    return Content.of(parameters + " -> " + inner);
                } else {
                    return Content.of(parameters + " -> { " + inner + " }");
                }
            } else {
                return Content.builder().add(parameters + " -> {").add(inner.indent()).add("}").build();
            }
        }

        public Content visit(NullLiteralExpression expression) {
            return Content.of("null");
        }

        @Override
        public Content visit(InfixExpression expression) {
            return Content.of(render(expression.getLeftExpression()) + " " + expression.getOperator() + " " + render(expression.getRightExpression()));
        }

        @Override
        public Content visit(TernaryExpression expression) {
            return Content.of(render(expression.getCondition()) + " ? " + render(expression.getTrueExpression()) + " : " + render(expression.getFalseExpression()));
        }

        @Override
        public Content visit(PrefixExpression expression) {
            return Content.of(expression.getOperator() + render(expression.getExpression()));
        }

        @Override
        public Content visit(PostfixExpression expression) {
            return Content.of(render(expression.getExpression()) + expression.getOperator());
        }
    }
}
