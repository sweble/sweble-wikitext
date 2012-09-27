package org.sweble.wikitext.parser.nodes;

import xtc.tree.Location;
import de.fau.cs.osr.ptk.common.ast.AstNode;
import de.fau.cs.osr.ptk.common.ast.AstNodePropertyIterator;
import de.fau.cs.osr.ptk.common.ast.InnerNode.InnerNode1;
import de.fau.cs.osr.ptk.common.ast.RtDataPtk;

public abstract class WtInnerNode1
		extends
			InnerNode1
		implements
			WikitextNode
{
	
	private static final long serialVersionUID = -3023143947405463528L;
	
	private RtDataPtk rtd = null;
	
	// =========================================================================
	
	public WtInnerNode1()
	{
		super();
	}
	
	public WtInnerNode1(AstNode n0)
	{
		super(n0);
	}
	
	public WtInnerNode1(Location arg0, AstNode n0)
	{
		super(arg0, n0);
	}
	
	public WtInnerNode1(Location arg0)
	{
		super(arg0);
	}
	
	// =========================================================================
	
	@Override
	public RtDataPtk setRtd(RtDataPtk rtd)
	{
		RtDataPtk old = this.rtd;
		this.rtd = rtd;
		return old;
	}
	
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
					return WtInnerNode1.this.setRtd((RtDataPtk) value);
					
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
		n.rtd = (RtDataPtk) n.rtd.clone();
		return n;
	}
}
