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
package org.sweble.wikitext.engine.nodes;

import org.sweble.wikitext.parser.WtRtData;
import org.sweble.wikitext.parser.nodes.WtNode;

import de.fau.cs.osr.ptk.common.ast.AstLeafNodeImpl;

public class EngLogLeafNode
		extends
			AstLeafNodeImpl<WtNode>
		implements
			WtNode,
			EngLogNode
{
	private static final long serialVersionUID = -1024830219477280567L;

	// =========================================================================

	@Override
	public void setRtd(WtRtData rtd)
	{
		throw new UnsupportedOperationException(genMsg());
	}

	@Override
	public void setRtd(Object... glue)
	{
		throw new UnsupportedOperationException(genMsg());
	}

	@Override
	public void setRtd(String... glue)
	{
		throw new UnsupportedOperationException(genMsg());
	}

	@Override
	public WtRtData getRtd()
	{
		return null;
	}

	@Override
	public void clearRtd()
	{
		throw new UnsupportedOperationException(genMsg());
	}

	@Override
	public void suppressRtd()
	{
		throw new UnsupportedOperationException(genMsg());
	}

	// =========================================================================

	protected String genMsg()
	{
		return "Operation not supported by EngLog* nodes!";
	}
}
