package org.sweble.wikitext.parser.nodes;

import de.fau.cs.osr.ptk.common.ast.AstNodePropertyIterator;

/**
 * <h1>TemplateParameter</h1> <h2>Grammar</h2>
 */
public class TemplateParameter
		extends
			WtInnerNode3
{
	private static final long serialVersionUID = 1L;
	
	// =========================================================================
	
	public TemplateParameter()
	{
		super(new WtList(), new TemplateArgument(), new WtList());
		
	}
	
	public TemplateParameter(
			WtList name,
			TemplateArgument defaultValue,
			WtList garbage)
	{
		super(name, defaultValue, garbage);
		
	}
	
	@Override
	public int getNodeType()
	{
		return org.sweble.wikitext.parser.AstNodeTypes.NT_TEMPLATE_PARAMETER;
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
		return new WtInnerNode3PropertyIterator()
		{
			@Override
			protected int getPropertyCount()
			{
				return TemplateParameter.this.getPropertyCount();
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
						return TemplateParameter.this.getPrecededByNewline();
						
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
						return TemplateParameter.this.setPrecededByNewline((Boolean) value);
						
					default:
						return super.setValue(index, value);
				}
			}
		};
	}
	
	// =========================================================================
	// Children
	
	public final void setName(WtList name)
	{
		set(0, name);
	}
	
	public final WtList getName()
	{
		return (WtList) get(0);
	}
	
	public final void setDefaultValue(TemplateArgument defaultValue)
	{
		set(1, defaultValue);
	}
	
	public final TemplateArgument getDefaultValue()
	{
		return (TemplateArgument) get(1);
	}
	
	public final void setGarbage(WtList garbage)
	{
		set(2, garbage);
	}
	
	public final WtList getGarbage()
	{
		return (WtList) get(2);
	}
	
	private static final String[] CHILD_NAMES = new String[] { "name", "defaultValue", "garbage" };
	
	public final String[] getChildNames()
	{
		return CHILD_NAMES;
	}
}
