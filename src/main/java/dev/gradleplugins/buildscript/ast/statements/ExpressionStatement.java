package dev.gradleplugins.buildscript.ast.statements;

import dev.gradleplugins.buildscript.ast.expressions.Expression;

// Represents an expression used as a statement, such as a method call.
public final class ExpressionStatement implements Statement {
    private final Expression expression;

    public ExpressionStatement(Expression expression) {
        this.expression = expression;
    }

    public Expression getExpression() {
        return expression;
    }

    @Override
    public <ReturnType> ReturnType accept(Visitor<ReturnType> visitor) {
        return visitor.visit(this);
    }
}
