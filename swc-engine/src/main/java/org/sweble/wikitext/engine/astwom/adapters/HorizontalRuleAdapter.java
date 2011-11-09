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

import lombok.AccessLevel;
import lombok.Delegate;
import lombok.Getter;
import lombok.Setter;

import org.sweble.wikitext.engine.astwom.AttributeManager;
import org.sweble.wikitext.engine.astwom.ChildManager;
import org.sweble.wikitext.engine.astwom.NativeOrXmlElement;
import org.sweble.wikitext.engine.astwom.WomNodeFactory;
import org.sweble.wikitext.engine.wom.WomHorizAlign;
import org.sweble.wikitext.engine.wom.WomHorizontalRule;
import org.sweble.wikitext.engine.wom.WomUniversalAttributes;
import org.sweble.wikitext.engine.wom.WomValueWithUnit;
import org.sweble.wikitext.lazy.parser.HorizontalRule;
import org.sweble.wikitext.lazy.parser.XmlElement;

import de.fau.cs.osr.ptk.common.ast.NodeList;

public class HorizontalRuleAdapter
        extends
            NativeOrXmlElement
        implements
            WomHorizontalRule
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
	
	public HorizontalRuleAdapter()
	{
		super(new HorizontalRule());
	}
	
	public HorizontalRuleAdapter(HorizontalRule astNode)
	{
		super(astNode);
	}
	
	public HorizontalRuleAdapter(WomNodeFactory womNodeFactory, XmlElement astNode)
	{
		super("hr", astNode);
	}
	
	// =========================================================================
	
	@Override
	public String getNodeName()
	{
		return "hr";
	}
	
	// =========================================================================
	
	protected XmlElement convertToXmlElement()
	{
		return new XmlElement(
		        "hr",
		        true,
		        new NodeList(),
		        new NodeList());
	}
	
	// =========================================================================
	
	private interface AttribAccessors
	{
		public WomHorizAlign getAlign();
		
		public WomHorizAlign setAlign(WomHorizAlign align);
		
		public boolean isNoshade();
		
		public boolean setNoshade(boolean noshade);
		
		public Integer getSize();
		
		public Integer setSize(Integer size);
		
		public WomValueWithUnit getWidth();
		
		public WomValueWithUnit setWidth(WomValueWithUnit width);
	}
}
