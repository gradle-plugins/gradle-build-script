package dev.gradleplugins.buildscript.syntax.normalizer;

import dev.gradleplugins.buildscript.ast.expressions.DelegateExpression;
import dev.gradleplugins.buildscript.ast.expressions.Expression;
import dev.gradleplugins.buildscript.ast.expressions.ItExpression;
import dev.gradleplugins.buildscript.ast.expressions.LambdaExpression;
import dev.gradleplugins.buildscript.ast.expressions.LiteralExpression;
import dev.gradleplugins.buildscript.ast.statements.GradleBlockStatement;
import dev.gradleplugins.buildscript.ast.statements.Statement;
import dev.gradleplugins.buildscript.ast.type.Type;
import dev.gradleplugins.buildscript.syntax.ASTTransformer;

import static dev.gradleplugins.buildscript.ast.type.UnknownType.unknownType;

public final class UseExplicitItTransformer implements ASTTransformer {
    private final String name;
    private final Type type;

    public UseExplicitItTransformer() {
        this("it", unknownType());
    }

    public UseExplicitItTransformer(String name) {
        this(name, unknownType());
    }

    public UseExplicitItTransformer(String name, Type type) {
        this.name = name;
        this.type = type;
    }

    @Override
    public Statement visit(GradleBlockStatement blockStatement) {
        final Expression itExpression = new LiteralExpression(type, name);

        return new GradleBlockStatement(blockStatement.getSelector(), new ASTTransformer() {
            @Override
            public LambdaExpression visit(LambdaExpression expression) {
                LambdaExpression result = expression;
                if (expression.getParameters() instanceof LambdaExpression.SingleImplicitParameter) {
                    result = (LambdaExpression) ASTTransformer.super.visit(result);
                    if (!name.equals("it")) {
                        result = new LambdaExpression(expression.getLambdaType(), ((LambdaExpression.SingleImplicitParameter) result.getParameters()).useExplicitIt(name, type), result.getBody().orElse(null));
                    }
                }
                return result;
            }

            @Override
            public Expression visit(DelegateExpression expression) {
                if (expression.equals(blockStatement.getDelegate())) {
                    return itExpression;
                }
                return ASTTransformer.super.visit(expression);
            }

            @Override
            public Expression visit(ItExpression expression) {
                if (expression.equals(blockStatement.getIt())) {
                    return itExpression;
                }
                return ASTTransformer.super.visit(expression);
            }
        }.visit(blockStatement.getBody()));
    }
}
