package dev.gradleplugins.buildscript;

import dev.gradleplugins.buildscript.ast.statements.ImportDeclaration;
import dev.gradleplugins.buildscript.syntax.Syntax;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public interface TypeImportDeclarationTester {
    Syntax syntax();

    @Test
    default void testImportType() {
        assertEquals(expectedImport__java_util_List(),
                new ImportDeclaration(ImportDeclaration.ImportType.TYPE, "java.util.List").toString(syntax()));
    }

    String expectedImport__java_util_List();

    @Test
    default void testImportPackage() {
        assertEquals(expectedImport__java_util(),
                new ImportDeclaration(ImportDeclaration.ImportType.TYPE, "java.util.*").toString(syntax()));
    }

    String expectedImport__java_util();
}
