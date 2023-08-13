package dev.gradleplugins.buildscript.ast.expressions;

import dev.gradleplugins.buildscript.ast.type.Type;

public final class VariableDeclarator implements Expression {
    private final Type type;
    private final String variableName;
    private final Expression initializer;

    public VariableDeclarator(Type type, String variableName, Expression initializer) {
        this.type = type;
        this.variableName = variableName;
        this.initializer = initializer;
    }

    public String getVariableName() {
        return variableName;
    }

    public Expression getInitializer() {
        return initializer;
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
