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
package org.sweble.wikitext.engine;

import org.sweble.wikitext.engine.nodes.EngNode;
import org.sweble.wikitext.parser.nodes.WtNode;
import org.sweble.wikitext.parser.nodes.WtNodeList;
import org.sweble.wikitext.parser.nodes.WtParserEntity;
import org.sweble.wikitext.parser.nodes.WtText;

import de.fau.cs.osr.ptk.common.NodeTypeAstVisitor;
import de.fau.cs.osr.ptk.common.ast.AstNode;
import de.fau.cs.osr.ptk.common.ast.AstNodeList;
import de.fau.cs.osr.ptk.common.ast.AstParserEntity;
import de.fau.cs.osr.ptk.common.ast.AstText;

public class NodeTypeEngVisitor
		extends
			NodeTypeAstVisitor<WtNode>
{
	@Override
	protected Object resolveAndVisit(WtNode node, int type)
	{
		switch (type)
		{
			case EngNode.NT_TEXT:
				return visit((WtText) node);

			case EngNode.NT_NODE_LIST:
				return visit((WtNodeList) node);

			case EngNode.NT_PARSER_ENTITY:
				return visit((WtParserEntity) node);

			default:
				return visitUnspecific(node);
		}
	}

	// =========================================================================

	protected Object visitUnspecific(WtNode node)
	{
		return node;
	}

	protected Object visit(WtText node)
	{
		return visitUnspecific(node);
	}

	protected Object visit(WtNodeList node)
	{
		return visitUnspecific(node);
	}

	protected Object visit(WtParserEntity node)
	{
		return visitUnspecific(node);
	}

	// =========================================================================
	// Make the original methods unusable

	@Override
	protected final Object visitUnspecific(AstNode<WtNode> node)
	{
		throw new AssertionError();
	}

	@Override
	protected final Object visit(AstText<WtNode> node)
	{
		throw new AssertionError();
	}

	@Override
	protected final Object visit(AstNodeList<WtNode> node)
	{
		throw new AssertionError();
	}

	@Override
	protected final Object visit(AstParserEntity<WtNode> node)
	{
		throw new AssertionError();
	}
}
