package org.sweble.wikitext.parser.nodes;

import xtc.tree.Location;
import de.fau.cs.osr.ptk.common.ast.AstNode;
import de.fau.cs.osr.ptk.common.ast.AstNodePropertyIterator;
import de.fau.cs.osr.ptk.common.ast.ContentNode;
import de.fau.cs.osr.ptk.common.ast.NodeList;
import de.fau.cs.osr.ptk.common.ast.RtDataPtk;

public abstract class WtContentNode
		extends
			ContentNode
		implements
			WikitextNode
{
	private static final long serialVersionUID = -4825960747538151723L;
	
	private RtDataPtk rtd = null;
	
	// =========================================================================
	
	public WtContentNode()
	{
		super();
	}
	
	public WtContentNode(AstNode content, Location location)
	{
		super(content, location);
	}
	
	public WtContentNode(AstNode content)
	{
		super(content);
	}
	
	public WtContentNode(NodeList content, Location location)
	{
		super(content, location);
	}
	
	public WtContentNode(NodeList content)
	{
		super(content);
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
					return WtContentNode.this.setRtd((RtDataPtk) value);
					
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
		n.rtd = (RtDataPtk) n.rtd.clone();
		return n;
	}
}
