/**
 * Copyright 2011 The Open Source Research Group,
 *                University of Erlangen-Nürnberg
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.sweble.wikitext.engine.ext;

import java.util.LinkedList;
import java.util.regex.Pattern;

import org.sweble.wikitext.engine.ExpansionFrame;
import org.sweble.wikitext.engine.ParserFunctionBase;
import org.sweble.wikitext.lazy.preprocessor.Template;
import org.sweble.wikitext.lazy.utils.TextUtils;
import org.sweble.wikitext.lazy.utils.WikitextPrinter;

import de.fau.cs.osr.ptk.common.ast.AstNode;
import de.fau.cs.osr.ptk.common.ast.NodeList;

public class ParserFunctionIfError
        extends
            ParserFunctionBase
{
	private static final long serialVersionUID = 1L;
	
	private static final Pattern classErrorPattern =
	        Pattern.compile("<[^<>\\s]*?\\sclass\\s*=\\s*\"error\"[^<>]*>");
	
	public ParserFunctionIfError()
	{
		super("#iferror");
	}
	
	@Override
	public AstNode invoke(Template template, ExpansionFrame preprocessorFrame, LinkedList<AstNode> args)
	{
		// FIXME: Complain about wrong number of arguments 1 > count(args) > 3 
		if (args.size() < 1)
			return new NodeList();
		
		AstNode test = preprocessorFrame.expand(args.get(0));
		
		boolean wasError = searchForError(test);
		
		AstNode result;
		if (wasError)
		{
			result = null;
			if (args.size() >= 2)
				result = args.get(1);
		}
		else
		{
			result = test;
			if (args.size() >= 3)
				result = args.get(2);
		}
		
		if (result != test && result != null)
			result = preprocessorFrame.expand(result);
		
		if (result == null)
			result = new NodeList();
		
		if (result.isNodeType(AstNode.NT_NODE_LIST))
			return TextUtils.trim((NodeList) result);
		
		return result;
	}
	
	private boolean searchForError(AstNode test)
	{
		String s = WikitextPrinter.print(test);
		return classErrorPattern.matcher(s).find();
	}
	
	/* FIXME: This would only work if we had also parsed the arguments. 
	 * However, since the arguments are only preprocessed, no XML elements and
	 * such were recognized.
	 * 
	private boolean searchForError(AstNode test)
	{
		if (test.isNodeType(AstNodeTypes.NT_XML_ELEMENT))
		{
			XmlElement element = (XmlElement) test;
			for (AstNode a : element.getXmlAttributes())
			{
				if (a.isNodeType(AstNodeTypes.NT_XML_ATTRIBUTE))
				{
					XmlAttribute attr = (XmlAttribute) a;
					if (attr.getName().trim().equalsIgnoreCase("class") &&
					        attr.getHasValue())
					{
						try
						{
							String s = StringConverter.convert(attr.getValue());
							if (s.trim().equalsIgnoreCase("error"))
								return true;
						}
						catch (StringConversionException ex)
						{
						}
					}
				}
			}
		}
		
		for (AstNode n : test)
		{
			if (searchForError(n))
				return true;
		}
		
		return false;
	}
	*/
}
