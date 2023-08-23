package dev.gradleplugins.buildscript.syntax;

import dev.gradleplugins.buildscript.syntax.normalizer.GroovyDslTransformer;
import dev.gradleplugins.buildscript.syntax.normalizer.KotlinCastingTransformer;
import dev.gradleplugins.buildscript.syntax.normalizer.KotlinPropertyAccessTransformer;
import dev.gradleplugins.buildscript.syntax.normalizer.KotlinVariableTransformer;
import dev.gradleplugins.buildscript.syntax.normalizer.MethodCallToGradleBlockTransformer;

import java.util.Arrays;

public class KotlinCompatibility extends ASTNormalizer {
    public KotlinCompatibility() {
        super(Arrays.asList(
                new GroovyDslTransformer(),
                new MethodCallToGradleBlockTransformer(),
                new KotlinPropertyAccessTransformer(),
                new KotlinVariableTransformer(),
                new KotlinCastingTransformer()
        ));
    }
}
