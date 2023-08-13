package dev.gradleplugins.buildscript.ast.expressions;

import dev.gradleplugins.buildscript.ast.ExpressionBuilder;
import dev.gradleplugins.buildscript.ast.type.Type;

import java.util.Arrays;
import java.util.List;

import static dev.gradleplugins.buildscript.ast.expressions.CurrentScopeExpression.current;
import static dev.gradleplugins.buildscript.ast.type.UnknownType.unknownType;

// Represents a method call expression.
public final class MethodCallExpression implements Expression {
    private final Expression objectExpression;
    private List<Expression> arguments;
    private final boolean alwaysUseParenthesis;

    public MethodCallExpression(Expression scope, String name, List<Expression> arguments) {
        this(new QualifiedExpression(scope, new LiteralExpression(unknownType(), name)), arguments, false);
    }

    public MethodCallExpression(Expression objectExpression, List<Expression> arguments) {
        this(objectExpression, arguments, false);
    }

    private MethodCallExpression(Expression objectExpression, List<Expression> arguments, boolean alwaysUseParenthesis) {
        assert objectExpression != null;
        assert arguments != null;
        this.objectExpression = objectExpression;
        this.arguments = arguments;
        this.alwaysUseParenthesis = alwaysUseParenthesis;
    }

    // Method call where the scope is the delegate (or first argument)
    public static ExpressionBuilder<MethodCallExpression> call(String name, Expression... arguments) {
        return new ExpressionBuilder<>(new MethodCallExpression(current(), name, Arrays.asList(arguments)));
    }

    public static MethodCallExpression call(Expression scope, String name, Expression... arguments) {
        return new MethodCallExpression(scope, name, Arrays.asList(arguments));
    }

    public Expression getObjectExpression() {
        return objectExpression;
    }

    public List<Expression> getArguments() {
        return arguments;
    }

    public MethodCallExpression alwaysUseParenthesis() {
        return new MethodCallExpression(objectExpression, arguments, true);
    }

    @Override
    public Type getType() {
        return null;
    }

    @Override
    public <ReturnType> ReturnType accept(Visitor<ReturnType> visitor) {
        return visitor.visit(this);
    }
}
