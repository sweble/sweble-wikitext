package org.sweble.wikitext.parser.nodes;

import de.fau.cs.osr.ptk.common.ast.AstNodePropertyIterator;
import de.fau.cs.osr.ptk.common.ast.GenericStringContentNode;
import de.fau.cs.osr.ptk.common.ast.RtDataPtk;

public abstract class WtStringContentNode
		extends
			GenericStringContentNode<WikitextNode>
		implements
			WikitextNode
{
	private static final long serialVersionUID = -2087712873453224402L;
	
	private RtDataPtk rtd = null;
	
	// =========================================================================
	
	public WtStringContentNode()
	{
		super();
	}
	
	public WtStringContentNode(String content)
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
		return 1 + getSuperPropertyCount();
	}
	
	private final int getSuperPropertyCount()
	{
		return super.getPropertyCount();
	}
	
	@Override
	public AstNodePropertyIterator propertyIterator()
	{
		return new WtStringContentNodePropertyIterator();
	}
	
	protected class WtStringContentNodePropertyIterator
			extends
				StringContentNodePropertyIterator
	{
		@Override
		protected int getPropertyCount()
		{
			return WtStringContentNode.this.getPropertyCount();
		}
		
		@Override
		protected String getName(int index)
		{
			switch (index - getSuperPropertyCount())
			{
				case 0:
					return "rtd";
					
				default:
					return super.getName(index);
			}
		}
		
		@Override
		protected Object getValue(int index)
		{
			switch (index - getSuperPropertyCount())
			{
				case 0:
					return WtStringContentNode.this.getRtd();
					
				default:
					return super.getValue(index);
			}
		}
		
		@Override
		protected Object setValue(int index, Object value)
		{
			switch (index - getSuperPropertyCount())
			{
				case 0:
					return WtStringContentNode.this.setRtd((RtDataPtk) value);
					
				default:
					return super.setValue(index, value);
			}
		}
	}
	
	// =========================================================================
	
	@Override
	public Object clone() throws CloneNotSupportedException
	{
		WtStringContentNode n = (WtStringContentNode) super.clone();
		n.rtd = (RtDataPtk) n.rtd.clone();
		return n;
	}
}
