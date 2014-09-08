/**
 * Copyright 2011 The Open Source Research Group,
 *                University of Erlangen-Nürnberg
 */
package org.sweble.wom3;

/**
 * A category statement.
 * 
 * Corresponds to the XWML 1.0 element "category".
 * 
 * <b>Child elements:</b> -
 */
public interface Wom3Category
		extends
			Wom3ElementNode,
			Wom3Link
{
	/**
	 * Return the name of the category.
	 * 
	 * Corresponds to the XWML 1.0 attribute "name".
	 * 
	 * @return The name of the category without the category namespace
	 *         specifier.
	 */
	public String getName();
	
	/**
	 * Set the name of the category.
	 * 
	 * Corresponds to the XWML 1.0 attribute "name".
	 * 
	 * @param name
	 *            The new name of the category without the category namespace
	 *            specifier.
	 * @return The old name of the category.
	 * @throws NullPointerException
	 *             Thrown if the given category is <code>null</code>.
	 * @throws IllegalArgumentException
	 *             Thrown if another category with the given name is already
	 *             associated with this page.
	 */
	public String setName(String name) throws NullPointerException, IllegalArgumentException;
	
	// ==[ Link interface ]=====================================================
	
	/**
	 * Returns an empty title since category statements do not provide a title.
	 * 
	 * @return An empty title.
	 */
	@Override
	public Wom3Title getLinkTitle();
	
	/**
	 * Return the name of the category this statement is pointing to.
	 * 
	 * @return The name of the category including the category namespace
	 *         specifier.
	 */
	@Override
	public String getLinkTarget();
}
