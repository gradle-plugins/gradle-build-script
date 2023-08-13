package dev.gradleplugins.buildscript.ast.expressions;

import dev.gradleplugins.buildscript.ast.type.Type;

public final class LiteralExpression implements Expression {
    private final Type type;
    private final Object literal;

    public LiteralExpression(Type type, Object literal) {
        this.type = type;
        this.literal = literal;
    }

    public Object get() {
        return literal;
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
