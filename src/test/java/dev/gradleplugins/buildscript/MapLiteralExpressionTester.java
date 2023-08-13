package dev.gradleplugins.buildscript;

import dev.gradleplugins.buildscript.ast.expressions.MapLiteralExpression;
import dev.gradleplugins.buildscript.syntax.Syntax;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static dev.gradleplugins.buildscript.ast.type.ReferenceType.stringType;
import static dev.gradleplugins.buildscript.ast.type.UnknownType.unknownType;
import static dev.gradleplugins.buildscript.syntax.Syntax.string;
import static org.junit.jupiter.api.Assertions.assertEquals;

public interface MapLiteralExpressionTester {
    Syntax syntax();

    @Test
    default void testEmptyMap() {
        assertEquals(expectedEmptyMapLiteral(),
                new MapLiteralExpression(unknownType(), Collections.emptyMap()).toString(syntax()));
    }

    String expectedEmptyMapLiteral();

    @Test
    default void testMapWithSingleElement() {
        assertEquals(expectedSingleElementMapLiteral__k1_to_v1_string(),
                new MapLiteralExpression(stringType(), Collections.singletonMap("k1", string("v1"))).toString(syntax()));
    }

    String expectedSingleElementMapLiteral__k1_to_v1_string();
}
