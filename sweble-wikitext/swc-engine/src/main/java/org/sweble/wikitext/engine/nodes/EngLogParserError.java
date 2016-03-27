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

import de.fau.cs.osr.ptk.common.ast.AstNodePropertyIterator;

public class EngLogParserError
		extends
			EngLogLeafNode
{
	private static final long serialVersionUID = 1L;

	// =========================================================================

	protected EngLogParserError()
	{
	}

	protected EngLogParserError(String message)
	{
		setMessage(message);
	}

	// =========================================================================
	// Properties

	private String message;

	public final String getMessage()
	{
		return this.message;
	}

	public final void setMessage(String message)
	{
		this.message = message;
	}

	@Override
	public final int getPropertyCount()
	{
		return 1;
	}

	@Override
	public final AstNodePropertyIterator propertyIterator()
	{
		return new AstNodePropertyIterator()
		{
			@Override
			protected int getPropertyCount()
			{
				return 1;
			}

			@Override
			protected String getName(int index)
			{
				switch (index)
				{
					case 0:
						return "message";

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
						return EngLogParserError.this.getMessage();

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
					{
						String old = EngLogParserError.this.getMessage();
						EngLogParserError.this.setMessage((String) value);
						return old;
					}

					default:
						throw new IndexOutOfBoundsException();
				}
			}
		};
	}
}
