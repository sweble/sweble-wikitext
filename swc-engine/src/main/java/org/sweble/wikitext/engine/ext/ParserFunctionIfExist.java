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

import java.util.List;

import org.sweble.wikitext.engine.ExpansionFrame;
import org.sweble.wikitext.engine.PageTitle;
import org.sweble.wikitext.engine.ParserFunctionBase;
import org.sweble.wikitext.lazy.LinkTargetException;
import org.sweble.wikitext.lazy.preprocessor.Template;
import org.sweble.wikitext.lazy.utils.StringConversionException;
import org.sweble.wikitext.lazy.utils.StringConverter;
import org.sweble.wikitext.lazy.utils.TextUtils;

import de.fau.cs.osr.ptk.common.ast.AstNode;
import de.fau.cs.osr.ptk.common.ast.NodeList;

public class ParserFunctionIfExist
		extends
			ParserFunctionBase
{
	private static final long serialVersionUID = 1L;
	
	public ParserFunctionIfExist()
	{
		super("#ifexist");
	}
	
	@Override
	public AstNode invoke(
			Template template,
			ExpansionFrame preprocessorFrame,
			List<? extends AstNode> args)
	{
		if (args.size() < 2)
			return new NodeList();
		
		AstNode test = preprocessorFrame.expand(args.get(0));
		
		String testStr = null;
		try
		{
			testStr = StringConverter.convert(test).trim();
		}
		catch (StringConversionException e1)
		{
			// We have to convert the entire argument to a string to create a page name from it.
			return template;
		}
		
		PageTitle pageTitle;
		try
		{
			pageTitle = PageTitle.make(preprocessorFrame.getWikiConfig(), testStr);
		}
		catch (LinkTargetException e)
		{
			// A page with an illegal name cannot exist.
			return template;
		}
		
		boolean eval;
		try
		{
			eval = preprocessorFrame.existsPage(pageTitle);
		}
		catch (Exception e)
		{
			// Interpret an error while testing for existence as non-existence.
			eval = false;
		}
		
		AstNode result;
		if (eval)
		{
			result = args.get(1);
		}
		else
		{
			result = null;
			if (args.size() >= 3)
				result = args.get(2);
		}
		
		if (result != null)
			result = preprocessorFrame.expand(result);
		
		if (result == null)
			result = new NodeList();
		
		if (result.isNodeType(AstNode.NT_NODE_LIST))
			return TextUtils.trim((NodeList) result);
		
		return result;
	}
}
