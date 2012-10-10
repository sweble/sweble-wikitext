package org.sweble.wikitext.engine.nodes;

import java.util.List;

import org.sweble.wikitext.engine.lognodes.CompilerLog;
import org.sweble.wikitext.parser.WtEntityMap;
import org.sweble.wikitext.parser.nodes.WikitextNodeFactory;
import org.sweble.wikitext.parser.nodes.WtNode;
import org.sweble.wikitext.parser.nodes.WtNodeList;
import org.sweble.wikitext.parser.nodes.WtXmlElement;

import de.fau.cs.osr.ptk.common.Warning;

public interface EngineNodeFactory
		extends
			WikitextNodeFactory
{
	EngNowiki nowiki(String text);
	
	EngSoftErrorNode softError(String message);
	
	EngSoftErrorNode softError(WtNode pfn);
	
	EngSoftErrorNode softError(WtNodeList content, Exception e);
	
	EngCompiledPage compiledPage(
			EngPage page,
			List<Warning> warnings,
			CompilerLog log);
	
	EngCompiledPage compiledPage(
			EngPage page,
			List<Warning> warnings,
			WtEntityMap entityMap,
			CompilerLog log);
	
	EngPage page(WtNodeList content);
	
	// --[ Modification ]-------------------------------------------------------
	
	<T extends WtXmlElement> T addCssClass(T elem, String cssClass);
}
