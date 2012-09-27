package org.sweble.wikitext.parser.nodes;

import de.fau.cs.osr.ptk.common.ast.AstNodePropertyIterator;

/**
 * <h1>Link Option: Keyword</h1> <h2>Grammar</h2>
 * <ul>
 * <li>
 * <p>
 * LinkOptionKeyword ::= Ws* LinkOptionKeyword Ws*
 * </p>
 * </li>
 * </ul>
 */
public class LinkOptionKeyword
		extends
			WtLeafNode
{
	private static final long serialVersionUID = 1L;
	
	// =========================================================================
	
	public LinkOptionKeyword()
	{
		super();
		
	}
	
	public LinkOptionKeyword(String keyword)
	{
		super();
		setKeyword(keyword);
		
	}
	
	@Override
	public int getNodeType()
	{
		return org.sweble.wikitext.parser.AstNodeTypes.NT_LINK_OPTION_KEYWORD;
	}
	
	// =========================================================================
	// Properties
	
	private String keyword;
	
	public final String getKeyword()
	{
		return this.keyword;
	}
	
	public final String setKeyword(String keyword)
	{
		String old = this.keyword;
		this.keyword = keyword;
		return old;
	}
	
	@Override
	public final int getPropertyCount()
	{
		return 1 + getSuperPropertyCount();
	}
	
	private final int getSuperPropertyCount()
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
				return LinkOptionKeyword.this.getPropertyCount();
			}
			
			@Override
			protected String getName(int index)
			{
				switch (index - getSuperPropertyCount())
				{
					case 0:
						return "keyword";
						
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
						return LinkOptionKeyword.this.getKeyword();
						
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
						return LinkOptionKeyword.this.setKeyword((String) value);
						
					default:
						return super.setValue(index, value);
				}
			}
		};
	}
}
