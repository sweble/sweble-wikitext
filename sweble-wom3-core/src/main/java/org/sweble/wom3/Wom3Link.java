/**
 * Copyright 2011 The Open Source Research Group,
 *                University of Erlangen-Nürnberg
 */
package org.sweble.wom3;

/**
 * This interface groups elements of Wikitext that link to different pages/urls.
 */
public interface Wom3Link
		extends
			Wom3Node
{
	/**
	 * Returns the title of this link.
	 * 
	 * @return The title node or <code>null</code> if the link does not support
	 *         a title or does not specify a title.
	 */
	public Wom3Title getLinkTitle();
	
	/**
	 * Retrieve the target of this link.
	 * 
	 * @return The target of this link.
	 */
	public Object getLinkTarget();
}
