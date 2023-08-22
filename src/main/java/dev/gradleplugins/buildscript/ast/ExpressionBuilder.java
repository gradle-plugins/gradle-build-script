package dev.gradleplugins.buildscript.ast;

import dev.gradleplugins.buildscript.ast.expressions.CastingExpression;
import dev.gradleplugins.buildscript.ast.expressions.EnclosedExpression;
import dev.gradleplugins.buildscript.ast.expressions.Expression;
import dev.gradleplugins.buildscript.ast.expressions.InfixExpression;
import dev.gradleplugins.buildscript.ast.expressions.MethodCallExpression;
import dev.gradleplugins.buildscript.ast.expressions.PrefixExpression;
import dev.gradleplugins.buildscript.ast.expressions.PropertyAccessExpression;
import dev.gradleplugins.buildscript.ast.expressions.QualifiedExpression;
import dev.gradleplugins.buildscript.ast.expressions.TypeComparisonExpression;
import dev.gradleplugins.buildscript.ast.statements.GradleBlockStatement;
import dev.gradleplugins.buildscript.ast.type.ReferenceType;
import dev.gradleplugins.buildscript.ast.type.Type;
import dev.gradleplugins.buildscript.ast.type.UnknownType;

import java.util.Arrays;
import java.util.function.Consumer;

import static dev.gradleplugins.buildscript.syntax.Syntax.literal;

public final class ExpressionBuilder<T extends Expression> implements Expression {
    private final T thiz;

    public ExpressionBuilder(T thiz) {
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

    public ExpressionBuilder<CastingExpression> as(Type type) {
        return new ExpressionBuilder<>(new CastingExpression(CastingExpression.CastingType.AS, type, thiz));
    }

    public ExpressionBuilder<CastingExpression> as(Class<?> type) {
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

    public ExpressionBuilder<TypeComparisonExpression> instanceOf(ReferenceType type) {
        return new ExpressionBuilder<>(new TypeComparisonExpression(TypeComparisonExpression.ComparisonType.INSTANCE_OF, thiz, type));
    }

    public ExpressionBuilder<TypeComparisonExpression> instanceOf(Class<?> type) {
        return new ExpressionBuilder<>(new TypeComparisonExpression(TypeComparisonExpression.ComparisonType.INSTANCE_OF, thiz, new ReferenceType(type.getCanonicalName())));
    }

    public GradleBlockStatementBuilder block(Consumer<? super GradleBlockStatement.Body.Builder<?>> configureAction) {
        return GradleBlockStatement.block(this, configureAction);
    }

    // TODO: Maybe we should take Expression and convert it into the proper unified expression
    public ExpressionBuilder<QualifiedExpression> dot(String name) {
        return new ExpressionBuilder<>(new QualifiedExpression(thiz, literal(name)));
    }

    public ExpressionBuilder<InfixExpression> and(Expression expression) {
        return new ExpressionBuilder<>(new InfixExpression(thiz, InfixExpression.Operator.And, expression));
    }

    public ExpressionBuilder<InfixExpression> equalTo(Expression expression) {
        return new ExpressionBuilder<>(new InfixExpression(thiz, InfixExpression.Operator.EqualTo, expression));
    }

    public ExpressionBuilder<PrefixExpression> negate() {
        return new ExpressionBuilder<>(new PrefixExpression(PrefixExpression.Not, thiz));
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
        return new ExpressionBuilder<>(new InfixExpression(thiz, InfixExpression.Operator.Assignment, expression));
    }

    public T get() {
        return thiz;
    }
}
