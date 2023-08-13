package dev.gradleplugins.buildscript.ast.expressions;

import dev.gradleplugins.buildscript.ast.Node;
import dev.gradleplugins.buildscript.ast.statements.ExpressionStatement;
import dev.gradleplugins.buildscript.ast.type.Type;

public interface Expression extends Node {
    // Nullable?
    Type getType();

    default ExpressionStatement toStatement() {
        return new ExpressionStatement(this);
    }

    @Override
    default <ReturnType> ReturnType accept(Node.Visitor<ReturnType> visitor) {
        return visitor.visit(this);
    }

    <ReturnType> ReturnType accept(Visitor<ReturnType> visitor);

    interface Visitor<ReturnType> {
        ReturnType visit(AssignmentExpression expression);
        ReturnType visit(FieldAccessExpression expression);
        ReturnType visit(QualifiedExpression expression);
        ReturnType visit(InfixExpression expression);
        ReturnType visit(NullLiteralExpression expression);
        ReturnType visit(PropertyAccessExpression expression);
        ReturnType visit(LiteralExpression expression);
        ReturnType visit(CastExpression expression);
        ReturnType visit(AsExpression expression);
        ReturnType visit(SetLiteralExpression expression);
        ReturnType visit(StringLiteralExpression expression);
        ReturnType visit(BooleanLiteralExpression expression);
        ReturnType visit(MethodCallExpression expression);
        ReturnType visit(MapLiteralExpression expression);
        ReturnType visit(ClassLiteralExpression expression);
        ReturnType visit(InstanceOfExpression expression);
        ReturnType visit(EnclosedExpression expression);
        ReturnType visit(NotExpression expression);

        ReturnType visit(TypeExpression expression);

        ReturnType visit(CurrentScopeExpression expression); // ???
        ReturnType visit(ItExpression expression); // ???
        ReturnType visit(VariableDeclarationExpression expression); // ???
        ReturnType visit(AssignExpression expression); // ???
        ReturnType visit(VariableDeclarator expression); // ???

        ReturnType visit(LambdaExpression expression);
    }
}
