package dev.gradleplugins.buildscript.ast.expressions;

import dev.gradleplugins.buildscript.ast.type.Type;

import java.util.Iterator;
import java.util.List;

import static dev.gradleplugins.buildscript.ast.type.ReferenceType.stringType;

public final class StringInterpolationExpression implements Expression, Iterable<Expression> {
    private final List<Expression> content;

    public StringInterpolationExpression(List<Expression> content) {
        this.content = content;
    }

    @Override
    public Type getType() {
        return stringType();
    }

    @Override
    public <ReturnType> ReturnType accept(Visitor<ReturnType> visitor) {
        return visitor.visit(this);
    }

    @Override
    public Iterator<Expression> iterator() {
        return content.iterator();
    }
}
