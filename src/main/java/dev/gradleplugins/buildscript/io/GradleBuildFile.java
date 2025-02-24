package dev.gradleplugins.buildscript.io;

import dev.gradleplugins.buildscript.GradleDsl;
import dev.gradleplugins.buildscript.ProjectBuildScript;
import dev.gradleplugins.buildscript.ast.expressions.Expression;
import dev.gradleplugins.buildscript.ast.statements.ExpressionStatement;
import dev.gradleplugins.buildscript.ast.statements.GradleBlockStatement;
import dev.gradleplugins.buildscript.ast.statements.MultiStatement;
import dev.gradleplugins.buildscript.ast.statements.Statement;
import dev.gradleplugins.buildscript.blocks.BuildscriptBlock;
import dev.gradleplugins.buildscript.blocks.PluginsDslBlock;
import dev.gradleplugins.buildscript.blocks.ProjectBlock;

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

public final class GradleBuildFile extends AbstractBuildScriptFile implements ProjectBuildScript, GradleBuildScriptFile {
    private BuildScriptLocation location;
    private PluginsDslBlock pluginsDslBlock;
    private BuildscriptBlock buildScriptBlock;
    private final List<Statement> statements;

	private GradleBuildFile(BuildScriptLocation location) {
		this.location = location;
		this.statements = new ArrayList<>();
	}

	private GradleBuildFile(BuildScriptLocation location, PluginsDslBlock pluginsDslBlock, BuildscriptBlock buildScriptBlock, List<Statement> statements) {
		this.location = location;
		this.pluginsDslBlock = pluginsDslBlock;
		this.buildScriptBlock = buildScriptBlock;
		this.statements = statements;
	}

    public static GradleBuildFile inDirectory(Path location) {
		requireNonNull(location, "'location' must not be null");
        return new GradleBuildFile(new BuildScriptLocation(location.resolve("build"), GradleDsl.GROOVY)).writeScriptToFileSystem();
    }

	public static GradleBuildFile fromDirectory(Path location) {
		requireNonNull(location, "'location' must not be null");

		Path file = location.resolve("build.gradle");
		if (!Files.exists(file)) {
			file = location.resolve("build.gradle.kts");
		}
		return new GradleBuildFile(BuildScriptLocation.of(file));
	}

    @Override
    public GradleBuildFile plugins(Consumer<? super PluginsDslBlock> configureAction) {
        if (pluginsDslBlock == null) {
            pluginsDslBlock = new PluginsDslBlock();
        }
        configureAction.accept(pluginsDslBlock);
        return writeScriptToFileSystem();
    }

    @Override
    public ProjectBuildScript configure(Consumer<? super ProjectBlock> configureAction) {
        final ProjectBlock block = new ProjectBlock();
        configureAction.accept(block);
        statements.add((Statement) block.build().getBody().get());
        return writeScriptToFileSystem();
    }

    @Override
    public ProjectBuildScript buildscript(Consumer<? super BuildscriptBlock> configureAction) {
        if (buildScriptBlock == null) {
            buildScriptBlock = new BuildscriptBlock();
        }
        configureAction.accept(buildScriptBlock);
        return writeScriptToFileSystem();
    }

    public GradleBuildFile append(Statement statement) {
        statements.add(statement);
        return writeScriptToFileSystem();
    }

    public GradleBuildFile append(Expression expression) {
        statements.add(new ExpressionStatement(expression));
        return writeScriptToFileSystem();
    }

    public GradleBuildFile useKotlinDsl() {
        try {
            Files.deleteIfExists(location.getPath());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        location = location.use(GradleDsl.KOTLIN);
        return writeScriptToFileSystem();
    }

    public GradleBuildFile useGroovyDsl() {
        try {
            Files.deleteIfExists(location.getPath());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        location = location.use(GradleDsl.GROOVY);
        return writeScriptToFileSystem();
    }

	public GradleBuildFile writeToDirectory(Path directory) {
		return new GradleBuildFile(BuildScriptLocation.of(directory.resolve(location.getPath().getFileName().toString())), pluginsDslBlock, buildScriptBlock, statements);
	}

    private GradleBuildFile writeScriptToFileSystem() {
        List<Statement> stmt = new ArrayList<>();
        if (buildScriptBlock != null) {
            stmt.add(new GradleBlockStatement(literal("buildscript"), buildScriptBlock.build()));
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
