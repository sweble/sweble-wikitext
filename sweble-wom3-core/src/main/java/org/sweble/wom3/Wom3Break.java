/**
 * Copyright 2011 The Open Source Research Group,
 *                University of Erlangen-Nürnberg
 */
package org.sweble.wom3;

/**
 * Denotes a single line break.
 * 
 * Corresponds to the XHTML 1.0 Transitional element "br".
 * 
 * <b>Child elements:</b> -
 */
public interface Wom3Break
		extends
			Wom3ElementNode,
			Wom3CoreAttributes
{
	/**
	 * Get the sides on which floating elements are not allowed.
	 * 
	 * Corresponds to the XHTML 1.0 Transitional attribute "clear".
	 * 
	 * @return The sides on which floating elements are not allowed.
	 */
	public Wom3Clear getClear();
	
	/**
	 * Set the sides on which floating elements are not allowed.
	 * 
	 * Corresponds to the XHTML 1.0 Transitional attribute "clear".
	 * 
	 * @param clear
	 *            The new sides on which floating elements are not allowed or
	 *            <code>null</code> if the attribute is not specified.
	 * @return The old sides on which floating elements are not allowed.
	 */
	public Wom3Clear setClear(Wom3Clear clear);
}
