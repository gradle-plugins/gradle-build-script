package dev.gradleplugins.buildscript.syntax.normalizer;

import dev.gradleplugins.buildscript.ast.expressions.LiteralExpression;
import dev.gradleplugins.buildscript.ast.expressions.MethodCallExpression;
import dev.gradleplugins.buildscript.ast.expressions.StringLiteralExpression;
import dev.gradleplugins.buildscript.ast.statements.ExpressionStatement;
import dev.gradleplugins.buildscript.ast.statements.GradleBlockStatement;
import dev.gradleplugins.buildscript.ast.statements.MultiStatement;
import dev.gradleplugins.buildscript.ast.statements.Statement;
import dev.gradleplugins.buildscript.blocks.PluginsDslBlock;
import dev.gradleplugins.buildscript.syntax.ASTTransformer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static dev.gradleplugins.buildscript.ast.expressions.CurrentScopeExpression.current;

// Convert plugin DSL block into `project.getPluginManager().apply("...")` APIs
public final class PluginDslToPluginManagerTransformer implements ASTTransformer {
    @Override
    public Statement visit(GradleBlockStatement statement) {
        if (statement.getSelector() instanceof LiteralExpression && ((LiteralExpression) statement.getSelector()).get().equals("plugins")) {
            final List<Statement> statements = new ArrayList<>();
            ((Iterable<Statement>) statement.getBlock().accept(new ASTTransformer() {
                @Override
                public Statement visit(PluginsDslBlock.IdStatement statement) {
                    return new ExpressionStatement(new MethodCallExpression(new MethodCallExpression(current(), "getPluginManager", Collections.emptyList()), "apply", Collections.singletonList(new StringLiteralExpression(statement.getPluginId())))).accept(PluginDslToPluginManagerTransformer.this);
                }
            })).forEach(statements::add);
            return MultiStatement.of(statements);
        }
        return ASTTransformer.super.visit(statement);
    }
}
