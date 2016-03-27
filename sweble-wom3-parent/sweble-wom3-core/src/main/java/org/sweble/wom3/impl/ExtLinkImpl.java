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

import java.net.MalformedURLException;
import java.net.URL;

import org.sweble.wom3.Wom3ExtLink;
import org.sweble.wom3.Wom3Title;

import de.fau.cs.osr.utils.WrappedException;

public class ExtLinkImpl
		extends
			BackboneContainer
		implements
			Wom3ExtLink
{
	private static final long serialVersionUID = 1L;

	private static final ChildDescriptor[] BODY_DESCRIPTOR = {
			childDesc("title") };

	private TitleImpl title;

	// =========================================================================

	public ExtLinkImpl(DocumentImpl owner)
	{
		super(owner);
		try
		{
			setTarget(new URL("http", null, "."));
		}
		catch (MalformedURLException e)
		{
			throw new WrappedException(e);
		}
	}

	// =========================================================================

	@Override
	public String getWomName()
	{
		return "extlink";
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
		if (added instanceof Wom3Title)
			this.title = (TitleImpl) added;
	}

	@Override
	public void childRemoved(Backbone prev, Backbone removed)
	{
		if (removed == this.title)
			this.title = null;
	}

	// =========================================================================

	@Override
	public URL getTarget()
	{
		return (URL) getAttributeNativeData("target");
	}

	@Override
	public URL getLinkTarget()
	{
		return getTarget();
	}

	@Override
	public URL setTarget(URL target)
	{
		return setAttributeDirect(ATTR_DESC_TARGET, "target", target);
	}

	@Override
	public boolean isPlainUrl()
	{
		return getBoolAttr("plainurl");
	}

	@Override
	public boolean setPlainUrl(boolean plainUrl)
	{
		return setBoolAttr(ATTR_DESC_PLAIN_URL, "plainurl", plainUrl);
	}

	// =========================================================================

	@Override
	public Wom3Title setLinkTitle(Wom3Title title) throws NullPointerException
	{
		return (Wom3Title) replaceOrAppend(this.title, title, false);
	}

	@Override
	public Wom3Title getLinkTitle()
	{
		return title;
	}

	// =========================================================================

	public static final AttrDescTarget ATTR_DESC_TARGET = new AttrDescTarget();

	public static final AttrDescPlainUrl ATTR_DESC_PLAIN_URL = new AttrDescPlainUrl();

	@Override
	protected AttributeDescriptor getAttributeDescriptor(
			String namespaceURL,
			String localName,
			String qualifiedName)
	{
		return getAttrDescStrict(namespaceURL, localName, qualifiedName,
				"target", ATTR_DESC_TARGET,
				"plainurl", ATTR_DESC_PLAIN_URL);
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
			return AttributeVerifiers.URL.verifyAndConvert(parent, verified);
		}
	}

	public static final class AttrDescPlainUrl
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
			return AttributeVerifiers.verifyAndConvertBool(parent, verified, "plainurl");
		}
	}
}
