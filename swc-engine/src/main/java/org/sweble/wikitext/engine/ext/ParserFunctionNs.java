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
import org.sweble.wikitext.engine.ParserFunctionBase;
import org.sweble.wikitext.engine.config.Namespace;
import org.sweble.wikitext.lazy.preprocessor.Template;
import org.sweble.wikitext.lazy.utils.StringConversionException;
import org.sweble.wikitext.lazy.utils.StringConverter;

import de.fau.cs.osr.ptk.common.ast.AstNode;
import de.fau.cs.osr.ptk.common.ast.Text;

public class ParserFunctionNs
        extends
            ParserFunctionBase
{
	private static final long serialVersionUID = 1L;
	
	public ParserFunctionNs()
	{
		super("ns");
	}
	
	@Override
	public AstNode invoke(Template template, ExpansionFrame preprocessorFrame, LinkedList<AstNode> args)
	{
		if (args.size() < 0)
			return null;
		
		AstNode arg0 = preprocessorFrame.expand(args.get(0));
		
		String arg;
		try
		{
			arg = StringConverter.convert(arg0).trim();
		}
		catch (StringConversionException e1)
		{
			return null;
		}
		
		Namespace namespace = preprocessorFrame.getWikiConfig().getNamespace(arg);
		if (namespace == null)
		{
			int ns;
			try
			{
				ns = Integer.parseInt(arg);
			}
			catch (NumberFormatException e)
			{
				return null;
			}
			
			namespace = preprocessorFrame.getWikiConfig().getNamespace(ns);
		}
		
		String result = "";
		if (namespace != null)
			result = namespace.getName();
		
		return new Text(result);
	}
}
