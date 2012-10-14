package org.sweble.wikitext.engine.utils;

import org.sweble.wikitext.parser.nodes.WtNode;
import org.sweble.wikitext.parser.utils.AstTextUtils;

public interface EngineAstTextUtils
		extends
			AstTextUtils
{
	public abstract WtNode trim(WtNode n);
	
	public abstract WtNode trimLeft(WtNode n);
	
	public abstract WtNode trimRight(WtNode n);
}
