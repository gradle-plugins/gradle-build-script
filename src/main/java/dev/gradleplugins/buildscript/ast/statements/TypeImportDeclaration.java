package dev.gradleplugins.buildscript.ast.statements;

// Represents an import statement.
public final class TypeImportDeclaration implements ImportDeclaration {
    private final String name;

    public TypeImportDeclaration(String name) {
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
}
