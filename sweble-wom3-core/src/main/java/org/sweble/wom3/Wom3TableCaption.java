/**
 * Copyright 2011 The Open Source Research Group,
 *                University of Erlangen-Nürnberg
 */
package org.sweble.wom3;

/**
 * Denotes a table caption.
 * 
 * Corresponds to the XHTML 1.0 Transitional element "caption".
 * 
 * <b>Child elements:</b> Mixed, [Inline elements]*
 */
public interface Wom3TableCaption
		extends
			Wom3ElementNode,
			Wom3UniversalAttributes
{
	/**
	 * Get the alignment of the caption.
	 * 
	 * Corresponds to the XHTML 1.0 Transitional attribute "align".
	 * 
	 * @return The alignment of the caption or <code>null</code> if the
	 *         attribute is not specified.
	 */
	public Wom3TableCaptionAlign getAlign();
	
	/**
	 * Set the alignment of the caption.
	 * 
	 * Corresponds to the XHTML 1.0 Transitional attribute "align".
	 * 
	 * @param align
	 *            The new alignment of the caption or <code>null</code> to
	 *            remove the attribute.
	 * @return The old alignment of the caption.
	 */
	public Wom3TableCaptionAlign setAlign(Wom3TableCaptionAlign align);
}
