/**
 * Copyright 2011 The Open Source Research Group,
 *                University of Erlangen-Nürnberg
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.sweble.wikitext.parser.utils;

import java.io.StringWriter;
import java.io.Writer;

import org.sweble.wikitext.parser.nodes.WtEmptyImmutableNode;
import org.sweble.wikitext.parser.nodes.WtNode;

import de.fau.cs.osr.ptk.common.AstPrinter;
import de.fau.cs.osr.ptk.common.ast.AstLeafNode;
import de.fau.cs.osr.ptk.common.ast.AstNode;
import de.fau.cs.osr.ptk.common.ast.AstNodeList;
import de.fau.cs.osr.ptk.common.ast.AstStringNode;
import de.fau.cs.osr.ptk.common.ast.AstText;

public class WtAstPrinter
		extends
			AstPrinter<WtNode>
{
	public void visit(AstNode<WtNode> n)
	{
		if (printAbsent((AstNode<?>) n))
			return;
		super.visit(n);
	}

	@Override
	public void visit(AstLeafNode<WtNode> n)
	{
		if (printAbsent((AstNode<?>) n))
			return;
		super.visit(n);
	}

	@Override
	public void visit(AstNodeList<WtNode> n)
	{
		if (printAbsent((AstNode<?>) n))
			return;
		super.visit(n);
	}

	@Override
	public void visit(AstStringNode<WtNode> n)
	{
		if (printAbsent((AstNode<?>) n))
			return;
		super.visit(n);
	}

	@Override
	public void visit(AstText<WtNode> n)
	{
		if (printAbsent((AstNode<?>) n))
			return;
		super.visit(n);
	}

	// =========================================================================

	private boolean printAbsent(@SuppressWarnings("rawtypes") AstNode n)
	{
		if (!(n instanceof WtEmptyImmutableNode))
			return false;

		WtEmptyImmutableNode ein = (WtEmptyImmutableNode) n;
		if (!ein.indicatesAbsence())
			return false;

		p.indentln('-');
		return true;
	}

	// =========================================================================

	public static String print(WtNode node)
	{
		return WtAstPrinter.print(new StringWriter(), node).toString();
	}

	public static Writer print(Writer writer, WtNode node)
	{
		new WtAstPrinter(writer).go(node);
		return writer;
	}

	// =========================================================================

	public WtAstPrinter(Writer writer)
	{
		super(writer);
	}
}
