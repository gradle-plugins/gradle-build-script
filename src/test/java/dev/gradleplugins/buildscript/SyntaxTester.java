package dev.gradleplugins.buildscript;

public abstract class SyntaxTester implements MapLiteralExpressionTester
    , StringLiteralExpressionTester
    , TypeComparisonExpressionTester
    , MethodCallExpressionTester
    , CastingExpressionTester
    , BooleanLiteralExpressionTester
    , AssertStatementTester
    , TypeImportDeclarationTester
    , StaticImportDeclarationTester
    , ExpressionStatementTester
    , VariableDeclarationExpressionTester
    , PropertyAccessExpressionTester
    , EnclosedExpressionTester
    , FieldAccessExpressionTester
    , GradleBlockStatementTester
    , CommentedStatementTester
    , NullLiteralExpressionTester
    , SetLiteralExpressionTester
    , GroupStatementTester
    , NotExpressionTester
{

}
