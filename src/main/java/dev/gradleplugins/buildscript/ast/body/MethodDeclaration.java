package dev.gradleplugins.buildscript.ast.body;

import dev.gradleplugins.buildscript.ast.Modifier;
import dev.gradleplugins.buildscript.ast.statements.GradleBlockStatement;

import java.util.List;
import java.util.Set;

public final class MethodDeclaration implements dev.gradleplugins.buildscript.ast.body.BodyDeclaration<MethodDeclaration> {
    private final Set<Modifier> modifiers;
    private final String methodName;
    private final List<Parameter> parameters;
    private final GradleBlockStatement.Body body;

    public MethodDeclaration(Set<Modifier> modifiers, String methodName, List<Parameter> parameters, GradleBlockStatement.Body body) {
        this.modifiers = modifiers;
        this.methodName = methodName;
        this.parameters = parameters;
        this.body = body;
    }

    @Override
    public <ReturnType> ReturnType accept(Visitor<ReturnType> visitor) {
        throw new UnsupportedOperationException();
    }
}
