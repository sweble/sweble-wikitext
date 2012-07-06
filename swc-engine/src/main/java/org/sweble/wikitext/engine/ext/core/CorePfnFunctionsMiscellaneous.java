package org.sweble.wikitext.engine.ext.core;

import static org.sweble.wikitext.lazy.utils.AstBuilder.*;

import java.util.List;
import java.util.ListIterator;

import org.sweble.wikitext.engine.ExpansionFrame;
import org.sweble.wikitext.engine.PfnArgumentMode;
import org.sweble.wikitext.engine.astwom.Toolbox;
import org.sweble.wikitext.engine.config.ParserFunctionGroup;
import org.sweble.wikitext.lazy.AstNodeTypes;
import org.sweble.wikitext.lazy.preprocessor.TagExtension;
import org.sweble.wikitext.lazy.preprocessor.Template;
import org.sweble.wikitext.lazy.preprocessor.TemplateArgument;
import org.sweble.wikitext.lazy.utils.RtWikitextPrinter;
import org.sweble.wikitext.lazy.utils.StringConversionException;
import org.sweble.wikitext.lazy.utils.StringConverter;

import de.fau.cs.osr.ptk.common.ast.AstNode;
import de.fau.cs.osr.ptk.common.ast.NodeList;
import de.fau.cs.osr.utils.XmlGrammar;

public class CorePfnFunctionsMiscellaneous
		extends
			ParserFunctionGroup
{
	private static final long serialVersionUID = 1L;
	
	// =========================================================================
	
	protected CorePfnFunctionsMiscellaneous()
	{
		super("Core - Parser Functions - Miscellaneous");
		addParserFunction(new TagPfn());
	}
	
	public static CorePfnFunctionsMiscellaneous group()
	{
		return new CorePfnFunctionsMiscellaneous();
	}
	
	// =========================================================================
	// ==
	// == TODO: {{#language:language code}}
	// ==       {{#language:ar}}
	// ==       {{#language:language code|target language code}}
	// ==       {{#language:ar|en}}
	// == TODO: {{#special:special page name}}
	// ==       {{#special:userlogin}}
	// == TODO: {{#speciale:special page name}}
	// ==       {{#speciale:userlogin}}
	// ==
	// =========================================================================
	
	// =========================================================================
	// ==
	// == TODO: {{#tag:tagname
	// ==           |content
	// ==           |attribute1=value1
	// ==           |attribute2=value2
	// ==       }}
	// ==
	// =========================================================================
	
	public static final class TagPfn
			extends
				CorePfnFunction
	{
		private static final long serialVersionUID = 1L;
		
		public TagPfn()
		{
			super(PfnArgumentMode.TEMPLATE_ARGUMENTS, "tag");
		}
		
		@Override
		public AstNode invoke(
				Template pfn,
				ExpansionFrame frame,
				List<? extends AstNode> argsValues)
		{
			if (argsValues.size() < 2)
				return pfn;
			
			TemplateArgument nameNode = (TemplateArgument) argsValues.get(0);
			
			String nameStr;
			try
			{
				AstNode expNameNode = frame.expand(nameNode.getValue());
				nameStr = StringConverter.convert(expNameNode).trim();
			}
			catch (StringConversionException e)
			{
				return pfn;
			}
			
			// FIXME: Meld 'name=' part into value
			// FIXME: Do something about the "remove comments" hack
			TemplateArgument bodyNode = (TemplateArgument) argsValues.get(1);
			AstNode expValueNode = frame.expand(bodyNode.getValue());
			expValueNode = stripComments(expValueNode);
			String bodyStr = RtWikitextPrinter.print(expValueNode);
			
			TagExtension tagExt = astTagExtension()
					.withName(nameStr)
					.withBody(bodyStr)
					.build();
			
			NodeList attribs = astList();
			for (int i = 2; i < argsValues.size(); ++i)
			{
				TemplateArgument arg = (TemplateArgument) argsValues.get(i);
				AstNode argNameNode = frame.expand(arg.getName());
				AstNode argValueNode = frame.expand(arg.getValue());
				if (argNameNode == null || argValueNode == null)
					continue;
				
				String argName;
				String argValue;
				try
				{
					argName = StringConverter.convert(argNameNode);
					argValue = StringConverter.convert(argValueNode);
				}
				catch (StringConversionException e)
				{
					continue;
				}
				
				if (!XmlGrammar.xmlName().matcher(argName).matches())
					continue;
				
				attribs.add(astXmlAttrib().withName(argName).withValue(argValue).build());
			}
			
			tagExt.setXmlAttributes(attribs);
			
			Toolbox.addRtData(tagExt);
			
			return frame.expand(tagExt);
		}
		
		private AstNode stripComments(AstNode n)
		{
			ListIterator<AstNode> i = n.listIterator();
			while (i.hasNext())
			{
				AstNode child = i.next();
				switch (child.getNodeType())
				{
					case AstNodeTypes.NT_XML_COMMENT:
					case AstNodeTypes.NT_IGNORED:
						i.remove();
						break;
					default:
						if (!child.isEmpty())
							stripComments(child);
				}
			}
			return n;
		}
	}
}
