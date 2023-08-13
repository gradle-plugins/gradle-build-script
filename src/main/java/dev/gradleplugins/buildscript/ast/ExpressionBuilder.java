package dev.gradleplugins.buildscript.ast;

import dev.gradleplugins.buildscript.ast.expressions.AssignExpression;
import dev.gradleplugins.buildscript.ast.expressions.AssignmentExpression;
import dev.gradleplugins.buildscript.ast.expressions.CastExpression;
import dev.gradleplugins.buildscript.ast.expressions.EnclosedExpression;
import dev.gradleplugins.buildscript.ast.expressions.Expression;
import dev.gradleplugins.buildscript.ast.expressions.InfixExpression;
import dev.gradleplugins.buildscript.ast.expressions.InstanceOfExpression;
import dev.gradleplugins.buildscript.ast.expressions.MethodCallExpression;
import dev.gradleplugins.buildscript.ast.expressions.NotExpression;
import dev.gradleplugins.buildscript.ast.expressions.PropertyAccessExpression;
import dev.gradleplugins.buildscript.ast.expressions.QualifiedExpression;
import dev.gradleplugins.buildscript.ast.statements.BlockStatement;
import dev.gradleplugins.buildscript.ast.statements.GradleBlockStatement;
import dev.gradleplugins.buildscript.ast.type.ReferenceType;
import dev.gradleplugins.buildscript.ast.type.Type;
import dev.gradleplugins.buildscript.ast.type.UnknownType;

import java.util.Arrays;
import java.util.function.Consumer;

import static dev.gradleplugins.buildscript.syntax.Syntax.literal;

public final class ExpressionBuilder<T extends Expression> implements Expression {
    private final Expression thiz;

    public ExpressionBuilder(Expression thiz) {
        this.thiz = thiz;
    }

    @Override
    public Type getType() {
        return null;
    }

    @Override
    public <ReturnType> ReturnType accept(Visitor<ReturnType> visitor) {
        return thiz.accept(visitor);
    }

    public ExpressionBuilder<CastExpression> as(Type type) {
        return new ExpressionBuilder<>(new CastExpression(type, thiz));
    }

    public ExpressionBuilder<CastExpression> as(Class<?> type) {
        return as(ReferenceType.of(type));
    }

    public ExpressionBuilder<EnclosedExpression> enclose() {
        return new ExpressionBuilder<>(new EnclosedExpression(thiz));
    }

    public ExpressionBuilder<PropertyAccessExpression> plainProperty(String propertyName) {
        return new ExpressionBuilder<>(new PropertyAccessExpression(UnknownType.unknownType(), PropertyAccessExpression.AccessType.PLAIN, thiz, propertyName));
    }

    public ExpressionBuilder<MethodCallExpression> call(String methodName, Expression... arguments) {
        return new ExpressionBuilder<>(new MethodCallExpression(thiz, methodName, Arrays.asList(arguments)));
    }

    public ExpressionBuilder<MethodCallExpression> call(Expression... arguments) {
        return new ExpressionBuilder<>(new MethodCallExpression(thiz, Arrays.asList(arguments)));
    }

    public ExpressionBuilder<InstanceOfExpression> instanceOf(ReferenceType type) {
        return new ExpressionBuilder<>(new InstanceOfExpression(thiz, type));
    }

    public ExpressionBuilder<InstanceOfExpression> instanceOf(Class<?> type) {
        return new ExpressionBuilder<>(new InstanceOfExpression(thiz, new ReferenceType(type.getCanonicalName())));
    }

    public GradleBlockStatement block(Consumer<? super BlockStatement.Builder<?>> configureAction) {
        return GradleBlockStatement.block(this, configureAction);
    }

    public ExpressionBuilder<QualifiedExpression> dot(String name) {
        return new ExpressionBuilder<>(new QualifiedExpression(thiz, literal(name)));
    }

    public ExpressionBuilder<InfixExpression> and(Expression expression) {
        return new ExpressionBuilder<>(new InfixExpression(thiz, InfixExpression.Operator.And, expression));
    }

    public ExpressionBuilder<InfixExpression> equalTo(Expression expression) {
        return new ExpressionBuilder<>(new InfixExpression(thiz, InfixExpression.Operator.EqualTo, expression));
    }

    public ExpressionBuilder<NotExpression> negate() {
        return new ExpressionBuilder<>(new NotExpression(thiz));
    }

    public ExpressionBuilder<InfixExpression> plus(Expression expression) {
        return new ExpressionBuilder<>(new InfixExpression(thiz, InfixExpression.Operator.Plus, expression));
    }

    public ExpressionBuilder<Expression> safeDot(String name) {
        throw new UnsupportedOperationException();
    }

    public ExpressionBuilder<PropertyAccessExpression> extensionOf(String extensionName) {
        throw new UnsupportedOperationException();
    }

    public ExpressionBuilder<Expression> assign(Expression expression) {
        return new ExpressionBuilder<>(new AssignExpression(thiz, expression));
    }
}
