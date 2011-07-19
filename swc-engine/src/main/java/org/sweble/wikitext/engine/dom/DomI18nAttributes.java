package org.sweble.wikitext.engine.dom;

/**
 * XHTML 1.0 Transitional Internationalization attributes.
 */
public interface DomI18nAttributes
{
	/**
	 * Get the text direction of the element.
	 * 
	 * Corresponds to the XHTML 1.0 Transitional attribute "dir".
	 * 
	 * @return The text direction of the element's content.
	 */
	public abstract I18nDir getDir();
	
	/**
	 * Get the language code for content inside this element.
	 * 
	 * Corresponds to the XHTML 1.0 Transitional attribute "lang".
	 * 
	 * @return The language code of the element's content.
	 */
	public abstract String getLang();
	
	/**
	 * Get the language code for content inside this element.
	 * 
	 * Corresponds to the XHTML 1.0 Transitional attribute "xml:lang".
	 * 
	 * @return The language code of the element's content.
	 */
	public abstract String getXmlLang();
}
