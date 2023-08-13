package dev.gradleplugins.buildscript.syntax.normalizer;

import dev.gradleplugins.buildscript.ast.expressions.Expression;
import dev.gradleplugins.buildscript.ast.expressions.MethodCallExpression;
import dev.gradleplugins.buildscript.ast.expressions.SetLiteralExpression;
import dev.gradleplugins.buildscript.ast.expressions.TypeExpression;
import dev.gradleplugins.buildscript.ast.type.ReferenceType;
import dev.gradleplugins.buildscript.syntax.ASTTransformer;

import java.util.Collections;

// Convert Set literal into Java 8 APIs
public class Java8SetLiteralTransformer implements ASTTransformer {
    @Override
    public Expression visit(SetLiteralExpression expression) {
        if (expression.isEmpty()) {
            return new MethodCallExpression(new TypeExpression(ReferenceType.of(Collections.class)), "emptySet", Collections.emptyList()).accept(this);
        } else if (expression.hasSingleElement()) {
            return new MethodCallExpression(new TypeExpression(ReferenceType.of(Collections.class)), "singleton", expression.getElements()).accept(this);
        } else {
            throw new UnsupportedOperationException();
        }
    }
}
