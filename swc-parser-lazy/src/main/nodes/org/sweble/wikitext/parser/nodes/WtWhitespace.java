package org.sweble.wikitext.parser.nodes;

import de.fau.cs.osr.ptk.common.ast.AstNodePropertyIterator;

/**
 * <h1>WtWhitespace</h1>
 */
public class WtWhitespace
		extends
			WtContentNodeMarkTwo
{
	private static final long serialVersionUID = 1L;
	
	// =========================================================================
	
	public WtWhitespace(WtNodeList content, boolean hasNewline)
	{
		super(content);
		setHasNewline(hasNewline);
	}
	
	@Override
	public int getNodeType()
	{
		return org.sweble.wikitext.parser.AstNodeTypes.NT_WHITESPACE;
	}
	
	// =========================================================================
	// Properties
	
	private boolean hasNewline;
	
	public final boolean getHasNewline()
	{
		return this.hasNewline;
	}
	
	public final boolean setHasNewline(boolean hasNewline)
	{
		boolean old = this.hasNewline;
		this.hasNewline = hasNewline;
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
		return new WtContentNodeMarkTwoPropertyIterator()
		{
			@Override
			protected int getPropertyCount()
			{
				return WtWhitespace.this.getPropertyCount();
			}
			
			@Override
			protected String getName(int index)
			{
				switch (index - getSuperPropertyCount())
				{
					case 0:
						return "hasNewline";
						
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
						return WtWhitespace.this.getHasNewline();
						
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
						return WtWhitespace.this.setHasNewline((Boolean) value);
						
					default:
						return super.setValue(index, value);
				}
			}
		};
	}
}
