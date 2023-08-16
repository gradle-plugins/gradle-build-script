package dev.gradleplugins.buildscript.syntax;

import dev.gradleplugins.buildscript.syntax.normalizer.Java11MapLiteralTransformer;
import dev.gradleplugins.buildscript.syntax.normalizer.Java11SetLiteralTransformer;
import dev.gradleplugins.buildscript.syntax.normalizer.Java11VariableTransformer;
import dev.gradleplugins.buildscript.syntax.normalizer.JavaCastingTransformer;
import dev.gradleplugins.buildscript.syntax.normalizer.JavaPropertyAccessTransformer;
import dev.gradleplugins.buildscript.syntax.normalizer.JavaSafeNavigationTransformer;

import java.util.Arrays;

public class Java11Compatibility extends ASTNormalizer {
    public Java11Compatibility() {
        super(Arrays.asList(
                new JavaPropertyAccessTransformer(),
                new Java11SetLiteralTransformer(),
                new Java11MapLiteralTransformer(),
                new Java11VariableTransformer(),
                new JavaCastingTransformer(),
                new JavaSafeNavigationTransformer()
        ));
    }
}
