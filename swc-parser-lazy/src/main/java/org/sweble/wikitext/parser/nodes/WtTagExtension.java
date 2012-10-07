package org.sweble.wikitext.parser.nodes;

import de.fau.cs.osr.ptk.common.ast.AstNodePropertyIterator;

public class WtTagExtension
		extends
			WtInnerNode2
		implements
			WtPreproNode
{
	private static final long serialVersionUID = 1L;
	
	public static final WtTagExtensionBody NO_BODY = WtTagExtensionBody.NULL_BODY;
	
	// =========================================================================
	
	/**
	 * Only for use by de-serialization code.
	 */
	protected WtTagExtension()
	{
		super(null, null);
	}
	
	public WtTagExtension(
			String name,
			WtXmlAttributes xmlAttributes)
	{
		super(xmlAttributes, NO_BODY);
		setName(name);
	}
	
	public WtTagExtension(
			String name,
			WtXmlAttributes xmlAttributes,
			WtTagExtensionBody body)
	{
		super(xmlAttributes, body);
		setName(name);
	}
	
	@Override
	public int getNodeType()
	{
		return NT_TAG_EXTENSION;
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
		if (name == null)
			throw new NullPointerException();
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
		return new WtInnerNode2PropertyIterator()
		{
			@Override
			protected int getPropertyCount()
			{
				return WtTagExtension.this.getPropertyCount();
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
						return WtTagExtension.this.getName();
						
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
						return WtTagExtension.this.setName((String) value);
						
					default:
						return super.setValue(index, value);
				}
			}
		};
	}
	
	// =========================================================================
	// Children
	
	public final void setXmlAttributes(WtXmlAttributes xmlAttributes)
	{
		set(0, xmlAttributes);
	}
	
	public final WtXmlAttributes getXmlAttributes()
	{
		return (WtXmlAttributes) get(0);
	}
	
	public final boolean hasBody()
	{
		return getBody() != NO_BODY;
	}
	
	public final void setBody(WtTagExtensionBody body)
	{
		set(1, body);
	}
	
	public final WtTagExtensionBody getBody()
	{
		return (WtTagExtensionBody) get(1);
	}
	
	private static final String[] CHILD_NAMES = new String[] { "xmlAttributes", "body" };
	
	public final String[] getChildNames()
	{
		return CHILD_NAMES;
	}
}
