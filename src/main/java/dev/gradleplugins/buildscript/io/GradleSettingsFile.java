package dev.gradleplugins.buildscript.io;

import dev.gradleplugins.buildscript.GradleDsl;
import dev.gradleplugins.buildscript.SettingsBuildScript;
import dev.gradleplugins.buildscript.ast.expressions.Expression;
import dev.gradleplugins.buildscript.ast.statements.ExpressionStatement;
import dev.gradleplugins.buildscript.ast.statements.GradleBlockStatement;
import dev.gradleplugins.buildscript.ast.statements.MultiStatement;
import dev.gradleplugins.buildscript.ast.statements.Statement;
import dev.gradleplugins.buildscript.blocks.BuildscriptBlock;
import dev.gradleplugins.buildscript.blocks.PluginManagementBlock;
import dev.gradleplugins.buildscript.blocks.PluginsDslBlock;
import dev.gradleplugins.buildscript.blocks.SettingsBlock;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static dev.gradleplugins.buildscript.syntax.Syntax.literal;

public final class GradleSettingsFile implements SettingsBuildScript {
    private final Path location;
    private PluginsDslBlock pluginsDslBlock;
    private PluginManagementBlock pluginManagementBlock;
    private GradleDsl dsl = GradleDsl.GROOVY;
    private BuildscriptBlock buildScriptBlock;
    private final List<Statement> statements = new ArrayList<>();

    private GradleSettingsFile(Path location) {
        this.location = location;
    }

    public static GradleSettingsFile inDirectory(Path location) {
        return new GradleSettingsFile(location).writeScriptToFileSystem();
    }

    @Override
    public GradleSettingsFile plugins(Consumer<? super PluginsDslBlock> configureAction) {
        if (pluginsDslBlock == null) {
            pluginsDslBlock = new PluginsDslBlock();
        }
        configureAction.accept(pluginsDslBlock);
        return writeScriptToFileSystem();
    }

    @Override
    public SettingsBuildScript pluginManagement(Consumer<? super PluginManagementBlock> configureAction) {
        if (pluginManagementBlock == null) {
            pluginManagementBlock = new PluginManagementBlock();
        }
        configureAction.accept(pluginManagementBlock);
        return writeScriptToFileSystem();
    }

    @Override
    public GradleSettingsFile configure(Consumer<? super SettingsBlock> configureAction) {
        throw new UnsupportedOperationException();
    }

    @Override
    public GradleSettingsFile buildscript(Consumer<? super BuildscriptBlock> configureAction) {
        if (buildScriptBlock == null) {
            buildScriptBlock = new BuildscriptBlock();
        }
        configureAction.accept(buildScriptBlock);
        return writeScriptToFileSystem();
    }

    public GradleSettingsFile useKotlinDsl() {
        try {
            Files.deleteIfExists(location.resolve(dsl.fileName("settings")));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        dsl = GradleDsl.KOTLIN;
        return writeScriptToFileSystem();
    }

    public GradleSettingsFile append(Statement statement) {
        statements.add(statement);
        return writeScriptToFileSystem();
    }

    public GradleSettingsFile append(Expression expression) {
        statements.add(new ExpressionStatement(expression));
        return writeScriptToFileSystem();
    }

    public GradleSettingsFile leftShift(Statement statement) {
        return append(statement);
    }

    public GradleSettingsFile leftShift(Expression expression) {
        return append(expression);
    }

    private GradleSettingsFile writeScriptToFileSystem() {
        try {
            List<Statement> stmt = new ArrayList<>();
            if (buildScriptBlock != null) {
                stmt.add(new GradleBlockStatement(literal("buildscript"), buildScriptBlock.build()));
            }
            if (pluginManagementBlock != null) {
                stmt.add(new GradleBlockStatement(literal("pluginManagement"), pluginManagementBlock.build()));
            }
            if (pluginsDslBlock != null) {
                stmt.add(new GradleBlockStatement(literal("plugins"), pluginsDslBlock.build()));
            }
            stmt.addAll(statements);
            Files.write(location.resolve(dsl.fileName("settings")), MultiStatement.of(stmt).toString(dsl).getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        return this;
    }
}
