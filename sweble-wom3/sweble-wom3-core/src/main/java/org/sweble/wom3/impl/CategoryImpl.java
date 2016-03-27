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

import org.sweble.wom3.Wom3Category;
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
		return setAttributeDirect(ATTR_DESC_NAME, "name", name);
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

	public static final AttrDescName ATTR_DESC_NAME = new AttrDescName();

	@Override
	protected AttributeDescriptor getAttributeDescriptor(
			String namespaceUri,
			String localName,
			String qualifiedName)
	{
		return getAttrDescStrict(namespaceUri, localName, qualifiedName,
				"name", ATTR_DESC_NAME);
	}

	public static final class AttrDescName
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

			Toolbox.checkValidCategory(verified.strValue);

			CategoryImpl catImpl = (CategoryImpl) parent;
			ArticleImpl pageImpl = (ArticleImpl) catImpl.getParentNode();
			if (pageImpl != null)
				pageImpl.validateCategoryNameChange(catImpl, verified.strValue);

			return true;
		}
	}
}
