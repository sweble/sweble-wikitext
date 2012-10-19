package org.sweble.wikitext.engine.nodes;

import java.util.List;

import org.sweble.wikitext.engine.config.WikiConfig;
import org.sweble.wikitext.engine.lognodes.CompilerLog;
import org.sweble.wikitext.parser.WtEntityMap;
import org.sweble.wikitext.parser.nodes.WikitextNodeFactoryImpl;
import org.sweble.wikitext.parser.nodes.WtNode;
import org.sweble.wikitext.parser.nodes.WtNodeList;
import org.sweble.wikitext.parser.nodes.WtTagExtension;
import org.sweble.wikitext.parser.nodes.WtTagExtensionBody;
import org.sweble.wikitext.parser.nodes.WtXmlAttribute;
import org.sweble.wikitext.parser.nodes.WtXmlAttributes;
import org.sweble.wikitext.parser.nodes.WtXmlElement;

import de.fau.cs.osr.ptk.common.Warning;
import de.fau.cs.osr.ptk.common.ast.RtData;

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
		EngNowiki nw = new EngNowiki(text);
		
		if (text.isEmpty())
		{
			nw.setRtd("<nowiki />");
		}
		else
		{
			nw.setRtd("<nowiki>", text, "</nowiki>");
		}
		
		return nw;
	}
	
	@Override
	public EngSoftErrorNode softError(String message)
	{
		return softError(text(message));
	}
	
	@Override
	public EngSoftErrorNode softError(WtNode content)
	{
		EngSoftErrorNode se = new EngSoftErrorNode(
				"strong",
				attrs(list(attr(
						"class",
						value(list(text("error")))))),
				body(list(content)));
		
		se.setRtd("<strong", RtData.SEP, ">", RtData.SEP, "</strong>");
		genAttrRtd(se.getXmlAttributes());
		
		return se;
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
	
	@Override
	public WtTagExtension tagExt(String name, WtXmlAttributes xmlAttributes)
	{
		WtTagExtension tagExt = super.tagExt(name, xmlAttributes);
		tagExt.setRtd(
				'<', tagExt.getName(), RtData.SEP,
				" />", RtData.SEP);
		genAttrRtd(xmlAttributes);
		return tagExt;
	}
	
	@Override
	public WtTagExtension tagExt(
			String name,
			WtXmlAttributes xmlAttributes,
			WtTagExtensionBody body)
	{
		WtTagExtension tagExt = super.tagExt(name, xmlAttributes, body);
		tagExt.setRtd(
				'<', tagExt.getName(), RtData.SEP,
				'>', RtData.SEP,
				"</", tagExt.getName(), '>');
		genAttrRtd(xmlAttributes);
		return tagExt;
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
	
	// =========================================================================
	
	private void genAttrRtd(WtXmlAttributes attrs)
	{
		for (WtNode attr : attrs)
		{
			if (!attr.isNodeType(WtNode.NT_XML_ATTRIBUTE))
				continue;
			WtXmlAttribute a = (WtXmlAttribute) attr;
			if (a.hasValue())
			{
				attr.setRtd(" ", a.getName(), "=\"", RtData.SEP, "\"");
			}
			else
			{
				attr.setRtd(" ", a.getName());
			}
		}
	}
}
