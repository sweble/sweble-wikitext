package org.sweble.wikitext.parser.nodes;

import de.fau.cs.osr.ptk.common.ast.GenericLeafNode;
import de.fau.cs.osr.ptk.common.ast.RtData;

public abstract class ForeignNode
		extends
			GenericLeafNode<WikitextNode>
		implements
			WikitextNode
{
	private static final long serialVersionUID = -4615966524233077629L;
	
	// =========================================================================
	
	public static ForeignNode from(Object... content)
	{
		if (content == null || content.length == 0)
		{
			return new EmptyForeignNode();
		}
		else if (content.length == 1)
		{
			return new ForeignNode1(content[0]);
		}
		else
		{
			return new ForeignNodeN(content);
		}
	}
	
	// =========================================================================
	
	@Override
	public RtData setRtd(RtData rtd)
	{
		throw new UnsupportedOperationException();
	}
	
	@Override
	public RtData setRtd(Object... glue)
	{
		throw new UnsupportedOperationException();
	}
	
	@Override
	public RtData setRtd(String... glue)
	{
		throw new UnsupportedOperationException();
	}
	
	@Override
	public RtData getRtd()
	{
		return null;
	}
	
	@Override
	public void clearRtd()
	{
	}
	
	// =========================================================================
	
	protected abstract Object getForeign(int index);
	
	public <T> T getForeign(int index, Class<? extends T> type)
	{
		@SuppressWarnings("unchecked")
		T o = (T) getForeign(index);
		return o;
	}
	
	// =========================================================================
	
	public static final class EmptyForeignNode
			extends
				ForeignNode
	{
		private static final long serialVersionUID = -2714647204132421385L;
		
		@Override
		protected Object getForeign(int index)
		{
			throw new IndexOutOfBoundsException();
		}
	}
	
	public static final class ForeignNode1
			extends
				ForeignNode
	{
		private static final long serialVersionUID = 3885854587740841986L;
		
		Object o;
		
		public ForeignNode1(Object o)
		{
			this.o = o;
		}
		
		@Override
		protected Object getForeign(int index)
		{
			if (index != 0)
				throw new IndexOutOfBoundsException();
			return o;
		}
	}
	
	public static final class ForeignNodeN
			extends
				ForeignNode
	{
		private static final long serialVersionUID = 3885854587740841986L;
		
		Object content[];
		
		public ForeignNodeN(Object... content)
		{
			this.content = content;
		}
		
		@Override
		protected Object getForeign(int index)
		{
			return content[index];
		}
	}
}
