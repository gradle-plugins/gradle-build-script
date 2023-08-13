package dev.gradleplugins.buildscript.ast.expressions;

import dev.gradleplugins.buildscript.ast.Node;
import dev.gradleplugins.buildscript.ast.body.Parameter;
import dev.gradleplugins.buildscript.ast.statements.ExpressionStatement;
import dev.gradleplugins.buildscript.ast.statements.MultiStatement;
import dev.gradleplugins.buildscript.ast.statements.Statement;
import dev.gradleplugins.buildscript.ast.type.Type;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static dev.gradleplugins.buildscript.ast.type.UnknownType.unknownType;

public final class LambdaExpression implements Expression {
    private final List<Parameter> parameters;
    private final Node body; // FIXME: Should be Statement or Expression

    public LambdaExpression(List<Parameter> parameters, Node body) {
        this.parameters = parameters;
        this.body = body;
    }

    public List<Parameter> getParameters() {
        return parameters;
    }

    public Node getBody() {
        return body;
    }

    @Override
    public Type getType() {
        throw new UnsupportedOperationException();
    }

    @Override
    public <ReturnType> ReturnType accept(Visitor<ReturnType> visitor) {
        return visitor.visit(this);
    }

    public static final class Builder {
        private final List<Parameter> parameters = new ArrayList<Parameter>() {{
            add(new Parameter(unknownType(), "it"));
        }};
        private final List<Node> statements = new ArrayList<>();

        public Builder add(Statement statement) {
            statements.add(statement);
            return this;
        }

        public Builder add(Expression expression) {
            statements.add(expression);
            return this;
        }

        public LambdaExpression build() {
            if (statements.size() == 1 && statements.get(0) instanceof Expression) {
                return new LambdaExpression(parameters, statements.get(0));
            } else {
                return new LambdaExpression(parameters, MultiStatement.of(statements.stream().map(it -> {
                    if (it instanceof Expression) {
                        return new ExpressionStatement((Expression) it);
                    } else {
                        return (Statement) it;
                    }
                }).collect(Collectors.toList())));
            }
        }
    }
}
