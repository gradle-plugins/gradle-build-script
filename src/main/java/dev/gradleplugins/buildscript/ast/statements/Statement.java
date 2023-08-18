package dev.gradleplugins.buildscript.ast.statements;

import dev.gradleplugins.buildscript.ast.Node;
import dev.gradleplugins.buildscript.ast.body.ClassDeclaration;
import dev.gradleplugins.buildscript.ast.comments.LineComment;
import dev.gradleplugins.buildscript.blocks.PluginsDslBlock;

public interface Statement extends Node {
    @Override
    default <ReturnType> ReturnType accept(Node.Visitor<ReturnType> visitor) {
        return visitor.visit(this);
    }

    <ReturnType> ReturnType accept(Visitor<ReturnType> visitor);

    interface Visitor<ReturnType> {
        ReturnType visit(AssertStatement statement);
        ReturnType visit(CommentedStatement<?> statement);
        ReturnType visit(ExpressionStatement statement);
        ReturnType visit(GroupStatement statement);
        ReturnType visit(ImportDeclaration statement);
        ReturnType visit(BlockStatement statement);
        ReturnType visit(GradleBlockStatement statement);
        ReturnType visit(PluginsDslBlock.IdStatement statement);

        ReturnType visit(LineComment statement);

        ReturnType visit(ClassDeclaration statement);

        ReturnType visit(MultiStatement statement);
    }
}
