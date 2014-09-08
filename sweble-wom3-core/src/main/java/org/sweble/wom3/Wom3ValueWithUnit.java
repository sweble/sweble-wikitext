/**
 * Copyright 2011 The Open Source Research Group,
 *                University of Erlangen-Nürnberg
 */
package org.sweble.wom3;

import java.io.Serializable;

/**
 * An HTML value associated with its unit of measurement.
 */
public interface Wom3ValueWithUnit
		extends
			Cloneable,
			Serializable
{
	/**
	 * Get the unit of measurement of this value.
	 * 
	 * @return The unit.
	 */
	public Wom3Unit getUnit();
	
	/**
	 * Get the actual value.
	 * 
	 * @return The value.
	 */
	public float getValue();
	
	/**
	 * Get the actual value rounded to an integer.
	 * 
	 * @return The value as integer.
	 */
	public int getIntValue();
}
