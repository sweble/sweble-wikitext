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

package org.sweble.wikitext.engine.ext.parser_functions;

import static org.sweble.wikitext.parser.utils.AstBuilder.astText;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.sweble.wikitext.engine.ExpansionFrame;
import org.sweble.wikitext.engine.PageTitle;
import org.sweble.wikitext.parser.LinkTargetException;
import org.sweble.wikitext.parser.nodes.Template;
import org.sweble.wikitext.parser.nodes.WtNode;
import org.sweble.wikitext.parser.utils.StringConversionException;
import org.sweble.wikitext.parser.utils.StringConverter;

public class ParserFunctionTitleparts
		extends
			ParserFunctionsExtPfn
{
	private static final long serialVersionUID = 1L;
	
	public ParserFunctionTitleparts()
	{
		super("titleparts");
	}
	
	@Override
	public WtNode invoke(
			Template pfn,
			ExpansionFrame frame,
			List<? extends WtNode> args)
	{
		if (args.size() < 1)
			return pfn;
		
		WtNode arg0 = frame.expand(args.get(0));
		
		if (args.size() < 2)
			return arg0;
		
		PageTitle pageTitle;
		Integer partCount = null;
		Integer firstPart = null;
		try
		{
			String titleStr = StringConverter.convert(arg0).trim();
			pageTitle = PageTitle.make(frame.getWikiConfig(), titleStr);
			
			WtNode arg1 = frame.expand(args.get(1));
			String countStr = StringConverter.convert(arg1).trim();
			try
			{
				partCount = Integer.parseInt(countStr);
			}
			catch (NumberFormatException e)
			{
			}
			
			WtNode arg2 = frame.expand(args.get(2));
			String firstStr = StringConverter.convert(arg2).trim();
			try
			{
				firstPart = Integer.parseInt(firstStr);
			}
			catch (NumberFormatException e)
			{
			}
		}
		catch (StringConversionException ee)
		{
			// We have to convert the entire argument to a string to create a page name from it.
			return pfn;
		}
		catch (LinkTargetException e)
		{
			// A page with an illegal name cannot be split properly.
			return pfn;
		}
		
		String title = pageTitle.getTitle();
		
		String[] parts = title.split("/", 25);
		
		if (partCount != null)
		{
			if (partCount < 0)
			{
				partCount = parts.length + partCount;
			}
			else if (partCount == 0)
			{
				partCount = parts.length;
			}
			if (partCount <= 0)
				return astText("");
		}
		else
			partCount = parts.length;
		
		if (firstPart != null)
		{
			if (firstPart > parts.length)
			{
				return astText("");
			}
			else if (firstPart < 0)
			{
				firstPart = parts.length + firstPart;
			}
			if (firstPart <= 0)
				firstPart = 1;
		}
		else
			firstPart = 1;
		
		int from = firstPart - 1;
		int to = Math.min(from + partCount, parts.length);
		String newTitle = StringUtils.join(Arrays.copyOfRange(parts, from, to), "/");
		
		return astText(pageTitle.newWithTitle(newTitle).getDenormalizedFullTitle());
	}
}
