package dev.gradleplugins.buildscript.ast.statements;

import dev.gradleplugins.buildscript.ast.ExpressionBuilder;
import dev.gradleplugins.buildscript.ast.GradleBlockStatementBuilder;
import dev.gradleplugins.buildscript.ast.Node;
import dev.gradleplugins.buildscript.ast.body.Parameter;
import dev.gradleplugins.buildscript.ast.expressions.DelegateExpression;
import dev.gradleplugins.buildscript.ast.expressions.Expression;
import dev.gradleplugins.buildscript.ast.expressions.ItExpression;
import dev.gradleplugins.buildscript.ast.expressions.LambdaExpression;
import dev.gradleplugins.buildscript.ast.expressions.LiteralExpression;
import dev.gradleplugins.buildscript.syntax.ASTTransformer;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

import static dev.gradleplugins.buildscript.syntax.Syntax.literal;

/**
 * <code>
 *     selector {
 *         // statements
 *     }
 * </code>
 *
 * Example:
 * <code>
 *     dependencies {
 *         ...
 *     }
 * </code>
 * or
 * <code>
 *     tasks.register('foo') {
 *         ...
 *     }
 * </code>
 */
// Represent a selector with a lambda expression
//   The selector support Action (or sometime Closure) -based lambdas
//   The lambda is assumed to be single parameter with a receiver/delegate
//   When no decoration exists, aka Kotlin lambda with receiver or Closure, it is explicitly required
//
//   The lambda expression is assumed to be Action (or Closure) -based
public final class GradleBlockStatement implements Statement {
    private final Expression selectorExpression;
    private final LambdaExpression bodyExpression;

    public GradleBlockStatement(Expression selectorExpression, LambdaExpression bodyExpression) {
        assert selectorExpression != null;
        assert bodyExpression != null;
        this.selectorExpression = selectorExpression;
        this.bodyExpression = bodyExpression;
    }

    public Expression getDelegate() {
        assert bodyExpression.getLambdaType() instanceof GradleBlockType;
        return ((GradleBlockType) bodyExpression.getLambdaType()).getReceiver();
    }

    public Expression getIt() {
        assert bodyExpression.getParameters().count() == 1;

        if (bodyExpression.getParameters() instanceof LambdaExpression.SingleImplicitParameter) {
            return ((LambdaExpression.SingleImplicitParameter) bodyExpression.getParameters()).it();
        } else {
            final Parameter explicitItParameter = bodyExpression.getParameters().iterator().next();
            return new LiteralExpression(explicitItParameter.getType(), explicitItParameter.getName());
        }
    }

    private static final class GradleBlockType implements LambdaExpression.LambdaType {
        private final Expression receiverExpression;

        public GradleBlockType() {
            this(new DelegateExpression());
        }

        public GradleBlockType(Expression receiverExpression) {
            this.receiverExpression = receiverExpression;
        }

        public Expression getReceiver() {
            return receiverExpression;
        }

        // Resolution strategy -> receiver, outer scope
    }

    public Expression getSelector() {
        return selectorExpression;
    }

    public LambdaExpression getBody() {
        return bodyExpression;
    }

