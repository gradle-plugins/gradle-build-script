package dev.gradleplugins.buildscript.ast.expressions;

import dev.gradleplugins.buildscript.ast.type.Type;

import java.util.List;
import java.util.function.Consumer;

// Represent a map literal whose keys are string literals.
public final class SetLiteralExpression implements Expression {
    private final Type type;
    private final List<Expression> literal;

    public SetLiteralExpression(Type type, List<Expression> literal) {
        this.type = type;
        this.literal = literal;
    }

    @Override
    public Type getType() {
        return type;
    }

    public boolean isEmpty() {
        return literal.isEmpty();
    }

    public boolean hasSingleElement() {
        return literal.size() == 1;
    }

    public List<Expression> getElements() {
        return literal;
    }

    public void forEach(Consumer<? super Expression> action) {
        literal.forEach(action);
    }

    @Override
    public <ReturnType> ReturnType accept(Visitor<ReturnType> visitor) {
        return visitor.visit(this);
    }
}
