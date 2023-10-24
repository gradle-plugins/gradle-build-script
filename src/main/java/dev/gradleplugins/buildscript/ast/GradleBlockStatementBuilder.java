package dev.gradleplugins.buildscript.ast;

import dev.gradleplugins.buildscript.ast.statements.GradleBlockStatement;
import dev.gradleplugins.buildscript.ast.statements.Statement;
import dev.gradleplugins.buildscript.ast.type.Type;
import dev.gradleplugins.buildscript.syntax.normalizer.UseGetterTransformer;

public class GradleBlockStatementBuilder implements Statement {
    private final GradleBlockStatement delegate;

    public GradleBlockStatementBuilder(GradleBlockStatement delegate) {
        this.delegate = delegate;
    }

    // Convert delegate expression into it expression then it expression into getter transformed selector and body unpacked
    public Statement useGetter() {
        return new UseGetterTransformer().visit(delegate);
    }

    // Convert delegate expression into it expression
    public GradleBlockStatement useExplicitIt() {
//        return new UseExplicitItTransformer().visit(delegate);
        throw new UnsupportedOperationException();
    }

    // Convert delegate expression into it expression then it expression into a named parameter
    public GradleBlockStatement useExplicitIt(String name) {
//        return new UseExplicitItTransformer(name).visit(delegate);
        throw new UnsupportedOperationException();
    }

    // Convert delegate expression into it expression then it expression into a named/typed parameter
    public GradleBlockStatement useExplicitIt(String name, Type type) {
//        return new UseExplicitItTransformer(name, type).visit(delegate);
        throw new UnsupportedOperationException();
    }

    @Override
    public <ReturnType> ReturnType accept(Visitor<ReturnType> visitor) {
        return delegate.accept(visitor);
    }

//    private final class UseExplicitItTransformer implements ASTTransformer {
//        private final String name;
//        private final Type type;
//
//        public UseExplicitItTransformer() {
//            this("it", unknownType());
//        }
//
//        public UseExplicitItTransformer(String name) {
//            this(name, unknownType());
//        }
//
//        public UseExplicitItTransformer(String name, Type type) {
//            this.name = name;
//            this.type = type;
//        }
//
//        // TODO: present named paraemter to the lambda
//
//
//        public GradleBlockStatement visit(GradleBlockStatement blockStatement) {
//            final Node body = blockStatement.getBody().getContent().accept(new Node.Visitor<Node>() {
//                @Override
//                public Node visit(Statement statement) {
//                    return statement.accept(new ReplaceItExpressionTransformer(blockStatement.getBody().getItExpression(), new LiteralExpression(type, name)));
//                }
//
//                @Override
//                public Node visit(Expression expression) {
//                    return expression.accept(new ReplaceItExpressionTransformer(blockStatement.getBody().getItExpression(), new LiteralExpression(type, name)));
//                }
//
//                @Override
//                public Node visit(Comment comment) {
//                    throw new UnsupportedOperationException();
//                }
//            });
//
//            if (name.equals("it")) {
//                // Implicit it parameter but explicit it usage, is that even legal?
//                return new GradleBlockStatement(delegate.getSelector(), new GradleBlockStatement.Body(Collections.emptyList(), new ItExpression(), body));
//            } else {
//                return new GradleBlockStatement(delegate.getSelector(), new GradleBlockStatement.Body(Collections.singletonList(new Parameter(type, name)), new ItExpression(), body));
//            }
//        }
//    }
//
//    private static final class SelectorToGetter implements ASTTransformer {
//        @Override
//        public Expression visit(MethodCallExpression expression) {
//            throw new UnsupportedOperationException();
//        }
//
//        @Override
//        public Expression visit(LiteralExpression expression) {
//            return plainProperty(expression.get().toString());
//        }
//
//        @Override
//        public Expression visit(QualifiedExpression expression) {
//            return new QualifiedExpression(expression.getLeftExpression(), expression.getRightExpression().accept(this));
//        }
//    }
//
//    private static final class UseGetterTransformer implements ASTTransformer {
//        public Statement visit(GradleBlockStatement blockStatement) {
//            assert blockStatement.getBody().delegate().getParameters().isEmpty(); // assume implicit it
//            Expression selectorExpression = blockStatement.getSelector().accept(new SelectorToGetter());
//            return MultiStatement.of(Collections.singletonList(blockStatement.getBody().delegate().getBody().accept(new Node.Visitor<Statement>() {
//                @Override
//                public Statement visit(Statement statement) {
//                    return statement.accept(new ReplaceDelegateExpressionTransformer(blockStatement.getBody().getDelegateExpression(), selectorExpression)).accept(new ReplaceItExpressionTransformer(blockStatement.getBody().getItExpression(), selectorExpression));
//                }
//
//                @Override
//                public Statement visit(Expression expression) {
//                    return new ExpressionStatement(expression.accept(new ReplaceDelegateExpressionTransformer(blockStatement.getBody().getDelegateExpression(), selectorExpression)).accept(new ReplaceItExpressionTransformer(blockStatement.getBody().getItExpression(), selectorExpression)));
//                }
//
//                @Override
//                public Statement visit(Comment comment) {
//                    throw new UnsupportedOperationException();
//                }
//            })));
//        }
//    }
//
//    private static final class ReplaceDelegateExpressionTransformer implements ASTTransformer {
//        private final Expression delegateExpression;
//        private final Expression replacementExpression;
//
//        private ReplaceDelegateExpressionTransformer(Expression delegateExpression, Expression replacementExpression) {
//            this.delegateExpression = delegateExpression;
//            this.replacementExpression = replacementExpression;
//        }
//
//        @Override
//        public Expression visit(DelegateExpression expression) {
//            if (expression.equals(delegateExpression)) {
//                return replacementExpression;
//            }
//            return expression;
//        }
//    }
//
//    private static final class ReplaceItExpressionTransformer implements ASTTransformer {
//        private final Expression itExpression;
//        private final Expression replacementExpression;
//
//        private ReplaceItExpressionTransformer(Expression itExpression, Expression replacementExpression) {
//            this.itExpression = itExpression;
//            this.replacementExpression = replacementExpression;
//        }
//
//        @Override
//        public Expression visit(ItExpression expression) {
//            if (expression.equals(itExpression)) {
//                return replacementExpression;
//            }
//            return expression;
//        }
//    }
}
