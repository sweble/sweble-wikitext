/**
 * Copyright 2011 The Open Source Research Group,
 *                University of Erlangen-Nürnberg
 */
package org.sweble.wom3;

/**
 * Denotes a paragraph.
 * 
 * Corresponds to the XHTML 1.0 Transitional element "p".
 * 
 * <b>Child elements:</b> Mixed, [Inline elements]*
 */
public interface Wom3Paragraph
		extends
			Wom3ElementNode,
			Wom3UniversalAttributes
{
	/**
	 * Get the number of empty lines in front of the paragraph.
	 * 
	 * This is a Wikitext-specific extension. In Wikitext empty lines in front
	 * of the text of a paragraph result in additional empty paragraphs and line
	 * breaks in front of the HTML paragraph. The number of empty lines
	 * determines the number of empty paragraphs and line breaks and is returned
	 * by this method. This attribute counts the number of empty lines minus one
	 * since one of the empty lines is interpreted as paragraph separator and
	 * does not add to the gap itself.
	 * 
	 * @return The number of empty lines in front of the paragraph that affect
	 *         the size of the gap or <code>0</code> if the attribute is not
	 *         specified.
	 */
	public int getTopGap();
	
	/**
	 * Set the number of empty lines in front of the paragraph.
	 * 
	 * See getTopGap() for details.
	 * 
	 * @param lines
	 *            The number of empty lines in front of the paragraph that
	 *            affect the size of the gap.
	 * @return The old number of lines or <code>0</code> if the attribute is not
	 *         specified.
	 */
	public int setTopGap(int lines);
	
	/**
	 * Get the number of empty lines following the paragraph.
	 * 
	 * See getTopGap() for details.
	 * 
	 * @return The number of empty lines following of the paragraph that affect
	 *         the size of the gap or <code>0</code> if the attribute is not
	 *         specified.
	 */
	public int getBottomGap();
	
	/**
	 * Set the number of empty lines following the paragraph.
	 * 
	 * See getTopGap() for details.
	 * 
	 * @param lines
	 *            The number of empty lines following the paragraph that affect
	 *            the size of the gap.
	 * @return The old number of lines or <code>0</code> if the attribute is not
	 *         specified.
	 */
	public int setBottomGap(int lines);
	
	/**
	 * Get the alignment of the content inside the tag.
	 * 
	 * Corresponds to the XHTML 1.0 Transitional attribute "align".
	 * 
	 * @return The alignment.
	 */
	public Wom3HorizAlign getAlign();
	
	/**
	 * Set the alignment of the content inside the tag.
	 * 
	 * Corresponds to the XHTML 1.0 Transitional attribute "align".
	 * 
	 * @param align
	 *            The new alignment.
	 * @return The old alignment.
	 */
	public Wom3HorizAlign setAlign(Wom3HorizAlign align);
}
