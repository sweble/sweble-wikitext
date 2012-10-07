package org.sweble.wikitext.parser.nodes;

import de.fau.cs.osr.ptk.common.ast.AstNodePropertyIterator;

public class WtTagExtension
		extends
			WtInnerNode1
		implements
			WtPreproNode
{
	private static final long serialVersionUID = 1L;
	
	// =========================================================================
	
	/**
	 * Only for use by de-serialization code.
	 */
	protected WtTagExtension()
	{
		super((WtNode) null);
	}

	public WtTagExtension(
			String name,
			WtXmlAttributes xmlAttributes)
	{
		super(xmlAttributes);
		setName(name);
		// FIXME: !!
		setBody(null);
	}

	public WtTagExtension(
			String name,
			WtXmlAttributes xmlAttributes,
			String body)
	{
		super(xmlAttributes);
		setName(name);
		setBody(body);
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
	
	private String body;
	
	public final String getBody()
	{
		return this.body;
	}
	
	public final String setBody(String body)
	{
		// !!!
		String old = this.body;
		this.body = body;
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
				return WtTagExtension.this.getPropertyCount();
			}
			
			@Override
			protected String getName(int index)
			{
				switch (index - getSuperPropertyCount())
				{
					case 0:
						return "name";
					case 1:
						return "body";
						
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
					case 1:
						return WtTagExtension.this.getBody();
						
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
					case 1:
						return WtTagExtension.this.setBody((String) value);
						
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
	
	private static final String[] CHILD_NAMES = new String[] { "xmlAttributes" };
	
	public final String[] getChildNames()
	{
		return CHILD_NAMES;
	}
}
