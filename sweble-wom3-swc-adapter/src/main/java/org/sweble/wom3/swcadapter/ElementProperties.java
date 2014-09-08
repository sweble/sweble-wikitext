/**
 * Copyright 2011 The Open Source Research Group,
 *                University of Erlangen-Nürnberg
 */
package org.sweble.wom3.swcadapter;

public enum ElementProperties
{
	P
	{
		@Override
		public String toString()
		{
			return "p";
		}
		
		@Override
		public boolean isBlockElement()
		{
			return true;
		}
	};
	
	public boolean isBlockElement()
	{
		return false;
	}
}
