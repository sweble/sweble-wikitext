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

import org.sweble.wom3.Wom3Redirect;
import org.sweble.wom3.Wom3Title;

public class RedirectImpl
		extends
			BackboneWomElement
		implements
			Wom3Redirect
{
	private static final long serialVersionUID = 1L;

	// =========================================================================

	public RedirectImpl(DocumentImpl owner)
	{
		super(owner);
		setTarget("unknown");
	}

	// =========================================================================

	@Override
	public String getWomName()
	{
		return "redirect";
	}

	// =========================================================================

	@Override
	public String getDisplacementId()
	{
		return getStringAttr("did");
	}

	@Override
	public String setDisplacementId(String did)
	{
		return setStringAttr(CommonAttributeDescriptors.ATTR_DESC_DID, "did", did);
	}

	@Override
	public String getTarget()
	{
		return getAttribute("target");
	}

	@Override
	public String setTarget(String page)
	{
		return setAttributeDirect(ATTR_DESC_TARGET, "target", page);
	}

	@Override
	public Wom3Title getLinkTitle()
	{
		// TODO: Implement
		throw new UnsupportedOperationException();

		/*
		return EmptyTitleImpl.get();
		*/
	}

	@Override
	public String getLinkTarget()
	{
		return getTarget();
	}

	// =========================================================================

	protected static final AttributeDescriptor ATTR_DESC_TARGET = new AttrDescTarget();

	@Override
	protected AttributeDescriptor getAttributeDescriptor(
			String namespaceUri,
			String localName,
			String qualifiedName)
	{
		return getAttrDescStrict(namespaceUri, localName, qualifiedName,
				"target", ATTR_DESC_TARGET,
				"did", CommonAttributeDescriptors.ATTR_DESC_DID);
	}

	public static final class AttrDescTarget
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
			Toolbox.checkValidTarget(verified.strValue);
			return true;
		}
	}
}
