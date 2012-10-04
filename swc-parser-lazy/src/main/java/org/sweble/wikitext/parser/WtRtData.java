package org.sweble.wikitext.parser;

import java.util.ArrayList;

import org.sweble.wikitext.parser.nodes.WtContentNodeMarkTwo;
import org.sweble.wikitext.parser.nodes.WtNode;

import de.fau.cs.osr.ptk.common.ast.AstNode;
import de.fau.cs.osr.ptk.common.ast.RtData;

public class WtRtData
		extends
			RtData
{
	private static final long serialVersionUID = 1L;
	
	// =========================================================================
	
	public WtRtData(AstNode<?> node, Object... glue)
	{
		super(node, glue);
	}
	
	public WtRtData(AstNode<?> node, String... glue)
	{
		super(node, glue);
	}
	
	public WtRtData(AstNode<?> node)
	{
		super(node);
	}
	
	public WtRtData(int size, Object... glue)
	{
		super(size, glue);
	}
	
	public WtRtData(int size, String... glue)
	{
		super(size, glue);
	}
	
	public WtRtData(int size)
	{
		super(size);
	}
	
	public WtRtData(WtRtData rtData)
	{
		super(rtData);
	}
	
	// =========================================================================
	
	@Override
	protected void addNodeOrObject(ArrayList<Object> result, Object o)
	{
		if (o instanceof WtContentNodeMarkTwo)
		{
			for (WtNode c : (WtContentNodeMarkTwo) o)
				addNodeOrObject(result, c);
		}
		else
		{
			super.addNodeOrObject(result, o);
		}
	}
	
	// =========================================================================
	
	@Override
	public String toString()
	{
		return super.toString();
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException
	{
		return new WtRtData(this);
	}
}
