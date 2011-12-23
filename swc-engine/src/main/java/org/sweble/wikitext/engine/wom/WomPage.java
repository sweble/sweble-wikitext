/**
 * Copyright 2011 The Open Source Research Group,
 *                University of Erlangen-Nürnberg
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.sweble.wikitext.engine.wom;

import java.util.Collection;

/**
 * The root node of every page.
 * 
 * Corresponds to the WXML 1.0 element "page".
 * 
 * <b>Child elements:</b> redirect? category* body
 */
public interface WomPage
		extends
			WomNode
{
	/**
	 * Returns the full name of an page including namespace and path.
	 * 
	 * @return The namespace, path and page name concatenated.
	 */
	public String getName();
	
	/**
	 * Returns the version of the XWML object model.
	 * 
	 * @return The version of the XWML object model.
	 */
	public String getVersion();
	
	/**
	 * Returns the name of the page without namespace and without path.
	 * 
	 * Corresponds to the XWML 1.0 attribute "title".
	 * 
	 * @return The name of the page without namespace and without path.
	 */
	public String getTitle();
	
	/**
	 * Set the name of the page without namespace and without path.
	 * 
	 * Corresponds to the XWML 1.0 attribute "title".
	 * 
	 * @param title
	 *            The new title of the page.
	 * @return The old title of the page.
	 * @throws IllegalArgumentException
	 *             Thrown if the given title is empty or not a valid MediaWiki
	 *             page title.
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
	 *         if this attribute is not given and this page is not a subpage.
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
	 * Tell whether this page is a redirecting page.
	 * 
	 * @return <code>True</code> if this page redirects to another page,
	 *         <code>false</code> otherwise.
	 */
	public boolean isRedirect();
	
	/**
	 * Get the redirection statement.
	 * 
	 * Operates on the first &lt;redirect> element found among this node's
	 * children.
	 * 
	 * @return The redirection statement or <code>null</code> if this page does
	 *         not redirect.
	 */
	public WomRedirect getRedirect();
	
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
	public WomRedirect setRedirect(WomRedirect redirect);
	
	/**
	 * Return a collection containing the categories that are assigned to this
	 * page.
	 * 
	 * @return A collection of categories.
	 */
	public Collection<WomCategory> getCategories();
	
	/**
	 * Test if this page belongs to a certain category.
	 * 
	 * @param name
	 *            The case-insensitive name of the category to test.
	 * @return <code>True</code> if this page belongs to the category with the
	 *         specified name, <code>false</code> otherwise.
	 * @throws NullPointerException
	 *             Thrown if the specified name is <code>null</code>.
	 */
	public boolean hasCategory(String name) throws NullPointerException;
	
	/**
	 * Remove a category from this page.
	 * 
	 * @param name
	 *            The case-insensitive name of the category to remove.
	 * @return The removed category or <code>null</code> if the specified
	 *         category does not exist.
	 * @throws NullPointerException
	 *             Thrown if the specified name is <code>null</code>.
	 */
	public WomCategory removeCategory(String name) throws NullPointerException;
	
	/**
	 * Add a category to this page.
	 * 
	 * @param name
	 *            The case-insensitive name of the category to add.
	 * @return The added category or an existing category if this page was
	 *         already assigned to the specified category.
	 * @throws NullPointerException
	 *             Thrown if the specified name is <code>null</code>.
	 */
	public WomCategory setCategory(String name) throws NullPointerException;
	
	// FIXME: deprecate setCategory and use addCategory instead.
	// FIXME: overload addCategory with WomCategory argument
	
	/**
	 * Get the page body.
	 * 
	 * Operates on the first &lt;body> element found among this node's children.
	 * 
	 * @return The body.
	 */
	public WomBody getBody();
	
	/**
	 * Set the page body.
	 * 
	 * Operates on the first &lt;body> element found among this node's children.
	 * 
	 * @param body
	 *            The new body.
	 * @return The old body.
	 * @throws NullPointerException
	 *             Thrown if <code>null</code> is given as body.
	 */
	public WomBody setBody(WomBody body) throws NullPointerException;
}
