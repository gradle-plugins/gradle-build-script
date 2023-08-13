package dev.gradleplugins.buildscript;

import dev.gradleplugins.buildscript.ast.expressions.MethodCallExpression;
import dev.gradleplugins.buildscript.ast.statements.ExpressionStatement;
import dev.gradleplugins.buildscript.syntax.Syntax;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static dev.gradleplugins.buildscript.ast.expressions.CurrentScopeExpression.current;
import static org.junit.jupiter.api.Assertions.assertEquals;

public interface ExpressionStatementTester {
    Syntax syntax();

    @Test
    default void testCallMethodAsStatement() {
        assertEquals(expectedCallMethod__foo_asStatement(),
                new ExpressionStatement(new MethodCallExpression(current(), "foo", Collections.emptyList())).toString(syntax()));
    }

    String expectedCallMethod__foo_asStatement();
}
