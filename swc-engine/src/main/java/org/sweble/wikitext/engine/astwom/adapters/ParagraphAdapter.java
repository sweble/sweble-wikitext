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
package org.sweble.wikitext.engine.astwom.adapters;

import static org.sweble.wikitext.engine.astwom.adapters.FullElementContentType.MIXED_INLINE;

import java.util.ListIterator;

import lombok.AccessLevel;
import lombok.Delegate;
import lombok.Getter;
import lombok.Setter;

import org.sweble.wikitext.engine.astwom.AstToWomNodeFactory;
import org.sweble.wikitext.engine.astwom.AttributeDescriptor;
import org.sweble.wikitext.engine.astwom.AttributeManagerBase;
import org.sweble.wikitext.engine.astwom.AttributeVerifiers;
import org.sweble.wikitext.engine.astwom.ChildManagerBase;
import org.sweble.wikitext.engine.astwom.GenericAttributeDescriptor;
import org.sweble.wikitext.engine.astwom.NativeOrXmlElement;
import org.sweble.wikitext.engine.astwom.Toolbox;
import org.sweble.wikitext.engine.astwom.UniversalAttributes;
import org.sweble.wikitext.engine.wom.WomHorizAlign;
import org.sweble.wikitext.engine.wom.WomNode;
import org.sweble.wikitext.engine.wom.WomParagraph;
import org.sweble.wikitext.engine.wom.WomUniversalAttributes;
import org.sweble.wikitext.parser.AstNodeTypes;
import org.sweble.wikitext.parser.nodes.WtNewline;
import org.sweble.wikitext.parser.nodes.WtParagraph;
import org.sweble.wikitext.parser.nodes.WtNode;
import org.sweble.wikitext.parser.nodes.WtNodeList;
import org.sweble.wikitext.parser.nodes.WtText;
import org.sweble.wikitext.parser.nodes.WtXmlElement;

import de.fau.cs.osr.utils.StringUtils;
import de.fau.cs.osr.utils.Utils;

