package org.sweble.wikitext.parser.nodes;

import xtc.tree.Location;
import de.fau.cs.osr.ptk.common.ast.AstNodePropertyIterator;
import de.fau.cs.osr.ptk.common.ast.GenericInnerNode.GenericInnerNode2;
import de.fau.cs.osr.ptk.common.ast.RtData;

public abstract class WtInnerNode2
		extends
			GenericInnerNode2<WikitextNode>
		implements
			WikitextNode
{
	private static final long serialVersionUID = -3133816643760188432L;
	
	private RtData rtd = null;
	
	// =========================================================================
	
	public WtInnerNode2()
	{
		super();
	}
	
	public WtInnerNode2(WikitextNode n0, WikitextNode n1)
	{
		super(n0, n1);
	}
	
	public WtInnerNode2(Location arg0, WikitextNode n0, WikitextNode n1)
	{
		super(arg0, n0, n1);
	}
	
	public WtInnerNode2(Location arg0)
	{
		super(arg0);
	}
	
	// =========================================================================
	
	@Override
	public RtData setRtd(RtData rtd)
	{
		RtData old = this.rtd;
		this.rtd = rtd;
		return old;
	}
	
	@Override
	public RtData setRtd(Object... glue)
	{
		rtd = new RtData(this, glue);
		return rtd;
	}
	
	@Override
	public RtData setRtd(String... glue)
	{
		rtd = new RtData(this, glue);
		return rtd;
	}
	
	@Override
	public RtData getRtd()
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
					return WtInnerNode2.this.setRtd((RtData) value);
					
				default:
					throw new IndexOutOfBoundsException();
			}
		}
	}
	
	// =========================================================================
	
	@Override
	public Object clone() throws CloneNotSupportedException
	{
		WtInnerNode2 n = (WtInnerNode2) super.clone();
		n.rtd = (RtData) n.rtd.clone();
		return n;
	}
}
