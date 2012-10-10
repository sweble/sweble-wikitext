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
 * A redirection statement.
 * 
 * Corresponds to the XWML 1.0 element "redirect".
 * 
 * <b>Child elements:</b> -
 */
public interface WomRedirect
		extends
			WomProcessingInstruction,
			WomLink
{
	/**
	 * Return the target page of the redirection.
	 * 
	 * Corresponds to the XWML 1.0 attribute "target".
	 * 
	 * @return The target page to redirect to.
	 */
	public String getTarget();
	
	/**
	 * Set the target page of the redirection.
	 * 
	 * Corresponds to the XWML 1.0 attribute "target".
	 * 
	 * @param page
	 *            The new target of the redirection.
	 * @return The old target of the redirection.
	 */
	public String setTarget(String page);
	
	// ==[ Link interface ]=====================================================
	
	/**
	 * Returns an empty title since redirection statements do not provide a
	 * title.
	 * 
	 * @return An empty title.
	 */
	@Override
	public WomTitle getLinkTitle();
	
	/**
	 * Return the target page of the redirection.
	 * 
	 * @return The target page to redirect to.
	 */
	@Override
	public String getLinkTarget();
}
