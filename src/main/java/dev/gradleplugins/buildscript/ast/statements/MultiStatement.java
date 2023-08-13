package dev.gradleplugins.buildscript.ast.statements;

import java.util.Iterator;

public interface MultiStatement extends Statement, Iterable<Statement> {
    static MultiStatement of(Iterable<Statement> statements) {
        return new MultiStatement() {
            @Override
            public <ReturnType> ReturnType accept(Visitor<ReturnType> visitor) {
                return visitor.visit(this);
            }

            @Override
            public Iterator<Statement> iterator() {
                return statements.iterator();
            }
        };
    }
}
