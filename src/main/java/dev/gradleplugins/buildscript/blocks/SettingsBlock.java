package dev.gradleplugins.buildscript.blocks;

import dev.gradleplugins.buildscript.ast.statements.GradleBlockStatement;

public final class SettingsBlock extends GradleBlockStatement.Body.Builder<SettingsBlock> {
    public SettingsBlock() {
        super(SettingsBlock.class);
    }

    @Override
    protected SettingsBlock newBuilder() {
        return new SettingsBlock();
    }
}
