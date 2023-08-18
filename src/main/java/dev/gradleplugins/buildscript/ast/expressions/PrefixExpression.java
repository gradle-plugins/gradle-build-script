package dev.gradleplugins.buildscript.ast.expressions;

import dev.gradleplugins.buildscript.ast.type.Type;

// Represent a prefixed expression, i.e !<expr>, ++<expr> --<expr>
public final class PrefixExpression implements Expression {
    private final InfixExpression.Operator operator;
    private final Expression expression;

    public PrefixExpression(InfixExpression.Operator operator, Expression expression) {
        this.operator = operator;
        this.expression = expression;
    }

    public InfixExpression.Operator getOperator() {
        return operator;
    }

    public Expression getExpression() {
        return expression;
    }

    @Override
    public Type getType() {
        throw new UnsupportedOperationException();
    }

    @Override
    public <ReturnType> ReturnType accept(Visitor<ReturnType> visitor) {
        return visitor.visit(this);
    }

    public static InfixExpression.Operator Increment = new InfixExpression.Operator() {
        @Override
        public String toString() {
            return "++";
        }
    };
    public static InfixExpression.Operator Decrement = new InfixExpression.Operator() {
        @Override
        public String toString() {
            return "--";
        }
    };
    public static InfixExpression.Operator Not = new InfixExpression.Operator() {
        @Override
        public String toString() {
            return "!";
        }
    };
}
