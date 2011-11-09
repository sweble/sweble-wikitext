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
package org.sweble.wikitext.engine.astdom.adapters;

import lombok.AccessLevel;
import lombok.Delegate;
import lombok.Getter;
import lombok.Setter;

import org.sweble.wikitext.engine.astdom.AttributeManager;
import org.sweble.wikitext.engine.astdom.ChildManager;
import org.sweble.wikitext.engine.astdom.NativeOrXmlElement;
import org.sweble.wikitext.engine.astdom.Toolbox;
import org.sweble.wikitext.engine.astdom.WomNodeFactory;
import org.sweble.wikitext.engine.wom.WomHorizAlign;
import org.sweble.wikitext.engine.wom.WomParagraph;
import org.sweble.wikitext.engine.wom.WomUniversalAttributes;
import org.sweble.wikitext.lazy.parser.Paragraph;
import org.sweble.wikitext.lazy.parser.XmlElement;

import de.fau.cs.osr.ptk.common.ast.NodeList;

public class ParagraphAdapter
        extends
            NativeOrXmlElement
        implements
            WomParagraph
{
	private static final long serialVersionUID = 1L;
	
	@Getter(AccessLevel.PROTECTED)
	@Setter(AccessLevel.PROTECTED)
	@Delegate(types = { WomUniversalAttributes.class, AttribAccessors.class })
	private AttributeManager attribManager = AttributeManager.emptyManager();
	
	@Getter(AccessLevel.PROTECTED)
	@Setter(AccessLevel.PROTECTED)
	private ChildManager childManager = ChildManager.emptyManager();
	
	// =========================================================================
	
	public ParagraphAdapter()
	{
		super(new Paragraph());
	}
	
	public ParagraphAdapter(WomNodeFactory womNodeFactory, Paragraph astNode)
	{
		super(astNode);
		addContent(womNodeFactory, astNode.getContent());
	}
	
	public ParagraphAdapter(WomNodeFactory womNodeFactory, XmlElement astNode)
	{
		super("p", astNode);
		addContent(womNodeFactory, astNode.getBody());
	}
	
	// =========================================================================
	
	@Override
	public String getNodeName()
	{
		return "p";
	}
	
	// =========================================================================
	
	protected XmlElement convertToXmlElement()
	{
		return Toolbox.addXmlRtData(new XmlElement(
		        "p",
		        false,
		        new NodeList(),
		        getAstChildContainer()));
	}
	
	@Override
	public NodeList getAstChildContainer()
	{
		return isXml() ? xml().getBody() : ((Paragraph) getAstNode()).getContent();
	}
	
	// =========================================================================
	
	private interface AttribAccessors
	{
		public WomHorizAlign getAlign();
		
		public WomHorizAlign setAlign(WomHorizAlign align);
	}
}
