package dev.gradleplugins.buildscript.ast.expressions;

import dev.gradleplugins.buildscript.ast.type.Type;

import static dev.gradleplugins.buildscript.ast.type.ReferenceType.stringType;

// Represents a string literal.
public final class StringLiteralExpression implements Expression {
    private final CharSequence value;

    public StringLiteralExpression(CharSequence value) {
        this.value = value;
    }

    @Override
    public Type getType() {
        return stringType();
    }

    public CharSequence get() {
        return value;
    }

    @Override
    public <ReturnType> ReturnType accept(Visitor<ReturnType> visitor) {
        return visitor.visit(this);
    }
}
