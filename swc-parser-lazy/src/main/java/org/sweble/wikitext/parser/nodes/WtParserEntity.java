package org.sweble.wikitext.parser.nodes;

import de.fau.cs.osr.ptk.common.ast.GenericText;
import de.fau.cs.osr.ptk.common.ast.RtDataPtk;

public class WtParserEntity
		extends
			GenericText<WikitextNode>
		implements
			WikitextNode
{
	
	private static final long serialVersionUID = 7333107598118095040L;
	
	// =========================================================================
	
	public WtParserEntity()
	{
	}
	
	public WtParserEntity(String content)
	{
		super(content);
	}
	
	// =========================================================================
	
	@Override
	public RtDataPtk setRtd(RtDataPtk rtd)
	{
		throw new UnsupportedOperationException();
	}
	
	@Override
	public RtDataPtk setRtd(Object... glue)
	{
		throw new UnsupportedOperationException();
	}
	
	@Override
	public RtDataPtk setRtd(String... glue)
	{
		throw new UnsupportedOperationException();
	}
	
	@Override
	public RtDataPtk getRtd()
	{
		return null;
	}
	
	@Override
	public void clearRtd()
	{
	}
	
}
