package org.sweble.wikitext.parser.nodes;

import java.util.Collection;

import org.sweble.wikitext.parser.WtRtData;

import xtc.util.Pair;
import de.fau.cs.osr.ptk.common.ast.AstNodeList;
import de.fau.cs.osr.ptk.common.ast.AstNodeListImpl;

public interface WtNodeList
		extends
			WtNode,
			AstNodeList<WtNode>
{
	public static final WtNullNodeList EMPTY = new WtNullNodeList();
	
	// =========================================================================
	
	public class WtNullNodeList
			extends
				WtNullNode
			implements
				WtNodeList
	{
		private static final long serialVersionUID = 2465445739660029292L;
		
		@Override
		public void exchange(AstNodeList<WtNode> other)
		{
			throw new UnsupportedOperationException(genMsg());
		}
	}
	
	// =========================================================================
	
	public class WtNodeListImpl
			extends
				AstNodeListImpl<WtNode>
			implements
				WtNodeList
	{
		private static final long serialVersionUID = 6285729315278264384L;
		
		// =====================================================================
		
		public WtNodeListImpl()
		{
		}
		
		public WtNodeListImpl(Collection<? extends WtNode> list)
		{
			super(list);
		}
		
		public WtNodeListImpl(Pair<? extends WtNode> list)
		{
			super(list);
		}
		
		public WtNodeListImpl(WtNode child)
		{
			super(child);
		}
		
		public WtNodeListImpl(Object... content)
		{
			for (Object o : content)
			{
				if (o == null)
				{
					continue;
				}
				else if (o instanceof WtNode)
				{
					add((WtNode) o);
				}
				else if (o instanceof Pair)
				{
					@SuppressWarnings("unchecked")
					Pair<? extends WtNode> cast = (Pair<? extends WtNode>) o;
					addAll(cast);
				}
				else if (o instanceof Collection)
				{
					@SuppressWarnings("unchecked")
					Collection<? extends WtNode> cast = (Collection<? extends WtNode>) o;
					addAll(cast);
				}
				else
				{
					throw new IllegalArgumentException("Can't add object of type: " + o.getClass().getName());
				}
			}
		}
		
		// =====================================================================
		
		@Override
		public String getNodeName()
		{
			return (getClass() == WtNodeListImpl.class) ?
					WtNodeList.class.getSimpleName() :
					super.getNodeName();
		}
		
		// =====================================================================
		
		@Override
		public WtRtData setRtd(WtRtData rtd)
		{
			throw new UnsupportedOperationException();
		}
		
		@Override
		public WtRtData setRtd(Object... glue)
		{
			throw new UnsupportedOperationException();
		}
		
		@Override
		public WtRtData setRtd(String... glue)
		{
			throw new UnsupportedOperationException();
		}
		
		@Override
		public WtRtData getRtd()
		{
			throw null;
		}
		
		@Override
		public void clearRtd()
		{
		}
	}
}
