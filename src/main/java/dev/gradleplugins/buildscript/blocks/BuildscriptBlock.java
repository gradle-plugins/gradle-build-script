package dev.gradleplugins.buildscript.blocks;

import dev.gradleplugins.buildscript.ast.statements.GradleBlockStatement;

import java.util.function.Consumer;

public final class BuildscriptBlock extends GradleBlockStatement.BlockBuilder<BuildscriptBlock> {
    public static Consumer<DependenciesBlock> classpath(DependencyNotation notation) {
        return builder -> builder.add("classpath", notation);
    }

    public BuildscriptBlock() {
        super(BuildscriptBlock.class);
    }

    public BuildscriptBlock dependencies(Consumer<? super DependenciesBlock> configureAction) {
        add(GradleBuildScriptBlocks.dependencies(configureAction));
        return this;
    }

    public BuildscriptBlock configurations(Consumer<? super ConfigurationsBlock> configureAction) {
        add(GradleBuildScriptBlocks.configurations(configureAction));
        return this;
    }

    public BuildscriptBlock repositories(Consumer<? super RepositoriesBlock> configureAction) {
        add(GradleBuildScriptBlocks.repositories(configureAction));
        return this;
    }

    @Override
    protected BuildscriptBlock newBuilder() {
        return new BuildscriptBlock();
    }
}
