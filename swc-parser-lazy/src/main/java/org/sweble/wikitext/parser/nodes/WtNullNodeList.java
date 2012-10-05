package org.sweble.wikitext.parser.nodes;

import de.fau.cs.osr.ptk.common.ast.AstNodeList;

public class WtNullNodeList
		extends
			WtNullNode
		implements
			WtNodeList
{
	private static final long serialVersionUID = 2465445739660029292L;
	
	@Override
	public void exchange(AstNodeList<WtNode> other)
	{
		throw new UnsupportedOperationException(genMsg());
	}
}
