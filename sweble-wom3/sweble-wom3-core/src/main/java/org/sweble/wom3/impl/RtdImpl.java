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

import org.sweble.wom3.Wom3Rtd;
import org.w3c.dom.Node;

public class RtdImpl
		extends
			BackboneContainer
		implements
			Wom3Rtd
{
	private static final long serialVersionUID = 1L;

	// =========================================================================

	public RtdImpl(DocumentImpl owner)
	{
		super(owner);
	}

	// =========================================================================

	@Override
	public String getWomName()
	{
		return "rtd";
	}

	@Override
	protected boolean ignoresContentWhitespace()
	{
		return false;
	}

	@Override
	protected void allowsInsertion(Backbone prev, Backbone child)
	{
		if (getOwnerDocument().getStrictErrorChecking())
		{
			if (child.getNodeType() != Node.TEXT_NODE)
				doesNotAllowInsertion(prev, child);
		}
	}

	@Override
	protected void allowsRemoval(Backbone child)
	{
	}

	@Override
	protected void allowsReplacement(Backbone oldChild, Backbone newChild)
	{
		if (getOwnerDocument().getStrictErrorChecking())
		{
			if (newChild.getNodeType() != Node.TEXT_NODE)
				doesNotAllowReplacement(oldChild, newChild);
		}
	}

	@Override
	protected AttributeDescriptor getAttributeDescriptor(
			String namespaceUri,
			String localName,
			String qualifiedName)
	{
		return getAttrDescStrict(namespaceUri, localName, qualifiedName);
	}
}
