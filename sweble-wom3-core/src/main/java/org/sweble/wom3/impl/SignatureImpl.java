/**
 * Copyright 2011 The Open Source Research Group,
 *                University of Erlangen-Nürnberg
 */
package org.sweble.wom3.impl;

import org.joda.time.DateTime;
import org.sweble.wom3.Wom3Node;
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
		return setAttributeDirect(org.sweble.wom3.impl.SignatureImpl.Attributes.FORMAT, "format", format);
	}
	
	@Override
	public String getAuthor()
	{
		return getAttribute("author");
	}
	
	@Override
	public String setAuthor(String author) throws IllegalArgumentException, NullPointerException
	{
		return setAttributeDirect(org.sweble.wom3.impl.SignatureImpl.Attributes.AUTHOR, "author", author);
	}
	
	@Override
	public DateTime getTimestamp()
	{
		return (DateTime) getAttributeNativeData("timestamp");
	}
	
	@Override
	public DateTime setTimestamp(DateTime timestamp) throws NullPointerException
	{
		return setAttributeDirect(org.sweble.wom3.impl.SignatureImpl.Attributes.TIMESTAMP, "timestamp", timestamp);
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
	
	@Override
	protected AttributeDescriptor getAttributeDescriptor(
			String namespaceUri,
			String localName,
			String qualifiedName)
	{
		return getAttrDescStrict(namespaceUri, localName, qualifiedName,
				"format", Attributes.FORMAT,
				"author", Attributes.AUTHOR,
				"timestamp", Attributes.TIMESTAMP);
	}
	
	public static enum Attributes implements AttributeDescriptor
	{
		FORMAT
		{
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
		},
		
		AUTHOR
		{
			@Override
			public boolean verifyAndConvert(
					Backbone parent,
					NativeAndStringValuePair verified)
			{
				return true;
			}
		},
		
		TIMESTAMP
		{
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
		};
		
		// =====================================================================
		
		@Override
		public boolean isRemovable()
		{
			return false;
		}
		
		@Override
		public Normalization getNormalizationMode()
		{
			return Normalization.CDATA;
		}
		
		@Override
		public void customAction(
				Wom3Node parent,
				AttributeBase oldAttr,
				AttributeBase newAttr)
		{
		}
	}
}
