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
import static java.util.Objects.requireNonNull;

public final class GradleSettingsFile extends AbstractBuildScriptFile implements SettingsBuildScript, GradleBuildScriptFile {
    private BuildScriptLocation location;
    private PluginsDslBlock pluginsDslBlock;
    private PluginManagementBlock pluginManagementBlock;
    private BuildscriptBlock buildScriptBlock;
    private final List<Statement> statements = new ArrayList<>();

    private GradleSettingsFile(BuildScriptLocation location) {
        this.location = location;
    }

    public static GradleSettingsFile inDirectory(Path location) {
		requireNonNull(location, "'location' must not be null");
        return new GradleSettingsFile(new BuildScriptLocation(location.resolve("settings"), GradleDsl.GROOVY)).writeScriptToFileSystem();
    }

	public static GradleSettingsFile fromDirectory(Path location) {
		requireNonNull(location, "'location' must not be null");

		Path file = location.resolve("settings.gradle");
		if (!Files.exists(file)) {
			file = location.resolve("settings.gradle.kts");
		}
		return new GradleSettingsFile(BuildScriptLocation.of(file));
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
            Files.deleteIfExists(location.getPath());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        location = location.use(GradleDsl.KOTLIN);
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

    private GradleSettingsFile writeScriptToFileSystem() {
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
        location.withPath((filePath, dsl) -> {
            try {
                Files.write(filePath, MultiStatement.of(stmt).toString(dsl).getBytes(StandardCharsets.UTF_8));
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        });

        return this;
    }
}
