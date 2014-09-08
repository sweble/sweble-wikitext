/**
 * Copyright 2011 The Open Source Research Group,
 *                University of Erlangen-Nürnberg
 */
package org.sweble.wom3;

import java.net.URL;

/**
 * Denotes a long quotation.
 * 
 * Corresponds to the XHTML 1.0 Transitional element "blockquote".
 * 
 * <b>Child elements:</b> [Block elements]*
 */
public interface Wom3Blockquote
		extends
			Wom3ElementNode,
			Wom3UniversalAttributes
{
	/**
	 * Get source of the quotation.
	 * 
	 * Corresponds to the XHTML 1.0 Transitional attribute "cite".
	 * 
	 * @return The source of the citation or <code>null</code> if the attribute
	 *         is not specified.
	 */
	public URL getCite();
	
	/**
	 * Set the source of the quotation.
	 * 
	 * Corresponds to the XHTML 1.0 Transitional attribute "cite".
	 * 
	 * @param source
	 *            The source of the citation or <code>null</code> to remove the
	 *            attribute.
	 * @return The source of the citation.
	 */
	public URL setCite(URL source);
}
