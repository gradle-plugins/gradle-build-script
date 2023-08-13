package dev.gradleplugins.buildscript.ast.expressions;

import dev.gradleplugins.buildscript.ast.type.ReferenceType;

public final class TypeExpression implements Expression {
    private final ReferenceType type;

    public TypeExpression(ReferenceType type) {
        this.type = type;
    }

    @Override
    public ReferenceType getType() {
        return type;
    }

    @Override
    public <ReturnType> ReturnType accept(Visitor<ReturnType> visitor) {
        return visitor.visit(this);
    }
}
