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
 * Denotes a Wikitext internal link.
 * 
 * Corresponds to the WXML 1.0 element "intlink".
 * 
 * <b>Child elements:</b> title?
 */
public interface WomIntLink
		extends
			WomInlineElement,
			WomLink
{
	/**
	 * Get the title of the internal link.
	 * 
	 * @return The title of the internal link or <code>null</code> if the link
	 *         does not specify a title.
	 */
	public WomTitle getTitle();
	
	/**
	 * Set the title of the internal link.
	 * 
	 * @param title
	 *            The new title of the internal link or <code>null</code> to
	 *            remove the title.
	 * @return The old link title.
	 */
	public WomTitle setTitle(WomTitle title);
	
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
	public WomTitle getLinkTitle();
	
	/**
	 * Retrieve the target of this link.
	 * 
	 * @return The target of this link.
	 */
	@Override
	public String getLinkTarget();
}
