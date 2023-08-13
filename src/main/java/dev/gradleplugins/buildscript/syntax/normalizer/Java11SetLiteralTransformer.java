package dev.gradleplugins.buildscript.syntax.normalizer;

import dev.gradleplugins.buildscript.ast.expressions.Expression;
import dev.gradleplugins.buildscript.ast.expressions.MethodCallExpression;
import dev.gradleplugins.buildscript.ast.expressions.SetLiteralExpression;
import dev.gradleplugins.buildscript.ast.expressions.TypeExpression;
import dev.gradleplugins.buildscript.ast.type.ReferenceType;
import dev.gradleplugins.buildscript.syntax.ASTTransformer;

import java.util.Set;

// Converts Set literal into Java 11 APIs
public class Java11SetLiteralTransformer implements ASTTransformer {
    @Override
    public Expression visit(SetLiteralExpression expression) {
        return new MethodCallExpression(new TypeExpression(ReferenceType.of(Set.class)), "of", expression.getElements()).accept(this);
    }
}
