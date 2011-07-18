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
