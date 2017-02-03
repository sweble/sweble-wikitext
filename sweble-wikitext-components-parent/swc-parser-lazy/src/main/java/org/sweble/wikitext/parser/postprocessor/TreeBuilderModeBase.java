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

package org.sweble.wikitext.parser.postprocessor;

import org.sweble.wikitext.parser.NonStandardElementBehavior;
import org.sweble.wikitext.parser.WtRtData;
import org.sweble.wikitext.parser.nodes.WtNamedXmlElement;
import org.sweble.wikitext.parser.nodes.WtNode;

import de.fau.cs.osr.ptk.common.AstVisitor;
import de.fau.cs.osr.ptk.common.ast.RtData;
import de.fau.cs.osr.utils.visitor.VisitorLogic;

public class TreeBuilderModeBase
		extends
			AstVisitor<WtNode>
{
	protected final TreeBuilder tb;

	// =====================================================================

	public TreeBuilderModeBase(
			VisitorLogic<WtNode> logic,
			TreeBuilder treeBuilder)
	{
		super(logic);
		this.tb = treeBuilder;
	}

	// =====================================================================

	@Override
	public Object dispatch(WtNode node)
	{
		if (TreeBuilder.DEBUG)
		{
			String nodeName = node.getNodeName();
			if (node instanceof WtNamedXmlElement)
				nodeName = String.format(
						"%s <%s>",
						nodeName,
						((WtNamedXmlElement) node).getName());

			tb.dbgIn("~~> %s (%08X)", nodeName, System.identityHashCode(node));
		}

		Object result = super.dispatch(node);

		if (TreeBuilder.DEBUG)
			tb.dbgOut("<~~ %s", node.getNodeName());

		return result;
	}

	// =====================================================================

	protected ElementFactory getFactory()
	{
		return tb.getFactory();
	}

	protected NonStandardElementBehavior getNonStandardElementBehavior(
			String elementName)
	{
		return tb.getConfig().getNonStandardElementBehavior(elementName);
	}

	protected static boolean isNodeOneOf(WtNode node, ElementType... types)
	{
		return TreeBuilder.isNodeTypeOneOf(node, types);
	}

	protected static ElementType getNodeType(WtNode node)
	{
		return TreeBuilder.getNodeType(node);
	}

	protected static boolean isTypeOneOf(
			ElementType nodeType,
			ElementType... types)
	{
		return TreeBuilder.isTypeOneOf(nodeType, types);
	}

	protected static void addRtDataOfEndTag(WtNode finish, WtNode endTag)
	{
		WtRtData etRtd = endTag.getRtd();
		if (etRtd == null)
			return;

		if (WtNodeFlags.isRepairNode(endTag))
			// Synthetic tags should not show up in RTD information.
			return;

		if (endTag.getNodeType() == WtNode.NT_IM_END_TAG)
		{
			// Special treatment for intermediate tags
			addRtDataOfImEndTag(finish, etRtd);
			return;
		}

		WtRtData feRtd = finish.getRtd();
		if (feRtd == null)
		{
			// The element we have to finish might not have RTD data associated.
			finish.setRtd((Object) null);
			feRtd = finish.getRtd();
		}

		int size = feRtd.size();
		if (size >= 2)
			feRtd.setField(size - 1, etRtd.getField(0));
	}

	protected static void addRtDataOfImEndTag(WtNode finish, WtRtData etRtd)
	{
		switch (finish.getNodeType())
		{
			case WtNode.NT_BOLD:
			case WtNode.NT_ITALICS:
			{
				RtData feRtd = finish.getRtd();
				if (feRtd == null)
				{
					finish.setRtd(RtData.SEP, etRtd.getField(0));
				}
				else
				{
					feRtd.setField(1, etRtd.getField(0));
				}
				break;
			}

			default:
				// Finish is assumed to be WtBold or WtItalics
				throw new AssertionError();
		}
	}
}
