/**
 * Copyright 2011 The Open Source Research Group,
 *                University of Erlangen-Nürnberg
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses/.
 */
package org.sweble.wom3.impl;

import java.util.ArrayList;

import org.sweble.wom3.Wom3Node;
import org.sweble.wom3.Wom3TableCellBase;
import org.sweble.wom3.Wom3TableRow;

public class TableField
{
	private final ArrayList<ArrayList<TableCellBaseImpl>> cells =
			new ArrayList<ArrayList<TableCellBaseImpl>>();

	private ArrayList<TableRowImpl> rows = new ArrayList<TableRowImpl>();

	private int colNum;

	// =========================================================================

	public TableField(TablePartitionImpl part)
	{
		ArrayList<Integer> ars = new ArrayList<Integer>();

		ArrayList<TableCellBaseImpl> lastRow = null;

		int rowIndex = 0;
		Wom3Node womRowNode = part.getFirstChild();
		while (womRowNode != null)
		{
			if (!(womRowNode instanceof TableRowImpl))
			{
				womRowNode = womRowNode.getNextSibling();
				continue;
			}
			TableRowImpl rowNode = (TableRowImpl) womRowNode;

			// Add row

			ArrayList<TableCellBaseImpl> row = new ArrayList<TableCellBaseImpl>();
			cells.add(row);
			rows.add(rowNode);

			// Add cells

			int colIndex = 0;
			Wom3Node womCellNode = rowNode.getFirstChild();
			while (womCellNode != null)
			{
				if (!(womCellNode instanceof TableCellBaseImpl))
				{
					womCellNode = womCellNode.getNextSibling();
					continue;
				}
				TableCellBaseImpl cellNode = (TableCellBaseImpl) womCellNode;

				// First deal with row span from the rows above
				boolean hadRowSpan = false;
				if (colIndex < ars.size())
				{
					Integer rowSpan = ars.get(colIndex);
					if (rowSpan != null && rowSpan > 0)
					{
						// Decrement remaining row span
						ars.set(colIndex, rowSpan - 1);
						// Add above cell to row
						row.add(lastRow.get(colIndex));
						hadRowSpan = true;
					}
				}

				if (!hadRowSpan)
				{
					cellNode.setCoords(rowIndex, colIndex);

					Integer rowSpan = cellNode.getRowspan();
					if (rowSpan == null)
						rowSpan = 1;
					Integer colSpan = cellNode.getColspan();
					if (colSpan == null)
						colSpan = 1;

					for (int cs = 0; cs < colSpan; ++cs)
					{
						row.add(cellNode);
						if (rowSpan > 1)
						{
							while (ars.size() <= colIndex)
								ars.add(null);
							ars.set(colIndex, rowSpan - 1);
						}

						++colIndex;
					}

					womCellNode = cellNode.getNextSibling();
				}
				else
				{
					// We're not done with the current cell since its spot was 
					// occupied by a rowspan cell from the rows above and the 
					// current cell therefore has to move to the right
					++colIndex;
				}
			}

			this.colNum = Math.max(this.colNum, colIndex);

			rowNode.setCoords(rowIndex);
			lastRow = row;

			++rowIndex;
			womRowNode = rowNode.getNextSibling();
		}
	}

	// =========================================================================

	public int getNumCols()
	{
		return colNum;
	}

	public int getNumRows()
	{
		return rows.size();
	}

	public Wom3TableRow getRow(int row)
	{
		if (row >= getNumRows())
			throw new IndexOutOfBoundsException("Row: " + String.valueOf(row));
		return rows.get(row);
	}

	public Wom3TableCellBase getCell(int row, int col)
	{
		if (row >= getNumRows())
			throw new IndexOutOfBoundsException("Row: " + String.valueOf(row));
		if (col >= getNumCols())
			throw new IndexOutOfBoundsException("Col: " + String.valueOf(col));
		ArrayList<TableCellBaseImpl> r = cells.get(row);
		if (col >= r.size())
			return null;
		return r.get(col);
	}
}
