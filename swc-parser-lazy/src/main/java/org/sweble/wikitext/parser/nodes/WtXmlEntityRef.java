package org.sweble.wikitext.parser.nodes;

import de.fau.cs.osr.ptk.common.ast.AstNodePropertyIterator;

public class WtXmlEntityRef
		extends
			WtLeafNode
{
	private static final long serialVersionUID = 1L;
	
	// =========================================================================
	
	/**
	 * Only for use by de-serialization code.
	 */
	protected WtXmlEntityRef()
	{
	}
	
	/**
	 * @param resolved
	 *            <code>null</code> allowed to indicate failed resolution.
	 */
	public WtXmlEntityRef(String name, String resolved)
	{
		setName(name);
		setResolved(resolved);
	}
	
	@Override
	public int getNodeType()
	{
		return NT_XML_ENTITY_REF;
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
	
	private String resolved;
	
	public final String getResolved()
	{
		return this.resolved;
	}
	
	public final String setResolved(String resolved)
	{
		String old = this.resolved;
		this.resolved = resolved;
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
		return new WtLeafNodePropertyIterator()
		{
			@Override
			protected int getPropertyCount()
			{
				return WtXmlEntityRef.this.getPropertyCount();
			}
			
			@Override
			protected String getName(int index)
			{
				switch (index - getSuperPropertyCount())
				{
					case 0:
						return "name";
					case 1:
						return "resolved";
						
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
						return WtXmlEntityRef.this.getName();
					case 1:
						return WtXmlEntityRef.this.getResolved();
						
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
						return WtXmlEntityRef.this.setName((String) value);
					case 1:
						return WtXmlEntityRef.this.setResolved((String) value);
						
					default:
						return super.setValue(index, value);
				}
			}
		};
	}
}
