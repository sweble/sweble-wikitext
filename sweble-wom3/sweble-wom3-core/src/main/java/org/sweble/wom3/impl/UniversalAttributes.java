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

import java.util.HashMap;
import java.util.Map;

public enum UniversalAttributes
{
	/* No additional attributes */;

	private static final Map<String, AttributeDescriptor> nameMap;

	public static Map<String, AttributeDescriptor> getNameMap()
	{
		if (nameMap != null)
			return nameMap;

		Map<String, AttributeDescriptor> nameMap =
				new HashMap<String, AttributeDescriptor>();

		nameMap.putAll(CoreAttributes.getNameMap());
		nameMap.putAll(I18nAttributes.getNameMap());
		nameMap.putAll(EventAttributes.getNameMap());

		return nameMap;
	}

	static
	{
		nameMap = getNameMap();
	}

	public static AttributeDescriptor getDescriptor(String name)
	{
		return nameMap.get(name);
	}
}
