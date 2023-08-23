package dev.gradleplugins.buildscript.syntax.normalizer;

import dev.gradleplugins.buildscript.ast.expressions.MethodCallExpression;
import dev.gradleplugins.buildscript.ast.expressions.PropertyAccessExpression;
import dev.gradleplugins.buildscript.ast.statements.GradleBlockStatement;
import dev.gradleplugins.buildscript.ast.statements.Statement;
import dev.gradleplugins.buildscript.ast.type.ReferenceType;
import dev.gradleplugins.buildscript.syntax.ASTTransformer;
import org.gradle.api.plugins.ExtensionContainer;

import java.util.Collections;

import static dev.gradleplugins.buildscript.syntax.Syntax.string;

public final class UseExtensionAwareApiTransformer implements ASTTransformer {
    @Override
    public Statement visit(GradleBlockStatement statement) {
        if (statement.getSelector() instanceof PropertyAccessExpression && ((PropertyAccessExpression) statement.getSelector()).getAccessType().equals(PropertyAccessExpression.AccessType.EXTENSION)) {
            final PropertyAccessExpression selector = (PropertyAccessExpression) statement.getSelector();
            // FIXME: convert block into lambda/closure
            return statement.toBuilder().withSelector(new MethodCallExpression(new PropertyAccessExpression(ReferenceType.of(ExtensionContainer.class), PropertyAccessExpression.AccessType.PLAIN, selector.getObjectExpression(), "extensions"), "configure", Collections.singletonList(string(selector.getPropertyName())))).build();
        }
        return statement;
    }
}
