package org.sweble.wikitext.parser.nodes;

import org.sweble.wikitext.parser.WtRtData;

import de.fau.cs.osr.ptk.common.ast.AstNodeImpl;

public class WtNullNode
		extends
			AstNodeImpl<WtNode>
		implements
			WtNode
{
	private static final long serialVersionUID = -8143436141986490761L;
	
	// =========================================================================
	
	@Override
	public WtRtData setRtd(WtRtData rtd)
	{
		throw new UnsupportedOperationException(genMsg());
	}
	
	@Override
	public WtRtData setRtd(Object... glue)
	{
		throw new UnsupportedOperationException(genMsg());
	}
	
	@Override
	public WtRtData setRtd(String... glue)
	{
		throw new UnsupportedOperationException(genMsg());
	}
	
	@Override
	public WtRtData getRtd()
	{
		return null;
	}
	
	@Override
	public void clearRtd()
	{
		throw new UnsupportedOperationException(genMsg());
	}
	
	// =========================================================================
	
	protected String genMsg()
	{
		return "You are operating on an immutable " + getNodeName() + " object!";
	}
}
