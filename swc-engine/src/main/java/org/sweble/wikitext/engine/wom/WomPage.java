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

/**
 * The root node of every page.
 * 
 * Corresponds to the WXML 1.0 element "page".
 * 
 * <b>Child elements:</b> redirect? body
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
	public String setTitle(String title) throws IllegalArgumentException, NullPointerException;
	
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
	 *         if this attribute is not given and this page, therefore, is not a
	 *         subpage.
	 */
	public String getPath();
	
	/**
	 * Set the path of pages that lead to this subpage.
	 * 
	 * @param path
	 *            The new path or <code>null</code> to remove the attribute.
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
