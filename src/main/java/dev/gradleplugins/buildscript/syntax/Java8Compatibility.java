package dev.gradleplugins.buildscript.syntax;

import dev.gradleplugins.buildscript.syntax.normalizer.Java8MapLiteralTransformer;
import dev.gradleplugins.buildscript.syntax.normalizer.Java8SetLiteralTransformer;
import dev.gradleplugins.buildscript.syntax.normalizer.Java8VariableTransformer;
import dev.gradleplugins.buildscript.syntax.normalizer.JavaCastingTransformer;
import dev.gradleplugins.buildscript.syntax.normalizer.JavaPropertyAccessTransformer;
import dev.gradleplugins.buildscript.syntax.normalizer.JavaSafeNavigationTransformer;

import java.util.Arrays;

public class Java8Compatibility extends ASTNormalizer {
    public Java8Compatibility() {
        super(Arrays.asList(
                new JavaPropertyAccessTransformer(),
                new Java8SetLiteralTransformer(),
                new Java8MapLiteralTransformer(),
                new Java8VariableTransformer(),
                new JavaCastingTransformer(),
                new JavaSafeNavigationTransformer()
        ));
    }
}
