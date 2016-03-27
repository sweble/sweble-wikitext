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
package org.sweble.wikitext.parser.postprocessor;

import org.sweble.wikitext.parser.nodes.WtNode;

public class WtNodeFlags
{
	public static final String PARSER_RECOGNIZED = "parser-recognized";

	public static void setParserRecognized(WtNode pEndTag)
	{
		pEndTag.setBooleanAttribute(PARSER_RECOGNIZED, true);
	}

	public static boolean isParserRecognized(WtNode n)
	{
		return n.getBooleanAttribute(PARSER_RECOGNIZED);
	}

	// =========================================================================

	public static final String REPAIR = "repair";

	public static WtNode setRepairNode(WtNode node)
	{
		node.setAttribute(REPAIR, true);
		return node;
	}

	public static boolean isRepairNode(WtNode node)
	{
		return node.getBooleanAttribute(REPAIR);
	}

	// =========================================================================

	private static final String IMPLICIT = "implicit";

	public static void setImplicit(WtNode node)
	{
		node.setBooleanAttribute(IMPLICIT, true);
	}

	public static boolean isImplicit(WtNode node)
	{
		return node.getBooleanAttribute(IMPLICIT);
	}
}
