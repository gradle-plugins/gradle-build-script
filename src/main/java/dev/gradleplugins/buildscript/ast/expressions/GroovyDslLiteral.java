package dev.gradleplugins.buildscript.ast.expressions;

import dev.gradleplugins.buildscript.ast.expressions.Expression;
import dev.gradleplugins.buildscript.ast.type.Type;

import java.util.Iterator;

import static dev.gradleplugins.buildscript.ast.type.UnknownType.unknownType;

public final class GroovyDslLiteral implements Expression, Iterable<String> {
    private final Iterable<String> lines;

    public GroovyDslLiteral(Iterable<String> lines) {
        this.lines = lines;
    }

    @Override
    public Type getType() {
        return unknownType();
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
