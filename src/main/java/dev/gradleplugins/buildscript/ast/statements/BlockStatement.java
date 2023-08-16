package dev.gradleplugins.buildscript.ast.statements;

import dev.gradleplugins.buildscript.ast.ExpressionBuilder;
import dev.gradleplugins.buildscript.ast.expressions.Expression;
import dev.gradleplugins.buildscript.ast.expressions.LambdaExpression;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public final class BlockStatement implements Statement, Iterable<Statement> {
    private final List<Statement> statements;

    public BlockStatement(List<Statement> statements) {
        this.statements = statements;
    }

    @Override
    public <ReturnType> ReturnType accept(Visitor<ReturnType> visitor) {
        throw new UnsupportedOperationException();
//        return visitor.visit(this);
    }

    @Override
    public Iterator<Statement> iterator() {
        return statements.iterator();
    }

    public static BlockStatement fromLambda(LambdaExpression expression) {
        if (expression.getBody() instanceof MultiStatement) {
            return new BlockStatement(StreamSupport.stream(((MultiStatement) expression.getBody()).spliterator(), false).collect(Collectors.toList()));
        } else {
            throw new UnsupportedOperationException();
        }
    }

    @SuppressWarnings("rawtypes")
    public static Builder newBuilder() {
        return new Builder(Builder.class) {
            @Override
            protected Builder newBuilder() {
                return BlockStatement.newBuilder();
            }
        };
    }

    public static abstract class Builder<SelfType extends Builder<SelfType>> {
        private final Class<SelfType> type;
        private final List<Statement> statements = new ArrayList<>();

        protected Builder(Class<SelfType> type) {
            this.type = type;
        }

        public SelfType add(Statement statement) {
            statements.add(statement);
            return type.cast(this);
        }

        public SelfType add(Expression expression) {
             return add(new ExpressionStatement(expression));
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

        public ExpressionBuilder<?> it() {
            throw new UnsupportedOperationException();
        }

        public ExpressionBuilder<?> scope() {
            throw new UnsupportedOperationException();
        }

        protected abstract SelfType newBuilder();
//        public Expression delegate() {
//            throw new UnsupportedOperationException();
//        }
//
//        public SelfType useExplicitIt() {
//            throw new UnsupportedOperationException();
//        }

        public BlockStatement build() {
            return new BlockStatement(statements);
        }
    }
}
