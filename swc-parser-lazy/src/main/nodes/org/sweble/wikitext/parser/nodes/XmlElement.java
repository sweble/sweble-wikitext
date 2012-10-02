package org.sweble.wikitext.parser.nodes;

import org.sweble.wikitext.parser.parser.NamedXmlElement;

import de.fau.cs.osr.ptk.common.ast.AstNodePropertyIterator;

/**
 * <h1>XML Element</h1>
 */
public class XmlElement
		extends
			WtInnerNode2
		implements
			NamedXmlElement
{
	private static final long serialVersionUID = 1L;
	
	// =========================================================================
	
	public XmlElement()
	{
		super(new WtList(), new WtList());
		
	}
	
	public XmlElement(
			String name,
			Boolean empty,
			WtList xmlAttributes,
			WtList body)
	{
		super(xmlAttributes, body);
		setName(name);
		setEmpty(empty);
		
	}
	
	@Override
	public int getNodeType()
	{
		return org.sweble.wikitext.parser.AstNodeTypes.NT_XML_ELEMENT;
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
	
	private Boolean empty;
	
	public final Boolean getEmpty()
	{
		return this.empty;
	}
	
	public final Boolean setEmpty(Boolean empty)
	{
		Boolean old = this.empty;
		this.empty = empty;
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
		return new WtInnerNode2PropertyIterator()
		{
			@Override
			protected int getPropertyCount()
			{
				return XmlElement.this.getPropertyCount();
			}
			
			@Override
			protected String getName(int index)
			{
				switch (index - getSuperPropertyCount())
				{
					case 0:
						return "name";
					case 1:
						return "empty";
						
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
						return XmlElement.this.getName();
					case 1:
						return XmlElement.this.getEmpty();
						
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
						return XmlElement.this.setName((String) value);
					case 1:
						return XmlElement.this.setEmpty((Boolean) value);
						
					default:
						return super.setValue(index, value);
				}
			}
		};
	}
	
	// =========================================================================
	// Children
	
	public final void setXmlAttributes(WtList xmlAttributes)
	{
		set(0, xmlAttributes);
	}
	
	public final WtList getXmlAttributes()
	{
		return (WtList) get(0);
	}
	
	public final void setBody(WtList body)
	{
		set(1, body);
	}
	
	public final WtList getBody()
	{
		return (WtList) get(1);
	}
	
	private static final String[] CHILD_NAMES = new String[] { "xmlAttributes", "body" };
	
	public final String[] getChildNames()
	{
		return CHILD_NAMES;
	}
}
