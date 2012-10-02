package org.sweble.wikitext.parser.nodes;

import java.util.Collection;

import xtc.util.Pair;
import de.fau.cs.osr.ptk.common.ast.AstNodePropertyIterator;
import de.fau.cs.osr.ptk.common.ast.GenericNodeList;
import de.fau.cs.osr.ptk.common.ast.RtData;

public class WtContentNodeMarkTwo
		extends
			GenericNodeList<WtNode>
		implements
			WtNode
{
	private static final long serialVersionUID = 3407356901471138122L;
	
	private RtData rtd = null;
	
	// =========================================================================
	
	public WtContentNodeMarkTwo()
	{
		super();
	}
	
	public WtContentNodeMarkTwo(Collection<? extends WtNode> list)
	{
		super(list);
	}
	
	public WtContentNodeMarkTwo(Pair<? extends WtNode> list)
	{
		super(list);
	}
	
	public WtContentNodeMarkTwo(WtNode car, Pair<? extends WtNode> cdr)
	{
		super(car, cdr);
	}
	
	public WtContentNodeMarkTwo(WtNode a, WtNode b, WtNode c, WtNode d)
	{
		super(a, b, c, d);
	}
	
	public WtContentNodeMarkTwo(WtNode a, WtNode b, WtNode c)
	{
		super(a, b, c);
	}
	
	public WtContentNodeMarkTwo(WtNode a, WtNode b)
	{
		super(a, b);
	}
	
	public WtContentNodeMarkTwo(WtNode... children)
	{
		super(children);
	}
	
	public WtContentNodeMarkTwo(WtNode child)
	{
		super(child);
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
		return new WtContentNodeMarkTwoPropertyIterator();
	}
	
	private class WtContentNodeMarkTwoPropertyIterator
			extends
				AstNodePropertyIterator
	{
		@Override
		protected int getPropertyCount()
		{
			return WtContentNodeMarkTwo.this.getPropertyCount();
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
					return WtContentNodeMarkTwo.this.getRtd();
					
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
					return WtContentNodeMarkTwo.this.setRtd((RtData) value);
					
				default:
					throw new IndexOutOfBoundsException();
			}
		}
	}
	
}
