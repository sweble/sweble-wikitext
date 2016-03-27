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

import static org.sweble.wom3.impl.CommonAttributeDescriptors.ATTR_DESC_COLOR;
import static org.sweble.wom3.impl.CommonAttributeDescriptors.ATTR_DESC_FACE;
import static org.sweble.wom3.impl.CommonAttributeDescriptors.ATTR_DESC_FONT_SIZE;

import java.util.HashMap;
import java.util.Map;

import org.sweble.wom3.Wom3Color;
import org.sweble.wom3.Wom3Font;

public class FontImpl
		extends
			BackboneWomElemWithCoreAttrs
		implements
			Wom3Font
{
	private static final long serialVersionUID = 1L;

	// =========================================================================

	public FontImpl(DocumentImpl owner)
	{
		super(owner);
	}

	// =========================================================================

	@Override
	public String getWomName()
	{
		return "font";
	}

	// =========================================================================

	@Override
	public Wom3Color getColor()
	{
		return getColorAttr("color");
	}

	@Override
	public Wom3Color setColor(Wom3Color color)
	{
		return setColorAttr(ATTR_DESC_COLOR, "color", color);
	}

	@Override
	public String getFace()
	{
		return getStringAttr("face");
	}

	@Override
	public String setFace(String face)
	{
		return setStringAttr(ATTR_DESC_FACE, "face", face);
	}

	@Override
	public Integer getSize()
	{
		return getIntAttr("size");
	}

	@Override
	public Integer setSize(Integer size)
	{
		return setIntAttr(ATTR_DESC_FONT_SIZE, "size", size);
	}

	// =========================================================================

	private static final Map<String, AttributeDescriptor> NAME_MAP = new HashMap<String, AttributeDescriptor>();

	static
	{
		NAME_MAP.putAll(CoreAttributes.getNameMap());
		NAME_MAP.putAll(I18nAttributes.getNameMap());
		NAME_MAP.put("color", ATTR_DESC_COLOR);
		NAME_MAP.put("face", ATTR_DESC_FACE);
		NAME_MAP.put("size", ATTR_DESC_FONT_SIZE);
	}

	@Override
	protected AttributeDescriptor getAttributeDescriptor(
			String namespaceUri,
			String localName,
			String qualifiedName)
	{
		return getAttrDesc(namespaceUri, localName, qualifiedName, NAME_MAP);
	}
}
