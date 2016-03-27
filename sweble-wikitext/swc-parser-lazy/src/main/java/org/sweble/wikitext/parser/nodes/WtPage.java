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

package org.sweble.wikitext.parser.nodes;

import java.util.Collections;
import java.util.List;

import org.sweble.wikitext.parser.WtEntityMap;
import org.sweble.wikitext.parser.nodes.WtContentNode.WtContentNodeImpl;

import de.fau.cs.osr.ptk.common.Warning;
import de.fau.cs.osr.ptk.common.ast.AstNodePropertyIterator;

public abstract class WtPage
		extends
			WtContentNodeImpl
{
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unchecked")
	public static final List<Warning> NO_WARNINGS = Collections.EMPTY_LIST;

	public static final WtEntityMap NO_ENTITIES = WtEntityMap.EMPTY_ENTITY_MAP;

	// =========================================================================

	protected WtPage()
	{
		setWarnings(NO_WARNINGS);
		setEntityMap(NO_ENTITIES);
	}

	protected WtPage(WtNodeList content)
	{
		super(content);
		setWarnings(NO_WARNINGS);
		setEntityMap(NO_ENTITIES);
	}

	protected WtPage(
			WtNodeList content,
			WtEntityMap entityMap)
	{
		super(content);
		setWarnings(NO_WARNINGS);
		setEntityMap(entityMap);
	}

	@Override
	public int getNodeType()
	{
		return NT_PARSED_WIKITEXT_PAGE;
	}

	// =========================================================================
	// Properties

	private List<Warning> warnings;

	public final boolean hasWarnings()
	{
		return this.warnings != NO_WARNINGS || !this.warnings.isEmpty();
	}

	public final List<Warning> getWarnings()
	{
		return this.warnings;
	}

	public final void setWarnings(List<Warning> warnings)
	{
		if (warnings == null)
			throw new NullPointerException();
		this.warnings = warnings;
	}

	private WtEntityMap entityMap;

	public final boolean hasEntities()
	{
		return this.entityMap != NO_ENTITIES || !this.entityMap.isEmpty();
	}

	public final WtEntityMap getEntityMap()
	{
		return this.entityMap;
	}

	public final void setEntityMap(WtEntityMap entityMap)
	{
		if (entityMap == null)
			throw new NullPointerException();
		this.entityMap = entityMap;
	}

	@Override
	public final int getPropertyCount()
	{
		return 2 + getSuperPropertyCount();
	}

	public int getSuperPropertyCount()
	{
		return super.getPropertyCount();
	}

	@Override
	public final AstNodePropertyIterator propertyIterator()
	{
		return new WtContentNodePropertyIterator()
		{
			@Override
			protected int getPropertyCount()
			{
				return WtPage.this.getPropertyCount();
			}

			@Override
			protected String getName(int index)
			{
				switch (index - getSuperPropertyCount())
				{
					case 0:
						return "warnings";
					case 1:
						return "entityMap";

					default:
						return super.getName(index);
				}
			}

			@Override
			protected Object getValue(int index)
			{
				switch (index - getSuperPropertyCount())
				{
					case 0:
						return WtPage.this.getWarnings();
					case 1:
						return WtPage.this.getEntityMap();

					default:
						return super.getValue(index);
				}
			}

			@Override
			protected Object setValue(int index, Object value)
			{
				switch (index - getSuperPropertyCount())
				{
					case 0:
					{
						@SuppressWarnings("unchecked")
						List<Warning> list = (List<Warning>) value;
						List<Warning> old = WtPage.this.getWarnings();
						WtPage.this.setWarnings(list);
						return old;
					}
					case 1:
					{
						WtEntityMap old = WtPage.this.getEntityMap();
						WtPage.this.setEntityMap((WtEntityMap) value);
						return old;
					}

					default:
						return super.setValue(index, value);
				}
			}
		};
	}
}
