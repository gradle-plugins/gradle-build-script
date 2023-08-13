package dev.gradleplugins.buildscript;

import dev.gradleplugins.buildscript.syntax.Syntax;
import org.junit.jupiter.api.Test;

import static dev.gradleplugins.buildscript.ast.expressions.AssignmentExpression.assign;
import static dev.gradleplugins.buildscript.syntax.Syntax.string;
import static dev.gradleplugins.buildscript.ast.expressions.VariableDeclarationExpression.val;
import static dev.gradleplugins.buildscript.ast.expressions.VariableDeclarationExpression.var;
import static org.junit.jupiter.api.Assertions.assertEquals;

public interface VariableDeclarationExpressionTester {
    Syntax syntax();

    @Test
    default void testValDeclaration() {
        assertEquals(expectedVariableDeclaration__val_myVar__initialized_with__foo_string(),
                val("myVar", assign(string("foo"))).toString(syntax()));
    }

    String expectedVariableDeclaration__val_myVar__initialized_with__foo_string();

    @Test
    default void testVarDeclaration() {
        assertEquals(expectedVariableDeclaration__var_myVar__initialized_with__bar_string(),
                var("myVar", assign(string("bar"))).toString(syntax()));
    }

    String expectedVariableDeclaration__var_myVar__initialized_with__bar_string();
}
