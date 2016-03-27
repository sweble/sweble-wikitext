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

import org.sweble.wikitext.parser.nodes.WtNode;

public interface EngNode
		extends
			WtNode
{
	// ...

	// =========================================================================

	public static final int NT_ENGINE_BITS /* .......... */= NT_MW_NODE_BITS | 0x100000;

	// -- General nodes --------------------------------------------------------

	public static final int NT_PAGE /* ................. */= NT_ENGINE_BITS + 1;

	public static final int NT_PROCESSED_PAGE /* ....... */= NT_ENGINE_BITS + 2;

	public static final int NT_NOWIKI /* ............... */= NT_ENGINE_BITS + 3;

	public static final int NT_SOFT_ERROR /* ........... */= NT_ENGINE_BITS + 4;
}
