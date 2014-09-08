/**
 * Copyright 2011 The Open Source Research Group,
 *                University of Erlangen-Nürnberg
 */
package org.sweble.wom3;

/**
 * Denotes a general block.
 * 
 * Corresponds to the XHTML 1.0 Transitional element "div".
 * 
 * <b>Child elements:</b> Mixed, [Flow elements]*
 */
public interface Wom3Div
		extends
			Wom3ElementNode,
			Wom3UniversalAttributes
{
	/**
	 * Get the alignment of the content inside the tag.
	 * 
	 * Corresponds to the XHTML 1.0 Transitional attribute "align".
	 * 
	 * @return The alignment or <code>null</code> if the attribute is not
	 *         specified.
	 */
	public Wom3HorizAlign getAlign();
	
	/**
	 * Set the alignment of the content inside the tag.
	 * 
	 * Corresponds to the XHTML 1.0 Transitional attribute "align".
	 * 
	 * @param align
	 *            The new alignment or <code>null</code> to remove the
	 *            attribute. Only the values LEFT, RIGHT, CENTER and JUSTIFY are
	 *            allowed.
	 * @return The old alignment.
	 * @throws IllegalArgumentException
	 *             Thrown if an illegal value is specified as alignment.
	 */
	public Wom3HorizAlign setAlign(Wom3HorizAlign align) throws IllegalArgumentException;
}
