package dev.gradleplugins.buildscript;

import dev.gradleplugins.buildscript.ast.statements.ImportDeclaration;
import dev.gradleplugins.buildscript.syntax.Syntax;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public interface StaticImportDeclarationTester {
    Syntax syntax();

    @Test
    default void testStaticImportMethod() {
        assertEquals(expectedStaticImport__java_util_Collections_emptyList(),
                new ImportDeclaration(ImportDeclaration.ImportType.STATIC, "java.util.Collections.emptyList").toString(syntax()));
    }

    String expectedStaticImport__java_util_Collections_emptyList();

    @Test
    default void testStaticImportMethods() {
        assertEquals(expectedImport__java_util_Collections(),
                new ImportDeclaration(ImportDeclaration.ImportType.STATIC, "java.util.Collections.*").toString(syntax()));
    }

    String expectedImport__java_util_Collections();
}
