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

package org.sweble.wikitext.engine.astwom;

import static org.junit.Assert.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import org.junit.Test;
import org.sweble.wikitext.parser.nodes.WtXmlAttribute;
import org.sweble.wikitext.parser.nodes.WtXmlElement;

import de.fau.cs.osr.utils.Utils;

public class AttributeSupportingElementTest
{
	@Test
	public void supportedAstAttributeIsRetrievableThroughWomInterface() throws Exception
	{
		WtXmlAttribute a = astXmlAttrib()
				.withName("align")
				.withValue("left")
				.build();
		
		ElementStub e = new ElementStubWithoutGenericAttributes(
				astE().withAttribs(a).build());
		
		assertEquals("left", e.getAttribute("align"));
	}
	
	@Test
	public void unsupportedAstAttributeIsHiddenInWomInterface() throws Exception
	{
		WtXmlAttribute a = astXmlAttrib()
				.withName("unsupported")
				.build();
		
		ElementStub e = new ElementStubWithoutGenericAttributes(
				astE().withAttribs(a).build());
		
		assertNull(e.getAttribute("align"));
	}
	
	@Test
	public void astAttributeWithUnsupportedValueIsHiddenInWomInterface() throws Exception
	{
		WtXmlAttribute a = astXmlAttrib()
				.withName("align")
				.withValue("invalid")
				.build();
		
		ElementStub e = new ElementStubForGenericAttributes(
				astE().withAttribs(a).build());
		
		assertNull(e.getAttribute("align"));
	}
	
	@Test
	public void onlyLastValidAstAttributeIsAccessibleThroughWomInterface() throws Exception
	{
		WtXmlAttribute a0 = astXmlAttrib()
				.withName("align")
				.withValue("right")
				.build();
		
		WtXmlAttribute a1 = astXmlAttrib()
				.withName("align")
				.withValue("left")
				.build();
		
		ElementStub e = new ElementStubWithoutGenericAttributes(
				astE().withAttribs(a0, a1).build());
		
		assertEquals("left", e.getAttribute("align"));
	}
	
	// =========================================================================
	
	public static abstract class ElementStub
			extends
				AttributeSupportingElement
	{
		private static final long serialVersionUID = 1L;
		
		@Getter(AccessLevel.PROTECTED)
		@Setter(AccessLevel.PROTECTED)
		private AttributeManagerBase attribManager = AttributeManagerBase.emptyManager();
		
		public ElementStub(WtXmlElement astNode)
		{
			super(astNode);
			addAttributes(astNode.getXmlAttributes());
		}
		
		@Override
		public String getNodeName()
		{
			return null;
		}
		
		@Override
		public WomNodeType getNodeType()
		{
			return null;
		}
	}
	
	public static final class ElementStubForGenericAttributes
			extends
				ElementStub
	{
		private static final long serialVersionUID = 1L;
		
		public ElementStubForGenericAttributes(WtXmlElement astNode)
		{
			super(astNode);
		}
		
		@Override
		protected AttributeDescriptor getAttributeDescriptor(String name)
		{
			AttributeDescriptor d = Utils.fromString(AttributesStub.class, name);
			if (d != null)
				return d;
			return GenericAttributeDescriptor.get();
		}
	}
	
	public static final class ElementStubWithoutGenericAttributes
			extends
				ElementStub
	{
		private static final long serialVersionUID = 1L;
		
		public ElementStubWithoutGenericAttributes(WtXmlElement astNode)
		{
			super(astNode);
		}
		
		@Override
		protected AttributeDescriptor getAttributeDescriptor(String name)
		{
			return Utils.fromString(AttributesStub.class, name);
		}
	}
	
	private static enum AttributesStub implements AttributeDescriptor
	{
		align
		{
			@Override
			public String verify(WomNode parent, String value) throws IllegalArgumentException
			{
				for (String valid : new String[] { "left", "right" })
				{
					if (valid.equals(value))
						return value;
				}
				throw new IllegalArgumentException();
			}
			
			@Override
			public boolean isRemovable()
			{
				return false;
			}
			
			@Override
			public boolean syncToAst()
			{
				return true;
			}
			
			@Override
			public Normalization getNormalizationMode()
			{
				return Normalization.NONE;
			}
			
			@Override
			public void customAction(
					WomNode parent,
					NativeOrXmlAttributeAdapter oldAttr,
					NativeOrXmlAttributeAdapter newAttr)
			{
			}
		}
	}
}
