package dev.gradleplugins.buildscript.blocks;

import dev.gradleplugins.buildscript.ast.statements.BlockStatement;

import java.util.function.Consumer;

public final class PluginManagementBlock extends BlockStatement.Builder<PluginManagementBlock> {
    public PluginManagementBlock() {
        super(PluginManagementBlock.class);
    }

    public PluginManagementBlock plugins(Consumer<? super PluginsDslBlock> configureAction) {
        add(GradleBuildScriptBlocks.plugins(configureAction));
        return this;
    }

    public PluginManagementBlock repositories(Consumer<? super RepositoriesBlock> configureAction) {
        add(GradleBuildScriptBlocks.repositories(configureAction));
        return this;
    }

    @Override
    protected PluginManagementBlock newBuilder() {
        return new PluginManagementBlock();
    }
}
