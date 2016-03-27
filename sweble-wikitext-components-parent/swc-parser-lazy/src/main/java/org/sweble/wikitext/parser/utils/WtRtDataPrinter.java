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

import java.io.StringWriter;
import java.io.Writer;

import org.sweble.wikitext.parser.WtRtData;
import org.sweble.wikitext.parser.nodes.WtContentNode;
import org.sweble.wikitext.parser.nodes.WtNode;
import org.sweble.wikitext.parser.nodes.WtNodeList;
import org.sweble.wikitext.parser.nodes.WtStringNode;
import org.sweble.wikitext.parser.nodes.WtText;

import de.fau.cs.osr.utils.PrinterBase;

public class WtRtDataPrinter
{
	protected void iterate(WtNode node)
	{
		for (WtNode c : node)
			dispatch(c);
	}

	protected void dispatch(WtNode node)
	{
		switch (node.getNodeType())
		{
			case WtNode.NT_TEXT:
				printText((WtText) node);
				break;

			case WtNode.NT_NODE_LIST:
				printNodeList((WtNodeList) node);
				break;

			default:
				WtRtData rtd = node.getRtd();
				if (node instanceof WtStringNode)
				{
					printStringNode(rtd, (WtStringNode) node);
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
		p.verbatim(text.getContent());
	}

	protected void printNodeList(WtNodeList node)
	{
		iterate(node);
	}

	protected void printContentNode(WtRtData rtd, WtContentNode contentNode)
	{
		if (rtd != null)
		{
			if (!rtd.isSuppress())
			{
				printRtd(rtd.getField(0));
				iterate(contentNode);
				printRtd(rtd.getField(1));
			}
		}
		else
		{
			iterate(contentNode);
		}
	}

	protected void printStringNode(WtRtData rtd, WtStringNode contentNode)
	{
		if (rtd != null)
		{
			if (!rtd.isSuppress())
				printRtd(rtd.getField(0));
		}
		else
		{
			p.verbatim(contentNode.getContent());
		}
	}

	protected void printAnyOtherNode(WtRtData rtd, WtNode node)
	{
		if (rtd != null)
		{
			if (!rtd.isSuppress())
			{
				int i = 0;
				for (WtNode n : node)
				{
					printRtd(rtd.getField(i++));
					dispatch(n);
				}
				printRtd(rtd.getField(i));
			}
		}
		else
		{
			iterate(node);
		}
	}

	protected void printRtd(Object[] fields)
	{
		for (Object o : fields)
		{
			if (o instanceof WtNode)
			{
				dispatch((WtNode) o);
			}
			else
			{
				p.verbatim(String.valueOf(o));
			}
		}
	}

	// =========================================================================

	public static String print(WtNode node)
	{
		return WtRtDataPrinter.print(new StringWriter(), node).toString();
	}

	public static Writer print(Writer writer, WtNode node)
	{
		new WtRtDataPrinter(writer).go(node);
		return writer;
	}

	// =========================================================================

	protected final PrinterBase p;

	protected WtRtDataPrinter(Writer writer)
	{
		this.p = new PrinterBase(writer);
		this.p.setMemoize(false);
	}

	protected void go(WtNode node)
	{
		dispatch(node);
		p.flush();
	}
}
