/**
 * Copyright 2011 The Open Source Research Group,
 *                University of Erlangen-Nürnberg
 */
package org.sweble.wom3;

/**
 * Denotes a Wikitext internal link.
 * 
 * Corresponds to the WXML 1.0 element "intlink".
 * 
 * <b>Child elements:</b> title?
 */
public interface Wom3IntLink
		extends
			Wom3ElementNode,
			Wom3Link
{
	/**
	 * Set the title of the internal link.
	 * 
	 * @param title
	 *            The new title of the internal link or <code>null</code> to
	 *            remove the title.
	 * @return The old link title.
	 */
	public Wom3Title setLinkTitle(Wom3Title title);
	
	/**
	 * Get the target of the internal link.
	 * 
	 * Corresponds to the XWML 1.0 attribute "target".
	 * 
	 * @return The target of the internal link.
	 */
	public String getTarget();
	
	/**
	 * Set the target of this internal link.
	 * 
	 * Corresponds to the XWML 1.0 attribute "target".
	 * 
	 * @param target
	 *            The new target of the internal link.
	 * @return The old target of the internal link.
	 * @throws IllegalArgumentException
	 *             Thrown if the given target is empty or not a valid page
	 *             title.
	 * @throws NullPointerException
	 *             Thrown if <code>null</code> is passed as target.
	 */
	public String setTarget(String target) throws IllegalArgumentException, NullPointerException;
	
	// ==[ Link interface ]=====================================================
	
	/**
	 * Returns the title of the internal link. If the internal link does not
	 * specify a title, the target (which specifies a page title) is returned as
	 * title.
	 * 
	 * @return The title of the internal link.
	 */
	@Override
	public Wom3Title getLinkTitle();
	
	/**
	 * Retrieve the target of this link.
	 * 
	 * @return The target of this link.
	 */
	@Override
	public String getLinkTarget();
}
