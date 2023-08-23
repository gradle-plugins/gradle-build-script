package dev.gradleplugins.buildscript;

import dev.gradleplugins.buildscript.ast.expressions.LiteralExpression;
import dev.gradleplugins.buildscript.ast.expressions.PrefixExpression;
import dev.gradleplugins.buildscript.syntax.Syntax;
import org.junit.jupiter.api.Test;

import static dev.gradleplugins.buildscript.ast.type.UnknownType.unknownType;
import static org.junit.jupiter.api.Assertions.assertEquals;

public interface NotExpressionTester {
    Syntax syntax();

    @Test
    default void testNotExpression() {
        assertEquals(expectedNotExpression__foo_literal(), new PrefixExpression(PrefixExpression.Not, new LiteralExpression(unknownType(), "foo")).toString(syntax()));
    }

    String expectedNotExpression__foo_literal();
}
