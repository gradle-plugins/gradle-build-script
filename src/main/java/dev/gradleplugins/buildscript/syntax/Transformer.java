package dev.gradleplugins.buildscript.syntax;

import dev.gradleplugins.buildscript.ast.Node;

public interface Transformer<T> {
    T transform(Node node);
}
