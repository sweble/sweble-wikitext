/**
 * Copyright 2011 The Open Source Research Group,
 *                University of Erlangen-Nürnberg
 */
package org.sweble.wom3;

/**
 * Denotes the heading of a section.
 * 
 * The level of the heading is stored in the section node which is always the
 * parent node of a heading. This interface is only concerned with the content
 * of the heading and the content's formatting.
 * 
 * Corresponds to the WXML 1.0 element "heading". Also partly corresponds to the
 * XHTML 1.0 Transitional elements "h1" - "h6".
 * 
 * <b>Child elements:</b> Mixed, [Inline elements]*
 */
public interface Wom3Heading
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
