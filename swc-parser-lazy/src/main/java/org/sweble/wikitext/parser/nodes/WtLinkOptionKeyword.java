package org.sweble.wikitext.parser.nodes;

import de.fau.cs.osr.ptk.common.ast.AstNodePropertyIterator;

public class WtLinkOptionKeyword
		extends
			WtLeafNode
{
	private static final long serialVersionUID = 1L;
	
	// =========================================================================
	
	/**
	 * Only for use by de-serialization code.
	 */
	protected WtLinkOptionKeyword()
	{
	}
	
	public WtLinkOptionKeyword(String keyword)
	{
		setKeyword(keyword);
	}
	
	@Override
	public int getNodeType()
	{
		return NT_LINK_OPTION_KEYWORD;
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
		if (keyword == null)
			throw new NullPointerException();
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
				return WtLinkOptionKeyword.this.getPropertyCount();
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
						return WtLinkOptionKeyword.this.getKeyword();
						
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
						return WtLinkOptionKeyword.this.setKeyword((String) value);
						
					default:
						return super.setValue(index, value);
				}
			}
		};
	}
}
