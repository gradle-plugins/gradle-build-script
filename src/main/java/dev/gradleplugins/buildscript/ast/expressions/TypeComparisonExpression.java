package dev.gradleplugins.buildscript.ast.expressions;

import dev.gradleplugins.buildscript.ast.type.Type;

import static dev.gradleplugins.buildscript.ast.type.PrimitiveType.booleanType;

public final class TypeComparisonExpression implements Expression {
    private final ComparisonType comparisonType;
    private final Expression expression;
    private final Type instanceType;

    public TypeComparisonExpression(ComparisonType comparisonType, Expression expression, Type instanceType) {
        this.comparisonType = comparisonType;
        this.expression = expression;
        this.instanceType = instanceType;
    }

    public enum ComparisonType {
        IS, SAFE_IS, INSTANCE_OF
    }

    public ComparisonType getComparisonType() {
        return comparisonType;
    }

    public Expression getExpression() {
        return expression;
    }

    public Type getInstanceType() {
        return instanceType;
    }

    @Override
    public Type getType() {
        return booleanType();
    }

    @Override
    public <ReturnType> ReturnType accept(Visitor<ReturnType> visitor) {
        return visitor.visit(this);
    }
}
