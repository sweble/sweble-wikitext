package org.sweble.wikitext.parser.nodes;

import xtc.tree.Location;
import de.fau.cs.osr.ptk.common.ast.AstNodePropertyIterator;
import de.fau.cs.osr.ptk.common.ast.GenericInnerNode.GenericInnerNode3;
import de.fau.cs.osr.ptk.common.ast.RtDataPtk;

public abstract class WtInnerNode3
		extends
			GenericInnerNode3<WikitextNode>
		implements
			WikitextNode
{
	private static final long serialVersionUID = 3962368845249207297L;
	
	private RtDataPtk rtd = null;
	
	// =========================================================================
	
	public WtInnerNode3()
	{
		super();
	}
	
	public WtInnerNode3(WikitextNode n0, WikitextNode n1, WikitextNode n2)
	{
		super(n0, n1, n2);
	}
	
	public WtInnerNode3(
			Location arg0,
			WikitextNode n0,
			WikitextNode n1,
			WikitextNode n2)
	{
		super(arg0, n0, n1, n2);
	}
	
	public WtInnerNode3(Location arg0)
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
		return new WtInnerNode3PropertyIterator();
	}
	
	protected class WtInnerNode3PropertyIterator
			extends
				AstNodePropertyIterator
	{
		@Override
		protected int getPropertyCount()
		{
			return WtInnerNode3.this.getPropertyCount();
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
					return WtInnerNode3.this.getRtd();
					
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
					return WtInnerNode3.this.setRtd((RtDataPtk) value);
					
				default:
					throw new IndexOutOfBoundsException();
			}
		}
	}
	
	// =========================================================================
	
	@Override
	public Object clone() throws CloneNotSupportedException
	{
		WtInnerNode3 n = (WtInnerNode3) super.clone();
		n.rtd = (RtDataPtk) n.rtd.clone();
		return n;
	}
}
