package org.sweble.wikitext.parser.nodes;

public class WtTemplateArgument
		extends
			WtInnerNode2
		implements
			WtPreproNode
{
	private static final long serialVersionUID = 1L;
	
	private static final WtName NO_NAME = WtName.NULL;
	
	// =========================================================================
	
	/**
	 * Only for use by de-serialization code.
	 */
	protected WtTemplateArgument()
	{
		super(null, null);
	}
	
	public WtTemplateArgument(WtValue value)
	{
		super(NO_NAME, value);
	}
	
	public WtTemplateArgument(WtName name, WtValue value)
	{
		super(name, value);
	}
	
	@Override
	public int getNodeType()
	{
		return NT_TEMPLATE_ARGUMENT;
	}
	
	// =========================================================================
	// Children
	
	public final boolean hasName()
	{
		return getName() != NO_NAME;
	}
	
	public final void setName(WtName name)
	{
		set(0, name);
	}
	
	public final WtName getName()
	{
		return (WtName) get(0);
	}
	
	public final void setValue(WtValue value)
	{
		set(1, value);
	}
	
	public final WtValue getValue()
	{
		return (WtValue) get(1);
	}
	
	private static final String[] CHILD_NAMES = new String[] { "name", "value" };
	
	public final String[] getChildNames()
	{
		return CHILD_NAMES;
	}
}
