package dev.gradleplugins.buildscript.io;

import dev.gradleplugins.buildscript.GradleDsl;
import dev.gradleplugins.buildscript.InitBuildScript;
import dev.gradleplugins.buildscript.ast.expressions.Expression;
import dev.gradleplugins.buildscript.ast.statements.ExpressionStatement;
import dev.gradleplugins.buildscript.ast.statements.GradleBlockStatement;
import dev.gradleplugins.buildscript.ast.statements.MultiStatement;
import dev.gradleplugins.buildscript.ast.statements.Statement;
import dev.gradleplugins.buildscript.blocks.BuildscriptBlock;
import dev.gradleplugins.buildscript.blocks.GradleBlock;

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

public final class GradleInitFile implements InitBuildScript {
    private final Path filePath;
    private GradleDsl dsl;
    private BuildscriptBlock buildScriptBlock;
    private final List<Statement> statements = new ArrayList<>();

    private GradleInitFile(Path filePath, GradleDsl dsl) {
        this.filePath = filePath;
        this.dsl = dsl;
    }

    public static GradleInitFile of(Path filePath) {
        requireNonNull(filePath, "'filePath' must not be null");

        GradleDsl dsl = GradleDsl.GROOVY;
        if (filePath.getFileName().toString().endsWith(".gradle.kts")) {
            dsl = GradleDsl.KOTLIN;
        }

        String fileName = filePath.getFileName().toString();
        int idx = fileName.lastIndexOf(".gradle");
        fileName = fileName.substring(0, idx);

        return new GradleInitFile(filePath.getParent().resolve(fileName), dsl).writeScriptToFileSystem();
    }

    @Override
    public GradleInitFile configure(Consumer<? super GradleBlock> configureAction) {
        throw new UnsupportedOperationException();
    }

    @Override
    public GradleInitFile initscript(Consumer<? super BuildscriptBlock> configureAction) {
        if (buildScriptBlock == null) {
            buildScriptBlock = new BuildscriptBlock();
        }
        configureAction.accept(buildScriptBlock);
        return writeScriptToFileSystem();
    }

    public GradleInitFile useKotlinDsl() {
        try {
            Files.deleteIfExists(filePath.getParent().resolve(dsl.fileName(filePath.getFileName().toString())));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        dsl = GradleDsl.KOTLIN;
        return writeScriptToFileSystem();
    }

    public GradleInitFile append(Statement statement) {
        statements.add(statement);
        return writeScriptToFileSystem();
    }

    public GradleInitFile append(Expression expression) {
        statements.add(new ExpressionStatement(expression));
        return writeScriptToFileSystem();
    }

    public GradleInitFile leftShift(Statement statement) {
        return append(statement);
    }

    public GradleInitFile leftShift(Expression expression) {
        return append(expression);
    }

    private GradleInitFile writeScriptToFileSystem() {
        try {
            List<Statement> stmt = new ArrayList<>();
            if (buildScriptBlock != null) {
                stmt.add(new GradleBlockStatement(literal("initscript"), buildScriptBlock.build()));
            }
            stmt.addAll(statements);
            Files.write(Files.createDirectories(filePath.getParent()).resolve(dsl.fileName(filePath.getFileName().toString())), MultiStatement.of(stmt).toString(dsl).getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        return this;
    }

    // TODO: Should implement File type interface
    public Path getLocation() {
        return filePath.getParent().resolve(dsl.fileName(filePath.getFileName().toString()));
    }
}
