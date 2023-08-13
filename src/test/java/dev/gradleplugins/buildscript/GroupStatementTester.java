package dev.gradleplugins.buildscript;

import dev.gradleplugins.buildscript.ast.statements.BlockStatement;
import dev.gradleplugins.buildscript.ast.statements.GroupStatement;
import dev.gradleplugins.buildscript.syntax.Syntax;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static dev.gradleplugins.buildscript.ast.expressions.MethodCallExpression.call;
import static dev.gradleplugins.buildscript.syntax.Syntax.string;
import static org.junit.jupiter.api.Assertions.assertEquals;

public interface GroupStatementTester {
    Syntax syntax();

    @Test
    default void testBlockWithSingleGroupStatement() {
        assertEquals(expectedBlockWithSingleGroup__call_someMethod_no_args(), BlockStatement.newBuilder().add(new GroupStatement(Collections.singletonList(call("someMethod").toStatement()))).build().toString(syntax()));
    }

    String expectedBlockWithSingleGroup__call_someMethod_no_args();

    @Test
    default void testBlockWithMultipleGroupStatements() {
        assertEquals(expectedBlockWithSingleGroup__call_someMethod_no_args__call_someOtherMethod_foo_string(), BlockStatement.newBuilder().add(new GroupStatement(Collections.singletonList(call("someMethod").toStatement()))).add(new GroupStatement(Collections.singletonList(call("someOtherMethod", string("foo")).toStatement()))).build().toString(syntax()));
    }

    String expectedBlockWithSingleGroup__call_someMethod_no_args__call_someOtherMethod_foo_string();
}
