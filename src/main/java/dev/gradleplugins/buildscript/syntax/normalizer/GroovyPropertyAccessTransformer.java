package dev.gradleplugins.buildscript.syntax.normalizer;

import dev.gradleplugins.buildscript.ast.expressions.Expression;
import dev.gradleplugins.buildscript.ast.expressions.FieldAccessExpression;
import dev.gradleplugins.buildscript.ast.expressions.PropertyAccessExpression;
import dev.gradleplugins.buildscript.ast.expressions.QualifiedExpression;
import dev.gradleplugins.buildscript.syntax.ASTTransformer;

import static dev.gradleplugins.buildscript.syntax.Syntax.literal;

// Convert property access into Groovy's qualified expressions
public final class GroovyPropertyAccessTransformer implements ASTTransformer {
    @Override
    public Expression visit(PropertyAccessExpression expression) {
        final Expression objectExpression = expression.getObjectExpression();
        if (expression.getAccessType().equals(PropertyAccessExpression.AccessType.FIELD)) {
            return new FieldAccessExpression(objectExpression, expression.getPropertyName()).accept(this);
        } else if (expression.getAccessType().equals(PropertyAccessExpression.AccessType.PLAIN)) {
            return new QualifiedExpression(objectExpression, literal(expression.getPropertyName())).accept(this);
        } else if (expression.getAccessType().equals(PropertyAccessExpression.AccessType.GRADLE)) {
            return new QualifiedExpression(objectExpression, literal(expression.getPropertyName())).accept(this);
        } else if (expression.getAccessType().equals(PropertyAccessExpression.AccessType.EXTRA)) {
            return new QualifiedExpression(expression.getObjectExpression(), literal(expression.getPropertyName())).accept(this);
        } else if (expression.getAccessType().equals(PropertyAccessExpression.AccessType.EXTENSION)) {
            return new QualifiedExpression(expression.getObjectExpression(), literal(expression.getPropertyName())).accept(this);
        } else {
            throw new UnsupportedOperationException();
        }
    }
}
