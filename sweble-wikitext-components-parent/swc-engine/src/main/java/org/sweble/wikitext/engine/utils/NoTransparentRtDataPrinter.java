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
package org.sweble.wikitext.engine.utils;

import java.io.StringWriter;
import java.io.Writer;

import org.sweble.wikitext.parser.nodes.WtNode;
import org.sweble.wikitext.parser.nodes.WtNodeList;
import org.sweble.wikitext.parser.utils.WtRtDataPrinter;

public final class NoTransparentRtDataPrinter
		extends
			WtRtDataPrinter
{
	@Override
	public void dispatch(WtNode node)
	{
		switch (node.getNodeType())
		{
			case WtNode.NT_IGNORED:
			case WtNode.NT_XML_COMMENT:
				break;

			case WtNode.NT_ONLY_INCLUDE:
				printNodeList((WtNodeList) node);
				break;

			default:
				super.dispatch(node);
				break;
		}
	}

	// =====================================================================

	public static String print(WtNode node)
	{
		return print(new StringWriter(), node).toString();
	}

	public static Writer print(Writer writer, WtNode node)
	{
		new NoTransparentRtDataPrinter(writer).go(node);
		return writer;
	}

	// =====================================================================

	public NoTransparentRtDataPrinter(Writer writer)
	{
		super(writer);
	}
}
