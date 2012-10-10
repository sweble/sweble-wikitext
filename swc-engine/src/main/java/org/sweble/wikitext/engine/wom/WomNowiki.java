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
 * Wraps text that must not be interpreted.
 * 
 * Corresponds to the WXML 1.0 element "nowiki".
 * 
 * <b>Child elements:</b> WtText
 */
public interface WomNowiki
		extends
			WomInlineElement
{
	/**
	 * Return the text inside the nowiki element.
	 * 
	 * @return The text inside the nowiki element.
	 */
	@Override
	public String getValue();
	
	/**
	 * Set the text inside the nowiki element.
	 * 
	 * @param text
	 *            The new text.
	 * @return The old text.
	 * @throws NullPointerException
	 *             Thrown if <code>null</code> is passed as text.
	 * @throws IllegalArgumentException
	 *             Thrown if the given text contains "&lt;/nowiki>".
	 */
	public String setValue(String text) throws IllegalArgumentException, NullPointerException;
}
