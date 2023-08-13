package dev.gradleplugins.buildscript.syntax.normalizer;

import dev.gradleplugins.buildscript.ast.Modifier;
import dev.gradleplugins.buildscript.ast.expressions.Expression;
import dev.gradleplugins.buildscript.ast.expressions.VariableDeclarationExpression;
import dev.gradleplugins.buildscript.ast.type.DefType;
import dev.gradleplugins.buildscript.ast.type.ValType;
import dev.gradleplugins.buildscript.ast.type.VarType;
import dev.gradleplugins.buildscript.syntax.ASTTransformer;

import java.util.ArrayList;
import java.util.List;

// Convert var and val type variable into def and final def types
public final class GroovyVariableTransformer implements ASTTransformer {
    public Expression visit(VariableDeclarationExpression expression) {
        if (expression.getType().getClass().equals(VarType.class)) {
            return new VariableDeclarationExpression(expression.getModifiers(), DefType.defType(), expression.getVariables());
        } else if (expression.getType().getClass().equals(ValType.class)) {
            List<Modifier> modifiers = new ArrayList<>();
            modifiers.add(Modifier.finalModifier());
            modifiers.addAll(expression.getModifiers());
            return new VariableDeclarationExpression(modifiers, DefType.defType(), expression.getVariables());
        } else {
            return ASTTransformer.super.visit(expression);
        }
    }
}
