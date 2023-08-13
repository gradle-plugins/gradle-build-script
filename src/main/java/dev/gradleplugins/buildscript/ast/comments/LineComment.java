package dev.gradleplugins.buildscript.ast.comments;

import dev.gradleplugins.buildscript.PrettyPrinter;

import java.io.IOException;

// Represent a comment that takes an entire line, i.e. starts with '//' and ends with '\n'
public class LineComment implements Comment {
    private final String content;

    public LineComment(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    @Override
    public void writeTo(PrettyPrinter out) throws IOException {
        throw new UnsupportedOperationException();
    }
}
