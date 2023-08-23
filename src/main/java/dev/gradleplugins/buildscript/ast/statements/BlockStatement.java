package dev.gradleplugins.buildscript.ast.statements;

import java.util.List;

public final class BlockStatement {
    private final List<Statement> statements;

    public BlockStatement(List<Statement> statements) {
        this.statements = statements;
    }

    public List<Statement> getStatements() {
        return statements;
    }
}
