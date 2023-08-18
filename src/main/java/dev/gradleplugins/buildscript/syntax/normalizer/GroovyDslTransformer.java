package dev.gradleplugins.buildscript.syntax.normalizer;

import dev.gradleplugins.buildscript.GroovyDslLexer;
import dev.gradleplugins.buildscript.GroovyDslParser;
import dev.gradleplugins.buildscript.GroovyDslParserBaseVisitor;
import dev.gradleplugins.buildscript.ast.Modifier;
import dev.gradleplugins.buildscript.ast.expressions.AsExpression;
import dev.gradleplugins.buildscript.ast.expressions.AssignmentExpression;
import dev.gradleplugins.buildscript.ast.expressions.BooleanLiteralExpression;
import dev.gradleplugins.buildscript.ast.expressions.CollectionLiteralExpression;
import dev.gradleplugins.buildscript.ast.expressions.EnclosedExpression;
import dev.gradleplugins.buildscript.ast.expressions.Expression;
import dev.gradleplugins.buildscript.ast.expressions.GroovyDslLiteral;
import dev.gradleplugins.buildscript.ast.expressions.InfixExpression;
import dev.gradleplugins.buildscript.ast.expressions.InstanceOfExpression;
import dev.gradleplugins.buildscript.ast.expressions.LambdaExpression;
import dev.gradleplugins.buildscript.ast.expressions.LiteralExpression;
import dev.gradleplugins.buildscript.ast.expressions.MethodCallExpression;
import dev.gradleplugins.buildscript.ast.expressions.NullLiteralExpression;
import dev.gradleplugins.buildscript.ast.expressions.PrefixExpression;
import dev.gradleplugins.buildscript.ast.expressions.QualifiedExpression;
import dev.gradleplugins.buildscript.ast.expressions.SafeNavigationExpression;
import dev.gradleplugins.buildscript.ast.expressions.SetLiteralExpression;
import dev.gradleplugins.buildscript.ast.expressions.StringInterpolationExpression;
import dev.gradleplugins.buildscript.ast.expressions.StringLiteralExpression;
import dev.gradleplugins.buildscript.ast.expressions.VariableDeclarationExpression;
import dev.gradleplugins.buildscript.ast.expressions.VariableDeclarator;
import dev.gradleplugins.buildscript.ast.statements.ExpressionStatement;
import dev.gradleplugins.buildscript.ast.statements.MultiStatement;
import dev.gradleplugins.buildscript.ast.statements.Statement;
import dev.gradleplugins.buildscript.ast.type.DefType;
import dev.gradleplugins.buildscript.ast.type.ReferenceType;
import dev.gradleplugins.buildscript.ast.type.Type;
import dev.gradleplugins.buildscript.syntax.ASTTransformer;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RuleContext;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.RuleNode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static dev.gradleplugins.buildscript.ast.type.UnknownType.unknownType;

public final class GroovyDslTransformer implements ASTTransformer {
    private static abstract class Base<T> extends GroovyDslParserBaseVisitor<T> {
        @Override
        protected T defaultResult() {
            return null;
        }

        @Override
        public T visitChildren(RuleNode node) {
            T result = defaultResult();
            int n = node.getChildCount();
            for (int i=0; i<n; i++) {
                if (!shouldVisitNextChild(node, result)) {
                    break;
                }

                ParseTree c = node.getChild(i);
                T childResult = c.accept(this);
                result = aggregateResult(result, childResult);
            }

            return Objects.requireNonNull(result, () -> "currently on " + this.getClass());
        }

        @Override
        protected T aggregateResult(T aggregate, T nextResult) {
            if (aggregate != null && nextResult != null) {
                throw new UnsupportedOperationException();
            }
            return super.aggregateResult(aggregate, nextResult);
        }
    }

    private static final class BaseVisitor extends Base<Statement> {
        interface PostfixUnarySuffixExpression {
            Expression withObjectExpression(Expression objectExpression);
        }

        private static final class ExpressionVisitor extends Base<Expression> {
            @Override
            public Expression visitSimpleIdentifier(GroovyDslParser.SimpleIdentifierContext ctx) {
                return new LiteralExpression(unknownType(), ctx.getText());
            }

            @Override
            public Expression visitParenthesizedExpression(GroovyDslParser.ParenthesizedExpressionContext ctx) {
                return new EnclosedExpression(ctx.expression().accept(this));
            }

