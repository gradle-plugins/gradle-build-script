package dev.gradleplugins.buildscript.ast.expressions;

import dev.gradleplugins.buildscript.ast.type.MapLiteralType;
import dev.gradleplugins.buildscript.ast.type.ParameterizedType;
import dev.gradleplugins.buildscript.ast.type.ReferenceType;
import dev.gradleplugins.buildscript.ast.type.Type;

import java.util.Arrays;
import java.util.Map;
import java.util.function.BiConsumer;

// Represent a map literal whose keys are string literals.
public final class MapLiteralExpression implements Expression {
    private final Type type;
    private final Map<String, Expression> literal;


    public MapLiteralExpression(Type valueType, Map<String, Expression> literal) {
        this.type = new ParameterizedType(new MapLiteralType(), Arrays.asList(new ReferenceType("java.lang.String"), valueType));
        this.literal = literal;
    }

    @Override
    public Type getType() {
        return type;
    }

    public Map<String, Expression> getElements() {
        return literal;
    }

    public boolean isEmpty() {
        return literal.isEmpty();
    }

    public boolean hasSingleElement() {
        return literal.size() == 1;
    }

    public void forEach(BiConsumer<? super String, ? super Expression> action) {
        literal.forEach(action);
    }

    @Override
    public <ReturnType> ReturnType accept(Visitor<ReturnType> visitor) {
        return visitor.visit(this);
    }
}
