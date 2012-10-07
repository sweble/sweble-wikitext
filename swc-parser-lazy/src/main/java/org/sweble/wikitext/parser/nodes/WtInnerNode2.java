package org.sweble.wikitext.parser.nodes;

import org.sweble.wikitext.parser.WtRtData;

import xtc.tree.Location;
import de.fau.cs.osr.ptk.common.ast.AstAbstractInnerNode.AstInnerNode2;
import de.fau.cs.osr.ptk.common.ast.AstNodePropertyIterator;

public abstract class WtInnerNode2
		extends
			AstInnerNode2<WtNode>
		implements
			WtNode
{
	private static final long serialVersionUID = -3133816643760188432L;
	
	private WtRtData rtd = null;
	
	// =========================================================================
	
	public WtInnerNode2()
	{
		super();
	}
	
	public WtInnerNode2(WtNode n0, WtNode n1)
	{
		super(n0, n1);
	}
	
	public WtInnerNode2(Location arg0, WtNode n0, WtNode n1)
	{
		super(arg0, n0, n1);
	}
	
	public WtInnerNode2(Location arg0)
	{
		super(arg0);
	}
	
	// =========================================================================
	
	@Override
	public WtRtData setRtd(WtRtData rtd)
	{
		if (rtd != null && rtd.size() != this.size() + 1)
			throw new IllegalArgumentException();
		WtRtData old = this.rtd;
		this.rtd = rtd;
		return old;
	}
	
	@Override
	public WtRtData setRtd(Object... glue)
	{
		rtd = new WtRtData(this, glue);
		return rtd;
	}
	
	@Override
	public WtRtData setRtd(String... glue)
	{
		rtd = new WtRtData(this, glue);
		return rtd;
	}
	
	@Override
	public WtRtData getRtd()
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
		return new WtInnerNode2PropertyIterator();
	}
	
	protected class WtInnerNode2PropertyIterator
			extends
				AstNodePropertyIterator
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
					return WtInnerNode2.this.setRtd((WtRtData) value);
					
				default:
					throw new IndexOutOfBoundsException();
			}
		}
	}
}
