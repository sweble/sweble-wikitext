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
package org.sweble.wom3.impl;

import org.sweble.wom3.Wom3Node;

public class ChildDescriptor
{
	public static final int REQUIRED = 1;

	public static final int MULTIPLE = 2;

	private final String namespaceUri;

	private final String tag;

	private final boolean required;

	private final boolean multiple;

	// =========================================================================

	public ChildDescriptor(String namespaceUri, String tag, int flags)
	{
		this.namespaceUri = namespaceUri;
		this.tag = tag;
		this.required = (flags & REQUIRED) == REQUIRED;
		this.multiple = (flags & MULTIPLE) == MULTIPLE;
	}

	// =========================================================================

	public String getTag()
	{
		return tag;
	}

	public boolean matches(Wom3Node n)
	{
		return tag.equals(n.getNodeName()) &&
				((namespaceUri == null) || (namespaceUri.equals(n.getNamespaceURI())));
	}

	public boolean isRequired()
	{
		return required;
	}

	public boolean isMultiple()
	{
		return multiple;
	}
}
