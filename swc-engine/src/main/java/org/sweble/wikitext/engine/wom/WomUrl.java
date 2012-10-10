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
 * A Wikitext plain url.
 * 
 * Corresponds to the WXML 1.0 element "url".
 * 
 * <b>Child elements:</b> -
 */
public interface WomUrl
		extends
			WomInlineElement,
			WomLink
{
	/**
	 * Get the target for this external link.
	 * 
	 * @return The target of the external link.
	 */
	public URL getTarget();
	
	/**
	 * Set the target for this external link.
	 * 
	 * @param target
	 *            The new target of the external link.
	 * @return The old target of the external link.
	 * @throws NullPointerException
	 *             Thrown if <code>null</code> is given as target.
	 */
	public URL setTarget(URL target) throws NullPointerException;
	
	// ==[ Link interface ]=====================================================
	
	/**
	 * Returns the URL as title of this link.
	 * 
	 * @return The URL as title of this link.
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
