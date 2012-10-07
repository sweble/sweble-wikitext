package org.sweble.wikitext.parser.nodes;

import org.sweble.wikitext.parser.WtRtData;

import de.fau.cs.osr.ptk.common.ast.AstParserEntity;

public class WtParserEntity
		extends
			AstParserEntity<WtNode>
		implements
			WtIntermediate
{
	private static final long serialVersionUID = 7333107598118095040L;
	
	// =========================================================================
	
	/**
	 * Only for use by de-serialization code.
	 */
	protected WtParserEntity()
	{
	}
	
	public WtParserEntity(int id)
	{
		super(id);
	}
	
	// =========================================================================
	
	@Override
	public boolean isSynthetic()
	{
		return false;
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
		return null;
	}
	
	@Override
	public void clearRtd()
	{
	}
}
