package dev.gradleplugins.buildscript.ast.statements;

import java.util.Iterator;

public final class GroovyDslLiteral implements Statement, Iterable<String> {
    private final Iterable<String> lines;

    public GroovyDslLiteral(Iterable<String> lines) {
        this.lines = lines;
    }

    @Override
    public <ReturnType> ReturnType accept(Visitor<ReturnType> visitor) {
        return visitor.visit(this);
    }

    @Override
    public Iterator<String> iterator() {
        return lines.iterator();
    }
}
