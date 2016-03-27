/**
 * Copyright 2011 The Open Source Research Group,
 *                University of Erlangen-Nürnberg
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses/.
 */
package org.sweble.wom3;

import java.util.Collection;

/**
 * The root node of every article.
 * 
 * Corresponds to the WXML 1.0 element "article".
 * 
 * <b>Child elements:</b> redirect? category* body
 */
public interface Wom3Article
		extends
			Wom3ElementNode
{
	/**
	 * Returns the full name of an article including namespace and path.
	 * 
	 * @return The namespace, path and article name concatenated.
	 */
	public String getName();

	/**
	 * Returns the version of the XWML object model.
	 * 
	 * @return The version of the XWML object model.
	 */
	public String getVersion();

	/**
	 * Returns the name of the article without namespace and without path.
	 * 
	 * Corresponds to the XWML 1.0 attribute "title".
	 * 
	 * @return The name of the article without namespace and without path.
	 */
	public String getTitle();

	/**
	 * Set the name of the article without namespace and without path.
	 * 
	 * Corresponds to the XWML 1.0 attribute "title".
	 * 
	 * @param title
	 *            The new title of the article.
	 * @return The old title of the article.
	 * @throws IllegalArgumentException
	 *             Thrown if the given title is empty or not a valid MediaWiki
	 *             article title.
	 * @throws NullPointerException
	 *             Thrown if the given title is <code>null</code>.
	 */
	public String setTitle(String title)
			throws IllegalArgumentException,
			NullPointerException;

	/**
	 * Returns the canonical namespace name.
	 * 
	 * Corresponds to the XWML 1.0 attribute "namespace".
	 * 
	 * @return The canonoical namespace name or <code>null</code> if the
	 *         attribute is not specified.
	 */
	public String getNamespace();

	/**
	 * Set the canonical namespace name.
	 * 
	 * Corresponds to the XWML 1.0 attribute "namespace".
	 * 
	 * @param namespace
	 *            The new namespace name or <code>null</code> to remove the
	 *            attribute.
	 * @return The old namespace name.
	 */
	public String setNamespace(String namespace);

	/**
	 * Returns the path of pages that lead to this subpage.
	 * 
	 * Corresponds to the XWML 1.0 attribute "path".
	 * 
	 * @return The path of pages that lead to this subpage or <code>null</code>
	 *         if this attribute is not given and this article is not a subpage.
	 *         The returned path never contains a trailing slash. The path is
	 *         also not anchored with a slash at the start but still always
	 *         denotes an absolute path.
	 */
	public String getPath();

	/**
	 * Set the path of pages that lead to this subpage.
	 * 
	 * @param path
	 *            The new path or <code>null</code> to remove the attribute. The
	 *            given path will be stripped of a trailing slash if present.
	 * @return The old path.
	 */
	public String setPath(String path);

	/**
	 * Tell whether this article is a redirecting article.
	 * 
	 * @return <code>True</code> if this article redirects to another article,
	 *         <code>false</code> otherwise.
	 */
	public boolean isRedirect();

	/**
	 * Get the redirection statement.
	 * 
	 * Operates on the first &lt;redirect> element found among this node's
	 * children.
	 * 
	 * @return The redirection statement or <code>null</code> if this article
	 *         does not redirect.
	 */
	public Wom3Redirect getRedirect();

	/**
	 * Set a redirection.
	 * 
	 * Operates on the first &lt;redirect> element found among this node's
	 * children. If no redirect node is found, the redirect will be added as the
	 * first child.
	 * 
	 * @param redirect
	 *            The new redirection to set or <code>null</code> to remove a
	 *            redirection.
	 * @return The old redirection.
	 */
	public Wom3Redirect setRedirect(Wom3Redirect redirect);

	/**
	 * Return a collection containing the categories that are assigned to this
	 * article.
	 * 
	 * @return A collection of categories.
	 */
	public Collection<Wom3Category> getCategories();

	/**
	 * Test if this article belongs to a certain category.
	 * 
	 * @param name
	 *            The case-insensitive name of the category to test.
	 * @return <code>True</code> if this article belongs to the category with
	 *         the specified name, <code>false</code> otherwise.
	 * @throws NullPointerException
	 *             Thrown if the specified name is <code>null</code>.
	 */
	public boolean hasCategory(String name) throws NullPointerException;

	/**
	 * Remove a category from this article.
	 * 
	 * @param name
	 *            The case-insensitive name of the category to remove.
	 * @return The removed category or <code>null</code> if the specified
	 *         category does not exist.
	 * @throws NullPointerException
	 *             Thrown if the specified name is <code>null</code>.
	 */
	public Wom3Category removeCategory(String name) throws NullPointerException;

	/**
	 * Add a category to this article.
	 * 
	 * @param name
	 *            The case-insensitive name of the category to add.
	 * @return The added category or an existing category if this article was
	 *         already assigned to the specified category.
	 * @throws NullPointerException
	 *             Thrown if the specified name is <code>null</code>.
	 */
	public Wom3Category addCategory(String name) throws NullPointerException;

	/**
	 * Get the article body.
	 * 
	 * Operates on the first &lt;body> element found among this node's children.
	 * 
	 * @return The body.
	 */
	public Wom3Body getBody();

	/**
	 * Set the article body.
	 * 
	 * Operates on the first &lt;body> element found among this node's children.
	 * 
	 * @param body
	 *            The new body.
	 * @return The old body.
	 * @throws NullPointerException
	 *             Thrown if <code>null</code> is given as body.
	 */
	public Wom3Body setBody(Wom3Body body) throws NullPointerException;
}
