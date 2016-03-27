/**
 * Copyright 2011 The Open Source Research Group,
 *                University of Erlangen-Nürnberg
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses/.
 */
package org.sweble.wom3.swcadapter.nodes.impl;

import org.sweble.wom3.impl.BackboneWomElement;
import org.sweble.wom3.impl.ChildDescriptor;
import org.sweble.wom3.impl.DocumentImpl;
import org.sweble.wom3.swcadapter.nodes.SwcNode;

@SuppressWarnings("serial")
public abstract class BackboneSwcElement
		extends
			BackboneWomElement
		implements
			SwcNode
{
	public BackboneSwcElement(DocumentImpl owner)
	{
		super(owner);
	}

	@Override
	public final String getWomName()
	{
		return getSwcName();
	}

	@Override
	public final String getNamespaceURI()
	{
		return SwcNode.MWW_NS_URI;
	}

	public static ChildDescriptor childDesc(String tag)
	{
		return childDesc(SwcNode.MWW_NS_URI, tag, 0);
	}

	public static ChildDescriptor childDesc(String tag, int flags)
	{
		return childDesc(SwcNode.MWW_NS_URI, tag, flags);
	}

	public abstract String getSwcName();
}
