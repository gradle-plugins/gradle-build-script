package dev.gradleplugins.buildscript.syntax.normalizer;

import dev.gradleplugins.buildscript.ast.expressions.Expression;
import dev.gradleplugins.buildscript.ast.expressions.LambdaExpression;
import dev.gradleplugins.buildscript.ast.expressions.MethodCallExpression;
import dev.gradleplugins.buildscript.ast.statements.BlockStatement;
import dev.gradleplugins.buildscript.ast.statements.ExpressionStatement;
import dev.gradleplugins.buildscript.ast.statements.GradleBlockStatement;
import dev.gradleplugins.buildscript.ast.statements.Statement;
import dev.gradleplugins.buildscript.syntax.ASTTransformer;

import java.util.List;

// only convert method call as statement with trailing lambda as Gradle block
public final class MethodCallToGradleBlockTransformer implements ASTTransformer {
    @Override
    public Statement visit(ExpressionStatement statement) {
        if (statement.getExpression() instanceof MethodCallExpression) {
            MethodCallExpression callExpression = (MethodCallExpression) statement.getExpression();
            List<Expression> arguments = callExpression.getArguments();
            if (arguments.size() > 1) {
                Expression lastArgument = arguments.get(arguments.size() - 1);
                if (lastArgument instanceof LambdaExpression) {
                    new GradleBlockStatement(new MethodCallExpression(callExpression.getObjectExpression(), arguments.subList(0, arguments.size() - 2)), BlockStatement.fromLambda((LambdaExpression) lastArgument));
                }
            }
        }
        return ASTTransformer.super.visit(statement);
    }
}
