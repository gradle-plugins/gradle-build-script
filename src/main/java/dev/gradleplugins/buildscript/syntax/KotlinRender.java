package dev.gradleplugins.buildscript.syntax;

import dev.gradleplugins.buildscript.ast.Node;
import dev.gradleplugins.buildscript.ast.comments.Comment;
import dev.gradleplugins.buildscript.ast.expressions.AsExpression;
import dev.gradleplugins.buildscript.ast.expressions.AssignmentExpression;
import dev.gradleplugins.buildscript.ast.expressions.BooleanLiteralExpression;
import dev.gradleplugins.buildscript.ast.expressions.CurrentScopeExpression;
import dev.gradleplugins.buildscript.ast.expressions.DelegationSpecifier;
import dev.gradleplugins.buildscript.ast.expressions.EnclosedExpression;
import dev.gradleplugins.buildscript.ast.expressions.Expression;
import dev.gradleplugins.buildscript.ast.expressions.FieldAccessExpression;
import dev.gradleplugins.buildscript.ast.expressions.InfixExpression;
import dev.gradleplugins.buildscript.ast.expressions.InstanceOfExpression;
import dev.gradleplugins.buildscript.ast.expressions.ItExpression;
import dev.gradleplugins.buildscript.ast.expressions.LambdaExpression;
import dev.gradleplugins.buildscript.ast.expressions.LiteralExpression;
import dev.gradleplugins.buildscript.ast.expressions.MapLiteralExpression;
import dev.gradleplugins.buildscript.ast.expressions.MethodCallExpression;
import dev.gradleplugins.buildscript.ast.expressions.NullLiteralExpression;
import dev.gradleplugins.buildscript.ast.expressions.PostfixExpression;
import dev.gradleplugins.buildscript.ast.expressions.PrefixExpression;
import dev.gradleplugins.buildscript.ast.expressions.QualifiedExpression;
import dev.gradleplugins.buildscript.ast.expressions.SafeAsExpression;
import dev.gradleplugins.buildscript.ast.expressions.SafeNavigationExpression;
import dev.gradleplugins.buildscript.ast.expressions.SetLiteralExpression;
import dev.gradleplugins.buildscript.ast.expressions.StringInterpolationExpression;
import dev.gradleplugins.buildscript.ast.expressions.StringLiteralExpression;
import dev.gradleplugins.buildscript.ast.expressions.TernaryExpression;
import dev.gradleplugins.buildscript.ast.expressions.VariableDeclarationExpression;
import dev.gradleplugins.buildscript.ast.expressions.VariableDeclarator;
import dev.gradleplugins.buildscript.ast.statements.AssertStatement;
import dev.gradleplugins.buildscript.ast.statements.CommentedStatement;
import dev.gradleplugins.buildscript.ast.statements.ExpressionStatement;
import dev.gradleplugins.buildscript.ast.statements.GradleBlockStatement;
import dev.gradleplugins.buildscript.ast.statements.GroupStatement;
import dev.gradleplugins.buildscript.ast.statements.MultiStatement;
import dev.gradleplugins.buildscript.ast.statements.Statement;
import dev.gradleplugins.buildscript.ast.statements.StaticImportDeclaration;
import dev.gradleplugins.buildscript.ast.statements.TypeImportDeclaration;
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

        public Content visit(InstanceOfExpression expression) {
            return Content.of(render(expression.getExpression()) + " is " + expression.getInstanceType());
        }

        public Content visit(AsExpression expression) {
            return Content.of(render(expression.getExpression()) + " as " + expression.getType());
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

        public Content visit(TypeImportDeclaration statement) {
            return Content.of("import " + statement.getName());
        }

        public Content visit(StaticImportDeclaration statement) {
            return Content.of("import static " + statement.getName());
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
            if (!(expression.getLeftExpression() instanceof CurrentScopeExpression)) {
                builder.append(render(expression.getLeftExpression()));
                builder.append(".");
            }
            builder.append(render(expression.getRightExpression()));
            return Content.of(builder.toString());
        }

        public Content visit(GradleBlockStatement statement) {
            final Content.Builder contentBuilder = Content.builder();
            statement.getBlock().forEach(it -> contentBuilder.add(it.accept(this)));
            Content inner = contentBuilder.build();

            final Content.Builder builder = Content.builder();

            if (inner.isEmpty()) {
                builder.add(render(statement.getSelector()) + " {}");
            } else if (inner.hasSingleLine()) {
                builder.add(render(statement.getSelector()) + " { " + inner + " }");
            } else {
                builder.add(render(statement.getSelector()) + " {");
                builder.add(inner.indent());
                builder.add("}");
            }
            return builder.build();
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

        @Override
        public Content visit(InfixExpression expression) {
            return Content.of(render(expression.getLeftExpression()) + " " + expression.getOperator() + " " + render(expression.getRightExpression()));
        }

        @Override
        public Content visit(PluginsDslBlock.IdStatement statement) {
            return new MethodCallExpression(current(), "id", Collections.singletonList(string(statement.getPluginId()))).accept(this);
        }

        @Override
        public Content visit(ItExpression expression) {
            return Content.of("it");
        }

        @Override
        public Content visit(LambdaExpression expression) {
            return Content.of("{ " + expression.getBody().accept(new Node.Visitor<String>() {
                @Override
                public String visit(Statement statement) {
                    return statement.accept(Render.this).toString();
                }

                @Override
                public String visit(Expression expression) {
                    return expression.accept(Render.this).toString();
                }

                @Override
                public String visit(Comment comment) {
                    throw new UnsupportedOperationException();
                }
            }) + " }");
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
        public Content visit(SafeAsExpression expression) {
            return Content.of(render(expression.getExpression()) + " as? " + expression.getType());
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
    }
}
