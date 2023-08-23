package dev.gradleplugins.buildscript.syntax;

import dev.gradleplugins.buildscript.syntax.normalizer.GroovyCastingTransformer;
import dev.gradleplugins.buildscript.syntax.normalizer.GroovyPropertyAccessTransformer;
import dev.gradleplugins.buildscript.syntax.normalizer.GroovyVariableTransformer;
import dev.gradleplugins.buildscript.syntax.normalizer.MethodCallToGradleBlockTransformer;

import java.util.Arrays;

public class GroovyCompatibility extends ASTNormalizer {
    public GroovyCompatibility() {
        super(Arrays.asList(
                new MethodCallToGradleBlockTransformer(),
                new GroovyPropertyAccessTransformer(),
                new GroovyVariableTransformer(),
                new GroovyCastingTransformer()
        ));
    }
}
