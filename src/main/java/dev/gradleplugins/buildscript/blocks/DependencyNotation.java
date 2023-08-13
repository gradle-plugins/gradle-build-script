/*
 * Copyright 2022 the original author or authors.
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
package dev.gradleplugins.buildscript.blocks;

import dev.gradleplugins.buildscript.ast.expressions.Expression;
import dev.gradleplugins.buildscript.ast.expressions.MethodCallExpression;
import dev.gradleplugins.buildscript.syntax.Syntax;

import java.io.File;
import java.util.Collections;
import java.util.Objects;
import java.util.stream.StreamSupport;

import static dev.gradleplugins.buildscript.ast.expressions.CurrentScopeExpression.current;
import static dev.gradleplugins.buildscript.syntax.Syntax.string;

public final class DependencyNotation {
	private final Expression expression;

	public DependencyNotation(Expression expression) {
		this.expression = expression;
	}

	public static DependencyNotation files(Iterable<? extends File> files) {
		return new DependencyNotation(MethodCallExpression.call("files", StreamSupport.stream(files.spliterator(), false).map(File::toURI).map(Objects::toString).map(Syntax::string).toArray(Expression[]::new)));
	}

	public static DependencyNotation fromString(String s) {
		return new DependencyNotation(string(s));
	}

	public static DependencyNotation project(String path) {
		return new DependencyNotation(new MethodCallExpression(current(), "project", Collections.singletonList(string(path))).alwaysUseParenthesis());
	}

	public static DependencyNotation platform(String path) {
		return new DependencyNotation(new MethodCallExpression(current(), "platform", Collections.singletonList(string(path))).alwaysUseParenthesis());
	}

	public static DependencyNotation call(String methodName) {
		return new DependencyNotation(MethodCallExpression.call(methodName));
	}

	Expression asExpression() {
		return expression;
	}
}
