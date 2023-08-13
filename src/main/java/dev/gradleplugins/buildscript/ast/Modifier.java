package dev.gradleplugins.buildscript.ast;

public final class Modifier {
    private static final Modifier ABSTRACT_MODIFIER = new Modifier(Keyword.ABSTRACT);
    private static final Modifier PUBLIC_MODIFIER = new Modifier(Keyword.PUBLIC);
    private static final Modifier FINAL_MODIFIER = new Modifier(Keyword.FINAL);
    private final Keyword keyword;

    private Modifier(Keyword keyword) {
        this.keyword = keyword;
    }

    public Keyword getKeyword() {
        return keyword;
    }

    public enum Keyword {
        ABSTRACT, DEFAULT, FINAL, PRIVATE, PROTECTED, STATIC, PUBLIC;

        @Override
        public String toString() {
            return name().toLowerCase();
        }
    }

    public static Modifier abstractModifier() {
        return ABSTRACT_MODIFIER;
    }

    public static Modifier publicModifier() {
        return PUBLIC_MODIFIER;
    }

    public static Modifier finalModifier() {
        return FINAL_MODIFIER;
    }
}
