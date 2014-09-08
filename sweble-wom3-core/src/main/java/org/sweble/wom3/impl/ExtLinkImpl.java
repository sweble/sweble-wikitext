/**
 * Copyright 2011 The Open Source Research Group,
 *                University of Erlangen-Nürnberg
 */
package org.sweble.wom3.impl;

import java.net.MalformedURLException;
import java.net.URL;

import org.sweble.wom3.Wom3ExtLink;
import org.sweble.wom3.Wom3Node;
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
		return setAttributeDirect(Attributes.TARGET, "target", target);
	}
	
	@Override
	public boolean isPlainUrl()
	{
		return getBoolAttr("plainurl");
	}
	
	@Override
	public boolean setPlainUrl(boolean plainUrl)
	{
		return setBoolAttr(Attributes.PLAINURL, "plainurl", plainUrl);
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
	
	@Override
	protected AttributeDescriptor getAttributeDescriptor(
			String namespaceURL,
			String localName,
			String qualifiedName)
	{
		return getAttrDescStrict(namespaceURL, localName, qualifiedName,
				"target", Attributes.TARGET,
				"plainurl", Attributes.PLAINURL);
	}
	
	private static enum Attributes implements AttributeDescriptor
	{
		TARGET
		{
			@Override
			public boolean verifyAndConvert(
					Backbone parent,
					NativeAndStringValuePair verified)
			{
				return AttributeVerifiers.URL.verifyAndConvert(parent, verified);
			}
		},
		PLAINURL
		{
			@Override
			public boolean verifyAndConvert(
					Backbone parent,
					NativeAndStringValuePair verified)
			{
				return AttributeVerifiers.verifyAndConvertBool(parent, verified, "plainurl");
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
