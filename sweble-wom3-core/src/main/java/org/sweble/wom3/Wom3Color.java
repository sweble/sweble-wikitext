/**
 * Copyright 2011 The Open Source Research Group,
 *                University of Erlangen-Nürnberg
 */
package org.sweble.wom3;

import java.io.Serializable;

/**
 * A color value.
 */
public interface Wom3Color
		extends
			Cloneable,
			Serializable
{
	/**
	 * Get the red color component.
	 * 
	 * @return A value between 0 and 255.
	 */
	public int getRed();
	
	/**
	 * Get the green color component.
	 * 
	 * @return A value between 0 and 255.
	 */
	public int getGreen();
	
	/**
	 * Get the blue color component.
	 * 
	 * @return A value between 0 and 255.
	 */
	public int getBlue();
	
	/**
	 * Format color as #RRGGBB.
	 */
	@Override
	public String toString();
}
