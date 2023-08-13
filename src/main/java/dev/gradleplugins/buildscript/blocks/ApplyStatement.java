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
import dev.gradleplugins.buildscript.ast.expressions.QualifiedExpression;
import dev.gradleplugins.buildscript.ast.statements.ExpressionStatement;
import dev.gradleplugins.buildscript.ast.statements.Statement;
import dev.gradleplugins.buildscript.ast.expressions.ClassLiteralExpression;
import dev.gradleplugins.buildscript.ast.expressions.StringLiteralExpression;
import dev.gradleplugins.buildscript.ast.type.ReferenceType;

import java.util.Collections;

import static dev.gradleplugins.buildscript.ast.expressions.CurrentScopeExpression.current;
import static dev.gradleplugins.buildscript.syntax.Syntax.literal;
import static dev.gradleplugins.buildscript.syntax.Syntax.mapOf;
import static dev.gradleplugins.buildscript.syntax.Syntax.string;

public final class ApplyStatement implements Statement {
	private final Notation notation;

	private ApplyStatement(Notation notation) {
		this.notation = notation;
	}

	public Notation getNotation() {
		return notation;
	}

	public static ApplyStatement apply(Notation notation) {
		return new ApplyStatement(notation);
	}

	@Override
	public <ReturnType> ReturnType accept(Visitor<ReturnType> visitor) {
		return new ExpressionStatement(new MethodCallExpression(new QualifiedExpression(current(), literal("apply")), Collections.singletonList(notation.asExpression()))).accept(visitor);
	}

	public interface Notation {
		static PluginNotation plugin(String pluginId) {
			return new PluginNotation(new StringLiteralExpression(pluginId));
		}

		static PluginNotation plugin(Class<?> pluginType) {
			return new PluginNotation(new ClassLiteralExpression(new ReferenceType(pluginType.getTypeName())));
		}

		static FromNotation from(String path) {
			return new FromNotation(path);
		}

		Expression asExpression();
	}

	public static final class PluginNotation implements Notation {
		private final Expression pluginExpression;

		public PluginNotation(Expression pluginExpression) {
			this.pluginExpression = pluginExpression;
		}

		public Expression getPluginExpression() {
			return pluginExpression;
		}

		@Override
		public Expression asExpression() {
			return mapOf("plugin", pluginExpression);
		}
	}

	public static final class FromNotation implements Notation {
		private final String path;

		public FromNotation(String path) {
			this.path = path;
		}

		public String getPath() {
			return path;
		}

		@Override
		public Expression asExpression() {
			return mapOf("from", string(path));
		}
	}
}
