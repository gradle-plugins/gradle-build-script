package dev.gradleplugins.buildscript.ast.type;

// Represent a value type when declaring a variable, i.e. val myVar = ...
//  In Java 11+, value type are essentially final variable type, i.e. final var myVar = ...
//  In Groovy, value type are essentially final variable type, i.e. final def myVar = ...
public final class ValType implements Type {
    private static final ValType INSTANCE = new ValType();

    private ValType() {}

    public static ValType valType() {
        return INSTANCE;
    }

    @Override
    public String toString() {
        return "val";
    }
}
