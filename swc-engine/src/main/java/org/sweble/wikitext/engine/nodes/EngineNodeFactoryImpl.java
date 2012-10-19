package org.sweble.wikitext.engine.nodes;

import java.util.List;

import org.sweble.wikitext.engine.config.WikiConfig;
import org.sweble.wikitext.engine.lognodes.CompilerLog;
import org.sweble.wikitext.parser.WtEntityMap;
import org.sweble.wikitext.parser.nodes.WikitextNodeFactoryImpl;
import org.sweble.wikitext.parser.nodes.WtNode;
import org.sweble.wikitext.parser.nodes.WtNodeList;
import org.sweble.wikitext.parser.nodes.WtXmlAttribute;
import org.sweble.wikitext.parser.nodes.WtXmlElement;

import de.fau.cs.osr.ptk.common.Warning;

public final class EngineNodeFactoryImpl
		extends
			WikitextNodeFactoryImpl
		implements
			EngineNodeFactory
{
	public EngineNodeFactoryImpl(WikiConfig wikiConfig)
	{
		super(wikiConfig.getParserConfig());
	}
	
	// =========================================================================
	
	@Override
	public EngNowiki nowiki(String text)
	{
		return new EngNowiki(text);
	}
	
	@Override
	public EngSoftErrorNode softError(String message)
	{
		return softError(text(message));
	}
	
	@Override
	public EngSoftErrorNode softError(WtNode content)
	{
		return new EngSoftErrorNode(
				"strong",
				attrs(list(attr(
						"class",
						value(list(text("error")))))),
				body(list(content)));
	}
	
	@Override
	public EngSoftErrorNode softError(WtNodeList content, Exception e)
	{
		EngSoftErrorNode error = softError(content);
		error.setAttribute("exception", e);
		return error;
	}
	
	@Override
	public EngCompiledPage compiledPage(
			EngPage page,
			List<Warning> warnings,
			CompilerLog log)
	{
		return new EngCompiledPage(page, warnings, log);
	}
	
	@Override
	public EngCompiledPage compiledPage(
			EngPage page,
			List<Warning> warnings,
			WtEntityMap entityMap,
			CompilerLog log)
	{
		return new EngCompiledPage(page, warnings, entityMap, log);
	}
	
	@Override
	public EngPage page(WtNodeList content)
	{
		return new EngPage(content);
	}
	
	// =========================================================================
	
	@Override
	public <T extends WtXmlElement> T addCssClass(T elem, String cssClass)
	{
		if (elem.getXmlAttributes().isEmpty())
			elem.setXmlAttributes(attrs(list()));
		
		for (WtNode attr : elem.getXmlAttributes())
		{
			if (attr == null || !(attr instanceof WtXmlAttribute))
				continue;
			
			WtXmlAttribute a = (WtXmlAttribute) attr;
			if (!a.getName().equals("class"))
				continue;
			
			if (!a.hasValue())
				a.setValue(value(list()));
			a.getValue().add(text(" " + cssClass));
		}
		
		return elem;
	}
}
