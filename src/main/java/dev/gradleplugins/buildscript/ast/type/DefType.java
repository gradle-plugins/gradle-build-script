package dev.gradleplugins.buildscript.ast.type;

// Represent a Groovy variable type when declaring a variable, i.e. def myVar = ...
public final class DefType implements Type {
    private static final DefType INSTANCE = new DefType();

    private DefType() {}

    public static DefType defType() {
        return INSTANCE;
    }

    @Override
    public String toString() {
        return "def";
    }
}
