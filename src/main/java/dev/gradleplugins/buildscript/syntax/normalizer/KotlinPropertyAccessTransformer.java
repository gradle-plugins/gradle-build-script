package dev.gradleplugins.buildscript.syntax.normalizer;

import dev.gradleplugins.buildscript.ast.expressions.AssignExpression;
import dev.gradleplugins.buildscript.ast.expressions.Expression;
import dev.gradleplugins.buildscript.ast.expressions.FieldAccessExpression;
import dev.gradleplugins.buildscript.ast.expressions.MethodCallExpression;
import dev.gradleplugins.buildscript.ast.expressions.PropertyAccessExpression;
import dev.gradleplugins.buildscript.ast.expressions.QualifiedExpression;
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

// Convert property access into Kotlin style
public final class KotlinPropertyAccessTransformer implements ASTTransformer {
    @Override
    public Expression visit(PropertyAccessExpression expression) {
        final Expression objectExpression = expression.getObjectExpression();
        if (expression.getAccessType().equals(PropertyAccessExpression.AccessType.FIELD)) {
            return new FieldAccessExpression(objectExpression, expression.getPropertyName()).accept(this);
        } else if (expression.getAccessType().equals(PropertyAccessExpression.AccessType.PLAIN)) {
            String propertyName = expression.getPropertyName();
            if (expression.getType().equals(booleanType())) {
                propertyName = "is" + capitalize(expression.getPropertyName());
            }
            return new QualifiedExpression(objectExpression, literal(propertyName)).accept(this);
        } else if (expression.getAccessType().equals(PropertyAccessExpression.AccessType.GRADLE)) {
            return new QualifiedExpression(objectExpression, literal(expression.getPropertyName())).accept(this);
        } else if (expression.getAccessType().equals(PropertyAccessExpression.AccessType.EXTRA)) {
            return new MethodCallExpression(new QualifiedExpression(expression.getType(), expression.getObjectExpression(), literal("extra")), "get", Collections.singletonList(string(expression.getPropertyName()))).accept(this);
        } else if (expression.getAccessType().equals(PropertyAccessExpression.AccessType.EXTENSION)) {
            return new QualifiedExpression(objectExpression, literal(expression.getPropertyName())).accept(this);
            // TODO: There should be a decision regarding if an extension is forced to access via API or not
//            return new MethodCallExpression(new QualifiedExpression(objectExpression, literal("extensions")), "getByName", Collections.singletonList(new StringLiteralExpression(expression.getPropertyName()))).accept(this);
        } else {
            throw new UnsupportedOperationException();
        }
    }

//    @Override
//    public Statement visit(GradleBlockStatement statement) {
//        if (statement.getSelector() instanceof PropertyAccessExpression && ((PropertyAccessExpression) statement.getSelector()).getAccessType().equals(PropertyAccessExpression.AccessType.EXTENSION)) {
//            final PropertyAccessExpression selector = (PropertyAccessExpression) statement.getSelector();
//            // FIXME: convert block into lambda/closure
//            return new ExpressionStatement(new MethodCallExpression(new MethodCallExpression(selector.getObjectExpression(), "getExtensions", Collections.emptyList()), "configure", Arrays.asList(string(selector.getPropertyName()), literal("it -> {}")))).accept(this);
//        }
//        return ASTTransformer.super.visit(statement);
//    }

    @Override
    public Expression visit(AssignExpression expression) {
        if (expression.getTarget() instanceof PropertyAccessExpression) {
            final PropertyAccessExpression target = (PropertyAccessExpression) expression.getTarget();
            final Expression objectExpression = target.getObjectExpression();
            final Expression valueExpression = expression.getValue();
            if (target.getAccessType().equals(PropertyAccessExpression.AccessType.EXTRA)) {
                return new MethodCallExpression(new QualifiedExpression(expression.getType(), objectExpression, literal("extra")), "set", Arrays.asList(string(target.getPropertyName()), expression.getValue())).accept(this);
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
