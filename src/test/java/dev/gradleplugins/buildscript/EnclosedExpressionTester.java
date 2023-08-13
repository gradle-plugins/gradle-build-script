package dev.gradleplugins.buildscript;

import dev.gradleplugins.buildscript.ast.expressions.CastExpression;
import dev.gradleplugins.buildscript.ast.expressions.EnclosedExpression;
import dev.gradleplugins.buildscript.ast.expressions.LiteralExpression;
import dev.gradleplugins.buildscript.ast.type.ReferenceType;
import dev.gradleplugins.buildscript.syntax.Syntax;
import org.gradle.api.plugins.ExtensionAware;
import org.junit.jupiter.api.Test;

import static dev.gradleplugins.buildscript.ast.type.ReferenceType.objectType;
import static org.junit.jupiter.api.Assertions.assertEquals;

public interface EnclosedExpressionTester {
    Syntax syntax();

    @Test
    default void testEnclosedCast() {
        assertEquals(expectedEnclosedCastOf_myVar_to_ExtensionAware(),
                new EnclosedExpression(new CastExpression(ReferenceType.of(ExtensionAware.class).typeOnly(), new LiteralExpression(objectType(), "myVar"))).toString(syntax()));
    }

    String expectedEnclosedCastOf_myVar_to_ExtensionAware();
}
