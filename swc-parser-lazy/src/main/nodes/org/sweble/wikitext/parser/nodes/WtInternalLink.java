package org.sweble.wikitext.parser.nodes;

import de.fau.cs.osr.ptk.common.ast.AstNodePropertyIterator;

/**
 * <h1>Internal Link</h1>
 */
public class WtInternalLink
		extends
			WtInnerNode1
{
	private static final long serialVersionUID = 1L;
	
	// =========================================================================
	
	public WtInternalLink()
	{
		super(new WtLinkTitle());
	}
	
	public WtInternalLink(
			String prefix,
			String target,
			WtLinkTitle title,
			String postfix)
	{
		super(title);
		setPrefix(prefix);
		setTarget(target);
		setPostfix(postfix);
	}
	
	@Override
	public int getNodeType()
	{
		return NT_INTERNAL_LINK;
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
	
	private String postfix;
	
	public final String getPostfix()
	{
		return this.postfix;
	}
	
	public final String setPostfix(String postfix)
	{
		String old = this.postfix;
		this.postfix = postfix;
		return old;
	}
	
	@Override
	public final int getPropertyCount()
	{
		return 3 + getSuperPropertyCount();
	}
	
	private final int getSuperPropertyCount()
	{
		return super.getPropertyCount();
	}
	
	@Override
	public final AstNodePropertyIterator propertyIterator()
	{
		return new WtInnerNode1PropertyIterator()
		{
			@Override
			protected int getPropertyCount()
			{
				return WtInternalLink.this.getPropertyCount();
			}
			
			@Override
			protected String getName(int index)
			{
				switch (index - getSuperPropertyCount())
				{
					case 0:
						return "target";
					case 1:
						return "prefix";
					case 2:
						return "postfix";
						
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
						return WtInternalLink.this.getTarget();
					case 1:
						return WtInternalLink.this.getPrefix();
					case 2:
						return WtInternalLink.this.getPostfix();
						
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
						return WtInternalLink.this.setTarget((String) value);
					case 1:
						return WtInternalLink.this.setPrefix((String) value);
					case 2:
						return WtInternalLink.this.setPostfix((String) value);
						
					default:
						return super.setValue(index, value);
				}
			}
		};
	}
	
	// =========================================================================
	// Children
	
	public final void setTitle(WtLinkTitle title)
	{
		set(0, title);
	}
	
	public final WtLinkTitle getTitle()
	{
		return (WtLinkTitle) get(0);
	}
	
	private static final String[] CHILD_NAMES = new String[] { "title" };
	
	public final String[] getChildNames()
	{
		return CHILD_NAMES;
	}
}
