/**
 * Copyright 2011 The Open Source Research Group,
 *                University of Erlangen-Nürnberg
 */
package org.sweble.wom3.impl;

public class NativeAndStringValuePair
{
	public Object value;
	
	public String strValue;
	
	public NativeAndStringValuePair(String strValue)
	{
		this.strValue = strValue;
	}
	
	public NativeAndStringValuePair(Object value)
	{
		this.value = value;
	}
}
