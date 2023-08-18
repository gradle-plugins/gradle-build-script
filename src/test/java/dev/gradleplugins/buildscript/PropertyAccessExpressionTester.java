package dev.gradleplugins.buildscript;

import dev.gradleplugins.buildscript.ast.expressions.InfixExpression;
import dev.gradleplugins.buildscript.ast.expressions.MethodCallExpression;
import dev.gradleplugins.buildscript.ast.expressions.PropertyAccessExpression;
import dev.gradleplugins.buildscript.syntax.Syntax;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static dev.gradleplugins.buildscript.ast.type.PrimitiveType.booleanType;
import static dev.gradleplugins.buildscript.ast.type.UnknownType.unknownType;
import static dev.gradleplugins.buildscript.syntax.Syntax.bool;
import static dev.gradleplugins.buildscript.syntax.Syntax.literal;
import static dev.gradleplugins.buildscript.syntax.Syntax.string;
import static org.junit.jupiter.api.Assertions.assertEquals;

public interface PropertyAccessExpressionTester {
    Syntax syntax();

    @Test
    default void testPlainPropertyAccess() {
        assertEquals(expectedPlainProperty__myVar_getFoo(),
                new PropertyAccessExpression(unknownType(), PropertyAccessExpression.AccessType.PLAIN, literal("myVar"), "foo").toString(syntax()));
    }

    String expectedPlainProperty__myVar_getFoo();

    @Test
    default void testPlainPropertyAssign() {
        assertEquals(expectedPlainProperty__myVar_setFoo__to__bar_string(),
                new InfixExpression(new PropertyAccessExpression(unknownType(), PropertyAccessExpression.AccessType.PLAIN, literal("myVar"), "foo"), InfixExpression.Operator.Assignment, string("bar")).toString(syntax()));
    }

    String expectedPlainProperty__myVar_setFoo__to__bar_string();

    @Test
    default void testPlainPropertyBooleanAccess() {
        assertEquals(expectedPlainProperty__myVar_isFoo(),
                new PropertyAccessExpression(booleanType(), PropertyAccessExpression.AccessType.PLAIN, literal("myVar"), "foo").toString(syntax()));
    }

    String expectedPlainProperty__myVar_isFoo();

    @Test
    default void testPlainPropertyBooleanAssign() {
        assertEquals(expectedPlainProperty__myVar_setFoo__to__true_boolean(),
                new InfixExpression(new PropertyAccessExpression(booleanType(), PropertyAccessExpression.AccessType.PLAIN, literal("myVar"), "foo"), InfixExpression.Operator.Assignment, bool(true)).toString(syntax()));
    }

    String expectedPlainProperty__myVar_setFoo__to__true_boolean();




    @Test
    default void testFieldPropertyAccess() {
        assertEquals(expectedFieldProperty__myVar_foo(),
                new PropertyAccessExpression(unknownType(), PropertyAccessExpression.AccessType.FIELD, literal("myVar"), "foo").toString(syntax()));
    }

    String expectedFieldProperty__myVar_foo();

    @Test
    default void testFieldPropertyAssign() {
        assertEquals(expectedFieldProperty__myVar_foo__assign__bar_string(),
                new InfixExpression(new PropertyAccessExpression(unknownType(), PropertyAccessExpression.AccessType.FIELD, literal("myVar"), "foo"), InfixExpression.Operator.Assignment, string("bar")).toString(syntax()));
    }

    String expectedFieldProperty__myVar_foo__assign__bar_string();




    @Test
    default void testExtraPropertyAccess() {
        assertEquals(expectedExtraProperty__myVar_foo(),
                new PropertyAccessExpression(unknownType(), PropertyAccessExpression.AccessType.EXTRA, literal("myVar"), "foo").toString(syntax()));
    }

    String expectedExtraProperty__myVar_foo();

    @Test
    default void testExtraPropertyAssign() {
        assertEquals(expectedExtraProperty__myVar_foo__setTo__bar_string(),
                new InfixExpression(new PropertyAccessExpression(unknownType(), PropertyAccessExpression.AccessType.EXTRA, literal("myVar"), "foo"), InfixExpression.Operator.Assignment, string("bar")).toString(syntax()));
    }

    String expectedExtraProperty__myVar_foo__setTo__bar_string();

//    @Test
//    default void testExtraPropertyDelegatedAccess() {
//        assertEquals(expectedExtraProperty__foo__by__myVar(),
//                new PropertyAccessExpression(unknownType(), PropertyAccessExpression.AccessType.EXTRA, literal("myVar"), "foo").byDelegation().toString(syntax()));
//    }

    String expectedExtraProperty__foo__by__myVar();




    @Test
    default void testGradlePropertyAccess() {
        assertEquals(expectedGradleProperty__myVar_getFoo(),
                new PropertyAccessExpression(unknownType(), PropertyAccessExpression.AccessType.GRADLE, literal("myVar"), "foo").toString(syntax()));
    }

    String expectedGradleProperty__myVar_getFoo();

    @Test
    default void testGradlePropertyAssign() {
        assertEquals(expectedGradleProperty__myVar_getFoo__set__bar_string(),
                new MethodCallExpression(new PropertyAccessExpression(unknownType(), PropertyAccessExpression.AccessType.GRADLE, literal("myVar"), "foo"), "set", Collections.singletonList(string("bar"))).toString(syntax()));
    }

    String expectedGradleProperty__myVar_getFoo__set__bar_string();

//    @Test
//    default void testGradlePropertyDecoratedAssign() {
//        assertEquals(expectedGradleProperty__myVar_getFoo__assign__bar_string(),
//                new PropertyAssignExpression(new GradlePropertyAccessExpression(unknownType(), literal("myVar"), "foo", true), string("bar")).toString(syntax()));
//    }

    String expectedGradleProperty__myVar_getFoo__assign__bar_string();


    @Test
    default void testExtensionAccess() {
        assertEquals(expectedGradleExtension__obj_myExtension(), new PropertyAccessExpression(unknownType(), PropertyAccessExpression.AccessType.EXTENSION, literal("obj"), "myExtension").toString(syntax()));
    }

    String expectedGradleExtension__obj_myExtension();
}
