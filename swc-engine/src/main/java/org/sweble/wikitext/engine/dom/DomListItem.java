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
 * Denotes a list item.
 * 
 * Corresponds to the XHTML 1.0 Transitional element "li".
 */
public interface DomListItem
        extends
            DomNode,
            DomUniversalAttributes
{
	/**
	 * Get the type of bullet point the list item uses.
	 * 
	 * Corresponds to the XHTML 1.0 Transitional attribute "type".
	 * 
	 * @return The type of bullet point.
	 */
	public String getItemType();
	
	/**
	 * Get the number of the list item.
	 * 
	 * Corresponds to the XHTML 1.0 Transitional attribute "value".
	 * 
	 * @return The number of the list item.
	 */
	public int getItemValue();
}
