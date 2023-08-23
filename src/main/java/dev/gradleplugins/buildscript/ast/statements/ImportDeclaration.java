package dev.gradleplugins.buildscript.ast.statements;

public final class ImportDeclaration implements Statement {
    private final ImportType importType;
    private final String name;

    public ImportDeclaration(ImportType importType, String name) {
        this.importType = importType;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public boolean isAsterisk() {
        return name.endsWith(".*");
    }

    @Override
    public <ReturnType> ReturnType accept(Visitor<ReturnType> visitor) {
        return visitor.visit(this);
    }

    public enum ImportType {
        // Represents an import static statement.
        STATIC,

        // Represents an import statement.
        TYPE
    }

    public ImportType getImportType() {
        return importType;
    }
}
