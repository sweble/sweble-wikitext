package org.sweble.wikitext.parser.nodes;

import java.util.Collection;

import org.sweble.wikitext.parser.WtRtData;

import xtc.util.Pair;
import de.fau.cs.osr.ptk.common.ast.AstNodeListImpl;

public class WtNodeListImpl
		extends
			AstNodeListImpl<WtNode>
		implements
			WtNodeList
{
	private static final long serialVersionUID = 6285729315278264384L;
	
	// =========================================================================
	
	public WtNodeListImpl()
	{
		super();
	}
	
	public WtNodeListImpl(Collection<? extends WtNode> list)
	{
		super(list);
	}
	
	public WtNodeListImpl(Pair<? extends WtNode> list)
	{
		super(list);
	}
	
	public WtNodeListImpl(WtNode car, Pair<? extends WtNode> cdr)
	{
		super(car, cdr);
	}
	
	public WtNodeListImpl(WtNode a, WtNode b, WtNode c, WtNode d)
	{
		super(a, b, c, d);
	}
	
	public WtNodeListImpl(WtNode a, WtNode b, WtNode c)
	{
		super(a, b, c);
	}
	
	public WtNodeListImpl(WtNode a, WtNode b)
	{
		super(a, b);
	}
	
	public WtNodeListImpl(WtNode... children)
	{
		super(children);
	}
	
	public WtNodeListImpl(WtNode child)
	{
		super(child);
	}
	
	// =========================================================================
	
	@Override
	public String getNodeName()
	{
		return (getClass() == WtNodeListImpl.class) ?
				WtNodeList.class.getSimpleName() :
				super.getNodeName();
	}
	
	// =========================================================================
	
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
