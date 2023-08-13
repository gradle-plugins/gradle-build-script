package dev.gradleplugins.buildscript.ast.expressions;

import dev.gradleplugins.buildscript.ast.type.PrimitiveType;
import dev.gradleplugins.buildscript.ast.type.Type;

// Represents a boolean literal.
public final class BooleanLiteralExpression implements Expression {
    private final boolean value;

    public BooleanLiteralExpression(boolean value) {
        this.value = value;
    }

    @Override
    public Type getType() {
        return PrimitiveType.booleanType();
    }

    public boolean get() {
        return value;
    }

    @Override
    public <ReturnType> ReturnType accept(Visitor<ReturnType> visitor) {
        return visitor.visit(this);
    }
}
