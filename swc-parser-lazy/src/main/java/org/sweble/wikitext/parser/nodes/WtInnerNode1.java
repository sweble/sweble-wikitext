package org.sweble.wikitext.parser.nodes;

import org.sweble.wikitext.parser.WtRtData;

import xtc.tree.Location;
import de.fau.cs.osr.ptk.common.ast.AstNodePropertyIterator;
import de.fau.cs.osr.ptk.common.ast.GenericInnerNode.GenericInnerNode1;

public abstract class WtInnerNode1
		extends
			GenericInnerNode1<WtNode>
		implements
			WtNode
{
	
	private static final long serialVersionUID = -3023143947405463528L;
	
	private WtRtData rtd = null;
	
	// =========================================================================
	
	public WtInnerNode1()
	{
		super();
	}
	
	public WtInnerNode1(WtNode n0)
	{
		super(n0);
	}
	
	public WtInnerNode1(Location arg0, WtNode n0)
	{
		super(arg0, n0);
	}
	
	public WtInnerNode1(Location arg0)
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
		return new WtInnerNode1PropertyIterator();
	}
	
	protected class WtInnerNode1PropertyIterator
			extends
				AstNodePropertyIterator
	{
		@Override
		protected int getPropertyCount()
		{
			return WtInnerNode1.this.getPropertyCount();
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
					return WtInnerNode1.this.getRtd();
					
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
					return WtInnerNode1.this.setRtd((WtRtData) value);
					
				default:
					throw new IndexOutOfBoundsException();
			}
		}
	}
	
	// =========================================================================
	
	@Override
	public Object clone() throws CloneNotSupportedException
	{
		WtInnerNode1 n = (WtInnerNode1) super.clone();
		n.rtd = (WtRtData) n.rtd.clone();
		return n;
	}
}
