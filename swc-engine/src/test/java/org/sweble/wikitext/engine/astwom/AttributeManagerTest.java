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

import org.junit.Before;
import org.junit.Test;
import org.sweble.wikitext.parser.nodes.WtNode;
import org.sweble.wikitext.parser.nodes.WtNodeList;

public class AttributeManagerTest
{
	private final WomBackboneStub parent = new WomBackboneStub(null);;
	
	private final WtNodeList attrContainer = new WtNodeList();
	
	private AttributeManagerBase manager = new AttributeManagerBase.AttributeManager();
	
	@Before
	public void initialize()
	{
		manager = new AttributeManagerBase.AttributeManager();
	}
	
	@Test
	public void getAttributeIsCaseSensitive()
	{
		manager.setAttribute(
				AttributesStub.test,
				"test",
				"value",
				parent,
				attrContainer);
		
		assertNull(manager.getAttribute("TEST"));
	}
	
	// =========================================================================
	
	private static final class WomBackboneStub
			extends
				WomBackbone
	{
		private static final long serialVersionUID = 1L;
		
		private WomBackboneStub(WtNode astNode)
		{
			super(astNode);
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
	
	private static enum AttributesStub implements AttributeDescriptor
	{
		// lower case on purpose!
		test;
		
		@Override
		public String verify(WomNode parent, String value) throws IllegalArgumentException
		{
			return null;
		}
		
		@Override
		public boolean isRemovable()
		{
			return false;
		}
		
		@Override
		public boolean syncToAst()
		{
			return false;
		}
		
		@Override
		public Normalization getNormalizationMode()
		{
			return null;
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
