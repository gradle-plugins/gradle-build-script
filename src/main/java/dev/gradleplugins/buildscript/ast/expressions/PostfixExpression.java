package dev.gradleplugins.buildscript.ast.expressions;

import dev.gradleplugins.buildscript.ast.type.Type;

// Represent a postfixed expression, i.e. <expr>--, <expr>++
public final class PostfixExpression implements Expression {
    private final Expression expression;
    private final InfixExpression.Operator operator;

    public PostfixExpression(Expression expression, InfixExpression.Operator operator) {
        this.expression = expression;
        this.operator = operator;
    }

    public Expression getExpression() {
        return expression;
    }

    public InfixExpression.Operator getOperator() {
        return operator;
    }

    @Override
    public Type getType() {
        throw new UnsupportedOperationException();
    }

    @Override
    public <ReturnType> ReturnType accept(Visitor<ReturnType> visitor) {
        return visitor.visit(this);
    }
}
