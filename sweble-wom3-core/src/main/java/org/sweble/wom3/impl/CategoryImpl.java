/**
 * Copyright 2011 The Open Source Research Group,
 *                University of Erlangen-Nürnberg
 */
package org.sweble.wom3.impl;

import org.sweble.wom3.Wom3Category;
import org.sweble.wom3.Wom3Node;
import org.sweble.wom3.Wom3Title;

public class CategoryImpl
		extends
			BackboneWomElement
		implements
			Wom3Category
{
	private static final long serialVersionUID = 1L;
	
	// =========================================================================
	
	public CategoryImpl(DocumentImpl owner)
	{
		super(owner);
	}
	
	// =========================================================================
	
	@Override
	public String getWomName()
	{
		return "category";
	}
	
	// =========================================================================
	
	@Override
	public String getName()
	{
		return getAttribute("name");
	}
	
	@Override
	public String setName(String name)
	{
		return setAttributeDirect(Attributes.NAME, "name", name);
	}
	
	@Override
	public Wom3Title getLinkTitle()
	{
		// TODO: Implement
		throw new UnsupportedOperationException();
	}
	
	@Override
	public String getLinkTarget()
	{
		// TODO: Implement
		throw new UnsupportedOperationException();
		
		/*
		// TODO: It's not always "Category:"!
		return "Category:" + getName();
		*/
	}
	
	// =========================================================================
	
	@Override
	protected AttributeDescriptor getAttributeDescriptor(
			String namespaceUri,
			String localName,
			String qualifiedName)
	{
		return getAttrDescStrict(namespaceUri, localName, qualifiedName,
				"name", Attributes.NAME);
	}
	
	private static enum Attributes implements AttributeDescriptor
	{
		NAME
		{
			@Override
			public boolean verifyAndConvert(
					Backbone parent,
					NativeAndStringValuePair verified)
			{
				if (verified.strValue == null)
					verified.strValue = (String) verified.value;
				
				Toolbox.checkValidCategory(verified.strValue);
				
				CategoryImpl catImpl = (CategoryImpl) parent;
				ArticleImpl pageImpl = (ArticleImpl) catImpl.getParentNode();
				if (pageImpl != null)
					pageImpl.validateCategoryNameChange(catImpl, verified.strValue);
				
				return true;
			}
			
			@Override
			public void customAction(
					Wom3Node parent,
					AttributeBase oldAttr,
					AttributeBase newAttr)
			{
			}
		};
		
		@Override
		public Normalization getNormalizationMode()
		{
			return Normalization.NON_CDATA;
		}
		
		@Override
		public boolean isRemovable()
		{
			return false;
		}
	}
}
