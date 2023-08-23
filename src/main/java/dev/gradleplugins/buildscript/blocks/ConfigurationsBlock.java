package dev.gradleplugins.buildscript.blocks;

import dev.gradleplugins.buildscript.ast.statements.GradleBlockStatement;

public final class ConfigurationsBlock extends GradleBlockStatement.BlockBuilder<ConfigurationsBlock> {
    public ConfigurationsBlock() {
        super(ConfigurationsBlock.class);
    }

    @Override
    protected ConfigurationsBlock newBuilder() {
        return new ConfigurationsBlock();
    }
}
