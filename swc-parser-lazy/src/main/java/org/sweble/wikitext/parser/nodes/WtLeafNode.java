package org.sweble.wikitext.parser.nodes;

import org.sweble.wikitext.parser.WtRtData;

import de.fau.cs.osr.ptk.common.ast.AstNodePropertyIterator;
import de.fau.cs.osr.ptk.common.ast.AstLeafNodeImpl;

public abstract class WtLeafNode
		extends
			AstLeafNodeImpl<WtNode>
		implements
			WtNode
{
	private static final long serialVersionUID = -2024251471331960556L;
	
	private WtRtData rtd = null;
	
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
		return new WtLeafNodePropertyIterator();
	}
	
	protected class WtLeafNodePropertyIterator
			extends
				AstNodePropertyIterator
	{
		@Override
		protected int getPropertyCount()
		{
			return WtLeafNode.this.getPropertyCount();
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
					return WtLeafNode.this.getRtd();
					
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
					return WtLeafNode.this.setRtd((WtRtData) value);
					
				default:
					throw new IndexOutOfBoundsException();
			}
		}
	}
	
	// =========================================================================
	
	@Override
	public Object clone() throws CloneNotSupportedException
	{
		WtLeafNode n = (WtLeafNode) super.clone();
		n.rtd = (WtRtData) n.rtd.clone();
		return n;
	}
}
