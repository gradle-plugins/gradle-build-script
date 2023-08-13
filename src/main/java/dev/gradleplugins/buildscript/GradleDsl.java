package dev.gradleplugins.buildscript;

import dev.gradleplugins.buildscript.ast.Node;
import dev.gradleplugins.buildscript.ast.statements.GroovyDslLiteral;
import dev.gradleplugins.buildscript.ast.statements.Statement;
import dev.gradleplugins.buildscript.syntax.GroovyCompatibility;
import dev.gradleplugins.buildscript.syntax.GroovyRender;
import dev.gradleplugins.buildscript.syntax.KotlinCompatibility;
import dev.gradleplugins.buildscript.syntax.KotlinRender;
import dev.gradleplugins.buildscript.syntax.Syntax;

import java.util.List;

public enum GradleDsl implements Syntax {
    GROOVY {
        @Override
        public Statement literal(List<String> literalScript) {
            return new GroovyDslLiteral(literalScript);
        }

        @Override
        public String getFileExtension() {
            return "gradle";
        }

        @Override
        public String render(Node node) {
            return new GroovyRender().render(new GroovyCompatibility().transform(node)).toString();
        }
    },
    KOTLIN {
        @Override
        public Statement literal(List<String> literalScript) {
            throw new UnsupportedOperationException();
        }

        @Override
        public String getFileExtension() {
            return "gradle.kts";
        }

        @Override
        public String render(Node node) {
            return new KotlinRender().render(new KotlinCompatibility().transform(node)).toString();
        }
    };

    public abstract Statement literal(List<String> literalScript);

    public abstract String getFileExtension();

    public String fileName(String pathWithoutExtension) {
        return pathWithoutExtension + "." + getFileExtension();
    }
}
