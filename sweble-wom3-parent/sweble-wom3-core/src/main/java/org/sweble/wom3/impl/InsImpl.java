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

import org.joda.time.DateTime;
import org.sweble.wom3.Wom3Ins;

public class InsImpl
		extends
			BackboneWomElemWithCoreAttrs
		implements
			Wom3Ins
{
	private static final long serialVersionUID = 1L;

	// =========================================================================

	public InsImpl(DocumentImpl owner)
	{
		super(owner);
	}

	// =========================================================================

	@Override
	public String getWomName()
	{
		return "ins";
	}

	// =========================================================================

	@Override
	public URL getCite()
	{
		return getUrlAttr("cite");
	}

	@Override
	public URL setCite(URL url)
	{
		return setUrlAttr(CommonAttributeDescriptors.ATTR_DESC_CITE, "cite", url);
	}

	@Override
	public DateTime getDatetime()
	{
		return setDatetimeAttr("datetime");
	}

	@Override
	public DateTime setDatetime(DateTime timestamp)
	{
		return setDatetimeAttr(CommonAttributeDescriptors.ATTR_DESC_DATETIME, "datetime", timestamp);
	}

	// =========================================================================

	private static final Map<String, AttributeDescriptor> NAME_MAP = new HashMap<String, AttributeDescriptor>();

	static
	{
		NAME_MAP.putAll(UniversalAttributes.getNameMap());
		NAME_MAP.put("cite", CommonAttributeDescriptors.ATTR_DESC_CITE);
		NAME_MAP.put("datetime", CommonAttributeDescriptors.ATTR_DESC_DATETIME);
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
