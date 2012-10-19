package org.sweble.wikitext.engine.nodes;

import org.sweble.wikitext.parser.nodes.WtNode;
import org.sweble.wikitext.parser.nodes.WtTagExtension;
import org.sweble.wikitext.parser.nodes.WtXmlAttribute;
import org.sweble.wikitext.parser.nodes.WtXmlAttributes;

import de.fau.cs.osr.ptk.common.ast.RtData;

public final class EngineRtData
{
	public static EngNowiki set(EngNowiki n)
	{
		if (n.getContent().isEmpty())
		{
			n.setRtd("<nowiki />");
		}
		else
		{
			n.setRtd("<nowiki>", n.getContent(), "</nowiki>");
		}
		return n;
	}
	
	public static EngSoftErrorNode set(EngSoftErrorNode n)
	{
		genAttrRtd(n.getXmlAttributes());
		if (n.hasBody())
		{
			n.setRtd("<strong", RtData.SEP, ">", RtData.SEP, "</strong>");
		}
		else
		{
			n.setRtd("<strong", RtData.SEP, " />", RtData.SEP);
		}
		return n;
	}
	
	public static EngCompiledPage set(EngCompiledPage n)
	{
		return n;
	}
	
	public static EngPage set(EngPage n)
	{
		return n;
	}
	
	public static WtTagExtension set(WtTagExtension n)
	{
		genAttrRtd(n.getXmlAttributes());
		if (n.hasBody())
		{
			n.setRtd(
					'<', n.getName(), RtData.SEP,
					'>', RtData.SEP,
					"</", n.getName(), '>');
		}
		else
		{
			n.setRtd(
					'<', n.getName(), RtData.SEP,
					" />", RtData.SEP);
		}
		return n;
	}
	
	// =========================================================================
	
	private static void genAttrRtd(WtXmlAttributes attrs)
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
