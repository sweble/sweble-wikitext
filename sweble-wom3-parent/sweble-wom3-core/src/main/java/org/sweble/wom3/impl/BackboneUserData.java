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
import java.util.Map.Entry;

import org.w3c.dom.UserDataHandler;

public class BackboneUserData
{

	private String onlyKey;

	private Object onlyValue;

	private Map<String, Object> map;

	// =========================================================================
	//
	public Object remove(String key)
	{
		Object oldValue = null;
		if ((onlyKey != null) && (onlyKey.equals(key)))
		{
			oldValue = onlyValue;
			onlyKey = null;
			onlyValue = null;
		}
		else if (map != null)
		{
			Object value = map.remove(key);
			oldValue = value;
			normalize();
		}
		return oldValue;
	}

	private void normalize()
	{
		if (map.size() == 1)
		{
			Entry<?, ?> e = map.entrySet().toArray(new Entry[1])[0];
			onlyKey = (String) e.getKey();
			onlyValue = e.getValue();
			map = null;
		}
		else if (map.isEmpty())
		{
			map = null;
		}
	}

	public Object set(String key, Object data, UserDataHandler handler)
	{
		if (handler != null)
		{
			throw new UnsupportedOperationException("Not supported yet.");
		}
		Object oldValue = null;
		if ((map == null) && (onlyKey == null))
		{
			onlyKey = key;
			onlyValue = data;
		}
		else if (onlyKey != null)
		{
			if (onlyKey.equals(key))
			{
				oldValue = onlyValue;
				onlyValue = data;
			}
			else
			{
				map = new HashMap<String, Object>();
				map.put(onlyKey, onlyValue);
				map.put(key, data);
				onlyKey = null;
				onlyValue = null;
			}
		}
		else
		// if (map != null)
		{
			oldValue = map.put(key, data);
		}
		return oldValue;
	}

	public Object get(String key)
	{
		if ((onlyKey != null) && (onlyKey.equals(key)))
		{
			return onlyValue;
		}
		if (map != null)
		{
			return map.get(key);
		}
		return null;
	}

	boolean isEmpty()
	{
		return (onlyKey == null) && (map == null);
	}

}
