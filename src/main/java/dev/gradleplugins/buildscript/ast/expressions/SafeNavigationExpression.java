package dev.gradleplugins.buildscript.ast.expressions;

import dev.gradleplugins.buildscript.ast.type.Type;

public final class SafeNavigationExpression implements Expression {
    private final Expression objectExpression;
    private final Expression propertyExpression;

    public SafeNavigationExpression(Expression objectExpression, Expression propertyExpression) {
        this.objectExpression = objectExpression;
        this.propertyExpression = propertyExpression;
    }

    public Expression getObjectExpression() {
        return objectExpression;
    }

    public Expression getPropertyExpression() {
        return propertyExpression;
    }

    @Override
    public Type getType() {
        throw new UnsupportedOperationException();
    }

    @Override
    public <ReturnType> ReturnType accept(Visitor<ReturnType> visitor) {
        return visitor.visit(this);
    }
}
