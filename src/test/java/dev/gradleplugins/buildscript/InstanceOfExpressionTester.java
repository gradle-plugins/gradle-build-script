package dev.gradleplugins.buildscript;

import dev.gradleplugins.buildscript.ast.expressions.InstanceOfExpression;
import dev.gradleplugins.buildscript.ast.type.ReferenceType;
import dev.gradleplugins.buildscript.syntax.Syntax;
import org.junit.jupiter.api.Test;

import static dev.gradleplugins.buildscript.ast.type.ReferenceType.stringType;
import static dev.gradleplugins.buildscript.syntax.Syntax.literal;
import static org.junit.jupiter.api.Assertions.assertEquals;

public interface InstanceOfExpressionTester {
    Syntax syntax();

    @Test
    default void testInstanceOf() {
        assertEquals(expectedInstanceOf__myVar_instanceof_String(),
                new InstanceOfExpression(literal("myVar"), stringType().typeOnly())
                        .toString(syntax()));
    }

    String expectedInstanceOf__myVar_instanceof_String();
}
