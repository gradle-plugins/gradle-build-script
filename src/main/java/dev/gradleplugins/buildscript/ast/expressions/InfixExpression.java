package dev.gradleplugins.buildscript.ast.expressions;

import dev.gradleplugins.buildscript.ast.type.Type;

import static dev.gradleplugins.buildscript.ast.type.PrimitiveType.booleanType;

public final class InfixExpression implements Expression {
    private final Expression leftExpression;
    private final Operator operator;
    private final Expression rightExpression;

    public InfixExpression(Expression leftExpression, Operator operator, Expression rightExpression) {
        assert leftExpression != null;
        assert operator != null;
        assert rightExpression != null;
        this.leftExpression = leftExpression;
        this.operator = operator;
        this.rightExpression = rightExpression;
    }

    public Expression getLeftExpression() {
        return leftExpression;
    }

    public Operator getOperator() {
        return operator;
    }

    public Expression getRightExpression() {
        return rightExpression;
    }

    @Override
    public Type getType() {
        if (operator.equals(Operator.Plus)) {
            return leftExpression.getType();
        } else if (operator.equals(Operator.Assignment)) {
            return rightExpression.getType();
        } else if (operator.equals(Operator.And) || operator.equals(Operator.EqualTo) || operator.equals(Operator.NotEqualTo)) {
            return booleanType();
        } else {
            throw new UnsupportedOperationException();
        }
    }

    @Override
    public <ReturnType> ReturnType accept(Visitor<ReturnType> visitor) {
        return visitor.visit(this);
    }

    public interface Operator {
        // Arithmetic operators
        Operator Plus = new Operator() {
            @Override
            public String toString() {
                return "+";
            }
        };

        // Logical operators
        Operator And = new Operator() {
            @Override
            public String toString() {
                return "&&";
            }
        };
        Operator Or = new Operator() {
            @Override
            public String toString() {
                return "||";
            }
        };

        // Relational operators
        Operator EqualTo = new Operator() {
            @Override
            public String toString() {
                return "==";
            }
        };
        Operator NotEqualTo = new Operator() {
            @Override
            public String toString() {
                return "!=";
            }
        };
        Operator LessThan = new Operator() {
            @Override
            public String toString() {
                return "<";
            }
        };
        Operator GreaterThan = new Operator() {
            @Override
            public String toString() {
                return ">";
            }
        };
        Operator LessThanOrEqualTo = new Operator() {
            @Override
            public String toString() {
                return "<=";
            }
        };
        Operator GreaterThanOrEqualTo = new Operator() {
            @Override
            public String toString() {
                return ">=";
            }
        };

        // Assignment operators
        Operator Assignment = new Operator() {
            @Override
            public String toString() {
                return "=";
            }
        };
    }
}
