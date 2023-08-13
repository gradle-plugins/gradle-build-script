package dev.gradleplugins.buildscript;

import dev.gradleplugins.buildscript.ast.expressions.FieldAccessExpression;
import dev.gradleplugins.buildscript.syntax.Syntax;
import org.junit.jupiter.api.Test;

import static dev.gradleplugins.buildscript.syntax.Syntax.literal;
import static org.junit.jupiter.api.Assertions.assertEquals;

public interface FieldAccessExpressionTester {
    Syntax syntax();

    @Test
    default void testFieldAccessWithScope() {
        assertEquals(expectedFieldAccess__myVar_foo(),
                new FieldAccessExpression(literal("myVar"), "foo").toString(syntax()));
    }

    String expectedFieldAccess__myVar_foo();
}
