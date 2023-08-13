package dev.gradleplugins.buildscript;

import dev.gradleplugins.buildscript.ast.expressions.StringLiteralExpression;
import dev.gradleplugins.buildscript.syntax.Syntax;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

interface StringLiteralExpressionTester {
    Syntax syntax();

    @Test
    default void testString() {
        assertEquals(expectedStringLiteral__foo(),
                new StringLiteralExpression("foo").toString(syntax()));
    }

    String expectedStringLiteral__foo();
}
