package dev.gradleplugins.buildscript;

import dev.gradleplugins.buildscript.ast.expressions.MethodCallExpression;
import dev.gradleplugins.buildscript.ast.expressions.PropertyAccessExpression;
import dev.gradleplugins.buildscript.ast.statements.BlockStatement;
import dev.gradleplugins.buildscript.ast.statements.GradleBlockStatement;
import dev.gradleplugins.buildscript.syntax.Syntax;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;

import static dev.gradleplugins.buildscript.ast.expressions.AssignmentExpression.assign;
import static dev.gradleplugins.buildscript.ast.expressions.CurrentScopeExpression.current;
import static dev.gradleplugins.buildscript.ast.expressions.MethodCallExpression.call;
import static dev.gradleplugins.buildscript.ast.expressions.VariableDeclarationExpression.val;
import static dev.gradleplugins.buildscript.ast.type.UnknownType.unknownType;
import static dev.gradleplugins.buildscript.syntax.Syntax.literal;
import static dev.gradleplugins.buildscript.syntax.Syntax.string;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public interface GradleBlockStatementTester {
    Syntax syntax();

    @Test
    default void testLiteralBlockSelector() {
        System.out.println(GradleBlockStatement.block(literal("foo"), val("myVar", assign(string("my-string"))).toStatement()).toString(syntax()));
        assertTrue(new GradleBlockStatement(literal("foo"), new BlockStatement(Collections.singletonList(val("myVar", assign(string("my-string"))).toStatement()))).toString(syntax()).startsWith(expectedGradleBlock__foo_literal_selector()));
    }

    String expectedGradleBlock__foo_literal_selector();

    @Test
    default void testMethodBlockSelector() {
        assertTrue(new GradleBlockStatement(call("register", string("foo")), new BlockStatement(Collections.singletonList(val("myVar", assign(string("my-string"))).toStatement()))).toString(syntax()).startsWith(expectedGradleBlock__register_method__foo_string_selector()));
    }

    String expectedGradleBlock__register_method__foo_string_selector();

    @Test
    default void testLiteralBlockSelectorWithEmptyBlock() {
        assertEquals(expectedGradleBlock__foo_literal_selector__empty_block(), new GradleBlockStatement(literal("foo"), new BlockStatement(Collections.emptyList())).toString(syntax()));
    }

    String expectedGradleBlock__foo_literal_selector__empty_block();

    @Test
    default void testLiteralBlockSelectorWithSingleStatementBlock() {
        assertEquals(expectedGradleBlock__foo_literal_selector__call_someMethod(), new GradleBlockStatement(literal("foo"), new BlockStatement(Collections.singletonList(new MethodCallExpression(current(), "someMethod", Collections.emptyList()).toStatement()))).toString(syntax()));
    }

    String expectedGradleBlock__foo_literal_selector__call_someMethod();

    @Test
    default void testLiteralBlockSelectorWithMultiStatementBlock() {
        assertEquals(expectedGradleBlock__foo_literal_selector__call_someMethod__call_someOtherMethod(), new GradleBlockStatement(literal("foo"), new BlockStatement(Arrays.asList(new MethodCallExpression(current(), "someMethod", Collections.emptyList()).toStatement(), new MethodCallExpression(current(), "someOtherMethod", Collections.emptyList()).toStatement()))).toString(syntax()));
    }

    String expectedGradleBlock__foo_literal_selector__call_someMethod__call_someOtherMethod();

    @Test
    default void testExtensionBlockSelector() {
        assertEquals(expectedGradleBlock__foo_extension_selector__empty_block(), new GradleBlockStatement(new PropertyAccessExpression(unknownType(), PropertyAccessExpression.AccessType.EXTENSION, current(), "foo"), new BlockStatement(Collections.emptyList())).toString(syntax()));
    }

    String expectedGradleBlock__foo_extension_selector__empty_block();

//    @Test
//    default void testExtensionBlockSelectorPreferringExtensionAwareApi() {
//        assertEquals(expectedGradleBlock__foo_extension_selector_using_ExtensionAware_API__empty_block(), new GradleBlockStatement(new GradleExtensionAccessExpression(null, "foo").use(GradleExtensionAccessExpression.ApiPreference.ExtensionAware), new BlockStatement(Collections.emptyList())).toString(syntax()));
//    }

    String expectedGradleBlock__foo_extension_selector_using_ExtensionAware_API__empty_block();

    // literal selector
    // method call selector
    // empty block
    // single line block
    // multi-line block

    // closure based block
    // action based block

    // explicit it
    // renamed it
}
