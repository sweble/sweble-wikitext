/**
 * Copyright 2011 The Open Source Research Group,
 *                University of Erlangen-Nürnberg
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
