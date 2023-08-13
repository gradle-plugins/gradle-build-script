package dev.gradleplugins.buildscript.ast.type;

import java.util.List;

public final class ParameterizedType implements Type {
    private final Type rawType;
    private final List<Type> typeArguments;

    // TODO: rawtype should be the interface that correspond to a reference type
    public ParameterizedType(/*ReferenceType*/Type rawType, List<Type> typeArguments) {
        this.rawType = rawType;
        this.typeArguments = typeArguments;
    }

    public Type getRawType() {
        return rawType;
    }

    public List<Type> getTypeArguments() {
        return typeArguments;
    }
}
