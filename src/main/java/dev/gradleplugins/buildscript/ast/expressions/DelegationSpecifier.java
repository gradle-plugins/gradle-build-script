package dev.gradleplugins.buildscript.ast.expressions;

import dev.gradleplugins.buildscript.ast.type.Type;

public final class DelegationSpecifier implements Expression {
    private final Expression identifier;

    public DelegationSpecifier(Expression identifier) {
        this.identifier = identifier;
    }

    public static DelegationSpecifier by(Expression identifier) {
        return new DelegationSpecifier(identifier);
    }

    @Override
    public Type getType() {
        return null;
    }

    @Override
    public <ReturnType> ReturnType accept(Visitor<ReturnType> visitor) {
        throw new UnsupportedOperationException();
    }

    public Expression getIdentifier() {
        return identifier;
    }
}
