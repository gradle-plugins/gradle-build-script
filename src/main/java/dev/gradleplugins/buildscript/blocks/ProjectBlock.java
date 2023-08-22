package dev.gradleplugins.buildscript.blocks;

import dev.gradleplugins.buildscript.ast.expressions.LiteralExpression;
import dev.gradleplugins.buildscript.ast.statements.GradleBlockStatement;
import dev.gradleplugins.buildscript.ast.type.UnknownType;

import java.util.function.Consumer;

public final class ProjectBlock extends GradleBlockStatement.Body.Builder<ProjectBlock> {
    public ProjectBlock() {
        super(ProjectBlock.class);
    }

    public ProjectBlock dependencies(Consumer<? super DependenciesBlock> configureAction) {
        add(GradleBuildScriptBlocks.dependencies(configureAction));
        return this;
    }

    public ProjectBlock configurations(Consumer<? super ConfigurationsBlock> configureAction) {
        add(GradleBuildScriptBlocks.configurations(configureAction));
        return this;
    }

    public ProjectBlock repositories(Consumer<? super RepositoriesBlock> configureAction) {
        add(GradleBuildScriptBlocks.repositories(configureAction));
        return this;
    }

    public ProjectBlock tasks(Consumer<? super GradleBlockStatement.Body.Builder<?>> configureAction) {
        final GradleBlockStatement.Body.Builder<?> block = GradleBlockStatement.Body.newBuilder();
        configureAction.accept(block);
        add(new GradleBlockStatement(new LiteralExpression(UnknownType.unknownType(), "tasks"), block.build()));
        return this;
    }

    @Override
    protected ProjectBlock newBuilder() {
        return new ProjectBlock();
    }
}
