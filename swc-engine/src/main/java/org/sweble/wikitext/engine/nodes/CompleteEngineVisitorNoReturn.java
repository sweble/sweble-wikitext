package org.sweble.wikitext.engine.nodes;

import org.sweble.wikitext.parser.nodes.CompleteWikitextVisitorNoReturn;

public interface CompleteEngineVisitorNoReturn
		extends
			CompleteWikitextVisitorNoReturn
{
	public void visit(EngCompiledPage n);
	
	public void visit(EngNowiki n);
	
	public void visit(EngPage n);
	
	public void visit(EngSoftErrorNode n);
}
