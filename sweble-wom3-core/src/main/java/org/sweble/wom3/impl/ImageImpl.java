/**
 * Copyright 2011 The Open Source Research Group,
 *                University of Erlangen-Nürnberg
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
import org.sweble.wom3.Wom3Node;
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
		return setAttributeDirect(Attributes.SOURCE, "source", source);
	}
	
	@Override
	public Wom3ImageFormat getFormat()
	{
		return (Wom3ImageFormat) getAttributeNativeData("format");
	}
	
	@Override
	public Wom3ImageFormat setFormat(Wom3ImageFormat format) throws NullPointerException
	{
		return setAttributeDirect(Attributes.FORMAT, "format", format);
	}
	
	@Override
	public boolean isBorder()
	{
		return getAttributeNativeData("border") != null;
	}
	
	@Override
	public boolean setBorder(boolean border)
	{
		return setAttributeDirect(Attributes.BORDER, "border", border) != null;
	}
	
	@Override
	public Wom3ImageHAlign getHAlign()
	{
		return (Wom3ImageHAlign) getAttributeNativeData("halign");
	}
	
	@Override
	public Wom3ImageHAlign setHAlign(Wom3ImageHAlign halign)
	{
		return setAttributeDirect(Attributes.HALIGN, "halign", halign);
	}
	
	@Override
	public Wom3ImageVAlign getVAlign()
	{
		return (Wom3ImageVAlign) getAttributeNativeData("valign");
	}
	
	@Override
	public Wom3ImageVAlign setVAlign(Wom3ImageVAlign valign)
	{
		return setAttributeDirect(Attributes.VALIGN, "valign", valign);
	}
	
	@Override
	public Integer getWidth()
	{
		return (Integer) getAttributeNativeData("width");
	}
	
	@Override
	public Integer setWidth(Integer width)
	{
		return setAttributeDirect(Attributes.WIDTH, "width", width);
	}
	
	@Override
	public Integer getHeight()
	{
		return (Integer) getAttributeNativeData("height");
	}
	
	@Override
	public Integer setHeight(Integer height)
	{
		return setAttributeDirect(Attributes.HEIGHT, "height", height);
	}
	
	@Override
	public boolean isUpright()
	{
		return getAttributeNativeData("upright") != null;
	}
	
	@Override
	public boolean setUpright(boolean upright)
	{
		return setAttributeDirect(Attributes.UPRIGHT, "upright", upright) != null;
	}
	
	@Override
	public URL getExtLink()
	{
		return getUrlAttr("extlink");
	}
	
	@Override
	public URL setExtLink(URL url)
	{
		return setUrlAttr(Attributes.EXTLINK, "extlink", url);
	}
	
	@Override
	public String getIntLink()
	{
		return getStringAttr("intlink");
	}
	
	@Override
	public String setIntLink(String target)
	{
		return setAttributeDirect(Attributes.INTLINK, "intlink", target);
	}
	
	@Override
	public String getAlt()
	{
		return getStringAttr("alt");
	}
	
	@Override
	public String setAlt(String alt)
	{
		return setAttributeDirect(Attributes.ALT, "alt", alt);
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
	
	private static final Map<String, AttributeDescriptor> nameMap = getNameMap();
	
	private static Map<String, AttributeDescriptor> getNameMap()
	{
		Map<String, AttributeDescriptor> nameMap =
				new HashMap<String, AttributeDescriptor>();
		
		nameMap.put("source", Attributes.SOURCE);
		nameMap.put("format", Attributes.FORMAT);
		nameMap.put("border", Attributes.BORDER);
		nameMap.put("halign", Attributes.HALIGN);
		nameMap.put("valign", Attributes.VALIGN);
		nameMap.put("width", Attributes.WIDTH);
		nameMap.put("height", Attributes.HEIGHT);
		nameMap.put("upright", Attributes.UPRIGHT);
		nameMap.put("extlink", Attributes.EXTLINK);
		nameMap.put("intlink", Attributes.INTLINK);
		nameMap.put("alt", Attributes.ALT);
		
		return nameMap;
	}
	
	@Override
	protected AttributeDescriptor getAttributeDescriptor(
			String namespaceUri,
			String localName,
			String qualifiedName)
	{
		return getAttrDescStrict(namespaceUri, localName, qualifiedName, nameMap);
	}
	
	private static enum Attributes implements AttributeDescriptor
	{
		SOURCE
		{
			@Override
			public boolean verifyAndConvert(
					Backbone parent,
					NativeAndStringValuePair verified)
			{
				if (verified.strValue == null)
					verified.strValue = (String) verified.value;
				
				// TODO: Check if image target or just page
				Toolbox.checkValidTarget(verified.strValue);
				return true;
			}
			
			@Override
			public boolean isRemovable()
			{
				return false;
			}
		},
		FORMAT
		{
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
		},
		BORDER
		{
			@Override
			public boolean verifyAndConvert(
					Backbone parent,
					NativeAndStringValuePair verified)
			{
				return AttributeVerifiers.verifyAndConvertBool(parent, verified, "border");
			}
		},
		HALIGN
		{
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
		},
		VALIGN
		{
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
		},
		WIDTH
		{
			@Override
			public boolean verifyAndConvert(
					Backbone parent,
					NativeAndStringValuePair verified)
			{
				return AttributeVerifiers.PIXELS.verifyAndConvert(parent, verified);
			}
		},
		HEIGHT
		{
			@Override
			public boolean verifyAndConvert(
					Backbone parent,
					NativeAndStringValuePair verified)
			{
				return AttributeVerifiers.PIXELS.verifyAndConvert(parent, verified);
			}
		},
		UPRIGHT
		{
			@Override
			public boolean verifyAndConvert(
					Backbone parent,
					NativeAndStringValuePair verified)
			{
				return AttributeVerifiers.verifyAndConvertBool(parent, verified, "upright");
			}
		},
		EXTLINK
		{
			@Override
			public boolean verifyAndConvert(
					Backbone parent,
					NativeAndStringValuePair verified)
			{
				return AttributeVerifiers.URL.verifyAndConvert(parent, verified);
			}
		},
		INTLINK
		{
			@Override
			public boolean verifyAndConvert(
					Backbone parent,
					NativeAndStringValuePair verified)
			{
				if (verified.strValue == null)
					verified.strValue = (String) verified.value;
				if (!verified.strValue.isEmpty())
					Toolbox.checkValidTarget(verified.strValue);
				return true;
			}
		},
		ALT
		{
			@Override
			public boolean verifyAndConvert(
					Backbone parent,
					NativeAndStringValuePair verified)
			{
				return true;
			}
		};
		
		// =====================================================================
		
		@Override
		public boolean isRemovable()
		{
			return true;
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
