package dev.gradleplugins.buildscript;

import dev.gradleplugins.buildscript.ast.expressions.SetLiteralExpression;
import dev.gradleplugins.buildscript.syntax.Syntax;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static dev.gradleplugins.buildscript.ast.type.ReferenceType.stringType;
import static dev.gradleplugins.buildscript.ast.type.UnknownType.unknownType;
import static dev.gradleplugins.buildscript.syntax.Syntax.string;
import static org.junit.jupiter.api.Assertions.assertEquals;

public interface SetLiteralExpressionTester {
    Syntax syntax();

    @Test
    default void testEmptySet() {
        assertEquals(expectedEmptySetLiteral(),
                new SetLiteralExpression(unknownType(), Collections.emptyList()).toString(syntax()));
    }

    String expectedEmptySetLiteral();

    @Test
    default void testSetWithSingleElement() {
        assertEquals(expectedSingleElementSetLiteral__value_string(),
                new SetLiteralExpression(stringType(), Collections.singletonList(string("value"))).toString(syntax()));
    }

    String expectedSingleElementSetLiteral__value_string();
}
