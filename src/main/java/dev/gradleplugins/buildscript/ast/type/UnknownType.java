package dev.gradleplugins.buildscript.ast.type;

public final class UnknownType implements Type {
    private static final UnknownType INSTANCE = new UnknownType();

    private UnknownType() {}

    public static final UnknownType unknownType() {
        return INSTANCE;
    }
}
