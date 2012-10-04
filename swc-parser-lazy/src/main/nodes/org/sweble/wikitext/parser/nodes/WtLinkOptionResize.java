package org.sweble.wikitext.parser.nodes;

import de.fau.cs.osr.ptk.common.ast.AstNodePropertyIterator;

/**
 * <h1>Link Option: Resize</h1> <h2>Grammar</h2>
 * <ul>
 * <li>
 * <p>
 * WtLinkOptionResize ::= Ws* Digit+ Space* 'px' Ws*
 * </p>
 * </li>
 * <li>
 * <p>
 * WtLinkOptionResize ::= Ws* Digit+ 'x' Digit* Space* 'px' Ws*
 * </p>
 * </li>
 * <li>
 * <p>
 * WtLinkOptionResize ::= Ws* 'x' Digit+ Space* 'px' Ws*
 * </p>
 * </li>
 * </ul>
 * <ul>
 * <li>
 * <p>
 * If heightString is null the first production matched.
 * </p>
 * </li>
 * <li>
 * <p>
 * If widthString is null the third production matched.
 * </p>
 * </li>
 * <li>
 * <p>
 * If both are null there is no information which production matched.
 * </p>
 * </li>
 * <li>
 * <p>
 * Otherwise the second production matched.
 * </p>
 * </li>
 * <li>
 * <p>
 * If one of the dimensions is missing the value of the width/height is set to
 * -1.
 * </p>
 * </li>
 * </ul>
 */
public class WtLinkOptionResize
		extends
			WtLeafNode
{
	private static final long serialVersionUID = 1L;
	
	// =========================================================================
	
	public WtLinkOptionResize()
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
		return org.sweble.wikitext.parser.AstNodeTypes.NT_LINK_OPTION_RESIZE;
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
