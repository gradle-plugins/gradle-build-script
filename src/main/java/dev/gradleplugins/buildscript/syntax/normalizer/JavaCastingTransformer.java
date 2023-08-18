package dev.gradleplugins.buildscript.syntax.normalizer;

import dev.gradleplugins.buildscript.ast.expressions.CastingExpression;
import dev.gradleplugins.buildscript.ast.expressions.Expression;
import dev.gradleplugins.buildscript.ast.expressions.NullLiteralExpression;
import dev.gradleplugins.buildscript.ast.expressions.TernaryExpression;
import dev.gradleplugins.buildscript.ast.expressions.TypeComparisonExpression;
import dev.gradleplugins.buildscript.syntax.ASTTransformer;

// Convert as non-Java casting
public final class JavaCastingTransformer implements ASTTransformer {
    @Override
    public Expression visit(CastingExpression expression) {
        switch (expression.getCastingType()) {
            case AS: return new CastingExpression(CastingExpression.CastingType.C_STYLE, expression.getType(), expression.getExpression().accept(this));

            // FIXME: expression will be evaluated twice
            case SAFE_AS: return new TernaryExpression(new TypeComparisonExpression(TypeComparisonExpression.ComparisonType.INSTANCE_OF, expression.getExpression(), expression.getType()), new CastingExpression(CastingExpression.CastingType.C_STYLE, expression.getType(), expression.getExpression()), new NullLiteralExpression()).accept(this);

            default: return ASTTransformer.super.visit(expression);
        }
    }
}
