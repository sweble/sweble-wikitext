package org.sweble.wikitext.parser.nodes;

import org.sweble.wikitext.parser.postprocessor.WtPreproNode;

import de.fau.cs.osr.ptk.common.ast.AstNodePropertyIterator;

/**
 * <h1>WtPageSwitch</h1> <h2>Grammar</h2>
 * <ul>
 * <li>
 * <p>
 * '__' Name '__'
 * </p>
 * </li>
 * </ul>
 */
public class WtPageSwitch
		extends
			WtLeafNode
		implements
			WtPreproNode
{
	private static final long serialVersionUID = 1L;
	
	// =========================================================================
	
	public WtPageSwitch()
	{
	}
	
	public WtPageSwitch(String name)
	{
		setName(name);
	}
	
	@Override
	public int getNodeType()
	{
		return org.sweble.wikitext.parser.AstNodeTypes.NT_PAGE_SWITCH;
	}
	
	// =========================================================================
	// Properties
	
	private String name;
	
	public final String getName()
	{
		return this.name;
	}
	
	public final String setName(String name)
	{
		String old = this.name;
		this.name = name;
		return old;
	}
	
	@Override
	public final int getPropertyCount()
	{
		return getSuperPropertyCount() + 1;
	}
	
	public final int getSuperPropertyCount()
	{
		return super.getPropertyCount();
	}
	
	@Override
	public final AstNodePropertyIterator propertyIterator()
	{
		return new WtLeafNodePropertyIterator()
		{
			@Override
			protected int getPropertyCount()
			{
				return WtPageSwitch.this.getPropertyCount();
			}
			
			@Override
			protected String getName(int index)
			{
				switch (index - getSuperPropertyCount())
				{
					case 0:
						return "name";
						
					default:
						return super.getName(index);
				}
			}
			
			@Override
			protected Object getValue(int index)
			{
				switch (index - getSuperPropertyCount())
				{
					case 0:
						return WtPageSwitch.this.getName();
						
					default:
						return super.getValue(index);
				}
			}
			
			@Override
			protected Object setValue(int index, Object value)
			{
				switch (index - getSuperPropertyCount())
				{
					case 0:
						return WtPageSwitch.this.setName((String) value);
						
					default:
						return super.setValue(index, value);
				}
			}
		};
	}
}
