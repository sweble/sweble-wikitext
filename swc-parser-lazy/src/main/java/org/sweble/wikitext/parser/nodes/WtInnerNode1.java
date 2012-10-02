package org.sweble.wikitext.parser.nodes;

import xtc.tree.Location;
import de.fau.cs.osr.ptk.common.ast.AstNodePropertyIterator;
import de.fau.cs.osr.ptk.common.ast.GenericInnerNode.GenericInnerNode1;
import de.fau.cs.osr.ptk.common.ast.RtData;

public abstract class WtInnerNode1
		extends
			GenericInnerNode1<WtNode>
		implements
			WtNode
{
	
	private static final long serialVersionUID = -3023143947405463528L;
	
	private RtData rtd = null;
	
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
					return WtInnerNode1.this.setRtd((RtData) value);
					
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
		n.rtd = (RtData) n.rtd.clone();
		return n;
	}
}
