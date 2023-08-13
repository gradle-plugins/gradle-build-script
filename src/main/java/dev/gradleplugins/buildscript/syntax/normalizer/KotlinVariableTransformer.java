package dev.gradleplugins.buildscript.syntax.normalizer;

import dev.gradleplugins.buildscript.ast.expressions.Expression;
import dev.gradleplugins.buildscript.ast.expressions.VariableDeclarationExpression;
import dev.gradleplugins.buildscript.ast.type.DefType;
import dev.gradleplugins.buildscript.ast.type.VarType;
import dev.gradleplugins.buildscript.syntax.ASTTransformer;

// Convert def type variable into var typed variables
public final class KotlinVariableTransformer implements ASTTransformer {
    public Expression visit(VariableDeclarationExpression expression) {
        if (expression.getType().getClass().equals(DefType.class)) {
            return new VariableDeclarationExpression(expression.getModifiers(), VarType.varType(), expression.getVariables());
        } else {
            return ASTTransformer.super.visit(expression);
        }
    }
}
