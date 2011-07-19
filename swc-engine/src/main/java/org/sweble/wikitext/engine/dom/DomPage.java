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
package org.sweble.wikitext.engine.dom;

/**
 * The root node of every page.
 */
public interface DomPage
        extends
            DomNode
{
	/**
	 * Returns the full name of an page including namespace and path.
	 */
	public String getName();
	
	/**
	 * Returns the version of the XWML DOM.
	 */
	public String getVersion();
	
	/**
	 * Returns the name of the page without namespace and without path.
	 */
	public String getTitle();
	
	/**
	 * Set the name of the page without namespace and without path.
	 * 
	 * @param title
	 *            The new title of the page.
	 * @return The old title of the page.
	 */
	public String setTitle(String title);
	
	/**
	 * Returns the canonical namespace name.
	 */
	public String getNamespace();
	
	/**
	 * Set the canonical namespace name.
	 * 
	 * @param namespace
	 *            The new namespace.
	 * @return The old namespace.
	 */
	public String setNamespace(String namespace);
	
	/**
	 * Returns the path of subpages that contain the page.
	 */
	public String getPath();
	
	/**
	 * Set the path of subpages that contain the page.
	 * 
	 * @param path
	 *            The new path.
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
	 * @return The redirection statement or <code>null</code> if this page does
	 *         not redirect.
	 */
	public DomRedirect getRedirect();
	
	/**
	 * Set a redirection.
	 * 
	 * @param redirect
	 *            The new redirection to set.
	 * @return The old redirection.
	 */
	public DomRedirect setRedirect(DomRedirect redirect);
}
