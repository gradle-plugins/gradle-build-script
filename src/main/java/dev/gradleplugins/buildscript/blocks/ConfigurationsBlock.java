package dev.gradleplugins.buildscript.blocks;

import dev.gradleplugins.buildscript.ast.statements.GradleBlockStatement;

public final class ConfigurationsBlock extends GradleBlockStatement.Body.Builder<ConfigurationsBlock> {
    public ConfigurationsBlock() {
        super(ConfigurationsBlock.class);
    }

    @Override
    protected ConfigurationsBlock newBuilder() {
        return new ConfigurationsBlock();
    }
}
