package org.sweble.wikitext.parser.nodes;

import de.fau.cs.osr.ptk.common.ast.AstNodePropertyIterator;

/**
 * <h1>Lists</h1> <h2>Grammar</h2>
 * <ul>
 * <li>
 * <p>
 * ( [*#:]+ ';'? | ';' ) ListItemContentStar
 * </p>
 * </li>
 * </ul>
 */
public class RawListItem
		extends
			WtContentNode
{
	private static final long serialVersionUID = 1L;
	
	// =========================================================================
	
	public RawListItem()
	{
		super();
	}
	
	public RawListItem(
			WtNodeList content,
			String rt0,
			WtNode rt1,
			String prefix)
	{
		super(content);
		setRt0(rt0);
		setRt1(rt1);
		setPrefix(prefix);
	}
	
	@Override
	public int getNodeType()
	{
		return NT_RAW_LIST_ITEM;
	}
	
	// =========================================================================
	// Properties
	
	private String rt0;
	
	public final String getRt0()
	{
		return this.rt0;
	}
	
	public final String setRt0(String rt0)
	{
		String old = this.rt0;
		this.rt0 = rt0;
		return old;
	}
	
	private WtNode rt1;
	
	public final WtNode getRt1()
	{
		return this.rt1;
	}
	
	public final WtNode setRt1(WtNode rt1)
	{
		WtNode old = this.rt1;
		this.rt1 = rt1;
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
	
	@Override
	public final int getPropertyCount()
	{
		return 3;
	}
	
	@Override
	public final AstNodePropertyIterator propertyIterator()
	{
		return new AstNodePropertyIterator()
		{
			@Override
			protected int getPropertyCount()
			{
				return 3;
			}
			
			@Override
			protected String getName(int index)
			{
				switch (index)
				{
					case 0:
						return "rt0";
					case 1:
						return "rt1";
					case 2:
						return "prefix";
						
					default:
						throw new IndexOutOfBoundsException();
				}
			}
			
			@Override
			protected Object getValue(int index)
			{
				switch (index)
				{
					case 0:
						return RawListItem.this.getRt0();
					case 1:
						return RawListItem.this.getRt1();
					case 2:
						return RawListItem.this.getPrefix();
						
					default:
						throw new IndexOutOfBoundsException();
				}
			}
			
			@Override
			protected Object setValue(int index, Object value)
			{
				switch (index)
				{
					case 0:
						return RawListItem.this.setRt0((String) value);
					case 1:
						return RawListItem.this.setRt1((WtNode) value);
					case 2:
						return RawListItem.this.setPrefix((String) value);
						
					default:
						throw new IndexOutOfBoundsException();
				}
			}
		};
	}
}
