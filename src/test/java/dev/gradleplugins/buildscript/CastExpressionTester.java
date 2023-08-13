package dev.gradleplugins.buildscript;

import dev.gradleplugins.buildscript.ast.expressions.CastExpression;
import dev.gradleplugins.buildscript.ast.expressions.LiteralExpression;
import dev.gradleplugins.buildscript.syntax.Syntax;
import org.junit.jupiter.api.Test;

import static dev.gradleplugins.buildscript.ast.type.ReferenceType.objectType;
import static dev.gradleplugins.buildscript.ast.type.ReferenceType.stringType;
import static org.junit.jupiter.api.Assertions.assertEquals;

public interface CastExpressionTester {
    Syntax syntax();

    @Test
    default void testCastToString() {
        assertEquals(expectedCast__myExpression_as_String(),
                new CastExpression(stringType().typeOnly(), new LiteralExpression(objectType(), "myExpression")).toString(syntax()));
    }

    String expectedCast__myExpression_as_String();
}
