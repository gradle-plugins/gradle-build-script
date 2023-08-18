package dev.gradleplugins.buildscript.syntax;

import dev.gradleplugins.buildscript.ast.body.BodyDeclaration;
import dev.gradleplugins.buildscript.ast.body.ClassDeclaration;
import dev.gradleplugins.buildscript.ast.expressions.Expression;
import dev.gradleplugins.buildscript.ast.expressions.TypeExpression;
import dev.gradleplugins.buildscript.ast.statements.ImportDeclaration;
import dev.gradleplugins.buildscript.ast.statements.Statement;
import dev.gradleplugins.buildscript.ast.type.ParameterizedType;
import dev.gradleplugins.buildscript.ast.type.ReferenceType;
import dev.gradleplugins.buildscript.ast.type.Type;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public final class ImporterTransformer implements ASTTransformer {
    private final Imports imports;

    public ImporterTransformer(Imports imports) {
        this.imports = imports;
    }

    @Override
    public Expression visit(TypeExpression expression) {
        imports.add(expression.getType());
        return new TypeExpression(expression.getType().typeOnly());
    }

    @Override
    public Statement visit(ClassDeclaration statement) {
        return new ClassDeclaration(statement.getModifiers(), statement.getName(), importTypes(statement.getImplementedTypes()), statement.getMembers().stream().map(it -> (BodyDeclaration<?>) it.accept(this)).collect(Collectors.toList()));
    }

    public List<Type> importTypes(List<Type> types) {
        return types.stream().map(it -> {
            if (it instanceof ReferenceType) {
                imports.add((ReferenceType) it);
                return ((ReferenceType) it).typeOnly();
            } else if (it instanceof ParameterizedType) {
                if (((ParameterizedType) it).getRawType() instanceof ReferenceType) {
                    imports.add((ReferenceType) ((ParameterizedType) it).getRawType());
                }
                return new ParameterizedType(((ParameterizedType) it).getRawType(), importTypes(((ParameterizedType) it).getTypeArguments()));
            } else {
                return it;
            }
        }).collect(Collectors.toList());
    }

    public static final class Imports {
        private final Set<ReferenceType> imports = new LinkedHashSet<>();

        void add(ReferenceType type) {
            imports.add(type);
        }

        public List<ImportDeclaration> getImportDeclarations() {
            return imports.stream().map(Object::toString).map(it -> new ImportDeclaration(ImportDeclaration.ImportType.TYPE, it)).collect(Collectors.toList());
        }
    }
}
