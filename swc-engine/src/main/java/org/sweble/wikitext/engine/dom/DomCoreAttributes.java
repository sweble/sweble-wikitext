package org.sweble.wikitext.engine.dom;

/**
 * The XHTML 1.0 Transitional core attributes.
 */
public interface DomCoreAttributes
{
	/**
	 * Get the unique id of the element.
	 * 
	 * Corresponds to the XHTML 1.0 Transitional attribute "id".
	 * 
	 * @return The unique id of the element.
	 */
	public abstract String getId();
	
	/**
	 * Get the stylesheet classes assigned to this element.
	 * 
	 * Corresponds to the XHTML 1.0 Transitional attribute "class".
	 * 
	 * @return An array containing the names of the stylesheet classes.
	 */
	public abstract String[] getClasses();
	
	/**
	 * Get CSS styles directly associated with this element.
	 * 
	 * Corresponds to the XHTML 1.0 Transitional attribute "style".
	 * 
	 * @return The CSS styles directly associated with this element.
	 */
	public abstract String getStyle();
	
	/**
	 * Get the title of the element.
	 * 
	 * Corresponds to the XHTML 1.0 Transitional attribute "title".
	 * 
	 * @return The title of the element.
	 */
	public abstract String getTitlte();
}
