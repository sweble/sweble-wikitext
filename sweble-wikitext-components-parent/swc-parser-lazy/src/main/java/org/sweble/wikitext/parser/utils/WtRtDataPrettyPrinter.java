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
import org.sweble.wikitext.parser.nodes.WtIgnored;
import org.sweble.wikitext.parser.nodes.WtNewline;
import org.sweble.wikitext.parser.nodes.WtNode;
import org.sweble.wikitext.parser.nodes.WtStringNode;
import org.sweble.wikitext.parser.nodes.WtText;

public class WtRtDataPrettyPrinter
		extends
			WtPrettyPrinter
{
	public void visit(WtNewline n)
	{
		p.verbatim(n.getContent());
	}

	public void visit(WtIgnored n)
	{
		p.verbatim(n.getContent());
	}

	public void visit(WtText n)
	{
		p.verbatim(n.getContent());
	}

	// =========================================================================

	@Override
	protected Object dispatch(WtNode node)
	{
		WtRtData rtd = node.getRtd();
		if (rtd != null)
		{
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
		}
		else
		{
			super.dispatch(node);
		}
		return null;
	}

	// =========================================================================

	protected void printStringNode(WtRtData rtd, WtStringNode contentNode)
	{
		if (rtd != null)
		{
			printRtd(rtd.getField(0));
		}
		else
		{
			p.verbatim(contentNode.getContent());
		}
	}

	protected void printContentNode(WtRtData rtd, WtContentNode contentNode)
	{
		if (rtd != null)
		{
			printRtd(rtd.getField(0));
			iterate(contentNode);
			printRtd(rtd.getField(1));
		}
		else
		{
			iterate(contentNode);
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
				dispatch(n);
			}
			printRtd(rtd.getField(i));
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

	public static <T extends WtNode> String print(T node)
	{
		return print(new StringWriter(), node).toString();
	}

	public static <T extends WtNode> Writer print(Writer writer, T node)
	{
		new WtRtDataPrettyPrinter(writer).go(node);
		return writer;
	}

	// =========================================================================

	public WtRtDataPrettyPrinter(Writer writer)
	{
		super(writer);
	}
}
