package org.sweble.wikitext.parser.nodes;


import de.fau.cs.osr.ptk.common.ast.AstNodePropertyIterator;

/**
 * <h1>WtTemplate</h1> <h2>Grammar</h2>
 */
public class WtTemplate
		extends
			WtInnerNode2
		implements
			WtPreproNode
{
	private static final long serialVersionUID = 1L;
	
	// =========================================================================
	
	public WtTemplate()
	{
		super(new WtNodeListImpl(), new WtNodeListImpl());
	}
	
	public WtTemplate(WtNodeList name, WtNodeList args)
	{
		super(name, args);
	}
	
	@Override
	public int getNodeType()
	{
		return NT_TEMPLATE;
	}
	
	// =========================================================================
	// Properties
	
	private boolean precededByNewline;
	
	public final boolean getPrecededByNewline()
	{
		return this.precededByNewline;
	}
	
	public final boolean setPrecededByNewline(boolean precededByNewline)
	{
		boolean old = this.precededByNewline;
		this.precededByNewline = precededByNewline;
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
		return new WtInnerNode2PropertyIterator()
		{
			@Override
			protected int getPropertyCount()
			{
				return WtTemplate.this.getPropertyCount();
			}
			
			@Override
			protected String getName(int index)
			{
				switch (index - getSuperPropertyCount())
				{
					case 0:
						return "precededByNewline";
						
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
						return WtTemplate.this.getPrecededByNewline();
						
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
						return WtTemplate.this.setPrecededByNewline((Boolean) value);
						
					default:
						return super.setValue(index, value);
				}
			}
		};
	}
	
	// =========================================================================
	// Children
	
	public final void setName(WtNodeList name)
	{
		set(0, name);
	}
	
	public final WtNodeList getName()
	{
		return (WtNodeList) get(0);
	}
	
	public final void setArgs(WtNodeList args)
	{
		set(1, args);
	}
	
	public final WtNodeList getArgs()
	{
		return (WtNodeList) get(1);
	}
	
	private static final String[] CHILD_NAMES = new String[] { "name", "args" };
	
	public final String[] getChildNames()
	{
		return CHILD_NAMES;
	}
}
