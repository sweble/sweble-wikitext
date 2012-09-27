package org.sweble.wikitext.parser.nodes;

import xtc.tree.Location;
import de.fau.cs.osr.ptk.common.ast.AstNode;
import de.fau.cs.osr.ptk.common.ast.AstNodePropertyIterator;
import de.fau.cs.osr.ptk.common.ast.InnerNode.InnerNode2;
import de.fau.cs.osr.ptk.common.ast.RtDataPtk;

public abstract class WtInnerNode2
		extends
			InnerNode2
		implements
			WikitextNode
{
	
	private static final long serialVersionUID = -3133816643760188432L;
	
	private RtDataPtk rtd = null;
	
	// =========================================================================
	
	public WtInnerNode2()
	{
		super();
	}
	
	public WtInnerNode2(AstNode n0, AstNode n1)
	{
		super(n0, n1);
	}
	
	public WtInnerNode2(Location arg0, AstNode n0, AstNode n1)
	{
		super(arg0, n0, n1);
	}
	
	public WtInnerNode2(Location arg0)
	{
		super(arg0);
	}
	
	// =========================================================================
	
	@Override
	public RtDataPtk setRtd(Object... glue)
	{
		rtd = new RtDataPtk(this, glue);
		return rtd;
	}
	
	@Override
	public RtDataPtk setRtd(String... glue)
	{
		rtd = new RtDataPtk(this, glue);
		return rtd;
	}
	
	@Override
	public RtDataPtk getRtd()
	{
		return rtd;
	}
	
	@Override
	public void clearRtd()
	{
		rtd = null;
	}
	
	// =========================================================================
	
	@Override
	public int getPropertyCount()
	{
		return 1;
	}
	
	@Override
	public AstNodePropertyIterator propertyIterator()
	{
		return new AstNodePropertyIterator()
		{
			@Override
			protected int getPropertyCount()
			{
				return WtInnerNode2.this.getPropertyCount();
			}
			
			@Override
			protected String getName(int index)
			{
				switch (index)
				{
					case 0:
						return "rtd";
						
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
						return WtInnerNode2.this.getRtd();
						
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
						return WtInnerNode2.this.setRtd((Object[]) value);
						
					default:
						throw new IndexOutOfBoundsException();
				}
			}
		};
	}
	
}
