package dev.gradleplugins.buildscript;

import dev.gradleplugins.buildscript.ast.expressions.CastingExpression;
import dev.gradleplugins.buildscript.ast.expressions.LiteralExpression;
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
                new CastingExpression(CastingExpression.CastingType.C_STYLE, stringType().typeOnly(), new LiteralExpression(objectType(), "myExpression")).toString(syntax()));
    }

    String expectedCast__myExpression_castTo_String();

    @Test
    default void testAsCastToString() {
        assertEquals(this.expectedCast__myExpression_as_String(),
                new CastingExpression(CastingExpression.CastingType.AS, stringType().typeOnly(), new LiteralExpression(objectType(), "myExpression")).toString(syntax()));
    }

    String expectedCast__myExpression_as_String();

    @Test
    default void testSafeAsCastToString() {
        assertEquals(this.expectedCast__myExpression_safeAs_String(),
                new CastingExpression(CastingExpression.CastingType.SAFE_AS, stringType().typeOnly(), new LiteralExpression(objectType(), "myExpression")).toString(syntax()));
    }

    String expectedCast__myExpression_safeAs_String();
}
