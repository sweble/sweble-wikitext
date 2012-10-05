package org.sweble.wikitext.parser.nodes;

import de.fau.cs.osr.ptk.common.ast.AstNodePropertyIterator;

/**
 * <h1>XML Attribute</h1> <h2>Grammar</h2>
 * <ul>
 * <li>
 * <p>
 * Ws* XmlName Ws* '=' Ws* '\'' AttributeValueSq* '\''
 * </p>
 * </li>
 * <li>
 * <p>
 * Ws* XmlName Ws* '=' Ws* '"' ValueDqStar '"'
 * </p>
 * </li>
 * <li>
 * <p>
 * Ws* XmlName Ws* '=' ValueNqStar
 * </p>
 * </li>
 * <li>
 * <p>
 * Ws* XmlName
 * </p>
 * </li>
 * </ul>
 */
public class WtXmlAttribute
		extends
			WtInnerNode1
{
	private static final long serialVersionUID = 1L;
	
	// =========================================================================
	
	public WtXmlAttribute()
	{
		super(new WtNodeList());
	}
	
	public WtXmlAttribute(String name, boolean hasValue)
	{
		super(new WtNodeList());
		setName(name);
		setHasValue(hasValue);
	}
	
	public WtXmlAttribute(String name, WtNodeList value, boolean hasValue)
	{
		super(value);
		setName(name);
		setHasValue(hasValue);
	}
	
	@Override
	public int getNodeType()
	{
		return NT_XML_ATTRIBUTE;
	}
	
	// =========================================================================
	// Properties
	
	private String name;
	
	public final String getName()
	{
		return this.name;
	}
	
	public final String setName(String name)
	{
		String old = this.name;
		this.name = name;
		return old;
	}
	
	private boolean hasValue;
	
	public final boolean getHasValue()
	{
		return this.hasValue;
	}
	
	public final boolean setHasValue(boolean hasValue)
	{
		boolean old = this.hasValue;
		this.hasValue = hasValue;
		return old;
	}
	
	@Override
	public final int getPropertyCount()
	{
		return 2 + getSuperPropertyCount();
	}
	
	public final int getSuperPropertyCount()
	{
		return super.getPropertyCount();
	}
	
	@Override
	public final AstNodePropertyIterator propertyIterator()
	{
		return new WtInnerNode1PropertyIterator()
		{
			@Override
			protected int getPropertyCount()
			{
				return WtXmlAttribute.this.getPropertyCount();
			}
			
			@Override
			protected String getName(int index)
			{
				switch (index - getSuperPropertyCount())
				{
					case 0:
						return "name";
					case 1:
						return "hasValue";
						
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
						return WtXmlAttribute.this.getName();
					case 1:
						return WtXmlAttribute.this.getHasValue();
						
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
						return WtXmlAttribute.this.setName((String) value);
					case 1:
						return WtXmlAttribute.this.setHasValue((Boolean) value);
						
					default:
						return super.setValue(index, value);
				}
			}
		};
	}
	
	// =========================================================================
	// Children
	
	public final void setValue(WtNodeList value)
	{
		set(0, value);
	}
	
	public final WtNodeList getValue()
	{
		return (WtNodeList) get(0);
	}
	
	private static final String[] CHILD_NAMES = new String[] { "value" };
	
	public final String[] getChildNames()
	{
		return CHILD_NAMES;
	}
}
