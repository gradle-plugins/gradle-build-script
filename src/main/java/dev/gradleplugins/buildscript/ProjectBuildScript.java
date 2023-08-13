package dev.gradleplugins.buildscript;

import dev.gradleplugins.buildscript.blocks.BuildscriptBlock;
import dev.gradleplugins.buildscript.blocks.PluginsDslBlock;
import dev.gradleplugins.buildscript.blocks.ProjectBlock;

import java.util.function.Consumer;

public interface ProjectBuildScript extends BuildScript<ProjectBlock> {
    ProjectBuildScript plugins(Consumer<? super PluginsDslBlock> configureAction);

    @Override
    ProjectBuildScript configure(Consumer<? super ProjectBlock> configureAction);

    ProjectBuildScript buildscript(Consumer<? super BuildscriptBlock> configureAction);
}
