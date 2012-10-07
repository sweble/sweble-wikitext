package org.sweble.wikitext.parser.nodes;

import de.fau.cs.osr.ptk.common.ast.AstNodePropertyIterator;

public class WtTemplate
		extends
			WtInnerNode2
		implements
			WtPreproNode
{
	private static final long serialVersionUID = 1L;
	
	// =========================================================================
	
	/**
	 * Only for use by de-serialization code.
	 */
	protected WtTemplate()
	{
		super(null, null);
	}
	
	public WtTemplate(WtName name, WtTemplateArguments args)
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
	
	public final void setName(WtName name)
	{
		set(0, name);
	}
	
	public final WtName getName()
	{
		return (WtName) get(0);
	}
	
	public final void setArgs(WtTemplateArguments args)
	{
		set(1, args);
	}
	
	public final WtTemplateArguments getArgs()
	{
		return (WtTemplateArguments) get(1);
	}
	
	private static final String[] CHILD_NAMES = new String[] { "name", "args" };
	
	public final String[] getChildNames()
	{
		return CHILD_NAMES;
	}
}
