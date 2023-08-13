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
package dev.gradleplugins.buildscript;

import dev.gradleplugins.buildscript.ast.Node;
import dev.gradleplugins.buildscript.ast.statements.Statement;
import dev.gradleplugins.buildscript.syntax.Syntax;

import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;
import java.io.Writer;


public final class PrettyPrinter implements Closeable, Flushable {
	private final Writer out;
	private final Syntax syntax;
//	private final String indent = "  ";

//	public static PrettyPrinter buildFile(GradleDsl dsl, Path destinationDirectory) throws IOException {
//		return new PrettyPrinter(new FileWriter(destinationDirectory.resolve(dsl.fileName("build")).toFile()));
//	}

//	public PrettyPrinter(Writer out) {
//		this(out, new GroovySyntax());
//	}

	public PrettyPrinter(Writer out, Syntax syntax) {
		this.out = out;
		this.syntax = syntax;
	}

	@Override
	public void close() throws IOException {
		out.close();
	}

	@Override
	public void flush() throws IOException {
		out.flush();
	}

	public PrettyPrinter write(Node node) throws IOException {
		out.write(syntax.render(node));
//		out.write(statement.accept(syntax).toString()); // TODO: pass indent here
		return this;
	}
}
