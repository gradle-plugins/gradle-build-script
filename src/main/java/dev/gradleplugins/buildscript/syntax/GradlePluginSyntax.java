package dev.gradleplugins.buildscript.syntax;

import dev.gradleplugins.buildscript.ast.Modifier;
import dev.gradleplugins.buildscript.ast.Node;
import dev.gradleplugins.buildscript.ast.body.Parameter;
import dev.gradleplugins.buildscript.ast.body.TypeDeclaration;
import dev.gradleplugins.buildscript.ast.expressions.Expression;
import dev.gradleplugins.buildscript.ast.statements.CompilationUnit;
import dev.gradleplugins.buildscript.ast.statements.ExpressionStatement;
import dev.gradleplugins.buildscript.ast.statements.Statement;
import dev.gradleplugins.buildscript.ast.type.ReferenceType;
import org.gradle.api.Project;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public final class GradlePluginSyntax implements Transformer<Node> {
    private static final Set<Modifier> PLUGIN_CLASS_MODIFIERS = new LinkedHashSet<Modifier>() {{
        add(Modifier.publicModifier());
        add(Modifier.abstractModifier());
    }};
    private static final Set<Modifier> APPLY_METHOD_MODIFIERS = new LinkedHashSet<Modifier>() {{
        add(Modifier.publicModifier());
    }};
    private static final List<Parameter> APPLY_METHOD_PARAMETERS = Collections.singletonList(new Parameter(ReferenceType.of(Project.class), "project"));

    private final Transformer<Node> language;

    public GradlePluginSyntax(Transformer<Node> language) {
        this.language = language;
    }

    @Override
    public Node transform(Node node) {
        Statement statement = null;
        if (node instanceof Statement) {
            statement = (Statement) node;
        } else if (node instanceof Expression) {
            statement = new ExpressionStatement((Expression) node);
        }

        // TODO: collect imports and rewrite types
        // TODO: fix current context so it refers `project` variable
//        statement = new ClassDeclaration(PLUGIN_CLASS_MODIFIERS, "MyPlugin", Collections.singletonList(ReferenceType.of(Plugin.class).where(ReferenceType.of(Project.class))), Collections.singletonList(new MethodDeclaration(APPLY_METHOD_MODIFIERS, "apply", APPLY_METHOD_PARAMETERS, new GradleBlockStatement.BodyDeclaration(Collections.singletonList((Statement) language.transform(statement))))));

        ImporterTransformer.Imports imports = new ImporterTransformer.Imports();
        statement = statement.accept(new ImporterTransformer(imports));

        statement = new CompilationUnit(imports.getImportDeclarations(), Collections.singletonList((TypeDeclaration) statement));

        return statement;
    }
}