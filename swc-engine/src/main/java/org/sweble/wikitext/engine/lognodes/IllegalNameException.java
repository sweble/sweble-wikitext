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
package org.sweble.wikitext.engine.lognodes;

import org.sweble.wikitext.parser.nodes.WtNode;

import de.fau.cs.osr.ptk.common.ast.AstNodePropertyIterator;

public class IllegalNameException
		extends
			LogLeafNode
{
	private static final long serialVersionUID = 1L;
	
	// =========================================================================
	
	public IllegalNameException(WtNode name, String message)
	{
		setName(name);
		setMessage(message);
	}
	
	// =========================================================================
	// Properties
	
	private WtNode name;
	
	public final WtNode getName()
	{
		return this.name;
	}
	
	public final WtNode setName(WtNode name)
	{
		WtNode old = this.name;
		this.name = name;
		return old;
	}
	
	private String message;
	
	public final String getMessage()
	{
		return this.message;
	}
	
	public final String setMessage(String message)
	{
		String old = this.message;
		this.message = message;
		return old;
	}
	
	@Override
	public final int getPropertyCount()
	{
		return 2;
	}
	
	@Override
	public final AstNodePropertyIterator propertyIterator()
	{
		return new AstNodePropertyIterator()
		{
			@Override
			protected int getPropertyCount()
			{
				return 2;
			}
			
			@Override
			protected String getName(int index)
			{
				switch (index)
				{
					case 0:
						return "name";
					case 1:
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
						return IllegalNameException.this.getName();
					case 1:
						return IllegalNameException.this.getMessage();
						
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
						return IllegalNameException.this.setName((WtNode) value);
					case 1:
						return IllegalNameException.this.setMessage((String) value);
						
					default:
						throw new IndexOutOfBoundsException();
				}
			}
		};
	}
}