    public Builder toBuilder() {
        return new Builder(selectorExpression, ((GradleBlockType) bodyExpression.getLambdaType()).getReceiver(), bodyExpression.getBody().orElse(null));
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static final class Builder {
        private Expression itExpression;
        private Expression delegateExpression;
        private Expression selectorExpression;
        private Node bodyExpression;

        public Builder() {}

        private Builder(Expression selectorExpression, Expression delegateExpression, @Nullable Node bodyExpression) {
            this.selectorExpression = selectorExpression;
            this.delegateExpression = delegateExpression;
            this.bodyExpression = bodyExpression;
        }

        public Builder withIt(ItExpression itExpression) {
            this.itExpression = itExpression;
            return this;
        }

        public Builder withDelegate(DelegateExpression delegateExpression) {
            this.delegateExpression = Objects.requireNonNull(delegateExpression);
            return this;
        }

        public Builder withSelector(Expression selectorExpression) {
            this.selectorExpression = Objects.requireNonNull(selectorExpression).accept(new ASTTransformer() {});
            return this;
        }

        public Builder withBody(Node node) {
            this.bodyExpression = node;
            return this;
        }

        public Builder withEmptyBody() {
            this.bodyExpression = null;
            return this;
        }

        public GradleBlockStatement build() {
            GradleBlockType lambdaType = new GradleBlockType();
            if (delegateExpression != null) {
                lambdaType = new GradleBlockType(delegateExpression);
            }

            LambdaExpression.SingleImplicitParameter parameters = new LambdaExpression.SingleImplicitParameter();
            if (itExpression != null) {
                parameters = new LambdaExpression.SingleImplicitParameter(itExpression);
            }

            return new GradleBlockStatement(selectorExpression, new LambdaExpression(lambdaType, parameters, bodyExpression));
        }
    }

    public static GradleBlockStatementBuilder block(Expression selector, Node node) {
        return new GradleBlockStatementBuilder(GradleBlockStatement.newBuilder().withSelector(selector).withBody(new LambdaExpression(new GradleBlockType(), LambdaExpression.Parameters.implicitParameter(), node)).build());
    }

    public static GradleBlockStatementBuilder block(Expression selector, Consumer<? super Body.Builder<?>> configureAction) {
        final Body.Builder<?> builder = Body.newBuilder();
        configureAction.accept(builder);
        return new GradleBlockStatementBuilder(GradleBlockStatement.newBuilder().withSelector(selector).withBody(builder.build()).build());
    }

    public static GradleBlockStatementBuilder block(String selector, Consumer<? super Body.Builder<?>> configureAction) {
        return block(literal(selector), configureAction);
    }

    // TODO: Allow never collapsing block
    //   By default, single statement cause the block to collapse.

    @Override
    public <ReturnType> ReturnType accept(Visitor<ReturnType> visitor) {
        // TODO: Convert into a method call with last parameter as lambda
        return visitor.visit(this);
    }

    public static final class Body {
        private final List<Parameter> parameters;
        private final Expression itExpression; // TODO: may or may not be known
        private final Expression delegateExpression = null;
        private final Node content;

        public Body(List<Parameter> parameters, Expression itExpression, Node content) {
            this.parameters = parameters;
            this.itExpression = itExpression;
            this.content = content;
        }

        public List<Parameter> getParameters() {
            return parameters;
        }

        public Node getContent() {
            return content;
        }

        public Expression getItExpression() {
            return itExpression;
        }

        public Expression getDelegateExpression() {
            return delegateExpression;
        }

        public static Body of(Node statement) {
            return new Body(Collections.emptyList(), new ItExpression(), statement);
        }

        public static Body empty() {
            return new Body(Collections.emptyList(), new ItExpression(), MultiStatement.of(Collections.emptyList()));
        }

        @SuppressWarnings("rawtypes")
        public static Builder newBuilder() {
            return new Builder(Body.Builder.class) {
                @Override
                protected Body.Builder newBuilder() {
                    return Body.newBuilder();
                }
            };
        }

        public static abstract class Builder<SelfType extends Builder<SelfType>> {
            private final Class<SelfType> type;
            private final LambdaExpression.BodyBuilder builder = new LambdaExpression.BodyBuilder();
            private final LambdaExpression.SingleImplicitParameter parameters = LambdaExpression.Parameters.implicitParameter();
            private final GradleBlockType lambdaType = new GradleBlockType();

            protected Builder(Class<SelfType> type) {
                this.type = type;
            }

            public SelfType add(Statement statement) {
                builder.add(statement);
                return type.cast(this);
            }

            public SelfType add(Expression expression) {
                builder.add(expression);
                 return type.cast(this);
            }

            public SelfType comment(String comment, Consumer<? super SelfType> configureAction) {
                final SelfType nestedBlock = newBuilder();
                configureAction.accept(nestedBlock);
    //                return add(new SingleLineCommentStatement<>(comment, nestedBlock.build()));
                throw new UnsupportedOperationException();
            }

            public SelfType commented(Consumer<? super SelfType> configureAction) {
                final SelfType nestedBlock = newBuilder();
                configureAction.accept(nestedBlock);
    //                return add(new CommentedStatement<>(nestedBlock.build()));
                throw new UnsupportedOperationException();
            }

            // Choose it to refer to the first parameter, mostly the delegate
            public ExpressionBuilder<?> it() {
                return new ExpressionBuilder<>(parameters.it());
            }

            // Choose delegate to refer to `it` implicitly, let's not be confused by scope which can be a lot of things
            public ExpressionBuilder<?> delegate() {
                return new ExpressionBuilder<>(lambdaType.getReceiver());
            }

            protected abstract SelfType newBuilder();

            public LambdaExpression build() {
                return new LambdaExpression(lambdaType, parameters, builder.build());
            }
        }
    }
}
