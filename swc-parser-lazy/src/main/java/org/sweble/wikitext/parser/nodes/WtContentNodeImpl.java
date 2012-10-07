package org.sweble.wikitext.parser.nodes;

import java.io.IOException;
import java.util.Iterator;

import org.sweble.wikitext.parser.WtRtData;

import de.fau.cs.osr.ptk.common.ast.AstNodePropertyIterator;

public abstract class WtContentNodeImpl
		extends
			WtNodeListImpl
		implements
			WtContentNode
{
	private static final long serialVersionUID = 3407356901471138122L;
	
	private WtRtData rtd = null;
	
	// =========================================================================
	
	public WtContentNodeImpl()
	{
	}
	
	public WtContentNodeImpl(WtNodeList content)
	{
		super(content);
	}
	
	// =========================================================================
	
	@Override
	public WtRtData setRtd(WtRtData rtd)
	{
		if (rtd != null && rtd.size() != 2)
			throw new IllegalArgumentException();
		WtRtData old = this.rtd;
		this.rtd = rtd;
		return old;
	}
	
	@Override
	public WtRtData setRtd(Object... glue)
	{
		rtd = new WtRtData(2, glue);
		return rtd;
	}
	
	@Override
	public WtRtData setRtd(String... glue)
	{
		rtd = new WtRtData(2, glue);
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
	
	public class WtContentNodePropertyIterator
			extends
				AstNodePropertyIterator
	{
		@Override
		protected int getPropertyCount()
		{
			return WtContentNodeImpl.this.getPropertyCount();
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
					return WtContentNodeImpl.this.getRtd();
					
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
					return WtContentNodeImpl.this.setRtd((WtRtData) value);
					
				default:
					throw new IndexOutOfBoundsException();
			}
		}
	}
	
	// =========================================================================
	
	@Override
	public void toString(Appendable out) throws IOException
	{
		out.append(getNodeName());
		out.append('[');
		
		for (Iterator<WtNode> i = this.iterator(); i.hasNext();)
		{
			WtNode node = i.next();
			if (node == null)
			{
				// TODO: Remove this case!
				out.append("null");
			}
			else
			{
				node.toString(out);
			}
			
			if (i.hasNext())
				out.append(", ");
		}
		
		out.append(']');
	}
}
