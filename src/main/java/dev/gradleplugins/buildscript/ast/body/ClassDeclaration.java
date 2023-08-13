package dev.gradleplugins.buildscript.ast.body;

import dev.gradleplugins.buildscript.ast.Modifier;
import dev.gradleplugins.buildscript.ast.type.Type;

import java.util.List;
import java.util.Set;

public final class ClassDeclaration implements TypeDeclaration {
    private final Set<Modifier> modifiers;
    private final String name;
    private final List<Type> implementedTypes;
    private final List<BodyDeclaration<?>> members;

    // FIXME: Type for implementedType should accept ReferenceType and ParameterizedType
    public ClassDeclaration(Set<Modifier> modifiers, String name, List<Type> implementedTypes, List<BodyDeclaration<?>> members) {
        this.modifiers = modifiers;
        this.name = name;
        this.implementedTypes = implementedTypes;
        this.members = members;
    }

    public Set<Modifier> getModifiers() {
        return modifiers;
    }

    public String getName() {
        return name;
    }

    public List<Type> getImplementedTypes() {
        return implementedTypes;
    }

    public List<BodyDeclaration<?>> getMembers() {
        return members;
    }

    @Override
    public <ReturnType> ReturnType accept(Visitor<ReturnType> visitor) {
        return visitor.visit(this);
    }
}
