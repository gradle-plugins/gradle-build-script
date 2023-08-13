package dev.gradleplugins.buildscript;

import dev.gradleplugins.buildscript.syntax.Syntax;
import org.junit.jupiter.api.Assumptions;

class GroovyDslSyntaxTests extends SyntaxTester {
    @Override
    public Syntax syntax() {
        return GradleDsl.GROOVY;
    }

    @Override
    public String expectedNotExpression__foo_literal() {
        return "!foo";
    }

    @Override
    public String expectedBlockWithSingleGroup__call_someMethod_no_args() {
        return "someMethod()";
    }

    @Override
    public String expectedBlockWithSingleGroup__call_someMethod_no_args__call_someOtherMethod_foo_string() {
        return "someMethod()\n\nsomeOtherMethod('foo')";
    }

    @Override
    public String expectedCommentedSingleLine__call_someMethod_no_args() {
        return "// someMethod()";
    }

    @Override
    public String expectedCommentedSingleLine__call_someMethod_no_args__call_someOtherMethod_foo_string() {
        return "// someMethod()\n// someOtherMethod('foo')";
    }

    @Override
    public String expectedGradleExtension__obj_myExtension() {
        return "obj.myExtension";
    }

    @Override
    public String expectedEmptySetLiteral() {
        return "[] as Set";
    }

    @Override
    public String expectedSingleElementSetLiteral__value_string() {
        return "['value'] as Set";
    }

    @Override
    public String expectedNullLiteral() {
        return "null";
    }

    @Override
    public String expectedGradleBlock__foo_literal_selector() {
        return "foo {";
    }

    @Override
    public String expectedGradleBlock__register_method__foo_string_selector() {
        return "register('foo') {";
    }

    @Override
    public String expectedGradleBlock__foo_literal_selector__empty_block() {
        return "foo {}";
    }

    @Override
    public String expectedGradleBlock__foo_literal_selector__call_someMethod() {
        return "foo { someMethod() }";
    }

    @Override
    public String expectedGradleBlock__foo_literal_selector__call_someMethod__call_someOtherMethod() {
        return "foo {\n  someMethod()\n  someOtherMethod()\n}";
    }

    @Override
    public String expectedGradleBlock__foo_extension_selector__empty_block() {
        return "foo {}";
    }

    @Override
    public String expectedGradleBlock__foo_extension_selector_using_ExtensionAware_API__empty_block() {
        return "extensions.configure('foo') {}";
    }

    @Override
    public String expectedFieldAccess__myVar_foo() {
        return "myVar.foo";
    }

    @Override
    public String expectedEnclosedCastOf_myVar_to_ExtensionAware() {
        return "((ExtensionAware) myVar)";
    }

    @Override
    public String expectedPlainProperty__myVar_getFoo() {
        return "myVar.foo";
    }

    @Override
    public String expectedPlainProperty__myVar_setFoo__to__bar_string() {
        return "myVar.foo = 'bar'";
    }

    @Override
    public String expectedPlainProperty__myVar_isFoo() {
        return "myVar.foo";
    }

    @Override
    public String expectedPlainProperty__myVar_setFoo__to__true_boolean() {
        return "myVar.foo = true";
    }

    @Override
    public String expectedFieldProperty__myVar_foo() {
        return "myVar.foo";
    }

    @Override
    public String expectedFieldProperty__myVar_foo__assign__bar_string() {
        return "myVar.foo = 'bar'";
    }

    @Override
    public String expectedExtraProperty__myVar_foo() {
        return "myVar.foo";
    }

    @Override
    public String expectedExtraProperty__myVar_foo__setTo__bar_string() {
        return "myVar.foo = 'bar'";
    }

    @Override
    public String expectedExtraProperty__foo__by__myVar() {
        return Assumptions.abort();
    }

    @Override
    public String expectedGradleProperty__myVar_getFoo() {
        return "myVar.foo";
    }

    @Override
    public String expectedGradleProperty__myVar_getFoo__set__bar_string() {
        return "myVar.foo.set('bar')";
    }

    @Override
    public String expectedGradleProperty__myVar_getFoo__assign__bar_string() {
        return "myVar.foo = 'bar'";
    }

    @Override
    public String expectedVariableDeclaration__val_myVar__initialized_with__foo_string() {
        return "final def myVar = 'foo'";
    }

    @Override
    public String expectedVariableDeclaration__var_myVar__initialized_with__bar_string() {
        return "def myVar = 'bar'";
    }

    @Override
    public String expectedCallMethod__foo_asStatement() {
        return "foo()";
    }

    @Override
    public String expectedStaticImport__java_util_Collections_emptyList() {
        return "import static java.util.Collections.emptyList";
    }

    @Override
    public String expectedImport__java_util_Collections() {
        return "import static java.util.Collections.*";
    }

    @Override
    public String expectedImport__java_util_List() {
        return "import java.util.List";
    }

    @Override
    public String expectedImport__java_util() {
        return "import java.util.*";
    }

    @Override
    public String expectedAssert__true_boolean() {
        return "assert true";
    }

    @Override
    public String expectedAssert__true_boolean__my_message_string() {
        return "assert true : 'my message'";
    }

    @Override
    public String expectedBooleanLiteral__true_boolean() {
        return "true";
    }

    @Override
    public String expectedBooleanLiteral__false_boolean() {
        return "false";
    }

    @Override
    public String expectedCast__myExpression_as_String() {
        return "(String) myExpression";
    }

    @Override
    public String expectedMethodCallOnDelegate__register__foo_string() {
        return "register('foo')";
    }

    @Override
    public String expectedInstanceOf__myVar_instanceof_String() {
        return "myVar instanceof String";
    }

    @Override
    public String expectedStringLiteral__foo() {
        return "'foo'";
    }

    @Override
    public String expectedEmptyMapLiteral() {
        return "[:]";
    }

    @Override
    public String expectedSingleElementMapLiteral__k1_to_v1_string() {
        return "[k1: 'v1']";
    }
}
