package org.sweble.wikitext.engine.utils;

import org.sweble.wikitext.parser.nodes.WtNode;
import org.sweble.wikitext.parser.utils.AstTextUtils;

public interface EngineAstTextUtils
		extends
			AstTextUtils
{
	public static final int DO_NOT_CONVERT_NOWIKI = AstTextUtils.AST_TO_TEXT_LAST_OPTION + 1;
	
	public static final int AST_TO_TEXT_LAST_OPTION = AstTextUtils.AST_TO_TEXT_LAST_OPTION + 2;
	
	public abstract WtNode trim(WtNode n);
	
	public abstract WtNode trimLeft(WtNode n);
	
	public abstract WtNode trimRight(WtNode n);
}
