package org.sweble.wikitext.parser.nodes;

import de.fau.cs.osr.ptk.common.ast.AstNodePropertyIterator;

/**
 * <h1>Signature</h1> <h2>Grammar</h2>
 * <ul>
 * <li>
 * <p>
 * '~~~' '~'*
 * </p>
 * </li>
 * </ul>
 */
public class Signature
		extends
			WtLeafNode
{
	private static final long serialVersionUID = 1L;
	
	// =========================================================================
	
	public Signature()
	{
		super();
		
	}
	
	public Signature(int tildeCount)
	{
		super();
		setTildeCount(tildeCount);
		
	}
	
	@Override
	public int getNodeType()
	{
		return org.sweble.wikitext.parser.AstNodeTypes.NT_SIGNATURE;
	}
	
	// =========================================================================
	// Properties
	
	private int tildeCount;
	
	public final int getTildeCount()
	{
		return this.tildeCount;
	}
	
	public final int setTildeCount(int tildeCount)
	{
		int old = this.tildeCount;
		this.tildeCount = tildeCount;
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
		return new WtLeafNodePropertyIterator()
		{
			@Override
			protected int getPropertyCount()
			{
				return Signature.this.getPropertyCount();
			}
			
			@Override
			protected String getName(int index)
			{
				switch (index - getSuperPropertyCount())
				{
					case 0:
						return "tildeCount";
						
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
						return Signature.this.getTildeCount();
						
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
						return Signature.this.setTildeCount((Integer) value);
						
					default:
						return super.setValue(index, value);
				}
			}
		};
	}
}
