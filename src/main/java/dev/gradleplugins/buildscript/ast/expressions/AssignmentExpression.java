package dev.gradleplugins.buildscript.ast.expressions;

import dev.gradleplugins.buildscript.ast.type.Type;

public final class AssignmentExpression implements Expression {
    private final Expression value;

    public AssignmentExpression(Expression value) {
        this.value = value;
    }

    public static AssignmentExpression assign(Expression value) {
        return new AssignmentExpression(value);
    }

    @Override
    public Type getType() {
        return value.getType();
    }

    public Expression getValue() {
        return value;
    }

    @Override
    public <ReturnType> ReturnType accept(Visitor<ReturnType> visitor) {
        return visitor.visit(this);
    }
}
