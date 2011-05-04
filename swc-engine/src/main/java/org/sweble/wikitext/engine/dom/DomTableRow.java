package org.sweble.wikitext.engine.dom;

public interface DomTableRow
        extends
            DomNode
{
	public void addCell(DomTableCell cell);
	
	public void insertCell(int before, DomTableCell cell) throws IndexOutOfBoundsException;
	
	public void insertCell(DomTableCell before, DomTableCell cell) throws IllegalArgumentException;
	
	public DomTableCell replaceCell(int col, DomTableCell replace) throws IndexOutOfBoundsException;
	
	public boolean replaceCell(DomTableCell search, DomTableCell replace);
	
	public DomTableCell removeCell(int col) throws IndexOutOfBoundsException;
	
	public boolean removeCell(DomTableCell cell);
}
