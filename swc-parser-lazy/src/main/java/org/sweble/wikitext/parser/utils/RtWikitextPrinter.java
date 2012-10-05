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

import org.sweble.wikitext.parser.WtRtData;
import org.sweble.wikitext.parser.nodes.WtContentNode;
import org.sweble.wikitext.parser.nodes.WtNode;
import org.sweble.wikitext.parser.nodes.WtStringNode;
import org.sweble.wikitext.parser.nodes.WtText;

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
		visit(node);
	}
	
	protected void visit(WtNode node)
	{
		switch (node.getNodeType())
		{
			case WtNode.NT_TEXT:
				printText((WtText) node);
				break;
			
			case WtNode.NT_NODE_LIST:
				printNodeList(node);
				break;
			
			default:
				WtRtData rtd = node.getRtd();
				if (node instanceof WtStringNode)
				{
					printStringContentNode(rtd, (WtStringNode) node);
				}
				else if (node instanceof WtContentNode)
				{
					printContentNode(rtd, (WtContentNode) node);
				}
				else
				{
					printAnyOtherNode(rtd, node);
				}
				break;
		}
	}
	
	// =========================================================================
	
	protected void printText(WtText text)
	{
		w.print(text.getContent());
	}
	
	protected void printNodeList(WtNode node)
	{
		iterate(node);
	}
	
	protected void printContentNode(
			WtRtData rtd,
			WtContentNode contentNode)
	{
		if (rtd != null)
		{
			printRtd(rtd.getField(0));
			printNodeList(contentNode);
			printRtd(rtd.getField(1));
		}
		else
		{
			printNodeList(contentNode);
		}
	}
	
	protected void printStringContentNode(
			WtRtData rtd,
			WtStringNode contentNode)
	{
		if (rtd != null)
		{
			printRtd(rtd.getField(0));
		}
		else
		{
			w.print(contentNode.getContent());
		}
	}
	
	protected void printAnyOtherNode(WtRtData rtd, WtNode node)
	{
		if (rtd != null)
		{
			int i = 0;
			for (WtNode n : node)
			{
				printRtd(rtd.getField(i++));
				// FIXME: Remove this check!
				if (n != null)
					go(n);
			}
			printRtd(rtd.getField(i));
		}
		else
		{
			printNodeList(node);
		}
	}
	
	// =========================================================================
	
	protected void printRtd(Object[] fields)
	{
		for (Object o : fields)
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
	
	protected void iterate(WtNode node)
	{
		for (WtNode c : node)
			go(c);
	}
}
