package org.sweble.wikitext.parser.nodes;

import de.fau.cs.osr.ptk.common.ast.AstNodePropertyIterator;

public class WtIllegalCodePoint
		extends
			WtLeafNode
{
	private static final long serialVersionUID = 1L;
	
	// =========================================================================
	
	/**
	 * Only for use by de-serialization code.
	 */
	protected WtIllegalCodePoint()
	{
	}
	
	public WtIllegalCodePoint(String codePoint, IllegalCodePointType type)
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
		if (codePoint == null)
			throw new NullPointerException();
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
		if (type == null)
			throw new NullPointerException();
		IllegalCodePointType old = this.type;
		this.type = type;
		return old;
	}
	
	@Override
	public final int getPropertyCount()
	{
		return 2 + getSuperPropertyCount();
	}
	
	public int getSuperPropertyCount()
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
				return WtIllegalCodePoint.this.getPropertyCount();
			}
			
			@Override
			protected String getName(int index)
			{
				switch (index - getSuperPropertyCount())
				{
					case 0:
						return "codePoint";
					case 1:
						return "type";
						
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
						return WtIllegalCodePoint.this.getCodePoint();
					case 1:
						return WtIllegalCodePoint.this.getType();
						
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
						return WtIllegalCodePoint.this.setCodePoint((String) value);
					case 1:
						return WtIllegalCodePoint.this.setType((IllegalCodePointType) value);
						
					default:
						return super.setValue(index, value);
				}
			}
		};
	}
	
	// =========================================================================
	
	public enum IllegalCodePointType
	{
		ISOLATED_SURROGATE,
		NON_CHARACTER,
		PRIVATE_USE_CHARACTER,
		CONTROL_CHARACTER
	}
}
