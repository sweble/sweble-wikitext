/**
 * Copyright 2011 The Open Source Research Group,
 *                University of Erlangen-Nürnberg
 */
package org.sweble.wom3.impl;

import org.sweble.wom3.Wom3TableCell;

public class TableCellImpl
		extends
			TableCellBaseImpl
		implements
			Wom3TableCell
{
	private static final long serialVersionUID = 1L;
	
	// =========================================================================
	
	public TableCellImpl(DocumentImpl owner)
	{
		super(owner);
	}
	
	// =========================================================================
	
	@Override
	public String getWomName()
	{
		return "td";
	}
}
