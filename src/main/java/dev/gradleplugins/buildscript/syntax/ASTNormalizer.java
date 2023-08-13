package dev.gradleplugins.buildscript.syntax;

import dev.gradleplugins.buildscript.ast.Node;
import dev.gradleplugins.buildscript.ast.expressions.Expression;
import dev.gradleplugins.buildscript.ast.statements.Statement;

import java.util.List;

public class ASTNormalizer implements Transformer<Node> {
    private final List<ASTTransformer> transformers;

    public ASTNormalizer(List<ASTTransformer> transformers) {
        this.transformers = transformers;
    }

    @Override
    public Node transform(Node node) {
        if (node instanceof Statement) {
            Statement statement = (Statement) node;
            for (ASTTransformer transformer : transformers) {
                statement = statement.accept(transformer);
            }
            return statement;
        } else if (node instanceof Expression) {
            Expression expression = (Expression) node;
            for (ASTTransformer transformer : transformers) {
                expression = expression.accept(transformer);
            }
            return expression;
        } else {
            throw new UnsupportedOperationException();
        }
    }
}
