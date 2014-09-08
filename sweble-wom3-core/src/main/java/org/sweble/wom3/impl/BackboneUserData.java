/**
 * Copyright 2011 The Open Source Research Group,
 *                University of Erlangen-Nürnberg
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
		else // if (map != null)
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
