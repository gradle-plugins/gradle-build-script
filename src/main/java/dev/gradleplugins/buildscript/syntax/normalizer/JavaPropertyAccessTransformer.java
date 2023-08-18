package dev.gradleplugins.buildscript.syntax.normalizer;

import dev.gradleplugins.buildscript.ast.expressions.Expression;
import dev.gradleplugins.buildscript.ast.expressions.FieldAccessExpression;
import dev.gradleplugins.buildscript.ast.expressions.InfixExpression;
import dev.gradleplugins.buildscript.ast.expressions.MethodCallExpression;
import dev.gradleplugins.buildscript.ast.expressions.PropertyAccessExpression;
import dev.gradleplugins.buildscript.ast.expressions.StringLiteralExpression;
import dev.gradleplugins.buildscript.ast.statements.ExpressionStatement;
import dev.gradleplugins.buildscript.ast.statements.GradleBlockStatement;
import dev.gradleplugins.buildscript.ast.statements.Statement;
import dev.gradleplugins.buildscript.syntax.ASTTransformer;

import java.util.Arrays;
import java.util.Collections;

import static dev.gradleplugins.buildscript.ast.type.PrimitiveType.booleanType;
import static dev.gradleplugins.buildscript.syntax.Syntax.literal;
import static dev.gradleplugins.buildscript.syntax.Syntax.string;

// Convert property access/assign into correct method calls
public final class JavaPropertyAccessTransformer implements ASTTransformer {
    @Override
    public Expression visit(PropertyAccessExpression expression) {
        final Expression objectExpression = expression.getObjectExpression();
        if (expression.getAccessType().equals(PropertyAccessExpression.AccessType.FIELD)) {
            return new FieldAccessExpression(objectExpression, expression.getPropertyName()).accept(this);
        } else if (expression.getAccessType().equals(PropertyAccessExpression.AccessType.PLAIN)) {
            String methodName = "get" + capitalize(expression.getPropertyName());
            if (expression.getType().equals(booleanType())) {
                methodName = "is" + capitalize(expression.getPropertyName());
            }
            return new MethodCallExpression(objectExpression, methodName, Collections.emptyList()).accept(this);
        } else if (expression.getAccessType().equals(PropertyAccessExpression.AccessType.GRADLE)) {
            return new MethodCallExpression(objectExpression, "get" + capitalize(expression.getPropertyName()), Collections.emptyList()).accept(this);
        } else if (expression.getAccessType().equals(PropertyAccessExpression.AccessType.EXTRA)) {
            return new MethodCallExpression(new MethodCallExpression(new MethodCallExpression(objectExpression, "getExtensions", Collections.emptyList()), "getExtraProperties", Collections.emptyList()), "get", Collections.singletonList(new StringLiteralExpression(expression.getPropertyName()))).accept(this);
        } else if (expression.getAccessType().equals(PropertyAccessExpression.AccessType.EXTENSION)) {
            return new MethodCallExpression(new MethodCallExpression(objectExpression, "getExtensions", Collections.emptyList()), "getByName", Collections.singletonList(new StringLiteralExpression(expression.getPropertyName()))).accept(this);
        } else {
            throw new UnsupportedOperationException();
        }
    }

    @Override
    public Statement visit(GradleBlockStatement statement) {
        if (statement.getSelector() instanceof PropertyAccessExpression && ((PropertyAccessExpression) statement.getSelector()).getAccessType().equals(PropertyAccessExpression.AccessType.EXTENSION)) {
            final PropertyAccessExpression selector = (PropertyAccessExpression) statement.getSelector();
            // FIXME: convert block into lambda/closure
            return new ExpressionStatement(new MethodCallExpression(new MethodCallExpression(selector.getObjectExpression(), "getExtensions", Collections.emptyList()), "configure", Arrays.asList(string(selector.getPropertyName()), literal("it -> {}"))).accept(this));
        }
        return ASTTransformer.super.visit(statement);
    }

    @Override
    public Expression visit(InfixExpression expression) {
        if (expression.getOperator().equals(InfixExpression.Operator.Assignment) && expression.getLeftExpression() instanceof PropertyAccessExpression) {
            final PropertyAccessExpression target = (PropertyAccessExpression) expression.getLeftExpression();
            final Expression objectExpression = target.getObjectExpression();
            final Expression valueExpression = expression.getRightExpression();
            if (target.getAccessType().equals(PropertyAccessExpression.AccessType.PLAIN)) {
                return new MethodCallExpression(objectExpression, "set" + capitalize(target.getPropertyName()), Collections.singletonList(valueExpression)).accept(this);
            } else if (target.getAccessType().equals(PropertyAccessExpression.AccessType.EXTRA)) {
                return new MethodCallExpression(new MethodCallExpression(new MethodCallExpression(objectExpression, "getExtensions", Collections.emptyList()), "getExtraProperties", Collections.emptyList()), "set", Arrays.asList(new StringLiteralExpression(target.getPropertyName()), valueExpression)).accept(this);
            } else if (target.getAccessType().equals(PropertyAccessExpression.AccessType.GRADLE)) {
                return new MethodCallExpression(target, "set", Collections.singletonList(valueExpression)).accept(this);
            }
        }
        return ASTTransformer.super.visit(expression);
    }

    private static String capitalize(String s) {
        return Character.toUpperCase(s.charAt(0)) + s.substring(1);
    }
}
