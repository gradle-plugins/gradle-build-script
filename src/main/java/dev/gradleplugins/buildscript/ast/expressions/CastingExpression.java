package dev.gradleplugins.buildscript.ast.expressions;

public interface CastingExpression extends Expression {
    enum CastingType {
        // Represent a cast expression using as keyword (Groovy/Kotlin)
        AS,

        // Represent a cast expression using as? keyword (Kotlin)
        SAFE_AS,

        // Represents a C-style cast expression (Groovy/Java)
        C_STYLE
    }

    CastingType getCastingType();

    Expression getExpression();
}
