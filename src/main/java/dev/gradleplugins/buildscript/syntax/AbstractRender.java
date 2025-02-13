package dev.gradleplugins.buildscript.syntax;

import dev.gradleplugins.buildscript.ast.body.ClassDeclaration;
import dev.gradleplugins.buildscript.ast.comments.LineComment;
import dev.gradleplugins.buildscript.ast.expressions.*;
import dev.gradleplugins.buildscript.ast.statements.AssertStatement;
import dev.gradleplugins.buildscript.ast.statements.CommentedStatement;
import dev.gradleplugins.buildscript.ast.statements.ExpressionStatement;
import dev.gradleplugins.buildscript.ast.statements.GradleBlockStatement;
import dev.gradleplugins.buildscript.ast.statements.GroupStatement;
import dev.gradleplugins.buildscript.ast.statements.ImportDeclaration;
import dev.gradleplugins.buildscript.ast.statements.MultiStatement;
import dev.gradleplugins.buildscript.ast.statements.Statement;
import dev.gradleplugins.buildscript.blocks.PluginsDslBlock;

public class AbstractRender implements Statement.Visitor<Syntax.Content>, Expression.Visitor<Syntax.Content> {
    @Override
    public Syntax.Content visit(AssignmentExpression expression) {
        throw invalidLanguageNode();
    }

    @Override
    public Syntax.Content visit(FieldAccessExpression expression) {
        throw invalidLanguageNode();
    }

    @Override
    public Syntax.Content visit(QualifiedExpression expression) {
        throw invalidLanguageNode();
    }

    @Override
    public Syntax.Content visit(InfixExpression expression) {
        throw invalidLanguageNode();
    }

    @Override
    public Syntax.Content visit(NullLiteralExpression expression) {
        throw invalidLanguageNode();
    }

    @Override
    public Syntax.Content visit(PropertyAccessExpression expression) {
        throw invalidLanguageNode();
    }

    @Override
    public Syntax.Content visit(LiteralExpression expression) {
        throw invalidLanguageNode();
    }

    @Override
    public Syntax.Content visit(SetLiteralExpression expression) {
        throw invalidLanguageNode();
    }

    @Override
    public Syntax.Content visit(StringLiteralExpression expression) {
        throw invalidLanguageNode();
    }

    @Override
    public Syntax.Content visit(StringInterpolationExpression expression) {
        throw invalidLanguageNode();
    }

    @Override
    public Syntax.Content visit(BooleanLiteralExpression expression) {
        throw invalidLanguageNode();
    }

    @Override
    public Syntax.Content visit(MethodCallExpression expression) {
        throw invalidLanguageNode();
    }

    @Override
    public Syntax.Content visit(MapLiteralExpression expression) {
        throw invalidLanguageNode();
    }

    @Override
    public Syntax.Content visit(ClassLiteralExpression expression) {
        throw invalidLanguageNode();
    }

    @Override
    public Syntax.Content visit(EnclosedExpression expression) {
        throw invalidLanguageNode();
    }

    @Override
    public Syntax.Content visit(CollectionLiteralExpression expression) {
        throw invalidLanguageNode();
    }

    @Override
    public Syntax.Content visit(SafeNavigationExpression expression) {
        throw invalidLanguageNode();
    }

    @Override
    public Syntax.Content visit(PrefixExpression expression) {
        throw invalidLanguageNode();
    }

    @Override
    public Syntax.Content visit(PostfixExpression expression) {
        throw invalidLanguageNode();
    }

    @Override
    public Syntax.Content visit(CastingExpression expression) {
        throw invalidLanguageNode();
    }

    @Override
    public Syntax.Content visit(TypeComparisonExpression expression) {
        throw invalidLanguageNode();
    }

    @Override
    public Syntax.Content visit(TypeExpression expression) {
        throw invalidLanguageNode();
    }

    @Override
    public Syntax.Content visit(TernaryExpression expression) {
        throw invalidLanguageNode();
    }

    @Override
    public Syntax.Content visit(CurrentScopeExpression expression) {
        throw invalidLanguageNode();
    }

    @Override
    public Syntax.Content visit(ItExpression expression) {
        throw invalidLanguageNode();
    }

    @Override
    public Syntax.Content visit(DelegateExpression expression) {
        throw invalidLanguageNode();
    }

    @Override
    public Syntax.Content visit(VariableDeclarationExpression expression) {
        throw invalidLanguageNode();
    }

    @Override
    public Syntax.Content visit(VariableDeclarator expression) {
        throw invalidLanguageNode();
    }

    @Override
    public Syntax.Content visit(LambdaExpression expression) {
        throw invalidLanguageNode();
    }

    @Override
    public Syntax.Content visit(AssertStatement statement) {
        throw invalidLanguageNode();
    }

    @Override
    public Syntax.Content visit(GroovyDslLiteral expression) {
        throw invalidLanguageNode();
    }

	@Override
	public Syntax.Content visit(KotlinDslLiteral expression) {
		throw invalidLanguageNode();
	}

	@Override
    public Syntax.Content visit(CommentedStatement<?> statement) {
        throw invalidLanguageNode();
    }

    @Override
    public Syntax.Content visit(ExpressionStatement statement) {
        throw invalidLanguageNode();
    }

    @Override
    public Syntax.Content visit(GroupStatement statement) {
        throw invalidLanguageNode();
    }

    @Override
    public Syntax.Content visit(ImportDeclaration statement) {
        throw invalidLanguageNode();
    }

    @Override
    public Syntax.Content visit(GradleBlockStatement statement) {
        throw invalidLanguageNode();
    }

    @Override
    public Syntax.Content visit(PluginsDslBlock.IdStatement statement) {
        throw invalidLanguageNode();
    }

    @Override
    public Syntax.Content visit(LineComment statement) {
        throw invalidLanguageNode();
    }

    @Override
    public Syntax.Content visit(ClassDeclaration statement) {
        throw invalidLanguageNode();
    }

    @Override
    public Syntax.Content visit(MultiStatement statement) {
        final Syntax.Content.Builder builder = Syntax.Content.builder();
        statement.forEach(it -> builder.add(it.accept(this)));
        return builder.build();
    }

    protected static RuntimeException invalidLanguageNode() {
        return new UnsupportedOperationException("Not a valid node for this language");
    }
}
