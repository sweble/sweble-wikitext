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
 * Denotes a block of preformatted text.
 * 
 * The text is obtained through the getValue() method.
 * 
 * Corresponds to the XHTML 1.0 Transitional element "pre".
 */
public interface DomPre
        extends
            DomBlockElement,
            DomUniversalAttributes
{
	/**
	 * Get the number of characters per line.
	 * 
	 * Corresponds to the XHTML 1.0 Transitional attribute "width".
	 * 
	 * @return The number of characters per line.
	 */
	public int getWidth();
	
	/**
	 * Set the number of characters per line.
	 * 
	 * Corresponds to the XHTML 1.0 Transitional attribute "width".
	 * 
	 * @param width
	 *            The new number of characters per line.
	 * @return The old number of characters per line.
	 */
	public int setWidth(int width);
}
