package org.sweble.wikitext.engine.dom;

/**
 * An attribute of a tag extension.
 * 
 * The value of the attribute can be retrieved or altered with the getValue()
 * and setValue() methods of DomNode.
 */
public interface DomAttr
{
	/**
	 * Get the name of the attribute.
	 * 
	 * @return The name of the attribute.
	 */
	public String getName();
	
	/**
	 * Set the name of the attribute.
	 * 
	 * @param name
	 *            The new name of the attribute.
	 * @return The old name of the attribute.
	 */
	public String setName(String name);
}
