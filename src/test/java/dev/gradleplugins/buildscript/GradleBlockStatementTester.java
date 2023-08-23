package dev.gradleplugins.buildscript;

import dev.gradleplugins.buildscript.ast.expressions.DelegateExpression;
import dev.gradleplugins.buildscript.ast.expressions.ItExpression;
import dev.gradleplugins.buildscript.ast.statements.GradleBlockStatement;
import dev.gradleplugins.buildscript.ast.statements.MultiStatement;
import dev.gradleplugins.buildscript.ast.type.ReferenceType;
import dev.gradleplugins.buildscript.syntax.Syntax;
import dev.gradleplugins.buildscript.syntax.normalizer.UseExplicitItTransformer;
import dev.gradleplugins.buildscript.syntax.normalizer.UseExtensionAwareApiTransformer;
import dev.gradleplugins.buildscript.syntax.normalizer.UseGetterTransformer;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static dev.gradleplugins.buildscript.ast.expressions.AssignmentExpression.assign;
import static dev.gradleplugins.buildscript.ast.expressions.MethodCallExpression.call;
import static dev.gradleplugins.buildscript.ast.expressions.PropertyAccessExpression.extensionProperty;
import static dev.gradleplugins.buildscript.ast.expressions.VariableDeclarationExpression.val;
import static dev.gradleplugins.buildscript.syntax.Syntax.literal;
import static dev.gradleplugins.buildscript.syntax.Syntax.string;
import static org.junit.jupiter.api.Assertions.assertEquals;

public interface GradleBlockStatementTester {
    Syntax syntax();

    @Test
    default void testLiteralBlockSelector() {
        assertEquals(expectedGradleBlock__foo_literal_selector__val_myVar__assignedTo__bar_string(),
                GradleBlockStatement.newBuilder()
                        .withSelector(literal("foo"))
                        .withBody(val("myVar", assign(string("bar"))).toStatement())
                        .build().toString(syntax()));
    }

    String expectedGradleBlock__foo_literal_selector__val_myVar__assignedTo__bar_string();

    @Test
    default void testMethodBlockSelector() {
        assertEquals(expectedGradleBlock__register_method__foo_string_selector__val_myVar__assignedTo__bar_string(),
                GradleBlockStatement.newBuilder()
                        .withSelector(call("register", string("foo")))
                        .withBody(val("myVar", assign(string("bar"))).toStatement())
                        .build().toString(syntax()));
    }

    String expectedGradleBlock__register_method__foo_string_selector__val_myVar__assignedTo__bar_string();

    @Test
    default void testLiteralBlockSelectorWithEmptyBlock() {
        assertEquals(expectedGradleBlock__foo_literal_selector__empty_block(),
                GradleBlockStatement.newBuilder()
                        .withSelector(literal("foo"))
                        .withEmptyBody()
                        .build().toString(syntax()));
    }

    String expectedGradleBlock__foo_literal_selector__empty_block();

    @Test
    default void testLiteralBlockSelectorWithSingleStatementBlock() {
        DelegateExpression delegate = new DelegateExpression();
        assertEquals(expectedGradleBlock__foo_literal_selector__call_someMethod_asStatement(),
                GradleBlockStatement.newBuilder()
                        .withSelector(literal("foo"))
                        .withDelegate(delegate)
                        .withBody(call(delegate, "someMethod").toStatement())
                        .build().toString(syntax()));
    }

    String expectedGradleBlock__foo_literal_selector__call_someMethod_asStatement();

    @Test
    default void testLiteralBlockSelectorWithSingleExpressionBlock() {
        DelegateExpression delegate = new DelegateExpression();
        assertEquals(expectedGradleBlock__foo_literal_selector__call_someMethod_asExpression(),
                GradleBlockStatement.newBuilder()
                        .withSelector(literal("foo"))
                        .withDelegate(delegate)
                        .withBody(call(delegate, "someMethod"))
                        .build().toString(syntax()));
    }

    String expectedGradleBlock__foo_literal_selector__call_someMethod_asExpression();

    @Test
    default void testLiteralBlockSelectorWithMultiStatementBlock() {
        DelegateExpression delegate = new DelegateExpression();
        assertEquals(expectedGradleBlock__foo_literal_selector__call_someMethod__call_someOtherMethod(),
                GradleBlockStatement.newBuilder()
                        .withSelector(literal("foo"))
                        .withDelegate(delegate)
                        .withBody(MultiStatement.of(Arrays.asList(
                                call(delegate, "someMethod").toStatement(),
                                call(delegate, "someOtherMethod").toStatement())))
                        .build().toString(syntax()));
    }

    String expectedGradleBlock__foo_literal_selector__call_someMethod__call_someOtherMethod();

    @Test
    default void testExtensionBlockSelector() {
        assertEquals(expectedGradleBlock__foo_extension_selector__empty_block(),
                GradleBlockStatement.newBuilder()
                        .withSelector(extensionProperty("foo"))
                        .withEmptyBody()
                        .build().toString(syntax()));
    }

