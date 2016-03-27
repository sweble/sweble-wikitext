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

public class WtLctVarConv
		extends
			WtInnerNode2
{
	private static final long serialVersionUID = 1L;

	// =========================================================================

	/**
	 * Only for use by de-serialization code.
	 */
	protected WtLctVarConv()
	{
		super(Uninitialized.X);
	}

	protected WtLctVarConv(WtLctFlags flags, WtBody body)
	{
		super(flags, body);
	}

	@Override
	public int getNodeType()
	{
		return NT_LCT_VAR_CONV;
	}

	// =========================================================================
	// Children

	public boolean hasFlags()
	{
		return getFlags() != WtLctFlags.NO_FLAGS;
	}

	public final void setFlags(WtLctFlags flags)
	{
		set(0, flags);
	}

	public final WtLctFlags getFlags()
	{
		return (WtLctFlags) get(0);
	}

	public final void setText(WtBody text)
	{
		set(0, text);
	}

	public final WtBody getText()
	{
		return (WtBody) get(1);
	}

	private static final String[] CHILD_NAMES = new String[] { "flags", "body" };

	public final String[] getChildNames()
	{
		return CHILD_NAMES;
	}
}
