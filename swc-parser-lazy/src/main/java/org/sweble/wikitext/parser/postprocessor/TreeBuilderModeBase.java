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

import org.sweble.wikitext.parser.WtRtData;
import org.sweble.wikitext.parser.nodes.WtImEndTag;
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
		if (endTag.getNodeType() == WtNode.NT_IM_END_TAG)
		{
			// Special treatment for intermediate tags
			addRtDataOfImEndTag(finish, (WtImEndTag) endTag);
			return;
		}
		
		if (endTag.getBooleanAttribute("synthetic"))
		{
			// Synthetic tags should not show up in RTD information.
			return;
		}
		
		// We expect "finish" to be an WtXmlElement. However, that's not always 
		// true. "* .. </li>" would be one such case. The endTag would be a tag
		// but the node we're finishing is a WtListItem.
		
		if (!(finish instanceof WtXmlElement))
			// We're violently fixing stuff here by dropping the illegal
			// </li> markup.
			return;
		
		WtXmlElement fe = (WtXmlElement) finish;
		
		RtData feRtd = fe.getRtd();
		if (feRtd == null)
		{
			// The element we have to finish might not have RTD data associated.F 
			finish.setRtd(RtData.SEP, RtData.SEP);
			feRtd = fe.getRtd();
		}
		
		WtNamedXmlElement et = (WtNamedXmlElement) endTag;
		RtData etRtd = et.getRtd();
		
		if (etRtd == null)
		{
			// End tag has no RTD, so we should not artifically generate it...
			//feRtd.setField(2, "</", et.getName(), ">");
			return;
		}
		
		switch (endTag.getNodeType())
		{
			case WtNode.NT_XML_END_TAG:
				feRtd.setField(2, etRtd.getField(0));
				break;
			
			default:
				throw new InternalError();
		}
	}
	
	protected static void addRtDataOfImEndTag(
			WtNode finish,
			WtImEndTag endTag)
	{
		WtRtData etRtd = endTag.getRtd();
		if (endTag.isSynthetic() || etRtd == null)
			return;
		
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
				throw new InternalError();
		}
	}
}
