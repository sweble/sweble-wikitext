package org.sweble.wikitext.parser.nodes;

import org.sweble.wikitext.parser.RtData;

import xtc.tree.Location;
import de.fau.cs.osr.ptk.common.ast.AstNode;
import de.fau.cs.osr.ptk.common.ast.InnerNode.InnerNode3;

public class WtInnerNode3
		extends
			InnerNode3
		implements
			WikitextNode
{
	
	private static final long serialVersionUID = 3962368845249207297L;
	
	public WtInnerNode3()
	{
		super();
		// TODO Auto-generated constructor stub
	}
	
	public WtInnerNode3(AstNode n0, AstNode n1, AstNode n2)
	{
		super(n0, n1, n2);
		// TODO Auto-generated constructor stub
	}
	
	public WtInnerNode3(Location arg0, AstNode n0, AstNode n1, AstNode n2)
	{
		super(arg0, n0, n1, n2);
		// TODO Auto-generated constructor stub
	}
	
	public WtInnerNode3(Location arg0)
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