    String expectedGradleBlock__foo_extension_selector__empty_block();

    @Test
    default void testExtensionBlockSelectorPreferringExtensionAwareApi() {
        assertEquals(expectedGradleBlock__foo_extension_selector_using_ExtensionAware_API__empty_block(),
                GradleBlockStatement.newBuilder()
                        .withSelector(extensionProperty("foo"))
                        .withEmptyBody()
                        .build().accept(new UseExtensionAwareApiTransformer()).toString(syntax()));
    }

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

    @Test
    default void testBlockSelectorExplicitIt_delegate() {
        DelegateExpression delegate = new DelegateExpression();
        assertEquals(expectedGradleBlock__foo_literal_selector__explicit_it_someMethod_bar_string(),
                GradleBlockStatement.newBuilder()
                        .withSelector(literal("foo"))
                        .withDelegate(delegate)
                        .withBody(call(delegate, "someMethod", string("bar")))
                        .build().accept(new UseExplicitItTransformer()).toString(syntax()));
    }

    @Test
    default void testBlockSelectorExplicitIt_it() {
        ItExpression it = new ItExpression();
        assertEquals(expectedGradleBlock__foo_literal_selector__explicit_it_someMethod_bar_string(),
                GradleBlockStatement.newBuilder()
                        .withSelector(literal("foo"))
                        .withBody(call(it, "someMethod", string("bar")))
                        .build().accept(new UseExplicitItTransformer()).toString(syntax()));
    }

    String expectedGradleBlock__foo_literal_selector__explicit_it_someMethod_bar_string();

    @Test
    default void testBlockSelectorExplicitNamed() {
        DelegateExpression delegate = new DelegateExpression();
        assertEquals(expectedGradleBlock__foo_literal_selector__explicit_it_named_task_someMethod_bar_string(),
                GradleBlockStatement.newBuilder()
                        .withSelector(literal("foo"))
                        .withDelegate(delegate)
                        .withBody(call(delegate, "someMethod", string("bar")))
                        .build().accept(new UseExplicitItTransformer("task")).toString(syntax()));
    }

    @Test
    default void testBlockSelectorExplicitNamedIt() {
        ItExpression it = new ItExpression();
        assertEquals(expectedGradleBlock__foo_literal_selector__explicit_it_named_task_someMethod_bar_string(),
                GradleBlockStatement.newBuilder()
                        .withSelector(literal("foo"))
                        .withIt(it)
                        .withBody(call(it, "someMethod", string("bar")))
                        .build().accept(new UseExplicitItTransformer("task")).toString(syntax()));
    }

    String expectedGradleBlock__foo_literal_selector__explicit_it_named_task_someMethod_bar_string();

    @Test
    default void testBlockSelectorExplicitNamedAndType() {
        DelegateExpression delegate = new DelegateExpression();
        assertEquals(expectedGradleBlock__foo_literal_selector__Task_task__explicit_it_named_task_someMethod_bar_string(),
                GradleBlockStatement.newBuilder()
                        .withSelector(literal("foo"))
                        .withDelegate(delegate)
                        .withBody(call(delegate, "someMethod", string("bar")))
                        .build().accept(new UseExplicitItTransformer("task", new ReferenceType("Task"))).toString(syntax()));
    }

    @Test
    default void testBlockSelectorExplicitNamedAndTypeIt() {
        ItExpression it = new ItExpression();
        assertEquals(expectedGradleBlock__foo_literal_selector__Task_task__explicit_it_named_task_someMethod_bar_string(),
                GradleBlockStatement.newBuilder()
                        .withSelector(literal("foo"))
                        .withIt(it)
                        .withBody(call(it, "someMethod", string("bar")))
                        .build().accept(new UseExplicitItTransformer("task", new ReferenceType("Task"))).toString(syntax()));
    }

    String expectedGradleBlock__foo_literal_selector__Task_task__explicit_it_named_task_someMethod_bar_string();


    @Test
    default void testBlockSelectorGetter() {
        DelegateExpression delegate = new DelegateExpression();
        assertEquals(expectedGradleBlock__foo_literal_selector__it_as_getter_someMethod_bar_string(),
                GradleBlockStatement.newBuilder()
                        .withSelector(literal("foo"))
                        .withDelegate(delegate)
                        .withBody(call(delegate, "someMethod", string("bar")))
                        .build().accept(new UseGetterTransformer()).toString(syntax()));
    }

    @Test
    default void testBlockSelectorGetter_it() {
        ItExpression it = new ItExpression();
        assertEquals(expectedGradleBlock__foo_literal_selector__it_as_getter_someMethod_bar_string(),
                GradleBlockStatement.newBuilder()
                        .withSelector(literal("foo"))
                        .withIt(it)
                        .withBody(call(it, "someMethod", string("bar")))
                        .build().accept(new UseGetterTransformer()).toString(syntax()));
    }

    String expectedGradleBlock__foo_literal_selector__it_as_getter_someMethod_bar_string();
}
