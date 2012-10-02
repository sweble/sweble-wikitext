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
import lombok.AccessLevel;
import lombok.Delegate;
import lombok.Getter;
import lombok.Setter;

import org.sweble.wikitext.engine.astwom.AstToWomNodeFactory;
import org.sweble.wikitext.engine.astwom.AttributeDescriptor;
import org.sweble.wikitext.engine.astwom.AttributeManagerBase;
import org.sweble.wikitext.engine.astwom.AttributeVerifiers;
import org.sweble.wikitext.engine.astwom.CoreAttributes;
import org.sweble.wikitext.engine.astwom.GenericAttributeDescriptor;
import org.sweble.wikitext.engine.wom.WomColor;
import org.sweble.wikitext.engine.wom.WomCoreAttributes;
import org.sweble.wikitext.engine.wom.WomFont;
import org.sweble.wikitext.engine.wom.WomI18nAttributes;
import org.sweble.wikitext.engine.wom.WomNode;
import org.sweble.wikitext.parser.nodes.XmlElement;

import de.fau.cs.osr.utils.Utils;

public class FontAdapter
		extends
			XmlElementWithChildren
		implements
			WomFont
{
	private static final long serialVersionUID = 1L;
	
	private static final String TAG_AND_NODE_NAME = "font";
	
	@Getter(AccessLevel.PROTECTED)
	@Setter(AccessLevel.PROTECTED)
	@Delegate(types = { WomCoreAttributes.class, WomI18nAttributes.class, AttribAccessors.class })
	private AttributeManagerBase attribManager = AttributeManagerBase.emptyManager();
	
	// =========================================================================
	
	public FontAdapter()
	{
		super(TAG_AND_NODE_NAME);
	}
	
	public FontAdapter(AstToWomNodeFactory womNodeFactory, XmlElement astNode)
	{
		super(MIXED_INLINE, TAG_AND_NODE_NAME, womNodeFactory, astNode);
	}
	
	// =========================================================================
	
	@Override
	public String getNodeName()
	{
		return TAG_AND_NODE_NAME;
	}
	
	// =========================================================================
	
	@Override
	protected AttributeDescriptor getAttributeDescriptor(String name)
	{
		AttributeDescriptor d = Utils.fromString(Attributes.class, name);
		if (d != null)
			return d;
		d = Utils.fromString(CoreAttributes.class, name);
		if (d != null)
			return d;
		return GenericAttributeDescriptor.get();
	}
	
	private static enum Attributes implements AttributeDescriptor
	{
		color
		{
			@Override
			public String verify(WomNode parent, String value) throws IllegalArgumentException
			{
				return AttributeVerifiers.COLOR.verify(parent, value);
			}
		},
		
		font
		{
			@Override
			public String verify(WomNode parent, String value) throws IllegalArgumentException
			{
				return value;
			}
		},
		
		size
		{
			@Override
			public String verify(WomNode parent, String value) throws IllegalArgumentException
			{
				return AttributeVerifiers.FONT_SIZE.verify(parent, value);
			}
		};
		
		// =====================================================================
		
		@Override
		public boolean isRemovable()
		{
			return true;
		}
		
		@Override
		public boolean syncToAst()
		{
			return true;
		}
		
		@Override
		public Normalization getNormalizationMode()
		{
			return Normalization.NON_CDATA;
		}
		
		@Override
		public void customAction(
				WomNode parent,
				NativeOrXmlAttributeAdapter oldAttr,
				NativeOrXmlAttributeAdapter newAttr)
		{
		}
	}
	
	private interface AttribAccessors
	{
		WomColor getColor();
		
		WomColor setColor(WomColor color);
		
		String getFace();
		
		String setFace(String face);
		
		int getSize();
		
		int setSize(int size);
	}
}
