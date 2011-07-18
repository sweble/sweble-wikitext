package org.sweble.wikitext.engine.dom;

public interface DomAttribute
        extends
            DomNode
{
	/**
	 * Attribute names are case-insensitive.
	 */
	public String getName();
	
	@Override
	public DomNode getNextSibling();
	
	@Override
	public DomNode getPrevSibling();
}
