package dev.gradleplugins.buildscript.syntax;

import dev.gradleplugins.buildscript.syntax.normalizer.GroovyPropertyAccessTransformer;
import dev.gradleplugins.buildscript.syntax.normalizer.GroovyVariableTransformer;

import java.util.Arrays;

public class GroovyCompatibility extends ASTNormalizer {
    public GroovyCompatibility() {
        super(Arrays.asList(
                new GroovyPropertyAccessTransformer(),
                new GroovyVariableTransformer()
        ));
    }
}