            @Override
            public Expression visitPrefixUnaryExpression(GroovyDslParser.PrefixUnaryExpressionContext ctx) {
                final Base<InfixExpression.Operator> visitor = new Base<InfixExpression.Operator>() {
                    @Override
                    public InfixExpression.Operator visitPrefixUnaryOperator(GroovyDslParser.PrefixUnaryOperatorContext ctx) {
                        switch (ctx.getText().trim()) {
                            case "--": return PrefixExpression.Decrement;
                            case "++": return PrefixExpression.Increment;
                            case "!": return PrefixExpression.Not;
                            default: throw new UnsupportedOperationException();
                        }
                    }
                };

                Expression result = ctx.postfixUnaryExpression().accept(this);
                for (int i = ctx.unaryPrefix().size() - 1; i >= 0; --i) {
                    result = new PrefixExpression(ctx.unaryPrefix(i).accept(visitor), result);
                }
                return result;
            }

            @Override
            public Expression visitLiteralConstant(GroovyDslParser.LiteralConstantContext ctx) {
                if (ctx.NullLiteral() != null) {
                    return new NullLiteralExpression();
                } else if (ctx.BooleanLiteral() != null) {
                    return new BooleanLiteralExpression(Boolean.parseBoolean(ctx.BooleanLiteral().getText()));
                } else {
                    throw new UnsupportedOperationException();
                }
            }

            //region String support
            @Override
            public Expression visitStringLiteral(GroovyDslParser.StringLiteralContext ctx) {
                return super.visitStringLiteral(ctx);
            }

            @Override
            public Expression visitCollectionLiteral(GroovyDslParser.CollectionLiteralContext ctx) {
                return new CollectionLiteralExpression(ctx.expression().stream().map(it -> it.accept(this)).collect(Collectors.toList()));
            }

            @Override
            public Expression visitAsExpression(GroovyDslParser.AsExpressionContext ctx) {
                Expression result = ctx.prefixUnaryExpression().accept(this);
                if (ctx.asOperator() != null) {
                    final Type type = ctx.type_().accept(new Base<Type>() {
                        @Override
                        public Type visitSimpleIdentifier(GroovyDslParser.SimpleIdentifierContext ctx) {
                            return new ReferenceType(ctx.getText());
                        }
                    });

                    if (result instanceof CollectionLiteralExpression && type instanceof ReferenceType && ((ReferenceType) type).typeOnly().toString().equals("Set")) {
                        return new SetLiteralExpression(result.getType(), ((CollectionLiteralExpression) result).getExpressions());
                    } else {
                        result = new AsExpression(type, result);
                    }
                }
                return result;
            }

            @Override // 'some string'
            public Expression visitLiteralLineStringLiteral(GroovyDslParser.LiteralLineStringLiteralContext ctx) {
                return new StringLiteralExpression(ctx.literalStringContent().stream().map(RuleContext::getText).collect(Collectors.joining()));
            }

            @Override // "${...}"
            public Expression visitLineStringLiteral(GroovyDslParser.LineStringLiteralContext ctx) {
                return new StringInterpolationExpression(ctx.accept(new Base<List<Expression>>() {
                    @Override
                    public List<Expression> visitLineStringLiteral(GroovyDslParser.LineStringLiteralContext ctx) {
                        return super.visitLineStringLiteral(ctx);
                    }

                    @Override
                    public List<Expression> visitLineStringContent(GroovyDslParser.LineStringContentContext ctx) {
                        return Collections.singletonList(new StringLiteralExpression(ctx.getText()));
                    }

                    @Override
                    public List<Expression> visitLineStringExpression(GroovyDslParser.LineStringExpressionContext ctx) {
                        return Collections.singletonList(ctx.expression().accept(ExpressionVisitor.this));
                    }

                    @Override
                    protected List<Expression> defaultResult() {
                        return Collections.emptyList();
                    }

                    @Override
                    protected List<Expression> aggregateResult(List<Expression> aggregate, List<Expression> nextResult) {
                        return new ArrayList<Expression>() {{
                            addAll(aggregate);
                            addAll(nextResult);
                        }};
                    }
                }));
            }
            //endregion

