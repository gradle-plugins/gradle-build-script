package dev.gradleplugins.buildscript.ast.type;

// Represent primitive type
public final class PrimitiveType implements Type {
    private static final PrimitiveType BOOLEAN_TYPE = new PrimitiveType(Primitive.Boolean);
    private final Primitive type;

    private PrimitiveType(Primitive type) {
        this.type = type;
    }

    public Primitive getType() {
        return type;
    }

    public enum Primitive {
        Boolean, Byte, Char, Double, Float, Int, Long, Short
    }

    public static PrimitiveType booleanType() {
        return BOOLEAN_TYPE;
    }

    @Override
    public String toString() {
        return type.name().toLowerCase();
    }
}
