/**
 * Copyright 2011 The Open Source Research Group,
 *                University of Erlangen-Nürnberg
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.sweble.wikitext.engine.wom;

/**
 * An HTML value associated with its unit of measurement.
 */
public interface WomValueWithUnit
{
	/**
	 * Get the unit of measurement of this value.
	 * 
	 * @return The unit.
	 */
	public WomUnit getUnit();
	
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
	
	/**
	 * Set a float value with its unit.
	 * 
	 * @param value
	 *            The value to set.
	 * @param unit
	 *            The unit to set.
	 */
	public void set(float value, WomUnit unit);
	
	/**
	 * Set an integer value with its unit.
	 * 
	 * @param value
	 *            The value to set.
	 * @param unit
	 *            The unit to set.
	 */
	public void set(int value, WomUnit unit);
}
