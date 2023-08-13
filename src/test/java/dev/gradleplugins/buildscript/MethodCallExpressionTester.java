package dev.gradleplugins.buildscript;

import dev.gradleplugins.buildscript.ast.expressions.MethodCallExpression;
import dev.gradleplugins.buildscript.syntax.Syntax;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static dev.gradleplugins.buildscript.ast.expressions.CurrentScopeExpression.current;
import static dev.gradleplugins.buildscript.syntax.Syntax.string;
import static org.junit.jupiter.api.Assertions.assertEquals;

public interface MethodCallExpressionTester {
    Syntax syntax();

    @Test
    default void testMethodCallDelegate() {
        assertEquals(expectedMethodCallOnDelegate__register__foo_string(), new MethodCallExpression(current(), "register", Collections.singletonList(string("foo"))).toString(syntax()));
    }

    String expectedMethodCallOnDelegate__register__foo_string();
}
