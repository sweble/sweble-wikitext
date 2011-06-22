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
 * XHTML 1.0 Transitional Internationalization attributes.
 */
public interface WomI18nAttributes
{
	/**
	 * Get the text direction of the element.
	 * 
	 * Corresponds to the XHTML 1.0 Transitional attribute "dir".
	 * 
	 * @return The text direction of the element's content or <code>null</code>
	 *         if the attribute is not specified.
	 */
	public WomI18nDir getDir();
	
	/**
	 * Get the text direction of the element.
	 * 
	 * Corresponds to the XHTML 1.0 Transitional attribute "dir".
	 * 
	 * @param dir
	 *            The new text direction or <code>null</code> to remove the
	 *            attribute.
	 * @return The old text direction.
	 */
	public WomI18nDir setDir(WomI18nDir dir);
	
	/**
	 * Get the language code for content inside this element.
	 * 
	 * Corresponds to the XHTML 1.0 Transitional attribute "lang".
	 * 
	 * @return The language code of the element's content or <code>null</code>
	 *         if the attribute is not specified.
	 */
	public String getLang();
	
	/**
	 * Get the language code for content inside this element.
	 * 
	 * Corresponds to the XHTML 1.0 Transitional attribute "lang".
	 * 
	 * @param lang
	 *            The new language code or <code>null</code> to remove the
	 *            attribute.
	 * @return The old language code.
	 */
	public String setLang(String lang);
	
	/**
	 * Get the language code for content inside this element.
	 * 
	 * Corresponds to the XHTML 1.0 Transitional attribute "xml:lang".
	 * 
	 * @return The language code of the element's content or <code>null</code>
	 *         if the attribute is not specified.
	 */
	public String getXmlLang();
	
	/**
	 * Get the language code for content inside this element.
	 * 
	 * Corresponds to the XHTML 1.0 Transitional attribute "xml:lang".
	 * 
	 * @param lang
	 *            The new language code or <code>null</code> to remove the
	 *            attribute.
	 * @return The old language code.
	 */
	public String setXmlLang(String lang);
}
