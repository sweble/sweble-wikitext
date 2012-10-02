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

import org.sweble.wikitext.parser.nodes.WtNode;
import org.sweble.wikitext.parser.nodes.WtNodeList;

import de.fau.cs.osr.ptk.common.ast.GenericStringContentNode;
import de.fau.cs.osr.ptk.common.ast.RtData;

public class RtWikitextPrinter
{
	protected PrintWriter w;
	
	// =========================================================================
	
	public RtWikitextPrinter(Writer writer)
	{
		this.w = new PrintWriter(writer);
	}
	
	// =========================================================================
	
	public static String print(WtNode node)
	{
		StringWriter writer = new StringWriter();
		new RtWikitextPrinter(writer).go(node);
		return writer.toString();
	}
	
	public static Writer print(Writer writer, WtNode node)
	{
		new RtWikitextPrinter(writer).go(node);
		return writer;
	}
	
	// =========================================================================
	
	public void go(WtNode node)
	{
		switch (node.getNodeType())
		{
			case WtNode.NT_NODE_LIST:
			{
				for (WtNode c : (WtNodeList) node)
					go(c);
				
				break;
			}
			
			default:
			{
				RtData rtd = node.getRtd();
				if (rtd != null)
				{
					int i = 0;
					for (WtNode n : node)
					{
						printRtd(rtd.getField(i++));
						if (n != null)
							go(n);
					}
					printRtd(rtd.getField(i));
				}
				else
				{
					RtData rtd2 = (RtData) node.getProperty("rtd", null);
					if (rtd2 != null)
					{
						int i = 0;
						for (WtNode n : node)
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
							w.print(((GenericStringContentNode<WtNode>) node).getContent());
						}
						else
						{
							for (WtNode n : node)
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
				if (o instanceof WtNode)
				{
					go((WtNode) o);
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
			if (o instanceof WtNode)
			{
				go((WtNode) o);
			}
			else
			{
				w.print(o);
			}
		}
	}
}
