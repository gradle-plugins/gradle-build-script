package dev.gradleplugins.buildscript.ast.expressions;

import dev.gradleplugins.buildscript.ast.type.Type;

public class CastingExpression implements Expression {
    private final CastingType castingType;
    private final Type type;
    private final Expression expression;

    public CastingExpression(CastingType castingType, Type type, Expression expression) {
        this.castingType = castingType;
        this.type = type;
        this.expression = expression;
    }

    public enum CastingType {
        // Represent a cast expression using as keyword (Groovy/Kotlin)
        AS,

        // Represent a cast expression using as? keyword (Kotlin)
        SAFE_AS,

        // Represents a C-style cast expression (Groovy/Java)
        C_STYLE
    }

    @Override
    public Type getType() {
        return type;
    }

    public CastingType getCastingType() {
        return castingType;
    }

    public Expression getExpression() {
        return expression;
    }

    @Override
    public <ReturnType> ReturnType accept(Visitor<ReturnType> visitor) {
        return visitor.visit(this);
    }
}
