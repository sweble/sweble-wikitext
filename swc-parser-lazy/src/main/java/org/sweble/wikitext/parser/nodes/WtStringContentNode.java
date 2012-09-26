package org.sweble.wikitext.parser.nodes;

import org.sweble.wikitext.parser.RtData;

import de.fau.cs.osr.ptk.common.ast.StringContentNode;

public class WtStringContentNode
		extends
			StringContentNode
		implements
			WikitextNode
{
	
	private static final long serialVersionUID = -2087712873453224402L;
	
	public WtStringContentNode()
	{
		super();
		// TODO Auto-generated constructor stub
	}
	
	public WtStringContentNode(String content)
	{
		super(content);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public RtData getRtData()
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void clearRtData()
	{
		// TODO Auto-generated method stub
		
	}
	
}
