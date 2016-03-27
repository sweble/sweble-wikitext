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

import org.sweble.wikitext.parser.nodes.WtNode;
import org.sweble.wikitext.parser.nodes.WtText;

public class ApplyToText
{
	private Functor fn;

	// =========================================================================

	public ApplyToText(Functor fn)
	{
		this.fn = fn;
	}

	// =========================================================================

	public void go(WtNode arg0)
	{
		if (arg0.getNodeType() == WtNode.NT_TEXT)
		{
			apply((WtText) arg0);
		}
		else
		{
			for (WtNode n : arg0)
			{
				if (n == null)
					continue;

				if (n.isNodeType(WtNode.NT_TEXT))
				{
					apply((WtText) n);
				}
				else
				{
					go(n);
				}
			}
		}
	}

	private void apply(WtText arg0)
	{
		arg0.setContent(fn.apply(arg0.getContent()));
	}

	// =========================================================================

	public interface Functor
	{
		public String apply(String text);
	}
}
