package dev.gradleplugins.buildscript.ast.expressions;

import dev.gradleplugins.buildscript.ast.type.Type;

public final class SafeAsExpression implements CastingExpression {
    private final Type type;
    private final Expression expression;

    public SafeAsExpression(Type type, Expression expression) {
        this.type = type;
        this.expression = expression;
    }

    @Override
    public CastingType getCastingType() {
        return CastingType.SAFE_AS;
    }

    @Override
    public Expression getExpression() {
        return expression;
    }

    @Override
    public Type getType() {
        return type;
    }

    @Override
    public <ReturnType> ReturnType accept(Visitor<ReturnType> visitor) {
        return visitor.visit(this);
    }
}
