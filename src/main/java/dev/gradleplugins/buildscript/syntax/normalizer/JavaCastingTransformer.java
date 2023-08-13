package dev.gradleplugins.buildscript.syntax.normalizer;

import dev.gradleplugins.buildscript.ast.expressions.AsExpression;
import dev.gradleplugins.buildscript.ast.expressions.CastExpression;
import dev.gradleplugins.buildscript.ast.expressions.Expression;
import dev.gradleplugins.buildscript.syntax.ASTTransformer;

// Convert as casting into C-style casting
public final class JavaCastingTransformer implements ASTTransformer {
    @Override
    public Expression visit(AsExpression expression) {
        return new CastExpression(expression.getType(), expression.getExpression().accept(this));
    }
}
