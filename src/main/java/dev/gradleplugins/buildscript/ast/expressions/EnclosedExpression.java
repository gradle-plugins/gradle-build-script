package dev.gradleplugins.buildscript.ast.expressions;

import dev.gradleplugins.buildscript.ast.type.Type;

// Represents an expression enclosed in parentheses.
public final class EnclosedExpression implements Expression {
    private final Expression inner;

    public EnclosedExpression(Expression inner) {
        this.inner = inner;
    }

    public Expression getInner() {
        return inner;
    }

    @Override
    public Type getType() {
        return inner.getType();
    }

    @Override
    public <ReturnType> ReturnType accept(Visitor<ReturnType> visitor) {
        return visitor.visit(this);
    }
}
