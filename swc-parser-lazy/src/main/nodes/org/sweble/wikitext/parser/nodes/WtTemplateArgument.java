package org.sweble.wikitext.parser.nodes;

import org.sweble.wikitext.parser.postprocessor.WtPreproNode;

import de.fau.cs.osr.ptk.common.ast.AstNodePropertyIterator;

/**
 * <h1>WtTemplateArgument</h1> <h2>Grammar</h2>
 */
public class WtTemplateArgument
		extends
			WtInnerNode2
		implements
			WtPreproNode
{
	private static final long serialVersionUID = 1L;
	
	// =========================================================================
	
	public WtTemplateArgument()
	{
		super(new WtNodeList(), new WtNodeList());
	}
	
	public WtTemplateArgument(WtNodeList value, boolean hasName)
	{
		super(new WtNodeList(), value);
		setHasName(hasName);
	}
	
	public WtTemplateArgument(WtNodeList name, WtNodeList value, boolean hasName)
	{
		super(name, value);
		setHasName(hasName);
	}
	
	@Override
	public int getNodeType()
	{
		return NT_TEMPLATE_ARGUMENT;
	}
	
	// =========================================================================
	// Properties
	
	private boolean hasName;
	
	public final boolean getHasName()
	{
		return this.hasName;
	}
	
	public final boolean setHasName(boolean hasName)
	{
		boolean old = this.hasName;
		this.hasName = hasName;
		return old;
	}
	
	@Override
	public final int getPropertyCount()
	{
		return 1 + getSuperPropertyCount();
	}
	
	public int getSuperPropertyCount()
	{
		return super.getPropertyCount();
	}
	
	@Override
	public final AstNodePropertyIterator propertyIterator()
	{
		return new WtInnerNode2PropertyIterator()
		{
			@Override
			protected int getPropertyCount()
			{
				return WtTemplateArgument.this.getPropertyCount();
			}
			
			@Override
			protected String getName(int index)
			{
				switch (index - getSuperPropertyCount())
				{
					case 0:
						return "hasName";
						
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
						return WtTemplateArgument.this.getHasName();
						
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
						return WtTemplateArgument.this.setHasName((Boolean) value);
						
					default:
						return super.setValue(index, value);
				}
			}
		};
	}
	
	// =========================================================================
	// Children
	
	public final void setName(WtNodeList name)
	{
		set(0, name);
	}
	
	public final WtNodeList getName()
	{
		return (WtNodeList) get(0);
	}
	
	public final void setValue(WtNodeList value)
	{
		set(1, value);
	}
	
	public final WtNodeList getValue()
	{
		return (WtNodeList) get(1);
	}
	
	private static final String[] CHILD_NAMES = new String[] { "name", "value" };
	
	public final String[] getChildNames()
	{
		return CHILD_NAMES;
	}
}
