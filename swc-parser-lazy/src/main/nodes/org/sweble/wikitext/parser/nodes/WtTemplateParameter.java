package org.sweble.wikitext.parser.nodes;

import org.sweble.wikitext.parser.postprocessor.WtPreproNode;

import de.fau.cs.osr.ptk.common.ast.AstNodePropertyIterator;

/**
 * <h1>TemplateParameter</h1> <h2>Grammar</h2>
 */
public class WtTemplateParameter
		extends
			WtInnerNode3
		implements
			WtPreproNode
{
	private static final long serialVersionUID = 1L;
	
	// =========================================================================
	
	public WtTemplateParameter()
	{
		super(new WtNodeList(), new WtTemplateArgument(), new WtNodeList());
	}
	
	public WtTemplateParameter(
			WtNodeList name,
			WtTemplateArgument defaultValue,
			WtNodeList garbage)
	{
		super(name, defaultValue, garbage);
	}
	
	@Override
	public int getNodeType()
	{
		return NT_TEMPLATE_PARAMETER;
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
				return WtTemplateParameter.this.getPropertyCount();
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
						return WtTemplateParameter.this.getPrecededByNewline();
						
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
						return WtTemplateParameter.this.setPrecededByNewline((Boolean) value);
						
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
	
	public final void setDefaultValue(WtTemplateArgument defaultValue)
	{
		set(1, defaultValue);
	}
	
	public final WtTemplateArgument getDefaultValue()
	{
		return (WtTemplateArgument) get(1);
	}
	
	public final void setGarbage(WtNodeList garbage)
	{
		set(2, garbage);
	}
	
	public final WtNodeList getGarbage()
	{
		return (WtNodeList) get(2);
	}
	
	private static final String[] CHILD_NAMES = new String[] { "name", "defaultValue", "garbage" };
	
	public final String[] getChildNames()
	{
		return CHILD_NAMES;
	}
}
