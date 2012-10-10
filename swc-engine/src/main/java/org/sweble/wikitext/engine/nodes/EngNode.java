package org.sweble.wikitext.engine.nodes;

import org.sweble.wikitext.parser.nodes.WtNode;

public interface EngNode
		extends
			WtNode
{
	// ...
	
	// =========================================================================
	
	public static final int NT_ENGINE_BITS /* .......... */= NT_MW_NODE_BITS | 0x100000;
	
	// -- General nodes --------------------------------------------------------
	
	public static final int NT_PAGE /* ................. */= NT_ENGINE_BITS + 1;
	
	public static final int NT_COMPILED_PAGE /* ........ */= NT_ENGINE_BITS + 2;
	
	public static final int NT_NOWIKI /* ............... */= NT_ENGINE_BITS + 3;
	
	public static final int NT_SOFT_ERROR /* ........... */= NT_ENGINE_BITS + 4;
}
