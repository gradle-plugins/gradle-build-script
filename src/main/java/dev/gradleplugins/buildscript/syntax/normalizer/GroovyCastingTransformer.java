package dev.gradleplugins.buildscript.syntax.normalizer;

import dev.gradleplugins.buildscript.ast.expressions.CastingExpression;
import dev.gradleplugins.buildscript.ast.expressions.Expression;
import dev.gradleplugins.buildscript.ast.expressions.NullLiteralExpression;
import dev.gradleplugins.buildscript.ast.expressions.TernaryExpression;
import dev.gradleplugins.buildscript.ast.expressions.TypeComparisonExpression;
import dev.gradleplugins.buildscript.syntax.ASTTransformer;

// Convert as non-Groovy casting
public final class GroovyCastingTransformer implements ASTTransformer {
    @Override
    public Expression visit(CastingExpression expression) {
        if (expression.getCastingType().equals(CastingExpression.CastingType.SAFE_AS)) {
            // FIXME: expression will be evaluated twice
            return new TernaryExpression(new TypeComparisonExpression(TypeComparisonExpression.ComparisonType.INSTANCE_OF, expression.getExpression(), expression.getType()), new CastingExpression(CastingExpression.CastingType.AS, expression.getType(), expression.getExpression()), new NullLiteralExpression()).accept(this);
        } else {
            return ASTTransformer.super.visit(expression);
        }
    }
}
