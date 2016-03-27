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

import de.fau.cs.osr.ptk.common.ast.Uninitialized;

public class WtXmlAttribute
		extends
			WtInnerNode2
{
	private static final long serialVersionUID = 1L;

	// =========================================================================

	/**
	 * Only for use by de-serialization code.
	 */
	protected WtXmlAttribute()
	{
		super(Uninitialized.X);
	}

	protected WtXmlAttribute(WtName name)
	{
		super(name, WtValue.NO_VALUE);
	}

	protected WtXmlAttribute(WtName name, WtValue value)
	{
		super(name, value);
	}

	@Override
	public int getNodeType()
	{
		return NT_XML_ATTRIBUTE;
	}

	// =========================================================================
	// Children

	public final void setName(WtName value)
	{
		set(0, value);
	}

	public final WtName getName()
	{
		return (WtName) get(0);
	}

	public final boolean hasValue()
	{
		return getValue() != WtValue.NO_VALUE;
	}

	public final void setValue(WtValue value)
	{
		set(1, value);
	}

	public final WtValue getValue()
	{
		return (WtValue) get(1);
	}

	private static final String[] CHILD_NAMES = new String[] { "name", "value" };

	public final String[] getChildNames()
	{
		return CHILD_NAMES;
	}
}
