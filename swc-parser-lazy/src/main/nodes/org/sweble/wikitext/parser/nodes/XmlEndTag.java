package org.sweble.wikitext.parser.nodes;

import org.sweble.wikitext.parser.parser.NamedXmlElement;

import de.fau.cs.osr.ptk.common.ast.AstNodePropertyIterator;

/**
 * <h1>Closing XML tag</h1>
 */
public class XmlEndTag
		extends
			WtLeafNode
		implements
			NamedXmlElement
{
	private static final long serialVersionUID = 1L;
	
	// =========================================================================
	
	public XmlEndTag()
	{
		super();
		
	}
	
	public XmlEndTag(String name)
	{
		super();
		setName(name);
		
	}
	
	@Override
	public int getNodeType()
	{
		return org.sweble.wikitext.parser.AstNodeTypes.NT_XML_TAG_CLOSE;
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
				return XmlEndTag.this.getPropertyCount();
			}
			
			@Override
			protected String getName(int index)
			{
				switch (index - getSuperPropertyCount())
				{
					case 0:
						return "name";
						
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
						return XmlEndTag.this.getName();
						
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
						return XmlEndTag.this.setName((String) value);
						
					default:
						return super.setValue(index, value);
				}
			}
		};
	}
}
