package org.sweble.wikitext.parser.nodes;

import de.fau.cs.osr.ptk.common.ast.AstNodePropertyIterator;

public class WtOnlyInclude
		extends
			WtContentNodeImpl
		implements
			WtPreproNode
{
	private static final long serialVersionUID = 1L;
	
	// =========================================================================
	
	/**
	 * Only for use by de-serialization code.
	 */
	protected WtOnlyInclude()
	{
	}
	
	public WtOnlyInclude(WtNodeList content, XmlElementType elementType)
	{
		super(content);
		setElementType(elementType);
	}
	
	@Override
	public int getNodeType()
	{
		return NT_ONLY_INCLUDE;
	}
	
	// =========================================================================
	// Properties
	
	private XmlElementType elementType;
	
	public final XmlElementType getElementType()
	{
		return this.elementType;
	}
	
	public final XmlElementType setElementType(XmlElementType elementType)
	{
		XmlElementType old = this.elementType;
		this.elementType = elementType;
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
		return new WtContentNodePropertyIterator()
		{
			@Override
			protected int getPropertyCount()
			{
				return WtOnlyInclude.this.getPropertyCount();
			}
			
			@Override
			protected String getName(int index)
			{
				switch (index - getSuperPropertyCount())
				{
					case 0:
						return "elementType";
						
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
						return WtOnlyInclude.this.getElementType();
						
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
						return WtOnlyInclude.this.setElementType((XmlElementType) value);
						
					default:
						return super.setValue(index, value);
				}
			}
		};
	}
	
	// =========================================================================
	
	public static enum XmlElementType
	{
		FULL_ELEMENT,
		EMPTY_ELEMENT,
		UNCLOSED_ELEMENT,
	}
}