public class ParagraphAdapter
		extends
			NativeOrXmlElement
		implements
			WomParagraph
{
	private static final int MAX_GAP = 65535;
	
	private static final long serialVersionUID = 1L;
	
	@Getter(AccessLevel.PROTECTED)
	@Setter(AccessLevel.PROTECTED)
	@Delegate(types = { WomUniversalAttributes.class, AttribAccessors.class })
	private AttributeManagerBase attribManager = AttributeManagerBase.emptyManager();
	
	@Getter(AccessLevel.PROTECTED)
	@Setter(AccessLevel.PROTECTED)
	private ChildManagerBase childManager = ChildManagerBase.emptyManager();
	
	// =========================================================================
	
	public ParagraphAdapter()
	{
		super(new WtParagraph());
	}
	
	public ParagraphAdapter(AstToWomNodeFactory factory, WtParagraph astNode)
	{
		super(astNode);
		// Done by gapsFromAst!
		//addContent(MIXED_INLINE, factory, astNode.getContent());
		gapsFromAst(factory);
	}
	
	public ParagraphAdapter(AstToWomNodeFactory factory, WtXmlElement astNode)
	{
		super("p", astNode);
		addContent(MIXED_INLINE, factory, astNode.getBody());
		addAttributes(astNode.getXmlAttributes());
		
		// FIXME: How about gaps and <p> elements?
	}
	
	// =========================================================================
	
	@Override
	public String getNodeName()
	{
		return "p";
	}
	
	// =========================================================================
	
	@Override
	public int getTopGap()
	{
		return getIntAttribute("topgap");
	}
	
	@Override
	public int setTopGap(int lines)
	{
		NativeOrXmlAttributeAdapter old = setAttribute(
				Attributes.topgap,
				"topgap",
				lines);
		
		return (old == null) ? null : old.getIntValue(0);
	}
	
	@Override
	public int getBottomGap()
	{
		return getIntAttribute("bottomgap");
	}
	
	@Override
	public int setBottomGap(int lines)
	{
		NativeOrXmlAttributeAdapter old = setAttribute(
				Attributes.bottomgap,
				"bottomgap",
				lines);
		
		return (old == null) ? null : old.getIntValue(0);
	}
	
	// =========================================================================
	
	protected WtXmlElement convertToXmlElement()
	{
		return Toolbox.addRtData(new WtXmlElement(
				"p",
				false,
				new WtNodeList(),
				getAstChildContainer()));
	}
	
	@Override
	public WtNodeList getAstChildContainer()
	{
		return isXml() ? xml().getBody() : ((WtParagraph) getAstNode()).getContent();
	}
	
	// =========================================================================
	
	private void gapsFromAst(AstToWomNodeFactory womNodeFactory)
	{
		WtNode first = null;
		WtNode last = null;
		
		WtNodeList container = getAstChildContainer();
		ListIterator<WtNode> i;
		
		int topGap = 0;
		i = container.listIterator();
		outer: while (i.hasNext())
		{
			WtNode n = i.next();
			switch (n.getNodeType())
			{
				case WtNode.NT_TEXT:
					if (StringUtils.isWhitespace(((WtText) n).getContent()))
						break;
					break outer;
				case AstNodeTypes.NT_NEWLINE:
					++topGap;
					break;
				default:
					first = n;
					break outer;
			}
		}
		
		setAttribute(Attributes.GAP_UNCHECKED, "topgap", topGap);
		
		int bottomGap = 0;
		i = container.listIterator(container.size());
		outer: while (i.hasPrevious())
		{
			WtNode n = i.previous();
			switch (n.getNodeType())
			{
				case WtNode.NT_TEXT:
					if (StringUtils.isWhitespace(((WtText) n).getContent()))
						break;
					break outer;
				case AstNodeTypes.NT_NEWLINE:
					++bottomGap;
					break;
				default:
					i.next();
					if (i.hasNext())
						last = i.next();
					break outer;
			}
		}
		
		setAttribute(Attributes.GAP_UNCHECKED, "bottomgap", bottomGap);
		
		if (bottomGap == 2)
		{
			addContent(MIXED_INLINE, womNodeFactory, container, first, last);
		}
		else
		{
			addContent(MIXED_INLINE, womNodeFactory, container, first, last);
		}
		
	}
	
	private void setTopGapInAst(int lines)
	{
		WtNodeList container = getAstChildContainer();
		ListIterator<WtNode> i = container.listIterator();
		
		int j = 0;
		outer: while (i.hasNext())
		{
			WtNode n = i.next();
			switch (n.getNodeType())
			{
				case WtNode.NT_TEXT:
					if (StringUtils.isWhitespace(((WtText) n).getContent()))
						break;
					break outer;
				case AstNodeTypes.NT_NEWLINE:
					++j;
					if (j >= lines)
						break outer;
					break;
				default:
					break outer;
			}
		}
		
		if (j < lines)
		{
			// add more newlines
			// i points to the first non-newline (or to end-of-list)
			while (j < lines)
			{
				i.add(new WtNewline("\n"));
				++j;
			}
		}
		else
		{
			// remove all remaining newlines (if any)
			// i points to last newline
			outer: while (i.hasNext())
			{
				WtNode n = i.next();
				switch (n.getNodeType())
				{
					case WtNode.NT_TEXT:
						if (StringUtils.isWhitespace(((WtText) n).getContent()))
							i.remove();
						break outer;
					case AstNodeTypes.NT_NEWLINE:
						i.remove();
						break;
					default:
						break outer;
				}
			}
		}
	}
	
	private void setBottomGapInAst(int lines)
	{
		WtNodeList container = getAstChildContainer();
		ListIterator<WtNode> i = container.listIterator(container.size());
		
		// We need one more newline at the end if we want "lines"
		// empty lines in the document.
		++lines;
		
		int j = 0;
		outer: while (i.hasPrevious())
		{
			WtNode n = i.previous();
			switch (n.getNodeType())
			{
				case WtNode.NT_TEXT:
					if (StringUtils.isWhitespace(((WtText) n).getContent()))
						break;
					break outer;
				case AstNodeTypes.NT_NEWLINE:
					++j;
					if (j >= lines)
						break outer;
					break;
				default:
					break outer;
			}
		}
		
		if (j < lines)
		{
			// add more newlines
			// i points to the last non-newline (or to end-of-list)
			while (j < lines)
			{
				i.add(new WtNewline("\n"));
				++j;
			}
		}
		else
		{
			// remove all remaining newlines (if any)
			// i points to last newline
			outer: while (i.hasPrevious())
			{
				WtNode n = i.previous();
				switch (n.getNodeType())
				{
					case WtNode.NT_TEXT:
						if (StringUtils.isWhitespace(((WtText) n).getContent()))
							i.remove();
						break outer;
					case AstNodeTypes.NT_NEWLINE:
						i.remove();
						break;
					default:
						break outer;
				}
			}
		}
	}
	
	// =========================================================================
	
	@Override
	protected AttributeDescriptor getAttributeDescriptor(String name)
	{
		AttributeDescriptor d = Utils.fromString(Attributes.class, name);
		if (d != null && d != Attributes.GAP_UNCHECKED)
			return d;
		d = Utils.fromString(UniversalAttributes.class, name);
		if (d != null)
			return d;
		return GenericAttributeDescriptor.get();
	}
	
	private enum Attributes implements AttributeDescriptor
	{
		topgap
		{
			@Override
			public String verify(WomNode parent, String value) throws IllegalArgumentException
			{
				return verifyGap(value);
			}
			
			@Override
			public void customAction(
					WomNode parent,
					NativeOrXmlAttributeAdapter oldAttr,
					NativeOrXmlAttributeAdapter newAttr)
			{
				ParagraphAdapter p = (ParagraphAdapter) parent;
				int lines = (newAttr != null) ? Integer.parseInt(newAttr.getValue()) : 0;
				p.setTopGapInAst(lines);
			}
		},
		
		bottomgap
		{
			@Override
			public String verify(WomNode parent, String value) throws IllegalArgumentException
			{
				return verifyGap(value);
			}
			
			@Override
			public void customAction(
					WomNode parent,
					NativeOrXmlAttributeAdapter oldAttr,
					NativeOrXmlAttributeAdapter newAttr)
			{
				ParagraphAdapter p = (ParagraphAdapter) parent;
				int lines = (newAttr != null) ? Integer.parseInt(newAttr.getValue()) : 0;
				p.setBottomGapInAst(lines);
			}
		},
		
		/**
		 * This descriptor also describes the "topgap" and "bottomgap"
		 * attributes, however, it doesn't verify the attribute's value and
		 * doesn't perform any custom actions (as opposed to
		 * setAttributeUnchecked() which only skips the verification).
		 */
		GAP_UNCHECKED
		{
			@Override
			public String verify(WomNode parent, String value) throws IllegalArgumentException
			{
				return value;
			}
		},
		
		align
		{
			@Override
			public String verify(WomNode parent, String value) throws IllegalArgumentException
			{
				return AttributeVerifiers.LCRJ_ALIGN.verify(parent, value);
			}
			
			@Override
			public boolean syncToAst()
			{
				return true;
			}
		};
		
		@Override
		public void customAction(
				WomNode parent,
				NativeOrXmlAttributeAdapter oldAttr,
				NativeOrXmlAttributeAdapter newAttr)
		{
		}
		
		@Override
		public boolean syncToAst()
		{
			return false;
		}
		
		@Override
		public Normalization getNormalizationMode()
		{
			return Normalization.NON_CDATA;
		}
		
		@Override
		public boolean isRemovable()
		{
			return true;
		}
		
		private static String verifyGap(String value)
		{
			int gap = AttributeVerifiers.verifyRange(value, 0, MAX_GAP);
			return (gap == 0) ? null : value;
		}
	}
	
	private interface AttribAccessors
	{
		public WomHorizAlign getAlign();
		
		public WomHorizAlign setAlign(WomHorizAlign align);
	}
}
