package dev.gradleplugins.buildscript.syntax.normalizer;

import dev.gradleplugins.buildscript.ast.Node;
import dev.gradleplugins.buildscript.ast.comments.Comment;
import dev.gradleplugins.buildscript.ast.expressions.DelegateExpression;
import dev.gradleplugins.buildscript.ast.expressions.Expression;
import dev.gradleplugins.buildscript.ast.expressions.ItExpression;
import dev.gradleplugins.buildscript.ast.expressions.LambdaExpression;
import dev.gradleplugins.buildscript.ast.expressions.LiteralExpression;
import dev.gradleplugins.buildscript.ast.expressions.MethodCallExpression;
import dev.gradleplugins.buildscript.ast.expressions.QualifiedExpression;
import dev.gradleplugins.buildscript.ast.statements.ExpressionStatement;
import dev.gradleplugins.buildscript.ast.statements.GradleBlockStatement;
import dev.gradleplugins.buildscript.ast.statements.MultiStatement;
import dev.gradleplugins.buildscript.ast.statements.Statement;
import dev.gradleplugins.buildscript.syntax.ASTTransformer;

import java.util.Collections;

import static dev.gradleplugins.buildscript.ast.expressions.PropertyAccessExpression.plainProperty;

public final class UseGetterTransformer implements ASTTransformer {
    public Statement visit(GradleBlockStatement blockStatement) {
        assert blockStatement.getBody().getParameters() instanceof LambdaExpression.SingleImplicitParameter; // assume implicit it
        Expression selectorExpression = blockStatement.getSelector().accept(new SelectorToGetter());

        // Convert delegate/it to selector's getter
        ASTTransformer itTransformer = new ASTTransformer() {
            @Override
            public Expression visit(DelegateExpression expression) {
                if (expression.equals(blockStatement.getDelegate())) {
                    return selectorExpression;
                }
                return ASTTransformer.super.visit(expression);
            }

            @Override
            public Expression visit(ItExpression expression) {
                if (expression.equals(blockStatement.getIt())) {
                    return selectorExpression;
                }
                return ASTTransformer.super.visit(expression);
            }
        };

        // Convert body into statements
        return blockStatement.getBody().getBody().map(it -> it.accept(new Node.Visitor<Statement>() {
            @Override
            public Statement visit(Statement statement) {
                return statement.accept(itTransformer);
            }

            @Override
            public Statement visit(Expression expression) {
                return new ExpressionStatement(expression.accept(itTransformer));
            }

            @Override
            public Statement visit(Comment comment) {
                throw new UnsupportedOperationException();
            }
        })).orElse(MultiStatement.of(Collections.emptyList()));
    }

    private static final class SelectorToGetter implements ASTTransformer {
        @Override
        public Expression visit(MethodCallExpression expression) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Expression visit(LiteralExpression expression) {
            return plainProperty(expression.get().toString());
        }

        @Override
        public Expression visit(QualifiedExpression expression) {
            return new QualifiedExpression(expression.getLeftExpression(), expression.getRightExpression().accept(this));
        }
    }
}
