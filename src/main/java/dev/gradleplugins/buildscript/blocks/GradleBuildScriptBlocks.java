package dev.gradleplugins.buildscript.blocks;

import dev.gradleplugins.buildscript.ast.expressions.LiteralExpression;
import dev.gradleplugins.buildscript.ast.statements.BlockStatement;
import dev.gradleplugins.buildscript.ast.statements.GradleBlockStatement;

import java.util.function.Consumer;

import static dev.gradleplugins.buildscript.ast.expressions.PropertyAccessExpression.plainProperty;
import static dev.gradleplugins.buildscript.ast.type.UnknownType.unknownType;
import static dev.gradleplugins.buildscript.syntax.Syntax.string;

public final class GradleBuildScriptBlocks {
    public static GradleBlockStatement doLast(Consumer<? super BlockStatement.Builder<?>> configureAction) {
        return GradleBlockStatement.block("doLast", configureAction);
    }

    public static GradleBlockStatement registerTask(String taskName, Consumer<? super BlockStatement.Builder<?>> configureAction) {
        return plainProperty("tasks").call("register", string(taskName)).block(configureAction);
    }

    public static GradleBlockStatement configurations(Consumer<? super ConfigurationsBlock> configureAction) {
        final ConfigurationsBlock block = new ConfigurationsBlock();
        configureAction.accept(block);
        return new GradleBlockStatement(new LiteralExpression(unknownType(), "configurations"), block.build());
    }

    public static GradleBlockStatement dependencies(Consumer<? super DependenciesBlock> configureAction) {
        final DependenciesBlock block = new DependenciesBlock();
        configureAction.accept(block);
        return new GradleBlockStatement(new LiteralExpression(unknownType(), "dependencies"), block.build());
    }

    public static GradleBlockStatement pluginManagement(Consumer<? super PluginManagementBlock> configureAction) {
        final PluginManagementBlock block = new PluginManagementBlock();
        configureAction.accept(block);
        return new GradleBlockStatement(new LiteralExpression(unknownType(), "pluginManagement"), block.build());
    }

    public static GradleBlockStatement plugins(Consumer<? super PluginsDslBlock> configureAction) {
        final PluginsDslBlock block = new PluginsDslBlock();
        configureAction.accept(block);
        return new GradleBlockStatement(new LiteralExpression(unknownType(), "plugins"), block.build());
    }

    public static GradleBlockStatement allprojects(Consumer<? super ProjectBlock> configureAction) {
        final ProjectBlock block = new ProjectBlock();
        configureAction.accept(block);
        return new GradleBlockStatement(new LiteralExpression(unknownType(), "allprojects"), block.build());
    }

    public static GradleBlockStatement subprojects(Consumer<? super ProjectBlock> configureAction) {
        final ProjectBlock block = new ProjectBlock();
        configureAction.accept(block);
        return new GradleBlockStatement(new LiteralExpression(unknownType(), "subprojects"), block.build());
    }

    public static GradleBlockStatement afterEvaluate(Consumer<? super ProjectBlock> configureAction) {
        final ProjectBlock block = new ProjectBlock();
        configureAction.accept(block);
        return new GradleBlockStatement(new LiteralExpression(unknownType(), "afterEvaluate"), block.build());
    }

    public static GradleBlockStatement repositories(Consumer<? super RepositoriesBlock> configureAction) {
        final RepositoriesBlock block = new RepositoriesBlock();
        configureAction.accept(block);
        return new GradleBlockStatement(new LiteralExpression(unknownType(), "repositories"), block.build());
    }

    public static GradleBlockStatement buildscript(Consumer<? super BuildscriptBlock> configureAction) {
        final BuildscriptBlock block = new BuildscriptBlock();
        configureAction.accept(block);
        return new GradleBlockStatement(new LiteralExpression(unknownType(), "buildscript"), block.build());
    }

    public static GradleBlockStatement initscript(Consumer<? super BuildscriptBlock> configureAction) {
        final BuildscriptBlock block = new BuildscriptBlock();
        configureAction.accept(block);
        return new GradleBlockStatement(new LiteralExpression(unknownType(), "initscript"), block.build());
    }

    //    public GradleBlock initscript(Consumer<? super BuildScriptBlock> configureAction) {
//        add(BuildScriptBlock.initscript(configureAction));
//        return this;
//    }
}
