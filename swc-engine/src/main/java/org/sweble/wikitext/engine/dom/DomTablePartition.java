package org.sweble.wikitext.engine.dom;

import java.util.Collection;

import org.sweble.wikitext.lazy.parser.TableCell;

public interface DomTablePartition
        extends
            DomNode
{
	public int getNumCols();
	
	public int getNumRows();
	
	public DomTableRow getRow(int row) throws IndexOutOfBoundsException;
	
	public TableCell getCell(int row, int col) throws IndexOutOfBoundsException;
	
	// ==[ Row modification ]===================================================
	
	public void addRow(DomTableRow row);
	
	public void insertRow(int before, DomTableRow row) throws IndexOutOfBoundsException;
	
	public void insertRow(DomTableRow before, DomTableRow row) throws IllegalArgumentException;
	
	public DomTableRow replaceRow(int row, DomTableRow replace) throws IndexOutOfBoundsException;
	
	public boolean replaceRow(DomTableRow search, DomTableRow replace);
	
	public DomTableRow removeRow(int row) throws IndexOutOfBoundsException;
	
	public boolean removeRow(DomTableRow row);
	
	// ==[ Column modification ]================================================
	
	public void addColumn(Collection<TableCell> column);
	
	public void insertColumn(int before, Collection<TableCell> column) throws IndexOutOfBoundsException;
	
	public void insertColumn(Collection<TableCell> before, Collection<TableCell> column) throws IllegalArgumentException;
	
	public Collection<TableCell> replaceColumn(int column, Collection<TableCell> replace) throws IndexOutOfBoundsException;
	
	public boolean replaceColumn(Collection<TableCell> search, Collection<TableCell> replace);
	
	public Collection<TableCell> removeColumn(int column) throws IndexOutOfBoundsException;
	
	public boolean removeColumn(Collection<TableCell> column);
	
}
