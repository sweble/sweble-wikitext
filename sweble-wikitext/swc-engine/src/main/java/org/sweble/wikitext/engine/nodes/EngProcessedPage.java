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

import java.util.List;

import org.sweble.wikitext.parser.WtEntityMap;
import org.sweble.wikitext.parser.nodes.WtInnerNode2;

import de.fau.cs.osr.ptk.common.Warning;
import de.fau.cs.osr.ptk.common.ast.AstNodePropertyIterator;
import de.fau.cs.osr.ptk.common.ast.Uninitialized;

public class EngProcessedPage
		extends
			WtInnerNode2
		implements
			EngNode
{
	private static final long serialVersionUID = 1L;

	// =========================================================================

	/**
	 * Only for use by de-serialization code.
	 */
	protected EngProcessedPage()
	{
		super(Uninitialized.X);
	}

	protected EngProcessedPage(
			EngPage page,
			EngLogProcessingPass log,
			List<Warning> warnings)
	{
		super(page, log);
		setWarnings(warnings);
	}

	protected EngProcessedPage(
			EngPage page,
			EngLogProcessingPass log,
			List<Warning> warnings,
			WtEntityMap entityMap)
	{
		super(page, log);
		setWarnings(warnings);
		setEntityMap(entityMap);
	}

	@Override
	public int getNodeType()
	{
		return NT_PROCESSED_PAGE;
	}

	// =========================================================================
	// Properties

	private EngLogProcessingPass log;

	public final EngLogProcessingPass getLog()
	{
		return this.log;
	}

	public final EngLogProcessingPass setLog(EngLogProcessingPass log)
	{
		EngLogProcessingPass old = this.log;
		this.log = log;
		return old;
	}

	private List<Warning> warnings;

	public final List<Warning> getWarnings()
	{
		return this.warnings;
	}

	public final List<Warning> setWarnings(List<Warning> warnings)
	{
		List<Warning> old = this.warnings;
		this.warnings = warnings;
		return old;
	}

	private WtEntityMap entityMap;

	public final WtEntityMap getEntityMap()
	{
		return this.entityMap;
	}

	public final WtEntityMap setEntityMap(WtEntityMap entityMap)
	{
		WtEntityMap old = this.entityMap;
		this.entityMap = entityMap;
		return old;
	}

	@Override
	public final int getPropertyCount()
	{
		return 3;
	}

	@Override
	public final AstNodePropertyIterator propertyIterator()
	{
		return new AstNodePropertyIterator()
		{
			@Override
			protected int getPropertyCount()
			{
				return 3;
			}

			@Override
			protected String getName(int index)
			{
				switch (index)
				{
					case 0:
						return "log";
					case 1:
						return "warnings";
					case 2:
						return "entityMap";

					default:
						throw new IndexOutOfBoundsException();
				}
			}

			@Override
			protected Object getValue(int index)
			{
				switch (index)
				{
					case 0:
						return EngProcessedPage.this.getLog();
					case 1:
						return EngProcessedPage.this.getWarnings();
					case 2:
						return EngProcessedPage.this.getEntityMap();

					default:
						throw new IndexOutOfBoundsException();
				}
			}

			@Override
			protected Object setValue(int index, Object value)
			{
				switch (index)
				{
					case 0:
						return EngProcessedPage.this.setLog((EngLogProcessingPass) value);
					case 1:
					{
						@SuppressWarnings("unchecked")
						List<Warning> warnings = (List<Warning>) value;
						return EngProcessedPage.this.setWarnings(warnings);
					}
					case 2:
						return EngProcessedPage.this.setEntityMap((WtEntityMap) value);

					default:
						throw new IndexOutOfBoundsException();
				}
			}
		};
	}

	// =========================================================================
	// Children

	public final void setPage(EngPage page)
	{
		set(0, page);
	}

	public final EngPage getPage()
	{
		return (EngPage) get(0);
	}

	private static final String[] CHILD_NAMES = new String[] { "page" };

	public final String[] getChildNames()
	{
		return CHILD_NAMES;
	}
}
