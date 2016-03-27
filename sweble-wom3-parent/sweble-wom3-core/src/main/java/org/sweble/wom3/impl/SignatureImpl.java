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

import org.joda.time.DateTime;
import org.sweble.wom3.Wom3Rtd;
import org.sweble.wom3.Wom3Signature;
import org.sweble.wom3.Wom3SignatureFormat;

public class SignatureImpl
		extends
			BackboneContainer
		implements
			Wom3Signature
{
	private static final long serialVersionUID = 1L;

	// =========================================================================

	public SignatureImpl(DocumentImpl owner)
	{
		super(owner);
		setSignatureFormat(Wom3SignatureFormat.USER);
		setAuthor("noname");
		setTimestamp(DateTime.now());
	}

	// =========================================================================

	@Override
	public String getWomName()
	{
		return "signature";
	}

	// =========================================================================

	@Override
	public Wom3SignatureFormat getSignatureFormat()
	{
		return (Wom3SignatureFormat) getAttributeNativeData("format");
	}

	@Override
	public Wom3SignatureFormat setSignatureFormat(Wom3SignatureFormat format) throws NullPointerException
	{
		return setAttributeDirect(ATTR_DESC_FORMAT, "format", format);
	}

	@Override
	public String getAuthor()
	{
		return getAttribute("author");
	}

	@Override
	public String setAuthor(String author) throws IllegalArgumentException, NullPointerException
	{
		return setAttributeDirect(ATTR_DESC_AUTHOR, "author", author);
	}

	@Override
	public DateTime getTimestamp()
	{
		return (DateTime) getAttributeNativeData("timestamp");
	}

	@Override
	public DateTime setTimestamp(DateTime timestamp) throws NullPointerException
	{
		return setAttributeDirect(ATTR_DESC_TIMESTAMP, "timestamp", timestamp);
	}

	// =========================================================================

	@Override
	protected void allowsInsertion(Backbone prev, Backbone child)
	{
		if (getOwnerDocument().getStrictErrorChecking())
		{
			if (!(child instanceof Wom3Rtd))
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
			if (!(newChild instanceof Wom3Rtd))
				doesNotAllowReplacement(oldChild, newChild);
		}
	}

	// =========================================================================

	protected static final AttributeDescriptor ATTR_DESC_FORMAT = new AttrDescFormat();

	protected static final AttributeDescriptor ATTR_DESC_AUTHOR = new AttrDescAuthor();

	protected static final AttributeDescriptor ATTR_DESC_TIMESTAMP = new AttrDescTimestamp();

	@Override
	protected AttributeDescriptor getAttributeDescriptor(
			String namespaceUri,
			String localName,
			String qualifiedName)
	{
		return getAttrDescStrict(namespaceUri, localName, qualifiedName,
				"format", ATTR_DESC_FORMAT,
				"author", ATTR_DESC_AUTHOR,
				"timestamp", ATTR_DESC_TIMESTAMP);
	}

	public static final class AttrDescFormat
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
			if (verified.strValue != null)
				verified.value = Toolbox.stringToSignatureFormat(verified.strValue);
			else
				verified.strValue = Toolbox.signatureFormatToString((Wom3SignatureFormat) verified.value);
			return true;
		}
	}

	public static final class AttrDescAuthor
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
			return super.verifyAndConvert(parent, verified);
		}
	}

	public static final class AttrDescTimestamp
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
			if (verified.strValue != null)
				verified.value = Toolbox.stringToDateTime(verified.strValue);
			else
				verified.strValue = Toolbox.dateTimeToString((DateTime) verified.value);
			return true;
		}
	}
}
