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

public final class GradleBuildFile implements ProjectBuildScript {
    private final Path location;
    private PluginsDslBlock pluginsDslBlock;
    private GradleDsl dsl = GradleDsl.GROOVY;
    private BuildscriptBlock buildScriptBlock;
    private final List<Statement> statements = new ArrayList<>();

    private GradleBuildFile(Path location) {
        this.location = location;
    }

    public static GradleBuildFile inDirectory(Path location) {
        return new GradleBuildFile(requireNonNull(location, "'location' must not be null")).writeScriptToFileSystem();
    }

    @Override
    public ProjectBuildScript plugins(Consumer<? super PluginsDslBlock> configureAction) {
        if (pluginsDslBlock == null) {
            pluginsDslBlock = new PluginsDslBlock();
        }
        configureAction.accept(pluginsDslBlock);
        return writeScriptToFileSystem();
    }

    @Override
    public ProjectBuildScript configure(Consumer<? super ProjectBlock> configureAction) {
        throw new UnsupportedOperationException();
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

    public GradleBuildFile leftShift(Statement statement) {
        return append(statement);
    }

    public GradleBuildFile leftShift(Expression expression) {
        return append(expression);
    }

    public GradleBuildFile useKotlinDsl() {
        try {
            Files.deleteIfExists(location.resolve(dsl.fileName("build")));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        dsl = GradleDsl.KOTLIN;
        return writeScriptToFileSystem();
    }

    private GradleBuildFile writeScriptToFileSystem() {
        try {
            List<Statement> stmt = new ArrayList<>();
            if (buildScriptBlock != null) {
                stmt.add(new GradleBlockStatement(literal("buildscript"), buildScriptBlock.build()));
            }
            if (pluginsDslBlock != null) {
                stmt.add(new GradleBlockStatement(literal("plugins"), pluginsDslBlock.build()));
            }
            stmt.addAll(statements);
            Files.write(location.resolve(dsl.fileName("build")), MultiStatement.of(stmt).toString(dsl).getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        return this;
    }
}
