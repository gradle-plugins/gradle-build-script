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

import dev.gradleplugins.buildscript.ast.expressions.DelegateExpression;
import dev.gradleplugins.buildscript.ast.expressions.InfixExpression;
import dev.gradleplugins.buildscript.ast.expressions.PropertyAccessExpression;
import dev.gradleplugins.buildscript.ast.statements.ExpressionStatement;
import dev.gradleplugins.buildscript.ast.statements.GradleBlockStatement;
import dev.gradleplugins.buildscript.ast.statements.Statement;

import java.net.URI;

import static dev.gradleplugins.buildscript.ast.expressions.MethodCallExpression.call;
import static dev.gradleplugins.buildscript.ast.type.UnknownType.unknownType;
import static dev.gradleplugins.buildscript.syntax.Syntax.literal;
import static dev.gradleplugins.buildscript.syntax.Syntax.string;

public final class ArtifactRepositoryStatements {
	public static Statement gradlePluginPortal() {
		return new ExpressionStatement(call("gradlePluginPortal"));
	}

	public static Statement mavenLocal() {
		return new ExpressionStatement(call("mavenLocal"));
	}

	public static Statement mavenCentral() {
		return new ExpressionStatement(call("mavenCentral"));
	}

	public static Statement maven(URI uri) {
		final DelegateExpression delegate = new DelegateExpression();
		return GradleBlockStatement.newBuilder().withSelector(literal("maven")).withBody(new InfixExpression(new PropertyAccessExpression(unknownType(), PropertyAccessExpression.AccessType.PLAIN, delegate, "uri"), InfixExpression.Operator.Assignment, string(uri.toString()))).withDelegate(delegate).build();
	}

	public static Statement ivy(URI uri) {
		final DelegateExpression delegate = new DelegateExpression();
		return GradleBlockStatement.newBuilder().withSelector(literal("ivy")).withBody(new InfixExpression(new PropertyAccessExpression(unknownType(), PropertyAccessExpression.AccessType.PLAIN, delegate, "uri"), InfixExpression.Operator.Assignment, string(uri.toString()))).withDelegate(delegate).build();
	}
}
