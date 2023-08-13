package dev.gradleplugins.buildscript.syntax.normalizer;

import dev.gradleplugins.buildscript.ast.expressions.Expression;
import dev.gradleplugins.buildscript.ast.expressions.MapLiteralExpression;
import dev.gradleplugins.buildscript.ast.expressions.MethodCallExpression;
import dev.gradleplugins.buildscript.ast.expressions.SetLiteralExpression;
import dev.gradleplugins.buildscript.ast.expressions.TypeExpression;
import dev.gradleplugins.buildscript.ast.type.ReferenceType;
import dev.gradleplugins.buildscript.syntax.ASTTransformer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static dev.gradleplugins.buildscript.syntax.Syntax.string;

// Convert Map literal into Java 8 APIs
public class Java8MapLiteralTransformer implements ASTTransformer {
    @Override
    public Expression visit(MapLiteralExpression expression) {
        final List<Expression> entries = new ArrayList<>();
        expression.forEach((k, v) -> {
            entries.add(string(k).accept(this));
            entries.add(v.accept(this));
        });

        if (expression.getElements().isEmpty()) {
            return new MethodCallExpression(new TypeExpression(ReferenceType.of(Collections.class)), "emptyMap", Collections.emptyList());
        } else if (expression.getElements().size() == 1) {
            return new MethodCallExpression(new TypeExpression(ReferenceType.of(Collections.class)), "singletonMap", entries);
        } else {
            throw new UnsupportedOperationException();
        }
    }
}
