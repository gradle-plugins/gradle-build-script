package dev.gradleplugins.buildscript.blocks;

import dev.gradleplugins.buildscript.ast.statements.GradleBlockStatement;

import java.util.function.Consumer;

public final class GradleBlock extends GradleBlockStatement.BlockBuilder<GradleBlock> {
    public GradleBlock() {
        super(GradleBlock.class);
    }

    public GradleBlock settingsEvaluated(Consumer<? super SettingsBlock> configureAction) {
        add(GradleBuildScriptBlocks.settingsEvaluated(configureAction));
        return this;
    }

    public GradleBlock beforeSettings(Consumer<? super SettingsBlock> configureAction) {
        add(GradleBuildScriptBlocks.beforeSettings(configureAction));
        return this;
    }

    @Override
    protected GradleBlock newBuilder() {
        return new GradleBlock();
    }
}