            @Override
            public Expression visitConjunction(GroovyDslParser.ConjunctionContext ctx) {
                Expression result = ctx.equality(0).accept(this);
                for (int i = 1; i < ctx.equality().size(); ++i) {
                    result = new InfixExpression(result, InfixExpression.Operator.And, ctx.equality(i).accept(this));
                }
                return result;
            }

            @Override
            public Expression visitDisjunction(GroovyDslParser.DisjunctionContext ctx) {
                Expression result = ctx.conjunction(0).accept(this);
                for (int i = 1; i < ctx.conjunction().size(); ++i) {
                    result = new InfixExpression(result, InfixExpression.Operator.Or, ctx.conjunction(i).accept(this));
                }
                return result;
            }

            @Override
            public Expression visitEquality(GroovyDslParser.EqualityContext ctx) {
                Base<InfixExpression.Operator> visitor = new Base<InfixExpression.Operator>() {
                    @Override
                    public InfixExpression.Operator visitEqualityOperator(GroovyDslParser.EqualityOperatorContext ctx) {
                        switch (ctx.getText()) {
                            case "==": return InfixExpression.Operator.EqualTo;
                            case "!=": return InfixExpression.Operator.NotEqualTo;
                            default: throw new UnsupportedOperationException();
                        }
                    }
                };

                Expression result = ctx.comparison(0).accept(this);
                for (int i = 1; i < ctx.comparison().size(); ++i) {
                    result = new InfixExpression(result, ctx.equalityOperator(i - 1).accept(visitor), ctx.comparison(i).accept(this));
                }
                return result;
            }

            @Override
            public Expression visitPropertyDeclaration(GroovyDslParser.PropertyDeclarationContext ctx) {
                return super.visitPropertyDeclaration(ctx);
            }

            @Override
            public Expression visitLambdaLiteral(GroovyDslParser.LambdaLiteralContext ctx) {
                List<Statement> statements = ctx.statements().statement().stream().map(it -> it.accept(new BaseVisitor())).collect(Collectors.toList());

                // FIXME: capture parameters
                // TODO: Use block instead of multi-statement
                return new LambdaExpression(Collections.emptyList(), MultiStatement.of(statements));
            }

            @Override
            public Expression visitPostfixUnaryExpression(GroovyDslParser.PostfixUnaryExpressionContext ctx) {
                class UnarySuffixVisitor extends Base<Expression> {
                    private final Expression objectExpression;

                    public UnarySuffixVisitor(Expression objectExpression) {
                        this.objectExpression = objectExpression;
                    }

                    @Override
                    public Expression visitNavigationSuffix(GroovyDslParser.NavigationSuffixContext ctx) {
                        final String operator = ctx.memberAccessOperator().getText();
                        assert operator.equals(".") || operator.equals("?.");
                        final Expression identifier = ((Supplier<Expression>) () -> {
                            if (ctx.simpleIdentifier() != null) {
                                return ctx.simpleIdentifier().accept(ExpressionVisitor.this);
                            } else {
                                throw new UnsupportedOperationException();
                            }
                        }).get();

                        if (operator.equals(".")) {
                            return new QualifiedExpression(objectExpression, identifier);
                        } else {
                            return new SafeNavigationExpression(objectExpression, identifier);
                        }
                    }

                    @Override
                    public Expression visitCallSuffix(GroovyDslParser.CallSuffixContext ctx) {
                        final List<Expression> arguments = ((Supplier<List<Expression>>) () -> {
                            // Note: use mutable list, so we can optionally add annotated lambda
                            if (ctx.valueArguments() == null) {
                                return new ArrayList<>();
                            } else {
                                return ctx.valueArguments().valueArgument().stream().map(it -> it.accept(ExpressionVisitor.this)).collect(Collectors.toList());
                            }
                        }).get();

                        if (ctx.annotatedLambda() != null) {
                            arguments.add(ctx.annotatedLambda().accept(ExpressionVisitor.this));
                        }

                        return new MethodCallExpression(objectExpression, arguments);
                    }
                };

                Expression result = ctx.primaryExpression().accept(this);
                for (GroovyDslParser.PostfixUnarySuffixContext suffix : ctx.postfixUnarySuffix()) {
                    result = suffix.accept(new UnarySuffixVisitor(result));
                }
                return result;
            }

