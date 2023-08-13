package dev.gradleplugins.buildscript.syntax.normalizer;

import dev.gradleplugins.buildscript.ast.expressions.Expression;
import dev.gradleplugins.buildscript.ast.expressions.MapLiteralExpression;
import dev.gradleplugins.buildscript.ast.expressions.MethodCallExpression;
import dev.gradleplugins.buildscript.ast.expressions.TypeExpression;
import dev.gradleplugins.buildscript.ast.type.ReferenceType;
import dev.gradleplugins.buildscript.syntax.ASTTransformer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static dev.gradleplugins.buildscript.syntax.Syntax.string;

// Convert Map literal into Java 11 APIs
public class Java11MapLiteralTransformer implements ASTTransformer {
    @Override
    public Expression visit(MapLiteralExpression expression) {
        final List<Expression> entries = new ArrayList<>();
        expression.forEach((k, v) -> {
            entries.add(string(k).accept(this));
            entries.add(v.accept(this));
        });

        return new MethodCallExpression(new TypeExpression(ReferenceType.of(Map.class)), "of", entries).accept(this);
    }
}
