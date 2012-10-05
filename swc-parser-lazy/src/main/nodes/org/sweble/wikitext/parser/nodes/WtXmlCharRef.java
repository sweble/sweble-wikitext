package org.sweble.wikitext.parser.nodes;

import de.fau.cs.osr.ptk.common.ast.AstNodePropertyIterator;

/**
 * <h1>Xml Character Reference</h1> <h2>Grammar</h2>
 * <ul>
 * <li>
 * <p>
 * '&#' Digit+ ';'
 * </p>
 * </li>
 * <li>
 * <p>
 * '&#x' HexDigit+ ';'
 * </p>
 * </li>
 * </ul>
 */
public class WtXmlCharRef
		extends
			WtLeafNode
{
	private static final long serialVersionUID = 1L;
	
	// =========================================================================
	
	public WtXmlCharRef(int codePoint)
	{
		setCodePoint(codePoint);
	}
	
	@Override
	public int getNodeType()
	{
		return org.sweble.wikitext.parser.AstNodeTypes.NT_XML_CHAR_REF;
	}
	
	// =========================================================================
	// Properties
	
	private int codePoint;
	
	public final int getCodePoint()
	{
		return this.codePoint;
	}
	
	public final int setCodePoint(int codePoint)
	{
		int old = this.codePoint;
		this.codePoint = codePoint;
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
				return WtXmlCharRef.this.getPropertyCount();
			}
			
			@Override
			protected String getName(int index)
			{
				switch (index - getSuperPropertyCount())
				{
					case 0:
						return "codePoint";
						
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
						return WtXmlCharRef.this.getCodePoint();
						
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
						return WtXmlCharRef.this.setCodePoint((Integer) value);
						
					default:
						return super.setValue(index, value);
				}
			}
		};
	}
}
