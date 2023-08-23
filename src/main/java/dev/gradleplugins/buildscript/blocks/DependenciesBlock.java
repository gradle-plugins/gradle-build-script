package dev.gradleplugins.buildscript.blocks;

import dev.gradleplugins.buildscript.ast.statements.GradleBlockStatement;

import static dev.gradleplugins.buildscript.ast.expressions.MethodCallExpression.call;
import static dev.gradleplugins.buildscript.syntax.Syntax.string;

public final class DependenciesBlock extends GradleBlockStatement.BlockBuilder<DependenciesBlock> {
    public DependenciesBlock() {
        super(DependenciesBlock.class);
    }

    public DependenciesBlock add(String name, String notation) {
        add(call(name, string(notation)));
        return this;
    }

    public DependenciesBlock add(String name, DependencyNotation notation) {
        add(call(name, notation.asExpression()));
        return this;
    }

    @Override
    protected DependenciesBlock newBuilder() {
        return new DependenciesBlock();
    }
}
