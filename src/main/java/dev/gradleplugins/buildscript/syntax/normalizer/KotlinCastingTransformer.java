package dev.gradleplugins.buildscript.syntax.normalizer;

import dev.gradleplugins.buildscript.ast.expressions.CastingExpression;
import dev.gradleplugins.buildscript.ast.expressions.Expression;
import dev.gradleplugins.buildscript.syntax.ASTTransformer;

// Convert C-style casting into as casting
public final class KotlinCastingTransformer implements ASTTransformer {
    @Override
    public Expression visit(CastingExpression expression) {
        switch (expression.getCastingType()) {
            case C_STYLE: return new CastingExpression(CastingExpression.CastingType.AS, expression.getType(), expression.getExpression().accept(this));
            default: return ASTTransformer.super.visit(expression);
        }
    }
}
