package dev.gradleplugins.buildscript.blocks;

import dev.gradleplugins.buildscript.ast.statements.BlockStatement;

public final class RepositoriesBlock extends BlockStatement.Builder<RepositoriesBlock> {
    public RepositoriesBlock() {
        super(RepositoriesBlock.class);
    }

    @Override
    protected RepositoriesBlock newBuilder() {
        return new RepositoriesBlock();
    }
}
