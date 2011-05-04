package org.sweble.wikitext.engine.dom;

public interface DomTable
        extends
            DomBlockElement
{
	public DomTableCaption getCaption();
	
	public DomTableHeader getHeader();
	
	public DomTableBody getBody();
}
