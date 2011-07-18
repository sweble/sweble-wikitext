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
