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

package org.sweble.wikitext.parser.utils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

import org.sweble.wikitext.parser.RtData;
import org.sweble.wikitext.parser.nodes.WikitextNode;
import org.sweble.wikitext.parser.nodes.WtList;

import de.fau.cs.osr.ptk.common.ast.GenericStringContentNode;
import de.fau.cs.osr.ptk.common.ast.RtDataPtk;

public class RtWikitextPrinter
{
	protected PrintWriter w;
	
	// =========================================================================
	
	public RtWikitextPrinter(Writer writer)
	{
		this.w = new PrintWriter(writer);
	}
	
	// =========================================================================
	
	public static String print(WikitextNode node)
	{
		StringWriter writer = new StringWriter();
		new RtWikitextPrinter(writer).go(node);
		return writer.toString();
	}
	
	public static Writer print(Writer writer, WikitextNode node)
	{
		new RtWikitextPrinter(writer).go(node);
		return writer;
	}
	
	// =========================================================================
	
	public void go(WikitextNode node)
	{
		switch (node.getNodeType())
		{
			case WikitextNode.NT_NODE_LIST:
			{
				for (WikitextNode c : (WtList) node)
					go(c);
				
				break;
			}
			
			default:
			{
				RtData rtd = (RtData) node.getAttribute("RTD");
				if (rtd != null)
				{
					int i = 0;
					for (WikitextNode n : node)
					{
						printRtd(rtd.getRts()[i++]);
						if (n != null)
							go(n);
					}
					printRtd(rtd.getRts()[i]);
				}
				else
				{
					RtDataPtk rtd2 = (RtDataPtk) node.getProperty("rtd", null);
					if (rtd2 != null)
					{
						int i = 0;
						for (WikitextNode n : node)
						{
							printRtd2(rtd2.getField(i++));
							if (n != null)
								go(n);
						}
						printRtd2(rtd2.getField(i));
					}
					else
					{
						if (node instanceof GenericStringContentNode)
						{
							w.print(((GenericStringContentNode<WikitextNode>) node).getContent());
						}
						else
						{
							for (WikitextNode n : node)
							{
								if (n != null)
									go(n);
							}
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
				if (o instanceof WikitextNode)
				{
					go((WikitextNode) o);
				}
				else
				{
					w.print(o);
				}
			}
		}
	}
	
	protected void printRtd2(Object[] objects)
	{
		for (Object o : objects)
		{
			if (o instanceof WikitextNode)
			{
				go((WikitextNode) o);
			}
			else
			{
				w.print(o);
			}
		}
	}
}
