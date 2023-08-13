package dev.gradleplugins.buildscript.ast.type;

// Represent a variable type used when declaring a variable, i.e. var myVar = ...
public final class VarType implements Type {
    private static final VarType INSTANCE = new VarType();

    private VarType() {}

    public static VarType varType() {
        return INSTANCE;
    }

    @Override
    public String toString() {
        return "var";
    }
}
