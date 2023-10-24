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

import dev.gradleplugins.buildscript.ast.expressions.Expression;
import dev.gradleplugins.buildscript.ast.statements.ExpressionStatement;
import dev.gradleplugins.buildscript.ast.statements.MultiStatement;
import dev.gradleplugins.buildscript.ast.statements.Statement;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public interface GradleBuildScriptFile {
    static GradleBuildScriptFile of(Path filePath) {
        class DefaultGradleBuildScriptFile extends AbstractBuildScriptFile implements GradleBuildScriptFile {
            private BuildScriptLocation location;
            private final List<Statement> statements = new ArrayList<>();

            private DefaultGradleBuildScriptFile(BuildScriptLocation location) {
                this.location = location;
            }

            @Override
            public GradleBuildScriptFile append(Statement statement) {
                statements.add(statement);
                return writeScriptToFileSystem();
            }

            @Override
            public GradleBuildScriptFile append(Expression expression) {
                statements.add(new ExpressionStatement(expression));
                return writeScriptToFileSystem();
            }

            private GradleBuildScriptFile writeScriptToFileSystem() {
                List<Statement> stmt = new ArrayList<>();
                stmt.addAll(statements);
                location.withPath((filePath, dsl) -> {
                    try {
                        Files.write(filePath, MultiStatement.of(stmt).toString(dsl).getBytes(StandardCharsets.UTF_8));
                    } catch (IOException e) {
                        throw new UncheckedIOException(e);
                    }
                });


                return this;
            }
        }

        return new DefaultGradleBuildScriptFile(BuildScriptLocation.of(filePath)).writeScriptToFileSystem();
    }

    GradleBuildScriptFile append(Statement statement);

    GradleBuildScriptFile append(Expression expression);
}
