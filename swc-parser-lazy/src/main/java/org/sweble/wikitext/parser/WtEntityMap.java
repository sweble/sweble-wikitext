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
	
	public static final class WtEmptyEntityMap
			implements
				WtEntityMap
	{
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
