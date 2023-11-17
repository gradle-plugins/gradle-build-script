package dev.gradleplugins.buildscript.syntax;

import dev.gradleplugins.buildscript.ast.ExpressionBuilder;
import dev.gradleplugins.buildscript.ast.Node;
import dev.gradleplugins.buildscript.ast.expressions.BooleanLiteralExpression;
import dev.gradleplugins.buildscript.ast.expressions.ClassLiteralExpression;
import dev.gradleplugins.buildscript.ast.expressions.Expression;
import dev.gradleplugins.buildscript.ast.expressions.GroovyDslLiteral;
import dev.gradleplugins.buildscript.ast.expressions.LambdaExpression;
import dev.gradleplugins.buildscript.ast.expressions.LiteralExpression;
import dev.gradleplugins.buildscript.ast.expressions.MapLiteralExpression;
import dev.gradleplugins.buildscript.ast.expressions.NullLiteralExpression;
import dev.gradleplugins.buildscript.ast.expressions.PrefixExpression;
import dev.gradleplugins.buildscript.ast.expressions.PropertyAccessExpression;
import dev.gradleplugins.buildscript.ast.expressions.SetLiteralExpression;
import dev.gradleplugins.buildscript.ast.expressions.StringLiteralExpression;
import dev.gradleplugins.buildscript.ast.statements.AssertStatement;
import dev.gradleplugins.buildscript.ast.statements.CommentedStatement;
import dev.gradleplugins.buildscript.ast.statements.ImportDeclaration;
import dev.gradleplugins.buildscript.ast.statements.Statement;
import dev.gradleplugins.buildscript.ast.type.ReferenceType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static dev.gradleplugins.buildscript.ast.expressions.CurrentScopeExpression.current;
import static dev.gradleplugins.buildscript.ast.type.UnknownType.unknownType;

public interface Syntax {
    String render(Node node);

    static ExpressionBuilder<StringLiteralExpression> string(CharSequence value) {
        return new ExpressionBuilder<>(new StringLiteralExpression(value));
    }

    static BooleanLiteralExpression bool(boolean value) {
        return new BooleanLiteralExpression(value);
    }

    static ExpressionBuilder<LiteralExpression> literal(Object obj) {
        return new ExpressionBuilder<>(new LiteralExpression(unknownType(), obj));
    }

    static ExpressionBuilder<MapLiteralExpression> emptyMap() {
        return new ExpressionBuilder<>(new MapLiteralExpression(unknownType(), Collections.emptyMap()));
    }

    static ExpressionBuilder<MapLiteralExpression> mapOf(String key, Expression value) {
        return new ExpressionBuilder<>(new MapLiteralExpression(value.getType(), Collections.singletonMap(key, value)));
    }

	static ExpressionBuilder<ClassLiteralExpression> classOf(String classNameOrCanonicalName) {
		return new ExpressionBuilder<>(new ClassLiteralExpression(new ReferenceType(classNameOrCanonicalName)));
	}

	static ExpressionBuilder<ClassLiteralExpression> classOf(ReferenceType type) {
		return new ExpressionBuilder<>(new ClassLiteralExpression(type));
	}

    static NullLiteralExpression nul() {
        return new NullLiteralExpression();
    }

    static ExpressionBuilder<SetLiteralExpression> emptySet() {
        return new ExpressionBuilder<>(new SetLiteralExpression(unknownType(), Collections.emptyList()));
    }

    static ExpressionBuilder<SetLiteralExpression> setOf(Expression first, Expression... others) {
        return new ExpressionBuilder<>(new SetLiteralExpression(first.getType(), new ArrayList<Expression>() {{
            add(first);
            addAll(Arrays.asList(others));
        }}));
    }

    static AssertStatement assertTrue(Expression expression) {
        return new AssertStatement(expression, null);
    }

    static ImportDeclaration importClass(Class<?> type) {
        return new ImportDeclaration(ImportDeclaration.ImportType.TYPE, type.getCanonicalName());
    }

    static ExpressionBuilder<PropertyAccessExpression> extensionOf(String extensionName) {
        return new ExpressionBuilder<>(new PropertyAccessExpression(unknownType(), PropertyAccessExpression.AccessType.EXTENSION, current(), extensionName));
    }

    static GroovyDslLiteral groovyDsl(String... lines) {
        return new GroovyDslLiteral(Arrays.asList(lines));
    }

    static <T extends Statement> CommentedStatement<T> commented(T commentedStatement) {
        return new CommentedStatement<>(Objects.requireNonNull(commentedStatement));
    }

    static <T extends Statement> Statement comment(String comment, T block) {
        throw new UnsupportedOperationException();
//        return new GroupStatement(Arrays.asList(new LineComment(comment), block));
    }

    static PrefixExpression not(Expression expression) {
        return new PrefixExpression(PrefixExpression.Not, expression);
    }

    static ExpressionBuilder<LambdaExpression> lambda(Consumer<? super LambdaExpression.Builder> configureAction) {
        final LambdaExpression.Builder builder = new LambdaExpression.Builder();
        configureAction.accept(builder);
        return new ExpressionBuilder<>(builder.build());
    }

    // TODO: Move to Renderer
    interface Content extends Iterable<String> {
        default Content indent() {
            final Builder builder = builder();
            forEach(line -> builder.add("  " + line));
            return builder.build();
        }

        default Content commented() {
            final Builder builder = builder();
            forEach(line -> builder.add("// " + line));
            return builder.build();
        }

        default Content grouped() {
            final Builder builder = builder();
            builder.add("");
            builder.add(this);
            return builder.build();
        }

        default boolean isEmpty() {
            return !iterator().hasNext();
        }

        default boolean hasSingleLine() {
            Iterator<?> iter = iterator();
            if (!iter.hasNext()) {
                return false;
            }
            iter.next();

            return !iter.hasNext();
        }

        static Content of(String s) {
            return new Content() {
                @Override
                public Iterator<String> iterator() {
                    return Stream.of(s).iterator();
                }

                @Override
                public String toString() {
                    return s;
                }
            };
        }

        static Content empty() {
            return new Content() {
                @Override
                public Iterator<String> iterator() {
                    return Stream.<String>empty().iterator();
                }
            };
        }

        static Builder builder() {
            return new Builder();
        }

        public final class Builder {
            private final List<String> lines = new ArrayList<>();

            public Builder add(String line) {
                lines.add(line);
                return this;
            }

            public Builder add(Content content) {
                content.forEach(lines::add);
                return this;
            }

            public Content build() {
                return new Content() {
                    @Override
                    public Iterator<String> iterator() {
                        return lines.iterator();
                    }

                    @Override
                    public String toString() {
                        return String.join("\n", lines).trim();
                    }
                };
            }
        }
    }
}
