package dev.gradleplugins.buildscript.syntax.normalizer;

import dev.gradleplugins.buildscript.ast.expressions.Expression;
import dev.gradleplugins.buildscript.ast.expressions.InfixExpression;
import dev.gradleplugins.buildscript.ast.expressions.NullLiteralExpression;
import dev.gradleplugins.buildscript.ast.expressions.QualifiedExpression;
import dev.gradleplugins.buildscript.ast.expressions.SafeNavigationExpression;
import dev.gradleplugins.buildscript.ast.expressions.TernaryExpression;
import dev.gradleplugins.buildscript.syntax.ASTTransformer;

public final class JavaSafeNavigationTransformer implements ASTTransformer {
    @Override
    public Expression visit(SafeNavigationExpression expression) {
        // FIXME: object expression will be evaluated twice
        return new TernaryExpression(new InfixExpression(expression.getObjectExpression(), InfixExpression.Operator.EqualTo, new NullLiteralExpression()), new QualifiedExpression(expression.getObjectExpression(), expression.getPropertyExpression()), new NullLiteralExpression()).accept(this);
    }
}
