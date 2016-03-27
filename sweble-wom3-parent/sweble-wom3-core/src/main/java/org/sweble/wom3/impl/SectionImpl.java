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

import org.sweble.wom3.Wom3Body;
import org.sweble.wom3.Wom3Heading;
import org.sweble.wom3.Wom3Section;

public class SectionImpl
		extends
			BackboneContainer
		implements
			Wom3Section
{
	private static final long serialVersionUID = 1L;

	private static final ChildDescriptor[] BODY_DESCRIPTOR = {
			childDesc("heading", ChildDescriptor.REQUIRED),
			childDesc("body", ChildDescriptor.REQUIRED) };

	private Wom3Heading heading;

	private Wom3Body body;

	// =========================================================================

	public SectionImpl(DocumentImpl owner)
	{
		super(owner);
		setLevelAttr(1);
	}

	// =========================================================================

	@Override
	public String getWomName()
	{
		return "section";
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
		if (added instanceof Wom3Heading)
			this.heading = (Wom3Heading) added;
		else if (added instanceof Wom3Body)
			this.body = (Wom3Body) added;
	}

	@Override
	public void childRemoved(Backbone prev, Backbone removed)
	{
		if (removed == heading)
			this.heading = null;
		else if (removed == body)
			this.body = null;
	}

	// =========================================================================

	@Override
	public int getLevel()
	{
		return (Integer) getAttributeNativeData("level");
	}

	@Override
	public int setLevel(int level) throws IllegalArgumentException
	{
		return setLevelAttr(level);
	}

	private Integer setLevelAttr(int level)
	{
		return setAttributeDirect(ATTR_DESC_LEVEL, "level", level);
	}

	// =========================================================================

	@Override
	public Wom3Heading getHeading()
	{
		return heading;
	}

	@Override
	public Wom3Heading setHeading(Wom3Heading heading)
	{
		return (Wom3Heading) replaceOrInsertBeforeOrAppend(
				this.heading, this.body, heading, true);
	}

	@Override
	public Wom3Body getBody()
	{
		return body;
	}

	@Override
	public Wom3Body setBody(Wom3Body body) throws NullPointerException
	{
		return (Wom3Body) replaceOrAppend(this.body, body, true);
	}

	// =========================================================================

	protected static final AttributeDescriptor ATTR_DESC_LEVEL = new AttrDescLevel();

	@Override
	protected AttributeDescriptor getAttributeDescriptor(
			String namespaceUri,
			String localName,
			String qualifiedName)
	{
		return getAttrDescStrict(namespaceUri, localName, qualifiedName,
				"level", ATTR_DESC_LEVEL);
	}

	public static final class AttrDescLevel
			extends
				AttributeDescriptor
	{
		@Override
		public int getFlags()
		{
			return makeFlags(
					false /* removable */,
					false /* readOnly */,
					false /* customAction */,
					Normalization.NON_CDATA);
		}

		@Override
		public boolean verifyAndConvert(
				Backbone parent,
				NativeAndStringValuePair verified)
		{
			super.verifyAndConvert(parent, verified);
			verified.value = AttributeVerifiers.verifyRange(verified.strValue, 1, 6);
			return true;
		}
	}
}
