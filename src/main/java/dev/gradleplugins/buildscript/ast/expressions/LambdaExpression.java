package dev.gradleplugins.buildscript.ast.expressions;

import dev.gradleplugins.buildscript.ast.Node;
import dev.gradleplugins.buildscript.ast.body.Parameter;
import dev.gradleplugins.buildscript.ast.statements.ExpressionStatement;
import dev.gradleplugins.buildscript.ast.statements.MultiStatement;
import dev.gradleplugins.buildscript.ast.statements.Statement;
import dev.gradleplugins.buildscript.ast.type.Type;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static dev.gradleplugins.buildscript.ast.type.UnknownType.unknownType;
import static java.util.Collections.singletonList;

public final class LambdaExpression implements Expression {
    private final LambdaType lambdaType;
    private final Parameters parameters;
    @Nullable private final Node body; // FIXME: Should be Statement or Expression

    public LambdaExpression(LambdaType lambdaType, Parameters parameters, @Nullable Node body) {
        this.lambdaType = lambdaType;
        this.parameters = parameters;
        this.body = body;
    }

    public Parameters getParameters() {
        return parameters;
    }

    public LambdaType getLambdaType() {
        return lambdaType;
    }

    public Optional<Node> getBody() {
        return Optional.ofNullable(body);
    }

    @Override
    public Type getType() {
        throw new UnsupportedOperationException();
    }

    @Override
    public <ReturnType> ReturnType accept(Visitor<ReturnType> visitor) {
        return visitor.visit(this);
    }

    private static final LambdaType DEFAULT_TYPE = new LambdaType() {};

    // Represent the type of lambda, i.e. Groovy with dynamic resolution, Kotlin with optional receiver, Java.
    public interface LambdaType {
        static LambdaType defaultType() {
            return DEFAULT_TYPE;
        }
    }

    // Represent the lambda parameters, i.e. none, single implicit, explicit
    public interface Parameters extends Iterable<Parameter> {
        static Parameters noParameters() {
            return new NoParameters();
        }

        static SingleImplicitParameter implicitParameter() {
            return new SingleImplicitParameter();
        }

        long count();

        default Stream<Parameter> stream() {
            return StreamSupport.stream(spliterator(), false);
        }
    }

    private static final class NoParameters implements Parameters {
        @Override
        public Iterator<Parameter> iterator() {
            return Collections.<Parameter>emptyList().iterator();
        }

        @Override
        public long count() {
            return 0;
        }
    }

    public static final class SingleImplicitParameter implements Parameters {
        private final Expression itExpression;

        public SingleImplicitParameter() {
            this(new ItExpression());
        }

        public SingleImplicitParameter(Expression itExpression) {
            this.itExpression = itExpression;
        }

        public Expression it() {
            return itExpression;
        }

        public Parameters useExplicitIt(String name) {
            return new ExplicitParameters(singletonList(new Parameter(unknownType(), name)));
        }

        public Parameters useExplicitIt(String name, Type type) {
            return new ExplicitParameters(singletonList(new Parameter(type, name)));
        }

        @Override
        public Iterator<Parameter> iterator() {
            return Collections.<Parameter>emptyList().iterator();
        }

        @Override
        public long count() {
            return 1;
        }
    }

    private static final class ExplicitParameters implements Parameters {
        private final List<Parameter> parameters;

        private ExplicitParameters(List<Parameter> parameters) {
            assert parameters != null;
            this.parameters = parameters;
        }

        @Override
        public Iterator<Parameter> iterator() {
            return parameters.iterator();
        }

        @Override
        public long count() {
            return parameters.size();
        }
    }

    // implicit parameter -> Groovy/Kotlin {  it... } vs Java it -> {}
    // no parameter -> Groovy/Kotlin? { -> ... } vs Java () -> {}
    // multiple parameters -> Groovy/Kotlin? { a, b, c -> ... } vs Java (a, b, c) -> {}

    // delegate -> Groovy { <delegate>... } vs Kotlin { <this?>... } vs Java???
    // this -> Groovy { <owner>... } vs Kotlin??? vs Java???
    // Resolution strategy -> Groovy (delegate, first parameter, etc.) vs Kotlin??? vs Java (outer scope)
    public static final class Builder {
        private Parameters parameters = new SingleImplicitParameter();
        @Nullable private Node body = null;
        private LambdaType lambdaType = LambdaType.defaultType();

        public Builder noParameters() {
            this.parameters = new NoParameters();
            return this;
        }

        public Builder withParameters(List<Parameter> parameters) {
            this.parameters = new ExplicitParameters(parameters);
            return this;
        }

        public Builder withParameters(Parameters parameters) {
            this.parameters = Objects.requireNonNull(parameters);
            return this;
        }

        public Builder withLambdaType(LambdaType lambdaType) {
            this.lambdaType = Objects.requireNonNull(lambdaType);
            return this;
        }

        public Builder withBody(@Nullable Node body) {
            assert body == null || body instanceof Expression || body instanceof Statement;
            this.body = body;
            return this;
        }

        public Builder withEmptyBody() {
            this.body = null; // TODO: Maybe consider an empty node?
            return this;
        }

        public LambdaExpression build() {
            return new LambdaExpression(lambdaType, parameters, body);
        }
    }

    public static final class BodyBuilder {
        private final List<Node> statements = new ArrayList<>();

        public BodyBuilder add(Statement statement) {
            statements.add(statement);
            return this;
        }

        public BodyBuilder add(Expression expression) {
            statements.add(expression);
            return this;
        }

        @Nullable public Node build() {
            if (statements.isEmpty()) {
                return null; // empty body
            } else if (statements.size() == 1 && statements.get(0) instanceof Expression) {
                return statements.get(0); // single expression
            } else {
                // multiple statements
                return MultiStatement.of(statements.stream().map(it -> {
                    if (it instanceof Expression) {
                        return new ExpressionStatement((Expression) it);
                    } else {
                        return (Statement) it;
                    }
                }).collect(Collectors.toList()));
            }
        }
    }
}
