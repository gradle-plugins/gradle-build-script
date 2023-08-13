package dev.gradleplugins.buildscript.ast.statements;

import dev.gradleplugins.buildscript.ast.body.TypeDeclaration;

import java.io.UncheckedIOException;
import java.util.List;

public final class CompilationUnit implements Statement {
    private final List<ImportDeclaration> imports;
    private final List<TypeDeclaration> types;

    public CompilationUnit(List<ImportDeclaration> imports, List<TypeDeclaration> types) {
        this.imports = imports;
        this.types = types;
    }

    @Override
    public <ReturnType> ReturnType accept(Visitor<ReturnType> visitor) {
        throw new UnsupportedOperationException();
    }
}
