package org.sweble.wikitext.parser.nodes;

import org.sweble.wikitext.parser.RtData;

import xtc.tree.Location;
import de.fau.cs.osr.ptk.common.ast.AstNode;
import de.fau.cs.osr.ptk.common.ast.ContentNode;
import de.fau.cs.osr.ptk.common.ast.NodeList;

public class WtContentNode
		extends
			ContentNode
		implements
			WikitextNode
{
	
	private static final long serialVersionUID = -4825960747538151723L;
	
	public WtContentNode()
	{
		super();
		// TODO Auto-generated constructor stub
	}
	
	public WtContentNode(AstNode content, Location location)
	{
		super(content, location);
		// TODO Auto-generated constructor stub
	}
	
	public WtContentNode(AstNode content)
	{
		super(content);
		// TODO Auto-generated constructor stub
	}
	
	public WtContentNode(NodeList content, Location location)
	{
		super(content, location);
		// TODO Auto-generated constructor stub
	}
	
	public WtContentNode(NodeList content)
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
