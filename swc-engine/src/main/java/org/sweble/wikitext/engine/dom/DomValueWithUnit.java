package org.sweble.wikitext.engine.dom;

/**
 * An HTML value associated with its unit of measurement.
 */
public interface DomValueWithUnit
{
	/**
	 * Get the unit of measurement of this value.
	 * 
	 * @return The unit.
	 */
	public DomUnit getUnit();
	
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
	public void set(float value, DomUnit unit);
	
	/**
	 * Set an integer value with its unit.
	 * 
	 * @param value
	 *            The value to set.
	 * @param unit
	 *            The unit to set.
	 */
	public void set(int value, DomUnit unit);
}
