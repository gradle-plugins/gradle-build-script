package dev.gradleplugins.buildscript.syntax.normalizer;

import dev.gradleplugins.buildscript.ast.expressions.AsExpression;
import dev.gradleplugins.buildscript.ast.expressions.CastExpression;
import dev.gradleplugins.buildscript.ast.expressions.Expression;
import dev.gradleplugins.buildscript.syntax.ASTTransformer;

// Convert C-style casting into as casting
public final class KotlinCastingTransformer implements ASTTransformer {
    @Override
    public Expression visit(CastExpression expression) {
        return new AsExpression(expression.getType(), expression.getExpression().accept(this));
    }
}
