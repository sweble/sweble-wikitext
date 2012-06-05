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

package org.sweble.wikitext.lazy.utils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

import org.sweble.wikitext.lazy.parser.RtData;

import de.fau.cs.osr.ptk.common.ast.AstNode;
import de.fau.cs.osr.ptk.common.ast.NodeList;
import de.fau.cs.osr.ptk.common.ast.StringContentNode;

public class RtWikitextPrinter
{
	protected PrintWriter w;
	
	// =========================================================================
	
	public RtWikitextPrinter(Writer writer)
	{
		this.w = new PrintWriter(writer);
	}
	
	// =========================================================================
	
	public static String print(AstNode node)
	{
		StringWriter writer = new StringWriter();
		new RtWikitextPrinter(writer).go(node);
		return writer.toString();
	}
	
	public static Writer print(Writer writer, AstNode node)
	{
		new RtWikitextPrinter(writer).go(node);
		return writer;
	}
	
	// =========================================================================
	
	protected void go(AstNode node)
	{
		switch (node.getNodeType())
		{
			case AstNode.NT_NODE_LIST:
			{
				for (AstNode c : (NodeList) node)
					go(c);
				
				break;
			}
			
			default:
			{
				RtData rtd = (RtData) node.getAttribute("RTD");
				if (rtd != null)
				{
					int i = 0;
					for (AstNode n : node)
					{
						printRtd(rtd.getRts()[i++]);
						if (n != null)
							go(n);
					}
					printRtd(rtd.getRts()[i]);
				}
				else
				{
					if (node instanceof StringContentNode)
					{
						w.print(((StringContentNode) node).getContent());
					}
					else
					{
						for (AstNode n : node)
						{
							if (n != null)
								go(n);
						}
					}
				}
				break;
			}
		}
	}
	
	protected void printRtd(Object[] objects)
	{
		if (objects != null)
		{
			for (Object o : objects)
			{
				if (o instanceof AstNode)
				{
					go((AstNode) o);
				}
				else
				{
					w.print(o);
				}
			}
		}
	}
}
