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

import org.sweble.wikitext.engine.ExpansionFrame;
import org.sweble.wikitext.engine.IllegalArgumentsWarning;
import org.sweble.wikitext.engine.IllegalNameWarning;
import org.sweble.wikitext.engine.IllegalPagenameWarning;
import org.sweble.wikitext.engine.PageTitle;
import org.sweble.wikitext.engine.ParserFunctionBase;
import org.sweble.wikitext.engine.config.Interwiki;
import org.sweble.wikitext.lazy.LinkTargetException;
import org.sweble.wikitext.lazy.parser.WarningSeverity;
import org.sweble.wikitext.lazy.preprocessor.Template;
import org.sweble.wikitext.lazy.utils.StringConversionException;
import org.sweble.wikitext.lazy.utils.StringConverter;

import de.fau.cs.osr.ptk.common.ast.AstNode;
import de.fau.cs.osr.ptk.common.ast.Text;

public class ParserFunctionFullurl
        extends
            ParserFunctionBase
{
	private static final long serialVersionUID = 1L;
	
	public ParserFunctionFullurl()
	{
		super("fullurl");
	}
	
	@Override
	public AstNode invoke(Template template, ExpansionFrame preprocessorFrame, LinkedList<AstNode> args)
	{
		if (args.size() < 1)
		{
			preprocessorFrame.fileWarning(
			        new IllegalArgumentsWarning(
			                WarningSeverity.NORMAL,
			                getClass(),
			                "Parser function was called with too few arguments!",
			                template));
			return template;
		}
		else if (args.size() > 2)
		{
			preprocessorFrame.fileWarning(
			        new IllegalArgumentsWarning(
			                WarningSeverity.NONE,
			                getClass(),
			                "Parser function was called with too many arguments!",
			                template));
		}
		
		AstNode titleNode = args.get(0);
		AstNode expTitleNode = preprocessorFrame.expand(titleNode);
		
		String titleStr;
		try
		{
			titleStr = StringConverter.convert(expTitleNode).trim();
		}
		catch (StringConversionException e1)
		{
			preprocessorFrame.fileWarning(
			        new IllegalNameWarning(
			                WarningSeverity.NORMAL,
			                getClass(),
			                expTitleNode));
			return template;
		}
		
		PageTitle title;
		try
		{
			title = PageTitle.make(preprocessorFrame.getWikiConfig(), titleStr);
		}
		catch (LinkTargetException e)
		{
			preprocessorFrame.fileWarning(
			        new IllegalPagenameWarning(
			                WarningSeverity.NORMAL,
			                getClass(),
			                titleNode,
			                titleStr));
			return template;
		}
		
		String result;
		Interwiki iw = title.getInterwikiLink();
		if (iw != null)
		{
			result = iw.getUrl();
			// FIXME: We have to replace a placeholder for interwiki URLs
		}
		else
		{
			result = preprocessorFrame.getWikiConfig().getWikiUrl();
		}
		
		Text text = null;
		
		if (args.size() >= 2)
		{
			AstNode queryNode = preprocessorFrame.expand(args.get(1));
			
			String queryStr;
			try
			{
				queryStr = StringConverter.convert(queryNode).trim();
				
				// FIXME: We must use an API URL here, not the default wiki URL
				result += title.getLinkString();
				result += "&" + queryStr;
				
				text = new Text(result);
			}
			catch (StringConversionException e)
			{
				preprocessorFrame.fileWarning(
				        new IllegalNameWarning(
				                WarningSeverity.NORMAL,
				                getClass(),
				                expTitleNode));
			}
		}
		else
		{
			// FIXME: We have to properly append the title to the URL 
			//        (separator char? query argument?). Maybe also use a 
			//        placeholder in wiki URL?
			
			result += title.getLinkString();
			
			text = new Text(result);
		}
		
		return text;
	}
}
