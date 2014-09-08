/**
 * Copyright 2011 The Open Source Research Group,
 *                University of Erlangen-Nürnberg
 */
package org.sweble.wom3.impl;

import org.sweble.wom3.Wom3TableCellBase;
import org.sweble.wom3.Wom3TableColumn;

public class TableColumnImpl
		implements
			Wom3TableColumn
{
	private final TablePartitionImpl partition;
	
	private final int col;
	
	// =========================================================================
	
	protected TableColumnImpl(TablePartitionImpl partition, int col)
	{
		this.partition = partition;
		this.col = col;
	}
	
	// =========================================================================
	
	@Override
	public int getColIndex()
	{
		isValidColumn();
		return col;
	}
	
	@Override
	public int getNumRows()
	{
		isValidColumn();
		return partition.getNumRows();
	}
	
	@Override
	public Wom3TableCellBase getCell(int row) throws IndexOutOfBoundsException
	{
		isValidColumn();
		return partition.getCell(row, col);
	}
	
	// =========================================================================
	
	private void isValidColumn()
	{
		if (partition.getNumCols() <= col)
			throw new IllegalStateException("This column is no longer valid (it was removed)");
	}
}
