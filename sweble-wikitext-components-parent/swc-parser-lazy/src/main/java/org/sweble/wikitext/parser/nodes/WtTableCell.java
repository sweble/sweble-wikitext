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

public class WtTableCell
		extends
			WtInnerNode2
{
	private static final long serialVersionUID = 1L;

	// =========================================================================

	/**
	 * Only for use by de-serialization code.
	 */
	protected WtTableCell()
	{
		super(Uninitialized.X);
	}

	protected WtTableCell(WtXmlAttributes xmlAttributes, WtBody body)
	{
		super(xmlAttributes, body);
	}

	@Override
	public int getNodeType()
	{
		return NT_TABLE_CELL;
	}

	// =========================================================================
	// Children

	public final void setXmlAttributes(WtXmlAttributes xmlAttributes)
	{
		set(0, xmlAttributes);
	}

	public final WtXmlAttributes getXmlAttributes()
	{
		return (WtXmlAttributes) get(0);
	}

	public final void setBody(WtBody body)
	{
		set(1, body);
	}

	public final WtBody getBody()
	{
		return (WtBody) get(1);
	}

	private static final String[] CHILD_NAMES = new String[] { "xmlAttributes", "body" };

	public final String[] getChildNames()
	{
		return CHILD_NAMES;
	}
}
