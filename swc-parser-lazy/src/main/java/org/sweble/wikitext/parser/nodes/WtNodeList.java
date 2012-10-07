package org.sweble.wikitext.parser.nodes;

import de.fau.cs.osr.ptk.common.ast.AstNodeList;

public interface WtNodeList
		extends
			WtNode,
			AstNodeList<WtNode>
{
	public static final WtNullNodeList EMPTY_NODE_LIST = new WtNullNodeList();
}
