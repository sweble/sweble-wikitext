package org.sweble.wikitext.parser.nodes;

import org.sweble.wikitext.parser.RtData;

import xtc.tree.Location;
import de.fau.cs.osr.ptk.common.ast.AstNode;
import de.fau.cs.osr.ptk.common.ast.InnerNode.InnerNode1;

public class WtInnerNode1
		extends
			InnerNode1
		implements
			WikitextNode
{
	
	private static final long serialVersionUID = -3023143947405463528L;
	
	public WtInnerNode1()
	{
		super();
		// TODO Auto-generated constructor stub
	}
	
	public WtInnerNode1(AstNode n0)
	{
		super(n0);
		// TODO Auto-generated constructor stub
	}
	
	public WtInnerNode1(Location arg0, AstNode n0)
	{
		super(arg0, n0);
		// TODO Auto-generated constructor stub
	}
	
	public WtInnerNode1(Location arg0)
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
