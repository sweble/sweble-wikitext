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

import static org.sweble.wom3.impl.CommonAttributeDescriptors.ATTR_DESC_ALIGN_LCR;
import static org.sweble.wom3.impl.CommonAttributeDescriptors.ATTR_DESC_NOSHADE;
import static org.sweble.wom3.impl.CommonAttributeDescriptors.ATTR_DESC_SIZE;
import static org.sweble.wom3.impl.CommonAttributeDescriptors.ATTR_DESC_WIDTH_LENGTH;

import java.util.HashMap;
import java.util.Map;

import org.sweble.wom3.Wom3HorizAlign;
import org.sweble.wom3.Wom3HorizontalRule;
import org.sweble.wom3.Wom3ValueWithUnit;

public class HorizontalRuleImpl
		extends
			BackboneWomElemWithCoreAttrs
		implements
			Wom3HorizontalRule
{
	private static final long serialVersionUID = 1L;

	// =========================================================================

	public HorizontalRuleImpl(DocumentImpl owner)
	{
		super(owner);
	}

	// =========================================================================

	@Override
	public String getWomName()
	{
		return "hr";
	}

	// =========================================================================

	@Override
	public Wom3HorizAlign getAlign()
	{
		return getAlignAttr("align");
	}

	@Override
	public Wom3HorizAlign setAlign(Wom3HorizAlign align)
	{
		return setAlignAttr(ATTR_DESC_ALIGN_LCR, "align", align);
	}

	@Override
	public boolean isNoshade()
	{
		return getBoolAttr("noshade");
	}

	@Override
	public boolean setNoshade(boolean noshade)
	{
		return setBoolAttr(ATTR_DESC_NOSHADE, "noshade", noshade);
	}

	@Override
	public Integer getSize()
	{
		return getIntAttr("size");
	}

	@Override
	public Integer setSize(Integer size)
	{
		return setIntAttr(ATTR_DESC_SIZE, "size", size);
	}

	@Override
	public Wom3ValueWithUnit getWidth()
	{
		return getValueWithUnitAttr("width");
	}

	@Override
	public Wom3ValueWithUnit setWidth(Wom3ValueWithUnit width)
	{
		return setValueWithUnitAttr(ATTR_DESC_WIDTH_LENGTH, "width", width);
	}

	// =========================================================================

	private static final Map<String, AttributeDescriptor> NAME_MAP = new HashMap<String, AttributeDescriptor>();

	{
		NAME_MAP.putAll(UniversalAttributes.getNameMap());
		NAME_MAP.put("align", ATTR_DESC_ALIGN_LCR);
		NAME_MAP.put("noshade", ATTR_DESC_NOSHADE);
		NAME_MAP.put("size", ATTR_DESC_SIZE);
		NAME_MAP.put("width", ATTR_DESC_WIDTH_LENGTH);
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
