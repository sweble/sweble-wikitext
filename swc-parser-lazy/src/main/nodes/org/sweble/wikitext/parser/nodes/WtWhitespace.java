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
	
	/*
	public WtWhitespace()
	{
		super();
	}
	*/
	
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
		return 1;
	}
	
	@Override
	public final AstNodePropertyIterator propertyIterator()
	{
		return new AstNodePropertyIterator()
		{
			@Override
			protected int getPropertyCount()
			{
				return 1;
			}
			
			@Override
			protected String getName(int index)
			{
				switch (index)
				{
					case 0:
						return "hasNewline";
						
					default:
						throw new IndexOutOfBoundsException();
				}
			}
			
			@Override
			protected Object getValue(int index)
			{
				switch (index)
				{
					case 0:
						return WtWhitespace.this.getHasNewline();
						
					default:
						throw new IndexOutOfBoundsException();
				}
			}
			
			@Override
			protected Object setValue(int index, Object value)
			{
				switch (index)
				{
					case 0:
						return WtWhitespace.this.setHasNewline((Boolean) value);
						
					default:
						throw new IndexOutOfBoundsException();
				}
			}
		};
	}
}
