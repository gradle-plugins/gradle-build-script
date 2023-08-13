package dev.gradleplugins.buildscript;

import dev.gradleplugins.buildscript.ast.statements.TypeImportDeclaration;
import dev.gradleplugins.buildscript.syntax.Syntax;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public interface TypeImportDeclarationTester {
    Syntax syntax();

    @Test
    default void testImportType() {
        assertEquals(expectedImport__java_util_List(),
                new TypeImportDeclaration("java.util.List").toString(syntax()));
    }

    String expectedImport__java_util_List();

    @Test
    default void testImportPackage() {
        assertEquals(expectedImport__java_util(),
                new TypeImportDeclaration("java.util.*").toString(syntax()));
    }

    String expectedImport__java_util();
}
