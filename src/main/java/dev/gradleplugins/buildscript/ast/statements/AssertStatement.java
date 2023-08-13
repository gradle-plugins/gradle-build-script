package dev.gradleplugins.buildscript.ast.statements;

import dev.gradleplugins.buildscript.ast.expressions.Expression;

import javax.annotation.Nullable;

// Represents an assert statement.
public final class AssertStatement implements Statement {
    private final Expression check;
    @Nullable private final Expression message;

    public AssertStatement(Expression check, @Nullable Expression message) {
        this.check = check;
        this.message = message;
    }

    public Expression getCheck() {
        return check;
    }

    @Nullable
    public Expression getMessage() {
        return message;
    }

    @Override
    public <ReturnType> ReturnType accept(Visitor<ReturnType> visitor) {
        return visitor.visit(this);
    }
}
