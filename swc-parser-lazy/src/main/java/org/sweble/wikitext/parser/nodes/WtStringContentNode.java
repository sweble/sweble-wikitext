package org.sweble.wikitext.parser.nodes;

import org.sweble.wikitext.parser.WtRtData;

import de.fau.cs.osr.ptk.common.ast.AstNodePropertyIterator;
import de.fau.cs.osr.ptk.common.ast.GenericStringContentNode;

public abstract class WtStringContentNode
		extends
			GenericStringContentNode<WtNode>
		implements
			WtNode
{
	private static final long serialVersionUID = -2087712873453224402L;
	
	private WtRtData rtd = null;
	
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
					return WtStringContentNode.this.setRtd((WtRtData) value);
					
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
		n.rtd = (WtRtData) n.rtd.clone();
		return n;
	}
}
