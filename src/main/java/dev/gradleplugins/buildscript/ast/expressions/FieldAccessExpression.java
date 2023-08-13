package dev.gradleplugins.buildscript.ast.expressions;

import dev.gradleplugins.buildscript.ast.type.Type;

public final class FieldAccessExpression implements Expression {
    private final Expression scope;
    private final String name;

    public FieldAccessExpression(Expression scope, String name) {
        this.scope = scope;
        this.name = name;
    }

    @Override
    public Type getType() {
        return null;
    }

    @Override
    public <ReturnType> ReturnType accept(Visitor<ReturnType> visitor) {
        return visitor.visit(this);
    }

    public Expression getScope() {
        return scope;
    }

    public String getName() {
        return name;
    }

    public AssignExpression assign(Expression value) {
        return new AssignExpression(this, value);
    }
}
