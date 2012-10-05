package org.sweble.wikitext.parser.nodes;

import org.sweble.wikitext.parser.encval.IllegalCodePointType;

import de.fau.cs.osr.ptk.common.ast.AstNodePropertyIterator;

/**
 * <h1>Encoding Validator</h1>
 */
public class IllegalCodePoint
		extends
			WtLeafNode
{
	private static final long serialVersionUID = 1L;
	
	// =========================================================================
	
	public IllegalCodePoint()
	{
	}
	
	public IllegalCodePoint(String codePoint, IllegalCodePointType type)
	{
		setCodePoint(codePoint);
		setType(type);
	}
	
	@Override
	public int getNodeType()
	{
		return NT_ILLEGAL_CODE_POINT;
	}
	
	// =========================================================================
	// Properties
	
	private String codePoint;
	
	public final String getCodePoint()
	{
		return this.codePoint;
	}
	
	public final String setCodePoint(String codePoint)
	{
		String old = this.codePoint;
		this.codePoint = codePoint;
		return old;
	}
	
	private IllegalCodePointType type;
	
	public final IllegalCodePointType getType()
	{
		return this.type;
	}
	
	public final IllegalCodePointType setType(IllegalCodePointType type)
	{
		IllegalCodePointType old = this.type;
		this.type = type;
		return old;
	}
	
	@Override
	public final int getPropertyCount()
	{
		return 2;
	}
	
	@Override
	public final AstNodePropertyIterator propertyIterator()
	{
		return new AstNodePropertyIterator()
		{
			@Override
			protected int getPropertyCount()
			{
				return 2;
			}
			
			@Override
			protected String getName(int index)
			{
				switch (index)
				{
					case 0:
						return "codePoint";
					case 1:
						return "type";
						
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
						return IllegalCodePoint.this.getCodePoint();
					case 1:
						return IllegalCodePoint.this.getType();
						
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
						return IllegalCodePoint.this.setCodePoint((String) value);
					case 1:
						return IllegalCodePoint.this.setType((IllegalCodePointType) value);
						
					default:
						throw new IndexOutOfBoundsException();
				}
			}
		};
	}
}
