package org.sweble.wikitext.engine.astwom;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.sweble.wikitext.engine.wom.WomNode;
import org.sweble.wikitext.engine.wom.WomNodeType;

import de.fau.cs.osr.ptk.common.ast.AstNode;
import de.fau.cs.osr.ptk.common.ast.NodeList;

public class AttributeManagerTest
{
	private final WomBackboneStub parent = new WomBackboneStub(null);;
	
	private final NodeList attrContainer = new NodeList();
	
	private AttributeManager manager = new AttributeManager.AttributeManagerThingy();
	
	@Before
	public void initialize()
	{
		manager = new AttributeManager.AttributeManagerThingy();
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
		
		private WomBackboneStub(AstNode astNode)
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
		public void customAction(WomNode parent, String value)
		{
		}
	}
}
