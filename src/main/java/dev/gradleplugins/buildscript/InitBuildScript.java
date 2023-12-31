package dev.gradleplugins.buildscript;

import dev.gradleplugins.buildscript.blocks.BuildscriptBlock;
import dev.gradleplugins.buildscript.blocks.GradleBlock;

import java.util.function.Consumer;

public interface InitBuildScript extends BuildScript<GradleBlock> {
    @Override
    default BuildScript<GradleBlock> buildscript(Consumer<? super BuildscriptBlock> configureAction) {
        return initscript(configureAction);
    }

    @Override
    InitBuildScript configure(Consumer<? super GradleBlock> configureAction);

    InitBuildScript initscript(Consumer<? super BuildscriptBlock> configureAction);
}
