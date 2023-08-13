package dev.gradleplugins.buildscript.ast.statements;

import java.util.Objects;

// Represent a statement commented out, i.e. '// val myVar = ...'
public final class CommentedStatement<T extends Statement> implements Statement {
    private final T commentedStatement;

    public CommentedStatement(T commentedStatement) {
        this.commentedStatement = commentedStatement;
    }

    @Override
    public <ReturnType> ReturnType accept(Visitor<ReturnType> visitor) {
        return visitor.visit(this);
    }

    public T getCommentedStatement() {
        return commentedStatement;
    }
}
