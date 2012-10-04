package org.sweble.wikitext.parser.nodes;

import de.fau.cs.osr.ptk.common.ast.AstNodePropertyIterator;

/**
 * <h1>WtSection</h1>
 */
public class WtSection
		extends
			WtInnerNode2
{
	private static final long serialVersionUID = 1L;
	
	// =========================================================================
	
	public WtSection()
	{
		super(new WtSectionHeading(), new WtSectionBody());
		
	}
	
	public WtSection(int level, WtSectionHeading heading, WtSectionBody body)
	{
		super(heading, body);
		setLevel(level);
		
	}
	
	@Override
	public int getNodeType()
	{
		return org.sweble.wikitext.parser.AstNodeTypes.NT_SECTION;
	}
	
	// =========================================================================
	// Properties
	
	private int level;
	
	public final int getLevel()
	{
		return this.level;
	}
	
	public final int setLevel(int level)
	{
		int old = this.level;
		this.level = level;
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
				return WtSection.this.getPropertyCount();
			}
			
			@Override
			protected String getName(int index)
			{
				switch (index - getSuperPropertyCount())
				{
					case 0:
						return "level";
						
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
						return WtSection.this.getLevel();
						
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
						return WtSection.this.setLevel((Integer) value);
						
					default:
						return super.setValue(index, value);
				}
			}
		};
	}
	
	// =========================================================================
	// Children
	
	public final void setHeading(WtSectionHeading title)
	{
		set(0, title);
	}
	
	public final WtSectionHeading getHeading()
	{
		return (WtSectionHeading) get(0);
	}
	
	public final void setBody(WtSectionBody body)
	{
		set(1, body);
	}
	
	public final WtSectionBody getBody()
	{
		return (WtSectionBody) get(1);
	}
	
	private static final String[] CHILD_NAMES = new String[] { "heading", "body" };
	
	public final String[] getChildNames()
	{
		return CHILD_NAMES;
	}
}
