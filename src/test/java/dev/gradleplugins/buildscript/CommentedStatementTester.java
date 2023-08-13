package dev.gradleplugins.buildscript;

import dev.gradleplugins.buildscript.ast.expressions.MethodCallExpression;
import dev.gradleplugins.buildscript.ast.expressions.StringLiteralExpression;
import dev.gradleplugins.buildscript.ast.statements.CommentedStatement;
import dev.gradleplugins.buildscript.ast.statements.MultiStatement;
import dev.gradleplugins.buildscript.syntax.Syntax;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;

import static dev.gradleplugins.buildscript.ast.expressions.CurrentScopeExpression.current;
import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.assertEquals;

public interface CommentedStatementTester {
    Syntax syntax();

    @Test
    default void testCommentedStatementSingleLine() {
        assertEquals(expectedCommentedSingleLine__call_someMethod_no_args(), new CommentedStatement<>(new MethodCallExpression(current(), "someMethod", emptyList()).toStatement()).toString(syntax()));
    }

    String expectedCommentedSingleLine__call_someMethod_no_args();

    @Test
    default void testCommentedStatementMultipleLines() {
        assertEquals(expectedCommentedSingleLine__call_someMethod_no_args__call_someOtherMethod_foo_string(), new CommentedStatement<>(MultiStatement.of(Arrays.asList(new MethodCallExpression(current(), "someMethod", emptyList()).toStatement(), new MethodCallExpression(current(), "someOtherMethod", Collections.singletonList(new StringLiteralExpression("foo"))).toStatement()))).toString(syntax()));
    }

    String expectedCommentedSingleLine__call_someMethod_no_args__call_someOtherMethod_foo_string();
}
