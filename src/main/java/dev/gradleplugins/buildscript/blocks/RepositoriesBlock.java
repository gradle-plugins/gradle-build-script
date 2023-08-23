package dev.gradleplugins.buildscript.blocks;

import dev.gradleplugins.buildscript.ast.statements.GradleBlockStatement;

public final class RepositoriesBlock extends GradleBlockStatement.BlockBuilder<RepositoriesBlock> {
    public RepositoriesBlock() {
        super(RepositoriesBlock.class);
    }

    @Override
    protected RepositoriesBlock newBuilder() {
        return new RepositoriesBlock();
    }
}
