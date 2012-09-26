package org.sweble.wikitext.parser.nodes;

import org.sweble.wikitext.parser.RtData;

import xtc.tree.Location;
import de.fau.cs.osr.ptk.common.ast.AstNode;
import de.fau.cs.osr.ptk.common.ast.InnerNode.InnerNode2;

public class WtInnerNode2
		extends
			InnerNode2
		implements
			WikitextNode
{
	
	private static final long serialVersionUID = -3133816643760188432L;
	
	public WtInnerNode2()
	{
		super();
		// TODO Auto-generated constructor stub
	}
	
	public WtInnerNode2(AstNode n0, AstNode n1)
	{
		super(n0, n1);
		// TODO Auto-generated constructor stub
	}
	
	public WtInnerNode2(Location arg0, AstNode n0, AstNode n1)
	{
		super(arg0, n0, n1);
		// TODO Auto-generated constructor stub
	}
	
	public WtInnerNode2(Location arg0)
	{
		super(arg0);
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
	
	@Override
	public String[] getChildNames()
	{
		// TODO Auto-generated method stub
		return null;
	}
	
}
