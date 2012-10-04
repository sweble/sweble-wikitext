package org.sweble.wikitext.parser.nodes;

import org.sweble.wikitext.parser.preprocessor.XmlElementType;

import de.fau.cs.osr.ptk.common.ast.AstNodePropertyIterator;

/**
 * <h1>WtOnlyInclude</h1> <h2>Grammar</h2>
 */
public class WtOnlyInclude
		extends
			WtContentNodeMarkTwo
{
	private static final long serialVersionUID = 1L;
	
	// =========================================================================
	
	public WtOnlyInclude()
	{
		super();
	}
	
	public WtOnlyInclude(WtNodeList content, XmlElementType elementType)
	{
		super(content);
		setElementType(elementType);
	}
	
	@Override
	public int getNodeType()
	{
		return org.sweble.wikitext.parser.AstNodeTypes.NT_ONLY_INCLUDE;
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
		return new WtContentNodeMarkTwoPropertyIterator()
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
}
