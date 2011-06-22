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
 * Denotes a block of preformatted text.
 * 
 * Corresponds to the XWML 1.0 element "pre".
 * 
 * <b>Child elements:</b> Text
 */
public interface WomPre
        extends
            WomBlockElement,
            WomUniversalAttributes
{
	/**
	 * Return the text inside the pre element.
	 * 
	 * @return The text inside the pre element.
	 */
	@Override
	public String getValue();
	
	/**
	 * Set the text inside the pre element.
	 * 
	 * @param text
	 *            The new text.
	 * @return The old text.
	 * @throws NullPointerException
	 *             Thrown if <code>null</code> is passed as text.
	 * @throws IllegalArgumentException
	 *             Thrown if the given text contains "&lt;/pre>".
	 */
	public String setValue(String text) throws IllegalArgumentException, NullPointerException;
	
	/**
	 * Get the number of characters per line.
	 * 
	 * Corresponds to the XHTML 1.0 Transitional attribute "width".
	 * 
	 * @return The number of characters per line or <code>null</code> if the
	 *         attribute is not specified.
	 */
	public Integer getWidth();
	
	/**
	 * Set the number of characters per line.
	 * 
	 * Corresponds to the XHTML 1.0 Transitional attribute "width".
	 * 
	 * @param width
	 *            The new number of characters per line or <code>null</code> to
	 *            remove the attribute.
	 * @return The old number of characters per line.
	 */
	public Integer setWidth(Integer width);
}
