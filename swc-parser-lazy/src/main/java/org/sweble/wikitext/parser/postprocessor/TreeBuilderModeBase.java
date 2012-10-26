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

import org.sweble.wikitext.parser.nodes.WtIntermediate;
import org.sweble.wikitext.parser.nodes.WtNamedXmlElement;
import org.sweble.wikitext.parser.nodes.WtNode;
import org.sweble.wikitext.parser.nodes.WtXmlElement;

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
	
	protected ElementFactory getFactory()
	{
		return tb.getFactory();
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
		if (finish.getRtd() == null)
		{
			return;
		}
		else
		{
			if (endTag.getNodeType() == WtNode.NT_IM_END_TAG)
			{
				addRtDataOfEndTag(finish, (WtIntermediate) endTag);
				return;
			}
			else if (endTag.getBooleanAttribute("synthetic"))
			{
				return;
			}
		}
		
		// Could happen: * .. </li> would be one such case.
		if (!(finish instanceof WtXmlElement))
			return;
		
		WtXmlElement fe = (WtXmlElement) finish;
		// Could also be an empty tag:
		WtNamedXmlElement et = (WtNamedXmlElement) endTag;
		
		RtData feRtd = fe.getRtd();
		RtData etRtd = et.getRtd();
		if (etRtd != null)
		{
			feRtd.setField(2, etRtd.getField(0));
		}
		else
		{
			feRtd.setField(2, "</", et.getName(), ">");
		}
	}
	
	protected static void addRtDataOfEndTag(WtNode finish, WtIntermediate endTag)
	{
		if (endTag.getRtd() == null)
			return;
		
		// finish is assumed to be WtBold or WtItalics
		finish.getRtd().setField(1, endTag.getRtd().getField(0));
	}
}
