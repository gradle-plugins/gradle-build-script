package dev.gradleplugins.buildscript;

import dev.gradleplugins.buildscript.ast.expressions.NullLiteralExpression;
import dev.gradleplugins.buildscript.syntax.Syntax;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public interface NullLiteralExpressionTester {
    Syntax syntax();

    @Test
    default void testNullLiteral() {
        assertEquals(expectedNullLiteral(), new NullLiteralExpression().toString(syntax()));
    }

    String expectedNullLiteral();
}
