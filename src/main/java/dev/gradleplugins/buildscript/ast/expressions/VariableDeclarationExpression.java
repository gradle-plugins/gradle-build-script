package dev.gradleplugins.buildscript.ast.expressions;

import dev.gradleplugins.buildscript.ast.Modifier;
import dev.gradleplugins.buildscript.ast.type.ValType;
import dev.gradleplugins.buildscript.ast.type.VarType;
import dev.gradleplugins.buildscript.ast.type.Type;

import java.util.Collections;
import java.util.List;

public final class VariableDeclarationExpression implements Expression {
    private final List<Modifier> modifiers;
//    private final List<AnnotationExpression> annotations;
    private final Type type;
    private final List<VariableDeclarator> variables;

    public VariableDeclarationExpression(List<Modifier> modifiers, Type type, List<VariableDeclarator> variables) {
        this.modifiers = modifiers;
        this.type = type;
        this.variables = variables;
    }

    public List<Modifier> getModifiers() {
        return modifiers;
    }

    public List<VariableDeclarator> getVariables() {
        return variables;
    }

    @Override
    public Type getType() {
        return type;
    }

    @Override
    public <ReturnType> ReturnType accept(Visitor<ReturnType> visitor) {
        return visitor.visit(this);
    }

    // FIXME: variable declaration should probably be statement
    public static VariableDeclarationExpression val(String variableName, Expression initializer) {
        return new VariableDeclarationExpression(Collections.emptyList(), ValType.valType(), Collections.singletonList(new VariableDeclarator(ValType.valType(), variableName, initializer)));
    }

    // FIXME: variable declaration should probably be statement
    public static VariableDeclarationExpression var(String variableName, Expression initializer) {
        return new VariableDeclarationExpression(Collections.emptyList(), VarType.varType(), Collections.singletonList(new VariableDeclarator(VarType.varType(), variableName, initializer)));
    }
}
