package org.sweble.wikitext.parser.nodes;

import org.sweble.wikitext.parser.WtRtData;

import xtc.tree.Location;
import de.fau.cs.osr.ptk.common.ast.AstNodePropertyIterator;
import de.fau.cs.osr.ptk.common.ast.GenericContentNode;

public abstract class WtContentNode
		extends
			GenericContentNode<WtNode, WtNodeList>
		implements
			WtNode
{
	private static final long serialVersionUID = -4825960747538151723L;
	
	private WtRtData rtd = null;
	
	// =========================================================================
	
	public WtContentNode()
	{
		super(new WtNodeList());
	}
	
	public WtContentNode(Location arg0, WtNode n0)
	{
		super(arg0, n0);
	}
	
	public WtContentNode(Location arg0)
	{
		super(arg0, new WtNodeList());
	}
	
	public WtContentNode(WtNode n0)
	{
		super(n0);
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
					return WtContentNode.this.setRtd((WtRtData) value);
					
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
		n.rtd = (WtRtData) n.rtd.clone();
		return n;
	}
}
