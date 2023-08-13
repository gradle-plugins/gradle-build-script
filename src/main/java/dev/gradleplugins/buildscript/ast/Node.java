package dev.gradleplugins.buildscript.ast;

import dev.gradleplugins.buildscript.PrettyPrinter;
import dev.gradleplugins.buildscript.ast.comments.Comment;
import dev.gradleplugins.buildscript.ast.expressions.Expression;
import dev.gradleplugins.buildscript.ast.statements.Statement;
import dev.gradleplugins.buildscript.syntax.Syntax;

import java.io.IOException;
import java.io.StringWriter;
import java.io.UncheckedIOException;

public interface Node {
    default void writeTo(PrettyPrinter out) throws IOException {
        out.write(this);
    }


    default String toString(Syntax syntax) {
        final StringWriter result = new StringWriter();
        try (PrettyPrinter writer = new PrettyPrinter(result, syntax)) {
            writeTo(writer);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return result.toString();
    }

    <ReturnType> ReturnType accept(Visitor<ReturnType> visitor);

    interface Visitor<ReturnType> {
        ReturnType visit(Statement statement);
        ReturnType visit(Expression expression);
        ReturnType visit(Comment comment);
    }
}
