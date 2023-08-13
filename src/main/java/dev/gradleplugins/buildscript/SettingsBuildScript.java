package dev.gradleplugins.buildscript;

import dev.gradleplugins.buildscript.blocks.BuildscriptBlock;
import dev.gradleplugins.buildscript.blocks.PluginManagementBlock;
import dev.gradleplugins.buildscript.blocks.PluginsDslBlock;
import dev.gradleplugins.buildscript.blocks.SettingsBlock;

import java.util.function.Consumer;

public interface SettingsBuildScript extends BuildScript<SettingsBlock> {
    @Override
    SettingsBuildScript configure(Consumer<? super SettingsBlock> configureAction);

    SettingsBuildScript plugins(Consumer<? super PluginsDslBlock> configureAction);

    SettingsBuildScript pluginManagement(Consumer<? super PluginManagementBlock> configureAction);

    SettingsBuildScript buildscript(Consumer<? super BuildscriptBlock> configureAction);
}
