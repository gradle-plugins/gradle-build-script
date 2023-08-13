package dev.gradleplugins.buildscript.ast.expressions;

import dev.gradleplugins.buildscript.ast.type.Type;

public final class QualifiedExpression implements Expression {
    private final Type type;
    private final Expression leftExpression;
    private final Expression rightExpression;

    public QualifiedExpression(Expression leftExpression, Expression rightExpression) {
        this(rightExpression.getType(), leftExpression, rightExpression);
    }

    public QualifiedExpression(Type type, Expression leftExpression, Expression rightExpression) {
        this.type = type;
        this.leftExpression = leftExpression;
        this.rightExpression = rightExpression;
    }

    public Expression getLeftExpression() {
        return leftExpression;
    }

    public Expression getRightExpression() {
        return rightExpression;
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
