package dev.gradleplugins.buildscript.ast.expressions;

import dev.gradleplugins.buildscript.ast.type.Type;

import static dev.gradleplugins.buildscript.ast.type.UnknownType.unknownType;

public final class ItExpression implements Expression {
    private final Type type;

    public ItExpression() {
        this(unknownType());
    }

    public ItExpression(Type type) {
        this.type = type;
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
