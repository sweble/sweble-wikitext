package org.sweble.wikitext.parser.nodes;


import de.fau.cs.osr.ptk.common.ast.AstNodePropertyIterator;

/**
 * <h1>XmlComment</h1> <h2>Grammar</h2>
 */
public class WtXmlComment
		extends
			WtStringNodeImpl
		implements
			WtPreproNode
{
	private static final long serialVersionUID = 1L;
	
	// =========================================================================
	
	public WtXmlComment()
	{
	}
	
	public WtXmlComment(String content)
	{
		super(content);
	}
	
	public WtXmlComment(String content, String prefix, String suffix)
	{
		super(content);
		setPrefix(prefix);
		setSuffix(suffix);
	}
	
	@Override
	public int getNodeType()
	{
		return NT_XML_COMMENT;
	}
	
	// =========================================================================
	// Properties
	
	private String prefix;
	
	public final String getPrefix()
	{
		return this.prefix;
	}
	
	public final String setPrefix(String prefix)
	{
		String old = this.prefix;
		this.prefix = prefix;
		return old;
	}
	
	private String suffix;
	
	public final String getSuffix()
	{
		return this.suffix;
	}
	
	public final String setSuffix(String suffix)
	{
		String old = this.suffix;
		this.suffix = suffix;
		return old;
	}
	
	@Override
	public final int getPropertyCount()
	{
		return 2 + getSuperPropertyCount();
	}
	
	public final int getSuperPropertyCount()
	{
		return super.getPropertyCount();
	}
	
	@Override
	public final AstNodePropertyIterator propertyIterator()
	{
		return new WtStringContentNodePropertyIterator()
		{
			@Override
			protected int getPropertyCount()
			{
				return WtXmlComment.this.getPropertyCount();
			}
			
			@Override
			protected String getName(int index)
			{
				switch (index - getSuperPropertyCount())
				{
					case 0:
						return "prefix";
					case 1:
						return "suffix";
						
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
						return WtXmlComment.this.getPrefix();
					case 1:
						return WtXmlComment.this.getSuffix();
						
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
						return WtXmlComment.this.setPrefix((String) value);
					case 1:
						return WtXmlComment.this.setSuffix((String) value);
						
					default:
						return super.setValue(index, value);
				}
			}
		};
	}
}
