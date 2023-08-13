package dev.gradleplugins.buildscript.ast.statements;

import java.util.Iterator;

// Represent a group of statements, groups are typically delimited by an empty line (except for the first group of a block)
public final class GroupStatement implements Statement, Iterable<Statement> {
    private final Iterable<Statement> statements;

    public GroupStatement(Iterable<Statement> statements) {
        this.statements = statements;
    }

    @Override
    public <ReturnType> ReturnType accept(Visitor<ReturnType> visitor) {
        return visitor.visit(this);
    }

    @Override
    public Iterator<Statement> iterator() {
        return statements.iterator();
    }
}
