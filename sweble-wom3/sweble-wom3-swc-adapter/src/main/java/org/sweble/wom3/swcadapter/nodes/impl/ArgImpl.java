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

import org.sweble.wom3.impl.AttributeDescriptor;
import org.sweble.wom3.impl.Backbone;
import org.sweble.wom3.impl.ChildDescriptor;
import org.sweble.wom3.impl.DocumentImpl;
import org.sweble.wom3.swcadapter.nodes.SwcArg;
import org.sweble.wom3.swcadapter.nodes.SwcName;
import org.sweble.wom3.swcadapter.nodes.SwcValue;

public class ArgImpl
		extends
			BackboneSwcElement
		implements
			SwcArg
{
	private static final long serialVersionUID = 1L;

	private static final ChildDescriptor[] BODY_DESCRIPTOR = {
			childDesc(MWW_NS_URI, "name"),
			childDesc(MWW_NS_URI, "value", ChildDescriptor.REQUIRED) };

	private SwcName name;

	private SwcValue value;

	// =========================================================================

	public ArgImpl(DocumentImpl owner)
	{
		super(owner);
	}

	// =========================================================================

	@Override
	public String getSwcName()
	{
		return "arg";
	}

	// =========================================================================

	@Override
	protected void allowsInsertion(Backbone prev, Backbone child)
	{
		checkInsertion(prev, child, BODY_DESCRIPTOR);
	}

	@Override
	protected void allowsRemoval(Backbone child)
	{
		checkRemoval(child, BODY_DESCRIPTOR);
	}

	@Override
	protected void allowsReplacement(Backbone oldChild, Backbone newChild)
	{
		checkReplacement(oldChild, newChild, BODY_DESCRIPTOR);
	}

	@Override
	public void childInserted(Backbone prev, Backbone added)
	{
		if (added instanceof SwcName)
			this.name = (SwcName) added;
		if (added instanceof SwcValue)
			this.value = (SwcValue) added;
	}

	@Override
	public void childRemoved(Backbone prev, Backbone removed)
	{
		if (removed == this.name)
			this.name = null;
		if (removed == this.value)
			this.value = null;
	}

	// =========================================================================

	@Override
	public boolean hasName()
	{
		return this.name != null;
	}

	@Override
	public SwcName setName(SwcName name)
	{
		return (SwcName) replaceOrInsertBeforeOrAppend(this.name, this.value, name, false);
	}

	@Override
	public SwcName getName()
	{
		return this.name;
	}

	@Override
	public SwcValue setValue(SwcValue value)
	{
		return (SwcValue) replaceOrAppend(this.value, value, true);
	}

	@Override
	public SwcValue getValue()
	{
		return this.value;
	}

	// =========================================================================

	@Override
	protected AttributeDescriptor getAttributeDescriptor(
			String namespaceUri,
			String localName,
			String qualifiedName)
	{
		return getAttrDescStrict(namespaceUri, localName, qualifiedName);
	}
}
