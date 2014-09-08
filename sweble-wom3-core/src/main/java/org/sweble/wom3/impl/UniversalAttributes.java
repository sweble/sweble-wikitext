/**
 * Copyright 2011 The Open Source Research Group,
 *                University of Erlangen-Nürnberg
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
