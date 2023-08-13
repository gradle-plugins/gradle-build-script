package dev.gradleplugins.buildscript.blocks;

import dev.gradleplugins.buildscript.ast.statements.BlockStatement;

public final class SettingsBlock extends BlockStatement.Builder<SettingsBlock> {
    public SettingsBlock() {
        super(SettingsBlock.class);
    }

    @Override
    protected SettingsBlock newBuilder() {
        return new SettingsBlock();
    }
}