            @Override
            public Expression visitInfixOperation(GroovyDslParser.InfixOperationContext ctx) {
                class InfixSuffixVisitor extends Base<Expression> {
                    private final Expression objectExpression;

                    public InfixSuffixVisitor(Expression objectExpression) {
                        this.objectExpression = objectExpression;
                    }

                    @Override
                    public Expression visitInstanceOfOperationSuffix(GroovyDslParser.InstanceOfOperationSuffixContext ctx) {
                        final Type userType = ctx.type_().accept(new Base<Type>() {
                            @Override
                            public Type visitUserType(GroovyDslParser.UserTypeContext ctx) {
                                return super.visitUserType(ctx);
                            }

                            @Override
                            public Type visitSimpleIdentifier(GroovyDslParser.SimpleIdentifierContext ctx) {
                                return new ReferenceType(ctx.getText());
                            }
                        });

                        return new InstanceOfExpression(objectExpression, userType);
                    }
                };

                Expression result = ctx.elvisExpression().accept(this);
                for (GroovyDslParser.InfixOperationSuffixContext suffix : ctx.infixOperationSuffix()) {
                    result = suffix.accept(new InfixSuffixVisitor(result));
                }
                return result;
            }
        }

        @Override
        public Statement visitBuildScript(GroovyDslParser.BuildScriptContext ctx) {
            List<Statement> statements = ctx.statement().stream().map(it -> it.accept(this)).collect(Collectors.toList());
            if (statements.size() == 1) {
                return statements.get(0);
            }
            return MultiStatement.of(statements);
        }

        @Override
        public Statement visitStatements(GroovyDslParser.StatementsContext ctx) {
            throw new UnsupportedOperationException(); // handled by buildScript rule
        }

        @Override
        public Statement visitExpression(GroovyDslParser.ExpressionContext ctx) {
            return new ExpressionStatement(ctx.accept(new ExpressionVisitor()));
        }

        @Override
        public Statement visitAssignment(GroovyDslParser.AssignmentContext ctx) {
            if (ctx.directlyAssignableExpression() != null) {
                return new ExpressionStatement(new InfixExpression(ctx.directlyAssignableExpression().accept(new ExpressionVisitor()), InfixExpression.Operator.Assignment, ctx.expression().accept(new ExpressionVisitor())));
            } else {
                throw new UnsupportedOperationException();
            }
        }

        @Override
        public Statement visitGroovyVariableDeclaration(GroovyDslParser.GroovyVariableDeclarationContext ctx) {
            Type userType = DefType.defType();
            if (ctx.type_() != null) {
                userType = ctx.type_().accept(new Base<Type>() {
                    @Override
                    public Type visitUserType(GroovyDslParser.UserTypeContext ctx) {
                        return super.visitUserType(ctx);
                    }

                    @Override
                    public Type visitSimpleIdentifier(GroovyDslParser.SimpleIdentifierContext ctx) {
                        return new ReferenceType(ctx.getText());
                    }
                });
            }

            List<Modifier> modifiers = new ArrayList<>();
            if (ctx.FINAL() != null) {
                modifiers.add(Modifier.finalModifier());
            }

            Expression initializer = ctx.expression().accept(new ExpressionVisitor());
            String variableName = ctx.simpleIdentifier().getText();
            return new ExpressionStatement(new VariableDeclarationExpression(modifiers, userType, Collections.singletonList(new VariableDeclarator(initializer.getType(), variableName, new AssignmentExpression(initializer)))));
        }
    }

    @Override
    public Expression visit(GroovyDslLiteral expression) {
        Statement result = parse(expression);
        if (result instanceof ExpressionStatement) {
            return ((ExpressionStatement) result).getExpression();
        }
        throw new UnsupportedOperationException();
    }

    @Override
    public Statement visit(ExpressionStatement statement) {
        if (statement.getExpression() instanceof GroovyDslLiteral) {
            return parse((GroovyDslLiteral) statement.getExpression());
        }
        return ASTTransformer.super.visit(statement);
    }

    private Statement parse(GroovyDslLiteral expression) {
        final String content = String.join("\n", expression);

        GroovyDslLexer lexer = new GroovyDslLexer(CharStreams.fromString(content));
        lexer.removeErrorListeners();

        CommonTokenStream tokens = new CommonTokenStream(lexer);
        GroovyDslParser parser = new GroovyDslParser(tokens);
        parser.removeErrorListeners();

        return parser.buildScript().accept(new BaseVisitor());
    }
}
