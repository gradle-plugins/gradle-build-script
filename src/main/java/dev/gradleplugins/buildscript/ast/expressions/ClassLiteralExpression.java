package dev.gradleplugins.buildscript.ast.expressions;

import dev.gradleplugins.buildscript.ast.type.ParameterizedType;
import dev.gradleplugins.buildscript.ast.type.ReferenceType;
import dev.gradleplugins.buildscript.ast.type.Type;

import java.util.Collections;

// Represents a class literal.
public final class ClassLiteralExpression implements Expression {
    private final Type value;

    public ClassLiteralExpression(Type value) {
        this.value = value;
    }

    @Override
    public Type getType() {
        return new ParameterizedType(ReferenceType.of(Class.class), Collections.singletonList(value));
    }

    public Type get() {
        return value;
    }

    @Override
    public <ReturnType> ReturnType accept(Visitor<ReturnType> visitor) {
        return visitor.visit(this);
    }
}
