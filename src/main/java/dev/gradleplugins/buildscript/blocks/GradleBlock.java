package dev.gradleplugins.buildscript.blocks;

import dev.gradleplugins.buildscript.ast.expressions.LiteralExpression;
import dev.gradleplugins.buildscript.ast.statements.BlockStatement;
import dev.gradleplugins.buildscript.ast.statements.GradleBlockStatement;

import java.util.function.Consumer;

import static dev.gradleplugins.buildscript.ast.type.UnknownType.unknownType;

public final class GradleBlock extends BlockStatement.Builder<GradleBlock> {
    public GradleBlock() {
        super(GradleBlock.class);
    }

    public GradleBlock settingsEvaluated(Consumer<? super SettingsBlock> action) {
        final SettingsBlock block = new SettingsBlock();
        action.accept(block);
        add(new GradleBlockStatement(new LiteralExpression(unknownType(), "settingsEvaluated"), block.build()).useExplicitIt());
        return this;
    }

    public GradleBlock beforeSettings(Consumer<? super SettingsBlock> action) {
        final SettingsBlock block = new SettingsBlock();
        action.accept(block);
        add(new GradleBlockStatement(new LiteralExpression(unknownType(), "beforeSettings"), block.build()).useExplicitIt());
        return this;
    }

    @Override
    protected GradleBlock newBuilder() {
        return new GradleBlock();
    }
}
