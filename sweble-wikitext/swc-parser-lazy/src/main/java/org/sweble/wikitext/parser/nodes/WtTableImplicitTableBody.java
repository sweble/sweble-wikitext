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

public class WtTableImplicitTableBody
		extends
			WtInnerNode1
{
	private static final long serialVersionUID = 1L;

	// =========================================================================

	/**
	 * Only for use by de-serialization code.
	 */
	protected WtTableImplicitTableBody()
	{
		super(Uninitialized.X);
	}

	protected WtTableImplicitTableBody(WtBody body)
	{
		super(body);
	}

	@Override
	public int getNodeType()
	{
		return NT_TABLE_IMPLICIT_TBODY;
	}

	// =========================================================================
	// Children

	public final void setBody(WtBody body)
	{
		set(0, body);
	}

	public final WtBody getBody()
	{
		return (WtBody) get(0);
	}

	private static final String[] CHILD_NAMES = new String[] { "body" };

	public final String[] getChildNames()
	{
		return CHILD_NAMES;
	}
}
