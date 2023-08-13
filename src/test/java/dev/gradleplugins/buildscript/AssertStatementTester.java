package dev.gradleplugins.buildscript;

import dev.gradleplugins.buildscript.ast.expressions.BooleanLiteralExpression;
import dev.gradleplugins.buildscript.ast.expressions.StringLiteralExpression;
import dev.gradleplugins.buildscript.ast.statements.AssertStatement;
import dev.gradleplugins.buildscript.syntax.Syntax;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public interface AssertStatementTester {
    Syntax syntax();

    @Test
    default void testAssert() {
        assertEquals(expectedAssert__true_boolean(),
                new AssertStatement(new BooleanLiteralExpression(true), null)
                        .toString(syntax()));
    }

    String expectedAssert__true_boolean();

    @Test
    default void testAssertWithStringMessage() {
        assertEquals(expectedAssert__true_boolean__my_message_string(),
                new AssertStatement(new BooleanLiteralExpression(true), new StringLiteralExpression("my message"))
                        .toString(syntax()));
    }

    String expectedAssert__true_boolean__my_message_string();
}
