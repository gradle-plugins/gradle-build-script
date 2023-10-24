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

public final class GradleInitFile extends AbstractBuildScriptFile implements InitBuildScript, GradleBuildScriptFile {
    private BuildScriptLocation location;
    private BuildscriptBlock buildScriptBlock;
    private final List<Statement> statements = new ArrayList<>();

    private GradleInitFile(BuildScriptLocation location) {
        this.location = location;
    }

    public static GradleInitFile of(Path filePath) {
        return new GradleInitFile(BuildScriptLocation.of(filePath)).writeScriptToFileSystem();
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
            Files.deleteIfExists(location.getPath());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        location = location.use(GradleDsl.KOTLIN);
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

    private GradleInitFile writeScriptToFileSystem() {
        List<Statement> stmt = new ArrayList<>();
        if (buildScriptBlock != null) {
            stmt.add(new GradleBlockStatement(literal("initscript"), buildScriptBlock.build()));
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

    // TODO: Should implement File type interface
    public Path getLocation() {
        return location.getPath();
    }
}
