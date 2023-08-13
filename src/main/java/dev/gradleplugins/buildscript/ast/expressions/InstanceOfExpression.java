package dev.gradleplugins.buildscript.ast.expressions;

import dev.gradleplugins.buildscript.ast.type.PrimitiveType;
import dev.gradleplugins.buildscript.ast.type.ReferenceType;
import dev.gradleplugins.buildscript.ast.type.Type;

// Represents an instance-of expression.
public final class InstanceOfExpression implements Expression {
    private final Expression expression;
    private final Type type;

    public InstanceOfExpression(Expression expression, Type type) {
        this.expression = expression;
        this.type = type;
    }

    @Override
    public Type getType() {
        return PrimitiveType.booleanType();
    }

    public Expression getExpression() {
        return expression;
    }

    // FIXME
    public Type getInstanceType() {
        return type;
    }

    @Override
    public <ReturnType> ReturnType accept(Visitor<ReturnType> visitor) {
        return visitor.visit(this);
    }
}
