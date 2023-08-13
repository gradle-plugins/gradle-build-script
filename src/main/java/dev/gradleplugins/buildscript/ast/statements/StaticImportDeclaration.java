package dev.gradleplugins.buildscript.ast.statements;

// Represents an import static statement.
public class StaticImportDeclaration implements ImportDeclaration {
    private final String name;

    public StaticImportDeclaration(String name) {
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
