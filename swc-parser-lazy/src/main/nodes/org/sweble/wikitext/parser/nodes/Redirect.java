package org.sweble.wikitext.parser.nodes;

import de.fau.cs.osr.ptk.common.ast.AstNodePropertyIterator;

/**
 * <h1>Redirect</h1> <h2>Grammar</h2>
 */
public class Redirect
		extends
			WtLeafNode
{
	private static final long serialVersionUID = 1L;
	
	// =========================================================================
	
	public Redirect()
	{
		super();
		
	}
	
	public Redirect(String target)
	{
		super();
		setTarget(target);
		
	}
	
	@Override
	public int getNodeType()
	{
		return org.sweble.wikitext.parser.AstNodeTypes.NT_REDIRECT;
	}
	
	// =========================================================================
	// Properties
	
	private String target;
	
	public final String getTarget()
	{
		return this.target;
	}
	
	public final String setTarget(String target)
	{
		String old = this.target;
		this.target = target;
		return old;
	}
	
	@Override
	public final int getPropertyCount()
	{
		return 1 + getSuperPropertyCount();
	}
	
	private int getSuperPropertyCount()
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
				return Redirect.this.getPropertyCount();
			}
			
			@Override
			protected String getName(int index)
			{
				switch (index - getSuperPropertyCount())
				{
					case 0:
						return "target";
						
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
						return Redirect.this.getTarget();
						
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
						return Redirect.this.setTarget((String) value);
						
					default:
						return super.setValue(index, value);
				}
			}
		};
	}
}
