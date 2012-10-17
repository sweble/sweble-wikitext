package org.sweble.wikitext.engine.utils;

import java.util.ListIterator;

import org.sweble.wikitext.engine.nodes.EngNode;
import org.sweble.wikitext.engine.nodes.EngNowiki;
import org.sweble.wikitext.parser.ParserConfig;
import org.sweble.wikitext.parser.nodes.WtNode;
import org.sweble.wikitext.parser.nodes.WtNodeList;
import org.sweble.wikitext.parser.nodes.WtText;
import org.sweble.wikitext.parser.utils.AstTextUtilsImpl;
import org.sweble.wikitext.parser.utils.StringConversionException;

import de.fau.cs.osr.utils.StringUtils;

public class EngineAstTextUtilsImpl
		extends
			AstTextUtilsImpl
		implements
			EngineAstTextUtils
{
	public EngineAstTextUtilsImpl(ParserConfig parserConfig)
	{
		super(parserConfig);
	}
	
	// =========================================================================
	
	@Override
	public WtNode trim(WtNode n)
	{
		return trimRight(trimLeft(n));
	}
	
	@Override
	public WtNode trimLeft(WtNode n)
	{
		if (n.isList())
		{
			WtNodeList l = (WtNodeList) n;
			ListIterator<WtNode> i = l.listIterator();
			outer: while (i.hasNext())
			{
				WtNode item = i.next();
				switch (item.getNodeType())
				{
					case WtNode.NT_TEXT:
						WtText t = (WtText) item;
						String text = t.getContent();
						if (text.isEmpty())
						{
							i.remove();
							continue;
						}
						
						String trimmed = StringUtils.trimLeft(text);
						if (trimmed.equals(text))
							break outer;
						
						if (trimmed.isEmpty())
						{
							i.remove();
							continue;
						}
						else
						{
							t.setContent(trimmed);
							break outer;
						}
						
					case WtNode.NT_IGNORED:
					case WtNode.NT_XML_COMMENT:
						continue;
						
					default:
						break outer;
				}
			}
			return n;
		}
		else if (n.getNodeType() == WtNode.NT_TEXT)
		{
			WtText t = (WtText) n;
			t.setContent(StringUtils.trimLeft(t.getContent()));
			return n;
		}
		else
		{
			return n;
		}
	}
	
	@Override
	public WtNode trimRight(WtNode n)
	{
		if (n.isList())
		{
			WtNodeList l = (WtNodeList) n;
			ListIterator<WtNode> i = l.listIterator(l.size());
			outer: while (i.hasPrevious())
			{
				WtNode item = i.previous();
				switch (item.getNodeType())
				{
					case WtNode.NT_TEXT:
						WtText t = (WtText) item;
						String text = t.getContent();
						if (text.isEmpty())
						{
							i.remove();
							continue;
						}
						
						String trimmed = StringUtils.trimRight(text);
						if (trimmed.equals(text))
							break outer;
						
						if (trimmed.isEmpty())
						{
							i.remove();
							continue;
						}
						else
						{
							t.setContent(trimmed);
							break outer;
						}
						
					case WtNode.NT_IGNORED:
					case WtNode.NT_XML_COMMENT:
						continue;
						
					default:
						break outer;
				}
			}
			return n;
		}
		else if (n.getNodeType() == WtNode.NT_TEXT)
		{
			WtText t = (WtText) n;
			t.setContent(StringUtils.trimRight(t.getContent()));
			return n;
		}
		else
		{
			return n;
		}
	}
	
	// =========================================================================
	
	@Override
	public String astToText(WtNode node) throws StringConversionException
	{
		return super.astToText(node, new SimpleEngineStringConverter());
	}
	
	@Override
	public String astToText(WtNode node, int... options) throws StringConversionException
	{
		return super.astToText(node, new SimpleEngineStringConverter(options));
	}
	
	@Override
	public PartialConversion astToTextPartial(WtNode node)
	{
		return super.astToTextPartial(node, new PartialEngineStringConverter());
	}
	
	@Override
	public PartialConversion astToTextPartial(WtNode node, int... options)
	{
		return super.astToTextPartial(node, new PartialEngineStringConverter(options));
	}
	
	// =========================================================================
	
	private class SimpleEngineStringConverter
			extends
				SimpleStringConverter
	{
		public SimpleEngineStringConverter()
		{
		}
		
		public SimpleEngineStringConverter(int[] options)
		{
			super(options);
		}
		
		@Override
		public void dispatch(WtNode node, int nodeType) throws StringConversionException
		{
			if (nodeType == EngNode.NT_NOWIKI)
			{
				visit((EngNowiki) node);
			}
			else
			{
				super.dispatch(node, nodeType);
			}
		}
		
		protected void visit(EngNowiki node)
		{
			b.append(node.getContent());
		}
	}
	
	// =========================================================================
	
	private class PartialEngineStringConverter
			extends
				PartialStringConverter
	{
		public PartialEngineStringConverter()
		{
		}
		
		public PartialEngineStringConverter(int[] options)
		{
			super(options);
		}
		
		@Override
		public void dispatch(WtNode node, int nodeType) throws StringConversionException
		{
			if (nodeType == EngNode.NT_NOWIKI)
			{
				visit((EngNowiki) node);
			}
			else
			{
				super.dispatch(node, nodeType);
			}
		}
		
		protected void visit(EngNowiki node)
		{
			b.append(node.getContent());
		}
	}
}
