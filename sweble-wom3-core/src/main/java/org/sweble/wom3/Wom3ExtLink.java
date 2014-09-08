/**
 * Copyright 2011 The Open Source Research Group,
 *                University of Erlangen-Nürnberg
 */
package org.sweble.wom3;

import java.net.URL;

/**
 * Denotes a Wikitext bracketed external link.
 * 
 * Corresponds to the WXML 1.0 element "extlink".
 * 
 * <b>Child elements:</b> title?
 */
public interface Wom3ExtLink
		extends
			Wom3ElementNode,
			Wom3Link
{
	/**
	 * Set the title of the external link.
	 * 
	 * @param title
	 *            The new title of the external link or <code>null</code> to
	 *            remove the title.
	 * @return The old link title node.
	 */
	public Wom3Title setLinkTitle(Wom3Title title);
	
	/**
	 * Retrieve the target of this link.
	 * 
	 * Corresponds to the XWML 1.0 attribute "target".
	 * 
	 * @return The target of this link.
	 */
	public URL getTarget();
	
	/**
	 * Set a new target for this external link.
	 * 
	 * Corresponds to the XWML 1.0 attribute "target".
	 * 
	 * @param target
	 *            The new target of the external link.
	 * @return The old target of the external link.
	 * @throws NullPointerException
	 *             Thrown if <code>null</code>is passed as URL.
	 */
	public URL setTarget(URL target) throws NullPointerException;
	
	public boolean isPlainUrl();
	
	public boolean setPlainUrl(boolean plainUrl);
	
	// ==[ Link interface ]=====================================================
	
	/**
	 * Returns the title of the external link. If the external link does not
	 * specify a title, an empty title is returned.
	 * 
	 * @return The title of the external link.
	 */
	@Override
	public Wom3Title getLinkTitle();
	
	/**
	 * Retrieve the target of this link.
	 * 
	 * @return The target of this link.
	 */
	@Override
	public URL getLinkTarget();
}
