package dev.gradleplugins.buildscript.syntax;

import dev.gradleplugins.buildscript.ast.body.ClassDeclaration;
import dev.gradleplugins.buildscript.ast.comments.LineComment;
import dev.gradleplugins.buildscript.ast.expressions.AsExpression;
import dev.gradleplugins.buildscript.ast.expressions.AssignExpression;
import dev.gradleplugins.buildscript.ast.expressions.AssignmentExpression;
import dev.gradleplugins.buildscript.ast.expressions.BooleanLiteralExpression;
import dev.gradleplugins.buildscript.ast.expressions.CastExpression;
import dev.gradleplugins.buildscript.ast.expressions.ClassLiteralExpression;
import dev.gradleplugins.buildscript.ast.expressions.CollectionLiteralExpression;
import dev.gradleplugins.buildscript.ast.expressions.CurrentScopeExpression;
import dev.gradleplugins.buildscript.ast.expressions.EnclosedExpression;
import dev.gradleplugins.buildscript.ast.expressions.Expression;
import dev.gradleplugins.buildscript.ast.expressions.FieldAccessExpression;
import dev.gradleplugins.buildscript.ast.expressions.GroovyDslLiteral;
import dev.gradleplugins.buildscript.ast.expressions.InfixExpression;
import dev.gradleplugins.buildscript.ast.expressions.InstanceOfExpression;
import dev.gradleplugins.buildscript.ast.expressions.ItExpression;
import dev.gradleplugins.buildscript.ast.expressions.LambdaExpression;
import dev.gradleplugins.buildscript.ast.expressions.LiteralExpression;
import dev.gradleplugins.buildscript.ast.expressions.MapLiteralExpression;
import dev.gradleplugins.buildscript.ast.expressions.MethodCallExpression;
import dev.gradleplugins.buildscript.ast.expressions.NotExpression;
import dev.gradleplugins.buildscript.ast.expressions.NullLiteralExpression;
import dev.gradleplugins.buildscript.ast.expressions.PropertyAccessExpression;
import dev.gradleplugins.buildscript.ast.expressions.QualifiedExpression;
import dev.gradleplugins.buildscript.ast.expressions.SafeNavigationExpression;
import dev.gradleplugins.buildscript.ast.expressions.SetLiteralExpression;
import dev.gradleplugins.buildscript.ast.expressions.StringInterpolationExpression;
import dev.gradleplugins.buildscript.ast.expressions.StringLiteralExpression;
import dev.gradleplugins.buildscript.ast.expressions.TypeExpression;
import dev.gradleplugins.buildscript.ast.expressions.VariableDeclarationExpression;
import dev.gradleplugins.buildscript.ast.expressions.VariableDeclarator;
import dev.gradleplugins.buildscript.ast.statements.AssertStatement;
import dev.gradleplugins.buildscript.ast.statements.BlockStatement;
import dev.gradleplugins.buildscript.ast.statements.CommentedStatement;
import dev.gradleplugins.buildscript.ast.statements.ExpressionStatement;
import dev.gradleplugins.buildscript.ast.statements.GradleBlockStatement;
import dev.gradleplugins.buildscript.ast.statements.GroupStatement;
import dev.gradleplugins.buildscript.ast.statements.MultiStatement;
import dev.gradleplugins.buildscript.ast.statements.Statement;
import dev.gradleplugins.buildscript.ast.statements.StaticImportDeclaration;
import dev.gradleplugins.buildscript.ast.statements.TypeImportDeclaration;
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
    public Syntax.Content visit(CastExpression expression) {
        throw invalidLanguageNode();
    }

    @Override
    public Syntax.Content visit(AsExpression expression) {
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
    public Syntax.Content visit(InstanceOfExpression expression) {
        throw invalidLanguageNode();
    }

    @Override
    public Syntax.Content visit(EnclosedExpression expression) {
        throw invalidLanguageNode();
    }

    @Override
    public Syntax.Content visit(NotExpression expression) {
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
    public Syntax.Content visit(TypeExpression expression) {
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
    public Syntax.Content visit(VariableDeclarationExpression expression) {
        throw invalidLanguageNode();
    }

    @Override
    public Syntax.Content visit(AssignExpression expression) {
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
    public Syntax.Content visit(TypeImportDeclaration statement) {
        throw invalidLanguageNode();
    }

    @Override
    public Syntax.Content visit(StaticImportDeclaration statement) {
        throw invalidLanguageNode();
    }

    @Override
    public Syntax.Content visit(BlockStatement statement) {
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

    private static RuntimeException invalidLanguageNode() {
        return new UnsupportedOperationException("Not a valid node for this language");
    }
}
