package dev.gradleplugins.buildscript.ast.type;

// Represent primitive void type
public final class VoidType implements Type {
    private static final VoidType INSTANCE = new VoidType();

    private VoidType() {}

    public static VoidType voidType() {
        return INSTANCE;
    }

    @Override
    public String toString() {
        return "void";
    }
}
