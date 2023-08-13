package dev.gradleplugins.buildscript;

import dev.gradleplugins.buildscript.blocks.BuildscriptBlock;

import java.util.function.Consumer;

public interface BuildScript<BlockType> {
    BuildScript<BlockType> buildscript(Consumer<? super BuildscriptBlock> configureAction);

    BuildScript<BlockType> configure(Consumer<? super BlockType> configureAction);
}
