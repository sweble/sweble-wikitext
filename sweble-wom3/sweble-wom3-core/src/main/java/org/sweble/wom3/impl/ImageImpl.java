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

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.sweble.wom3.Wom3Image;
import org.sweble.wom3.Wom3ImageCaption;
import org.sweble.wom3.Wom3ImageFormat;
import org.sweble.wom3.Wom3ImageHAlign;
import org.sweble.wom3.Wom3ImageVAlign;
import org.sweble.wom3.Wom3Title;

public class ImageImpl
		extends
			BackboneContainer
		implements
			Wom3Image
{
	private static final long serialVersionUID = 1L;

	private static final ChildDescriptor[] BODY_DESCRIPTOR = {
			childDesc("imgcaption") };

	private Wom3ImageCaption caption;

	// =========================================================================

	public ImageImpl(DocumentImpl owner)
	{
		super(owner);
		setSource("unknown");
	}

	// =========================================================================

	@Override
	public String getWomName()
	{
		return "image";
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
		if (added instanceof Wom3ImageCaption)
			this.caption = (Wom3ImageCaption) added;
	}

	@Override
	public void childRemoved(Backbone prev, Backbone removed)
	{
		if (removed == this.caption)
			this.caption = null;
	}

	// =========================================================================

	@Override
	public String getSource()
	{
		return getStringAttr("source");
	}

	@Override
	public String setSource(String source)
	{
		return setAttributeDirect(ATTR_DESC_SOURCE, "source", source);
	}

	@Override
	public Wom3ImageFormat getFormat()
	{
		return (Wom3ImageFormat) getAttributeNativeData("format");
	}

	@Override
	public Wom3ImageFormat setFormat(Wom3ImageFormat format) throws NullPointerException
	{
		return setAttributeDirect(ATTR_DESC_FORMAT, "format", format);
	}

	@Override
	public boolean isBorder()
	{
		return getAttributeNativeData("border") != null;
	}

	@Override
	public boolean setBorder(boolean border)
	{
		return setAttributeDirect(ATTR_DESC_BORDER, "border", border) != null;
	}

	@Override
	public Wom3ImageHAlign getHAlign()
	{
		return (Wom3ImageHAlign) getAttributeNativeData("halign");
	}

	@Override
	public Wom3ImageHAlign setHAlign(Wom3ImageHAlign halign)
	{
		return setAttributeDirect(ATTR_DESC_HALIGN, "halign", halign);
	}

	@Override
	public Wom3ImageVAlign getVAlign()
	{
		return (Wom3ImageVAlign) getAttributeNativeData("valign");
	}

	@Override
	public Wom3ImageVAlign setVAlign(Wom3ImageVAlign valign)
	{
		return setAttributeDirect(ATTR_DESC_VALIGN, "valign", valign);
	}

	@Override
	public Integer getWidth()
	{
		return (Integer) getAttributeNativeData("width");
	}

	@Override
	public Integer setWidth(Integer width)
	{
		return setAttributeDirect(ATTR_DESC_WIDTH, "width", width);
	}

	@Override
	public Integer getHeight()
	{
		return (Integer) getAttributeNativeData("height");
	}

	@Override
	public Integer setHeight(Integer height)
	{
		return setAttributeDirect(ATTR_DESC_HEIGHT, "height", height);
	}

	@Override
	public boolean isUpright()
	{
		return getAttributeNativeData("upright") != null;
	}

	@Override
	public boolean setUpright(boolean upright)
	{
		return setAttributeDirect(ATTR_DESC_UPRIGHT, "upright", upright) != null;
	}

	@Override
	public URL getExtLink()
	{
		return getUrlAttr("extlink");
	}

	@Override
	public URL setExtLink(URL url)
	{
		return setUrlAttr(ATTR_DESC_EXTLINK, "extlink", url);
	}

	@Override
	public String getIntLink()
	{
		return getStringAttr("intlink");
	}

	@Override
	public String setIntLink(String target)
	{
		return setAttributeDirect(ATTR_DESC_INTLINK, "intlink", target);
	}

	@Override
	public String getAlt()
	{
		return getStringAttr("alt");
	}

	@Override
	public String setAlt(String alt)
	{
		return setAttributeDirect(ATTR_DESC_ALT, "alt", alt);
	}

	// =========================================================================

	@Override
	public Wom3ImageCaption setCaption(Wom3ImageCaption caption) throws NullPointerException
	{
		return (Wom3ImageCaption) replaceOrAppend(this.caption, caption, false);
	}

	@Override
	public Wom3ImageCaption getCaption()
	{
		return caption;
	}

	// =========================================================================

	@Override
	public Wom3Title getLinkTitle()
	{
		// TODO: Implement
		throw new UnsupportedOperationException();

		/*
		String title = getSource();
		if (getAlt() != null)
			title = getAlt();
		TitleImpl titleImpl = new TitleImpl();
		titleImpl.appendChild(new TextImpl(title));
		return titleImpl;
		*/
	}

	@Override
	public String getLinkTarget()
	{
		String intLink = getIntLink();
		if (intLink != null)
			return intLink;
		return getSource();
	}

	// =========================================================================

	protected static final AttributeDescriptor ATTR_DESC_SOURCE = new AttrDescSource();

	protected static final AttributeDescriptor ATTR_DESC_FORMAT = new AttrDescFormat();

	protected static final AttributeDescriptor ATTR_DESC_BORDER = new AttrDescBorder();

	protected static final AttributeDescriptor ATTR_DESC_HALIGN = new AttrDescHAlign();

	protected static final AttributeDescriptor ATTR_DESC_VALIGN = new AttrDescVAlign();

	protected static final AttributeDescriptor ATTR_DESC_WIDTH = new AttrDescWidth();

	protected static final AttributeDescriptor ATTR_DESC_HEIGHT = new AttrDescHeight();

	protected static final AttributeDescriptor ATTR_DESC_UPRIGHT = new AttrDescUpright();

	protected static final AttributeDescriptor ATTR_DESC_EXTLINK = new AttrDescExtLink();

	protected static final AttributeDescriptor ATTR_DESC_INTLINK = new AttrDescIntLink();

	protected static final AttributeDescriptor ATTR_DESC_ALT = new AttrDescAlt();

	private static final Map<String, AttributeDescriptor> NAME_MAP = new HashMap<String, AttributeDescriptor>();

	static
	{
		NAME_MAP.put("source", ATTR_DESC_SOURCE);
		NAME_MAP.put("format", ATTR_DESC_FORMAT);
		NAME_MAP.put("border", ATTR_DESC_BORDER);
		NAME_MAP.put("halign", ATTR_DESC_HALIGN);
		NAME_MAP.put("valign", ATTR_DESC_VALIGN);
		NAME_MAP.put("width", ATTR_DESC_WIDTH);
		NAME_MAP.put("height", ATTR_DESC_HEIGHT);
		NAME_MAP.put("upright", ATTR_DESC_UPRIGHT);
		NAME_MAP.put("extlink", ATTR_DESC_EXTLINK);
		NAME_MAP.put("intlink", ATTR_DESC_INTLINK);
		NAME_MAP.put("alt", ATTR_DESC_ALT);
	}

	@Override
	protected AttributeDescriptor getAttributeDescriptor(
			String namespaceUri,
			String localName,
			String qualifiedName)
	{
		return getAttrDescStrict(namespaceUri, localName, qualifiedName, NAME_MAP);
	}

	// =========================================================================

	public static final class AttrDescSource
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

	public static final class AttrDescFormat
			extends
				AttributeDescriptor
	{
		@Override
		public int getFlags()
		{
			return makeFlags(
					true /* removable */,
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
				verified.value = Toolbox.stringToImageFormat(verified.strValue);
			else
				verified.strValue = Toolbox.imageFormatToString((Wom3ImageFormat) verified.value);
			return true;
		}
	}

	public static final class AttrDescBorder
			extends
				AttributeDescriptor
	{
		@Override
		public int getFlags()
		{
			return makeFlags(
					true /* removable */,
					false /* readOnly */,
					false /* customAction */,
					Normalization.NON_CDATA);
		}

		@Override
		public boolean verifyAndConvert(
				Backbone parent,
				NativeAndStringValuePair verified)
		{
			return AttributeVerifiers.verifyAndConvertBool(parent, verified, "border");
		}
	}

	public static final class AttrDescHAlign
			extends
				AttributeDescriptor
	{
		@Override
		public int getFlags()
		{
			return makeFlags(
					true /* removable */,
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
				verified.value = Toolbox.stringToImageHAlign(verified.strValue);
			else
				verified.strValue = Toolbox.imageHAlignToString((Wom3ImageHAlign) verified.value);
			return true;
		}
	}

	public static final class AttrDescVAlign
			extends
				AttributeDescriptor
	{
		@Override
		public int getFlags()
		{
			return makeFlags(
					true /* removable */,
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
				verified.value = Toolbox.stringToImageVAlign(verified.strValue);
			else
				verified.strValue = Toolbox.imageVAlignToString((Wom3ImageVAlign) verified.value);
			return true;
		}
	}

	public static final class AttrDescWidth
			extends
				AttributeDescriptor
	{
		@Override
		public int getFlags()
		{
			return makeFlags(
					true /* removable */,
					false /* readOnly */,
					false /* customAction */,
					Normalization.NON_CDATA);
		}

		@Override
		public boolean verifyAndConvert(
				Backbone parent,
				NativeAndStringValuePair verified)
		{
			return AttributeVerifiers.PIXELS.verifyAndConvert(parent, verified);
		}
	}

	public static final class AttrDescHeight
			extends
				AttributeDescriptor
	{
		@Override
		public int getFlags()
		{
			return makeFlags(
					true /* removable */,
					false /* readOnly */,
					false /* customAction */,
					Normalization.NON_CDATA);
		}

		@Override
		public boolean verifyAndConvert(
				Backbone parent,
				NativeAndStringValuePair verified)
		{
			return AttributeVerifiers.PIXELS.verifyAndConvert(parent, verified);
		}
	}

	public static final class AttrDescUpright
			extends
				AttributeDescriptor
	{
		@Override
		public int getFlags()
		{
			return makeFlags(
					true /* removable */,
					false /* readOnly */,
					false /* customAction */,
					Normalization.NON_CDATA);
		}

		@Override
		public boolean verifyAndConvert(
				Backbone parent,
				NativeAndStringValuePair verified)
		{
			return AttributeVerifiers.verifyAndConvertBool(parent, verified, "upright");
		}
	}

	public static final class AttrDescExtLink
			extends
				AttributeDescriptor
	{
		@Override
		public int getFlags()
		{
			return makeFlags(
					true /* removable */,
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

	public static final class AttrDescIntLink
			extends
				AttributeDescriptor
	{
		@Override
		public int getFlags()
		{
			return makeFlags(
					true /* removable */,
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
			if ((verified.strValue != null) && (!verified.strValue.isEmpty()))
				Toolbox.checkValidTarget(verified.strValue);

			return true;
		}
	}

	public static final class AttrDescAlt
			extends
				AttributeDescriptor
	{
		@Override
		public int getFlags()
		{
			return makeFlags(
					true /* removable */,
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
}
