package org.sweble.wikitext.parser.nodes;

import de.fau.cs.osr.ptk.common.ast.AstNodePropertyIterator;

public class WtTicks
		extends
			WtLeafNode
		implements
			WtIntermediate
{
	private static final long serialVersionUID = 1L;
	
	// =========================================================================
	
	/**
	 * Only for use by de-serialization code.
	 */
	protected WtTicks()
	{
	}
	
	public WtTicks(int tickCount)
	{
		setTickCount(tickCount);
	}
	
	@Override
	public int getNodeType()
	{
		return NT_TICKS;
	}
	
	// =========================================================================
	
	@Override
	public boolean isSynthetic()
	{
		return false;
	}
	
	// =========================================================================
	// Properties
	
	private int tickCount;
	
	public final int getTickCount()
	{
		return this.tickCount;
	}
	
	public final int setTickCount(int tickCount)
	{
		int old = this.tickCount;
		this.tickCount = tickCount;
		return old;
	}
	
	@Override
	public final int getPropertyCount()
	{
		return 1 + getSuperPropertyCount();
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
				return WtTicks.this.getPropertyCount();
			}
			
			@Override
			protected String getName(int index)
			{
				switch (index - getSuperPropertyCount())
				{
					case 0:
						return "tickCount";
						
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
						return WtTicks.this.getTickCount();
						
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
						return WtTicks.this.setTickCount((Integer) value);
						
					default:
						return super.setValue(index, value);
				}
			}
		};
	}
}
