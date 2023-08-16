package dev.gradleplugins.buildscript.syntax.normalizer;

import dev.gradleplugins.buildscript.ast.expressions.AsExpression;
import dev.gradleplugins.buildscript.ast.expressions.CastExpression;
import dev.gradleplugins.buildscript.ast.expressions.Expression;
import dev.gradleplugins.buildscript.ast.expressions.InstanceOfExpression;
import dev.gradleplugins.buildscript.ast.expressions.NullLiteralExpression;
import dev.gradleplugins.buildscript.ast.expressions.SafeAsExpression;
import dev.gradleplugins.buildscript.ast.expressions.TernaryExpression;
import dev.gradleplugins.buildscript.syntax.ASTTransformer;

// Convert as non-Java casting
public final class JavaCastingTransformer implements ASTTransformer {
    @Override
    public Expression visit(AsExpression expression) {
        return new CastExpression(expression.getType(), expression.getExpression().accept(this));
    }

    @Override
    public Expression visit(SafeAsExpression expression) {
        // FIXME: expression will be evaluated twice
        return new TernaryExpression(new InstanceOfExpression(expression.getExpression(), expression.getType()), new CastExpression(expression.getType(), expression.getExpression()), new NullLiteralExpression()).accept(this);
    }
}
