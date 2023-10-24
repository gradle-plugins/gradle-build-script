/*
 * Copyright 2023 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package dev.gradleplugins.buildscript.io;

import dev.gradleplugins.buildscript.GradleDsl;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static java.util.Objects.requireNonNull;

final class BuildScriptLocation {
    private final Path filePath;
    private final GradleDsl dsl;

    public BuildScriptLocation(Path filePath, GradleDsl dsl) {
        this.filePath = filePath;
        this.dsl = dsl;
    }

    public BuildScriptLocation use(GradleDsl dsl) {
        return new BuildScriptLocation(filePath, dsl);
    }

    public GradleDsl getBuildScriptDsl() {
        return dsl;
    }

    public Path getPath() {
        return filePath.getParent().resolve(dsl.fileName(filePath.getFileName().toString()));
    }

    public static BuildScriptLocation of(Path filePath) {
        requireNonNull(filePath, "'filePath' must not be null");

        GradleDsl dsl = GradleDsl.GROOVY;
        if (filePath.getFileName().toString().endsWith(".gradle.kts")) {
            dsl = GradleDsl.KOTLIN;
        }

        String fileName = filePath.getFileName().toString();
        int idx = fileName.lastIndexOf(".gradle");
        fileName = fileName.substring(0, idx);

        return new BuildScriptLocation(filePath.getParent().resolve(fileName), dsl);
    }

    public void withPath(BiConsumer<? super Path, ? super GradleDsl> pathAction) {
        try {
            Files.createDirectories(getPath().getParent());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        pathAction.accept(getPath(), dsl);
    }
}
