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
 * Specifies the font color, font face and size of the text content.
 * 
 * Corresponds to the XHTML 1.0 Transitional element "font".
 */
public interface DomFont
        extends
            DomInlineElement,
            DomUniversalAttributes
{
	/**
	 * Get the color of the text content.
	 * 
	 * @return The text color.
	 */
	public DomColor getColor();
	
	/**
	 * Set the color of the text content.
	 * 
	 * @return The old color of the text content.
	 */
	public DomColor setColor(DomColor color);
	
	/**
	 * Get the name of the font face of the text content.
	 * 
	 * @return The name of the font face.
	 */
	public String getFace();
	
	/**
	 * Set the name of the font face.
	 * 
	 * @param face
	 *            The name of the new font face.
	 * @return The name of the old font face.
	 */
	public String setFace(String face);
	
	/**
	 * Get the size of the text content.
	 * 
	 * @return The size of the text content. A value between 1 and 7.
	 */
	public int getSize();
	
	/**
	 * Set the text size.
	 * 
	 * @param size
	 *            The new text size. A value between 1 and 7.
	 * @return The old text size.
	 */
	public int setSize(int size);
}
