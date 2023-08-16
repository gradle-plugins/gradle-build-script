package dev.gradleplugins.buildscript;

import dev.gradleplugins.buildscript.ast.expressions.AsExpression;
import dev.gradleplugins.buildscript.ast.expressions.CastExpression;
import dev.gradleplugins.buildscript.ast.expressions.LiteralExpression;
import dev.gradleplugins.buildscript.ast.expressions.SafeAsExpression;
import dev.gradleplugins.buildscript.syntax.Syntax;
import org.junit.jupiter.api.Test;

import static dev.gradleplugins.buildscript.ast.type.ReferenceType.objectType;
import static dev.gradleplugins.buildscript.ast.type.ReferenceType.stringType;
import static org.junit.jupiter.api.Assertions.assertEquals;

public interface CastingExpressionTester {
    Syntax syntax();

    @Test
    default void testCastToString() {
        assertEquals(expectedCast__myExpression_castTo_String(),
                new CastExpression(stringType().typeOnly(), new LiteralExpression(objectType(), "myExpression")).toString(syntax()));
    }

    String expectedCast__myExpression_castTo_String();

    @Test
    default void testAsCastToString() {
        assertEquals(this.expectedCast__myExpression_as_String(),
                new AsExpression(stringType().typeOnly(), new LiteralExpression(objectType(), "myExpression")).toString(syntax()));
    }

    String expectedCast__myExpression_as_String();

    @Test
    default void testSafeAsCastToString() {
        assertEquals(this.expectedCast__myExpression_safeAs_String(),
                new SafeAsExpression(stringType().typeOnly(), new LiteralExpression(objectType(), "myExpression")).toString(syntax()));
    }

    String expectedCast__myExpression_safeAs_String();
}
