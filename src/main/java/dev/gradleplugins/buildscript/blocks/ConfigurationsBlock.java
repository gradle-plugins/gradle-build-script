package dev.gradleplugins.buildscript.blocks;

import dev.gradleplugins.buildscript.ast.statements.BlockStatement;

public final class ConfigurationsBlock extends BlockStatement.Builder<ConfigurationsBlock> {
    public ConfigurationsBlock() {
        super(ConfigurationsBlock.class);
    }

    @Override
    protected ConfigurationsBlock newBuilder() {
        return new ConfigurationsBlock();
    }
}
