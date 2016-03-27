/**
 * Copyright 2011 The Open Source Research Group,
 *                University of Erlangen-Nürnberg
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses/.
 */
package org.sweble.wom3;

/**
 * XHTML 1.0 Transitional Internationalization attributes.
 */
public interface Wom3I18nAttributes
{
	/**
	 * Get the text direction of the element.
	 * 
	 * Corresponds to the XHTML 1.0 Transitional attribute "dir".
	 * 
	 * @return The text direction of the element's content or <code>null</code>
	 *         if the attribute is not specified.
	 */
	public Wom3I18nDir getDir();

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
	public Wom3I18nDir setDir(Wom3I18nDir dir);

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
