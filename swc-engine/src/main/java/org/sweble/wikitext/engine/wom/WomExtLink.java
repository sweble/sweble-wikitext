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

import java.net.URL;

/**
 * Denotes a Wikitext bracketed external link.
 * 
 * Corresponds to the WXML 1.0 element "extlink".
 * 
 * <b>Child elements:</b> title?
 */
public interface WomExtLink
        extends
            WomInlineElement,
            WomLink
{
	/**
	 * Get the title of the external link.
	 * 
	 * @return The title of the external link or <code>null</code> if the link
	 *         does not specify a title.
	 */
	public WomTitle getTitle();
	
	/**
	 * Set the title of the external link.
	 * 
	 * @param title
	 *            The new title of the external link or <code>null</code> to
	 *            remove the title.
	 * @return The old link title node.
	 */
	public WomTitle setTitle(WomTitle title);
	
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
	
	// ==[ Link interface ]=====================================================
	
	/**
	 * Returns the title of the external link. If the external link does not
	 * specify a title, an empty title is returned.
	 * 
	 * @return The title of the external link.
	 */
	@Override
	public WomTitle getLinkTitle();
	
	/**
	 * Retrieve the target of this link.
	 * 
	 * @return The target of this link.
	 */
	@Override
	public URL getLinkTarget();
}
