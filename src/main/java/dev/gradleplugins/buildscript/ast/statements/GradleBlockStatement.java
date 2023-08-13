package dev.gradleplugins.buildscript.ast.statements;

import dev.gradleplugins.buildscript.ast.expressions.Expression;

import java.util.Collections;
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
public final class GradleBlockStatement implements Statement {
    private final Expression selector;
    private final BlockStatement block;

    public GradleBlockStatement(Expression selector, BlockStatement block) {
        this.selector = selector;
        this.block = block;
    }

    public static GradleBlockStatement block(Expression selector, Statement statement) {
        return new GradleBlockStatement(selector, new BlockStatement(Collections.singletonList(statement)));
    }

    public static GradleBlockStatement block(Expression selector, Consumer<? super BlockStatement.Builder<?>> configureAction) {
        final BlockStatement.Builder<?> builder = BlockStatement.newBuilder();
        configureAction.accept(builder);
        return new GradleBlockStatement(selector, builder.build());
    }

    public static GradleBlockStatement block(String selector, Consumer<? super BlockStatement.Builder<?>> configureAction) {
        return block(literal(selector), configureAction);
    }

    public Expression getSelector() {
        return selector;
    }

    public BlockStatement getBlock() {
        return block;
    }

    public GradleBlockStatement useGetter() {
        throw new UnsupportedOperationException();
    }

    public GradleBlockStatement useExplicitIt() {
        throw new UnsupportedOperationException();
    }

    // Always block... aka never single line block

    @Override
    public <ReturnType> ReturnType accept(Visitor<ReturnType> visitor) {
        return visitor.visit(this);
    }
}
