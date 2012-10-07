package org.sweble.wikitext.parser.nodes;

import de.fau.cs.osr.ptk.common.ast.AstNodePropertyIterator;

public class WtLinkOptionResize
		extends
			WtLeafNode
{
	private static final long serialVersionUID = 1L;
	
	// =========================================================================
	
	/**
	 * Only for use by de-serialization code.
	 */
	protected WtLinkOptionResize()
	{
	}
	
	public WtLinkOptionResize(int width, int height)
	{
		setWidth(width);
		setHeight(height);
	}
	
	@Override
	public int getNodeType()
	{
		return NT_LINK_OPTION_RESIZE;
	}
	
	// =========================================================================
	// Properties
	
	private int width;
	
	public final int getWidth()
	{
		return this.width;
	}
	
	public final int setWidth(int width)
	{
		int old = this.width;
		this.width = width;
		return old;
	}
	
	private int height;
	
	public final int getHeight()
	{
		return this.height;
	}
	
	public final int setHeight(int height)
	{
		int old = this.height;
		this.height = height;
		return old;
	}
	
	@Override
	public final int getPropertyCount()
	{
		return 2 + getSuperPropertyCount();
	}
	
	private final int getSuperPropertyCount()
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
				return WtLinkOptionResize.this.getPropertyCount();
			}
			
			@Override
			protected String getName(int index)
			{
				switch (index - getSuperPropertyCount())
				{
					case 0:
						return "width";
					case 1:
						return "height";
						
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
						return WtLinkOptionResize.this.getWidth();
					case 1:
						return WtLinkOptionResize.this.getHeight();
						
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
						return WtLinkOptionResize.this.setWidth((Integer) value);
					case 1:
						return WtLinkOptionResize.this.setHeight((Integer) value);
						
					default:
						return super.setValue(index, value);
				}
			}
		};
	}
}
