package dev.gradleplugins.buildscript.ast.type;

import java.util.Arrays;

public final class ReferenceType implements Type {
    private final String classType;

    public ReferenceType(String classType) {
        this.classType = classType;
    }

    public static ReferenceType of(Class<?> clazz) {
        return new ReferenceType(clazz.getTypeName());
    }

    // Convert current type into a parameterized type
    public ParameterizedType where(Type... types) {
        return new ParameterizedType(this, Arrays.asList(types));
    }

    public ReferenceType typeOnly() {
        return new ReferenceType(classType.substring(classType.lastIndexOf('.') + 1));
    }

    @Override
    public String toString() {
        return classType;
    }

    public static ReferenceType objectType() {
        return new ReferenceType(Object.class.getTypeName());
    }

    public static ReferenceType stringType() {
        return new ReferenceType(String.class.getTypeName());
    }
}
