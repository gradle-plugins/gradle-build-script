package dev.gradleplugins.buildscript.ast.comments;

import dev.gradleplugins.buildscript.ast.Node;

public interface Comment extends Node {
    @Override
    default <ReturnType> ReturnType accept(Visitor<ReturnType> visitor) {
        return visitor.visit(this);
    }
}
