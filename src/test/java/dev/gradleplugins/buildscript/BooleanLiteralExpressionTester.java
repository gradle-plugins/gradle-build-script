package dev.gradleplugins.buildscript;

import dev.gradleplugins.buildscript.ast.expressions.BooleanLiteralExpression;
import dev.gradleplugins.buildscript.syntax.Syntax;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public interface BooleanLiteralExpressionTester {
    Syntax syntax();

    @Test
    default void testTrueBoolean() {
        assertEquals(expectedBooleanLiteral__true_boolean(),
                new BooleanLiteralExpression(true).toString(syntax()));
    }

    String expectedBooleanLiteral__true_boolean();

    @Test
    default void testFalseBoolean() {
        assertEquals(expectedBooleanLiteral__false_boolean(),
                new BooleanLiteralExpression(false).toString(syntax()));
    }

    String expectedBooleanLiteral__false_boolean();
}
