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
import org.sweble.wikitext.engine.utils.EngineTextUtils;
import org.sweble.wikitext.lazy.preprocessor.Template;
import org.sweble.wikitext.lazy.utils.StringConversionException;
import org.sweble.wikitext.lazy.utils.StringConverter;

import de.fau.cs.osr.ptk.common.ast.AstNode;
import de.fau.cs.osr.ptk.common.ast.NodeList;
import de.fau.cs.osr.ptk.common.ast.Text;

public class ParserFunctionSwitch
		extends
			ParserFunctionBase
{
	private static final long serialVersionUID = 1L;
	
	public ParserFunctionSwitch()
	{
		super("#switch");
	}
	
	@Override
	public AstNode invoke(
			Template template,
			ExpansionFrame preprocessorFrame,
			LinkedList<AstNode> args)
	{
		if (args.size() < 1)
			return new NodeList();
		
		AstNode arg0 = preprocessorFrame.expand(args.get(0));
		
		String cmp = null;
		try
		{
			cmp = StringConverter.convert(arg0).trim();
		}
		catch (StringConversionException e)
		{
			// FIXME: Do recursive equality check
		}
		
		boolean numbers = false;
		double icmp = -1;
		if (cmp != null)
		{
			try
			{
				icmp = Double.parseDouble(cmp);
				numbers = true;
			}
			catch (NumberFormatException e)
			{
				numbers = false;
			}
		}
		
		boolean found = false;
		
		AstNode result = null;
		for (int i = 1; i < args.size(); ++i)
		{
			NodeList after = null;
			NodeList before = new NodeList();
			if (args.get(i).isNodeType(AstNode.NT_NODE_LIST))
			{
				for (AstNode c : args.get(i))
				{
					if (after == null)
					{
						if (c.isNodeType(AstNode.NT_TEXT))
						{
							String text = ((Text) c).getContent();
							
							int j = text.indexOf('=');
							if (j != -1)
							{
								before.add(new Text(text.substring(0, j)));
								after = new NodeList(new Text(text.substring(j + 1)));
							}
							else
							{
								before.add(c);
							}
						}
						else
						{
							before.add(c);
						}
					}
					else
					{
						after.add(c);
					}
				}
			}
			else
			{
				AstNode c = args.get(i);
				if (c.isNodeType(AstNode.NT_TEXT))
				{
					String text = ((Text) c).getContent();
					
					int j = text.indexOf('=');
					if (j != -1)
					{
						before.add(new Text(text.substring(0, j)));
						after = new NodeList(new Text(text.substring(j + 1)));
					}
					else
					{
						before.add(c);
					}
				}
			}
			
			if (!found)
			{
				before = (NodeList) preprocessorFrame.expand(before);
				
				String cmp2;
				try
				{
					cmp2 = StringConverter.convert(before).trim();
				}
				catch (StringConversionException e)
				{
					// FIXME: Do recursive equality check
					continue;
				}
				
				if (cmp2.equals("#default"))
				{
					result = after;
					break;
				}
				
				if (numbers)
				{
					double icmp2 = -1;
					try
					{
						icmp2 = Double.parseDouble(cmp2);
						if (icmp == icmp2)
							found = true;
					}
					catch (NumberFormatException e)
					{
					}
				}
				
				if (!found && cmp != null && cmp.equals(cmp2))
					found = true;
			}
			
			if (found && after != null)
			{
				result = after;
				break;
			}
		}
		
		if (result != null)
			result = EngineTextUtils.trim(preprocessorFrame.expand(result));
		
		if (result == null)
			result = new NodeList();
		
		return result;
	}
}
