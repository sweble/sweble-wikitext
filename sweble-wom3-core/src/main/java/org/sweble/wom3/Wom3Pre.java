/**
 * Copyright 2011 The Open Source Research Group,
 *                University of Erlangen-Nürnberg
 */
package org.sweble.wom3;

public interface Wom3Pre
		extends
			Wom3ElementNode,
			Wom3UniversalAttributes
{
	/**
	 * Get the number of characters per line.
	 * 
	 * Corresponds to the XHTML 1.0 Transitional attribute "width".
	 * 
	 * @return The number of characters per line or <code>null</code> if the
	 *         attribute is not specified.
	 */
	public Integer getWidth();
	
	/**
	 * Set the number of characters per line.
	 * 
	 * Corresponds to the XHTML 1.0 Transitional attribute "width".
	 * 
	 * @param width
	 *            The new number of characters per line or <code>null</code> to
	 *            remove the attribute.
	 * @return The old number of characters per line.
	 */
	public Integer setWidth(Integer width);
}
