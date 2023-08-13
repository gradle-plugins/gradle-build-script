package dev.gradleplugins.buildscript.ast.expressions;

import dev.gradleplugins.buildscript.ast.type.Type;

import static dev.gradleplugins.buildscript.ast.type.PrimitiveType.booleanType;

// Represent the logical not expression, i.e. !<expression>
public final class NotExpression implements Expression {
    private final Expression expression;

    public NotExpression(Expression expression) {
        this.expression = expression;
    }

    @Override
    public Type getType() {
        return booleanType();
    }

    public Expression getExpression() {
        return expression;
    }

    @Override
    public <ReturnType> ReturnType accept(Visitor<ReturnType> visitor) {
        return visitor.visit(this);
    }
}
