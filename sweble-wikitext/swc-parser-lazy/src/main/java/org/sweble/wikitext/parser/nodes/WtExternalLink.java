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

public class WtExternalLink
		extends
			WtInnerNode2
{
	private static final long serialVersionUID = 1L;

	// =========================================================================

	/**
	 * Only for use by de-serialization code.
	 */
	protected WtExternalLink()
	{
		super(Uninitialized.X);
	}

	protected WtExternalLink(WtUrl target)
	{
		super(target, WtLinkTitle.NO_TITLE);
	}

	protected WtExternalLink(WtUrl target, WtLinkTitle title)
	{
		super(target, title);
	}

	@Override
	public int getNodeType()
	{
		return NT_EXTERNAL_LINK;
	}

	// =========================================================================
	// Children

	public final void setTarget(WtUrl target)
	{
		set(0, target);
	}

	public final WtUrl getTarget()
	{
		return (WtUrl) get(0);
	}

	public final boolean hasTitle()
	{
		return getTitle() != WtLinkTitle.NO_TITLE;
	}

	public final void setTitle(WtLinkTitle title)
	{
		set(1, title);
	}

	public final WtLinkTitle getTitle()
	{
		return (WtLinkTitle) get(1);
	}

	private static final String[] CHILD_NAMES = new String[] { "target", "title" };

	public final String[] getChildNames()
	{
		return CHILD_NAMES;
	}
}
