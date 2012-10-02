package org.sweble.wikitext.parser.nodes;

import xtc.tree.Location;
import de.fau.cs.osr.ptk.common.ast.AstNodePropertyIterator;
import de.fau.cs.osr.ptk.common.ast.GenericContentNode;
import de.fau.cs.osr.ptk.common.ast.RtData;

public abstract class WtContentNode
		extends
			GenericContentNode<WikitextNode, WtList>
		implements
			WikitextNode
{
	private static final long serialVersionUID = -4825960747538151723L;
	
	private RtData rtd = null;
	
	// =========================================================================
	
	public WtContentNode()
	{
		super(new WtList());
	}
	
	public WtContentNode(Location arg0, WikitextNode n0)
	{
		super(arg0, n0);
	}
	
	public WtContentNode(Location arg0)
	{
		super(arg0, new WtList());
	}
	
	public WtContentNode(WikitextNode n0)
	{
		super(n0);
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
		return new WtContentNodePropertyIterator();
	}
	
	private class WtContentNodePropertyIterator
			extends
				AstNodePropertyIterator
	{
		@Override
		protected int getPropertyCount()
		{
			return WtContentNode.this.getPropertyCount();
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
					return WtContentNode.this.getRtd();
					
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
					return WtContentNode.this.setRtd((RtData) value);
					
				default:
					throw new IndexOutOfBoundsException();
			}
		}
	}
	
	// =========================================================================
	
	@Override
	public Object clone() throws CloneNotSupportedException
	{
		WtContentNode n = (WtContentNode) super.clone();
		n.rtd = (RtData) n.rtd.clone();
		return n;
	}
}
