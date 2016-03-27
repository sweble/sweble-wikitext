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

package org.sweble.wikitext.parser;

import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.sweble.wikitext.parser.nodes.WtNode;

import de.fau.cs.osr.ptk.common.AstEntityMap;

public interface WtEntityMap
		extends
			AstEntityMap<WtNode>
{
	public static final WtEntityMap EMPTY_ENTITY_MAP = new WtEmptyEntityMap();

	// =========================================================================

	public static final class WtEmptyEntityMap
			implements
				WtEntityMap
	{
		private WtEmptyEntityMap()
		{
		}

		@Override
		public int registerEntity(WtNode entity)
		{
			throw new UnsupportedOperationException("You're dealing with an immutable, empty entity map!");
		}

		@Override
		public WtNode getEntity(int id)
		{
			return null;
		}

		@Override
		public Set<Entry<Integer, WtNode>> getEntities()
		{
			return Collections.emptySet();
		}

		@Override
		public Map<Integer, WtNode> getMap()
		{
			return Collections.emptyMap();
		}

		@Override
		public boolean isEmpty()
		{
			return true;
		}
	}
}
