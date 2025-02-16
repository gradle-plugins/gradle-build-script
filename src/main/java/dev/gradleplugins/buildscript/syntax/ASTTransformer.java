package dev.gradleplugins.buildscript.syntax;

import dev.gradleplugins.buildscript.ast.Node;
import dev.gradleplugins.buildscript.ast.body.ClassDeclaration;
import dev.gradleplugins.buildscript.ast.comments.Comment;
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

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public interface ASTTransformer extends Expression.Visitor<Expression>, Statement.Visitor<Statement> {
    @Override
    default Expression visit(DelegateExpression expression) {
        return expression;
    }

    @Override
    default Expression visit(LambdaExpression expression) {
        return new LambdaExpression(expression.getLambdaType(), expression.getParameters(), expression.getBody().map(it -> it.accept(new Node.Visitor<Node>() {
            @Override
            public Node visit(Statement statement) {
                return statement.accept(ASTTransformer.this);
            }

            @Override
            public Node visit(Expression expression) {
                return expression.accept(ASTTransformer.this);
            }

            @Override
            public Node visit(Comment comment) {
                throw new UnsupportedOperationException();
            }
        })).orElse(null));
    }

    @Override
    default Statement visit(ImportDeclaration statement) {
        return statement;
    }

    @Override
    default Expression visit(TypeComparisonExpression expression) {
        return new TypeComparisonExpression(expression.getComparisonType(), expression.getExpression().accept(this), expression.getInstanceType());
    }

    @Override
    default Expression visit(CastingExpression expression) {
        return new CastingExpression(expression.getCastingType(), expression.getType(), expression.getExpression().accept(this));
    }

    @Override
    default Expression visit(PrefixExpression expression) {
        return new PrefixExpression(expression.getOperator(), expression.getExpression().accept(this));
    }

    @Override
    default Expression visit(PostfixExpression expression) {
        return new PostfixExpression(expression.getExpression().accept(this), expression.getOperator());
    }

    @Override
    default Expression visit(ItExpression expression) {
        return expression;
    }

    @Override
    default Expression visit(SafeNavigationExpression expression) {
        return new SafeNavigationExpression(expression.getObjectExpression().accept(this), expression.getPropertyExpression().accept(this));
    }

    @Override
    default Expression visit(TernaryExpression expression) {
        return new TernaryExpression(expression.getCondition().accept(this), expression.getTrueExpression().accept(this), expression.getFalseExpression().accept(this));
    }

    @Override
    default Expression visit(CollectionLiteralExpression expression) {
        return new CollectionLiteralExpression(expression.getExpressions().stream().map(it -> it.accept(this)).collect(Collectors.toList()));
    }

    @Override
    default Expression visit(StringInterpolationExpression expression) {
        return new StringInterpolationExpression(StreamSupport.stream(expression.spliterator(), false).map(it -> it.accept(this)).collect(Collectors.toList()));
    }

    @Override
    default Expression visit(AssignmentExpression expression) {
        return new AssignmentExpression(expression.getValue().accept(this));
    }

    @Override
    default Expression visit(FieldAccessExpression expression) {
        return new FieldAccessExpression(expression.getScope().accept(this), expression.getName());
    }

    @Override
    default Expression visit(QualifiedExpression expression) {
        return new QualifiedExpression(expression.getType(), expression.getLeftExpression().accept(this), expression.getRightExpression().accept(this));
    }

    @Override
    default Expression visit(InfixExpression expression) {
        return new InfixExpression(expression.getLeftExpression().accept(this), expression.getOperator(), expression.getRightExpression().accept(this));
    }

    @Override
    default Expression visit(NullLiteralExpression expression) {
        return expression;
    }

    @Override
    default Expression visit(PropertyAccessExpression expression) {
        return new PropertyAccessExpression(expression.getType(), expression.getAccessType(), expression.getObjectExpression().accept(this), expression.getPropertyName());
    }

    @Override
    default Expression visit(LiteralExpression expression) {
        return expression;
    }

    @Override
    default Expression visit(SetLiteralExpression expression) {
        return new SetLiteralExpression(expression.getType(), StreamSupport.stream(expression.getElements().spliterator(), false).map(it -> it.accept(this)).collect(Collectors.toList()));
    }

    @Override
    default Expression visit(StringLiteralExpression expression) {
        return expression;
    }

    @Override
    default Expression visit(BooleanLiteralExpression expression) {
        return expression;
    }

    @Override
    default Expression visit(MethodCallExpression expression) {
        return new MethodCallExpression(expression.getObjectExpression().accept(this), StreamSupport.stream(expression.getArguments().spliterator(), false).map(it -> it.accept(this)).collect(Collectors.toList()));
    }

    @Override
    default Expression visit(MapLiteralExpression expression) {
        Map<String, Expression> values = new LinkedHashMap<>();
        expression.getElements().forEach((k, v) -> {
            values.put(k, v.accept(this));
        });
        return new MapLiteralExpression(expression.getType(), values);
    }

    @Override
    default Expression visit(ClassLiteralExpression expression) {
        return expression;
    }

    @Override
    default Expression visit(EnclosedExpression expression) {
        return new EnclosedExpression(expression.getInner().accept(this));
    }

    @Override
    default Expression visit(CurrentScopeExpression expression) {
        return expression;
    }

    @Override
    default Expression visit(VariableDeclarationExpression expression) {
        return new VariableDeclarationExpression(expression.getModifiers(), expression.getType(), expression.getVariables().stream().map(it -> (VariableDeclarator) it.accept(this)).collect(Collectors.toList()));
    }

    @Override
    default Expression visit(TypeExpression expression) {
        return expression;
    }







    @Override
    default Statement visit(AssertStatement statement) {
        return new AssertStatement(statement.getCheck().accept(this), statement.getMessage() == null ? null : statement.getMessage().accept(this));
    }

    @Override
    default Expression visit(VariableDeclarator expression) {
        return new VariableDeclarator(expression.getType(), expression.getVariableName(), expression.getInitializer().accept(this));
    }

    @Override
    default Expression visit(GroovyDslLiteral expression) {
        return expression;
    }

	@Override
	default Expression visit(KotlinDslLiteral expression) {
		return expression;
	}

	@Override
    default Statement visit(CommentedStatement<?> statement) {
        return new CommentedStatement<>(statement.getCommentedStatement().accept(this));
    }

    @Override
    default Statement visit(ExpressionStatement statement) {
        return new ExpressionStatement(statement.getExpression().accept(this));
    }

    @Override
    default Statement visit(GroupStatement statement) {
        return new GroupStatement(StreamSupport.stream(statement.spliterator(), false).map(it -> it.accept(this)).collect(Collectors.toList()));
    }

    @Override
    default Statement visit(GradleBlockStatement statement) {
        return new GradleBlockStatement(statement.getSelector().accept(this), (LambdaExpression) statement.getBody().accept(this));
    }

    @Override
    default Statement visit(LineComment statement) {
        throw new UnsupportedOperationException();
    }

    @Override
    default Statement visit(ClassDeclaration statement) {
        throw new UnsupportedOperationException();
    }

    @Override
    default Statement visit(PluginsDslBlock.IdStatement statement) {
        return statement;
    }

    @Override
    default Statement visit(MultiStatement statement) {
        return MultiStatement.of(StreamSupport.stream(statement.spliterator(), false).map(it -> it.accept(this)).collect(Collectors.toList()));
    }
}
