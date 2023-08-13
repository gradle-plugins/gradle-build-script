package dev.gradleplugins.buildscript.ast.expressions;

import dev.gradleplugins.buildscript.ast.type.Type;

// Represents a cast expression.
public final class CastExpression implements CastingExpression {
    private final Type type;
    private final Expression expression;

    public CastExpression(Type type, Expression expression) {
        this.type = type;
        this.expression = expression;
    }

    @Override
    public Type getType() {
        return type;
    }

    public Expression getExpression() {
        return expression;
    }

    @Override
    public <ReturnType> ReturnType accept(Visitor<ReturnType> visitor) {
        return visitor.visit(this);
    }
}
