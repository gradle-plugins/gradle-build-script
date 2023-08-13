package dev.gradleplugins.buildscript.ast.expressions;

import dev.gradleplugins.buildscript.ast.ExpressionBuilder;
import dev.gradleplugins.buildscript.ast.type.Type;

public final class ItExpression implements Expression {
    @Override
    public Type getType() {
        return null;
    }

    @Override
    public <ReturnType> ReturnType accept(Visitor<ReturnType> visitor) {
        return visitor.visit(this);
    }

    public static ExpressionBuilder<ItExpression> it() {
        return new ExpressionBuilder<>(new ItExpression());
    }
}
