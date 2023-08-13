package dev.gradleplugins.buildscript.ast.expressions;

import dev.gradleplugins.buildscript.ast.type.Type;

public final class AssignExpression implements Expression {
    private final Expression target;
    private final Expression value;

    public AssignExpression(Expression target, Expression value) {
        this.target = target;
        this.value = value;
    }

    @Override
    public Type getType() {
        return null;
    }

    @Override
    public <ReturnType> ReturnType accept(Visitor<ReturnType> visitor) {
        return visitor.visit(this);
    }

    public Expression getTarget() {
        return target;
    }

    public Expression getValue() {
        return value;
    }
}
