package org.sweble.wikitext.parser.nodes;

import java.util.Collection;

import xtc.util.Pair;
import de.fau.cs.osr.ptk.common.ast.GenericNodeList;
import de.fau.cs.osr.ptk.common.ast.RtData;

public class WtList
		extends
			GenericNodeList<WikitextNode>
		implements
			WikitextNode
{
	
	private static final long serialVersionUID = 6285729315278264384L;
	
	// =========================================================================
	
	public WtList()
	{
		super();
	}
	
	public WtList(Collection<? extends WikitextNode> list)
	{
		super(list);
	}
	
	public WtList(Pair<? extends WikitextNode> list)
	{
		super(list);
	}
	
	public WtList(WikitextNode car, Pair<? extends WikitextNode> cdr)
	{
		super(car, cdr);
	}
	
	public WtList(WikitextNode a, WikitextNode b, WikitextNode c, WikitextNode d)
	{
		super(a, b, c, d);
	}
	
	public WtList(WikitextNode a, WikitextNode b, WikitextNode c)
	{
		super(a, b, c);
	}
	
	public WtList(WikitextNode a, WikitextNode b)
	{
		super(a, b);
	}
	
	public WtList(WikitextNode... children)
	{
		super(children);
	}
	
	public WtList(WikitextNode child)
	{
		super(child);
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
		throw null;
	}
	
	@Override
	public void clearRtd()
	{
	}
	
}
