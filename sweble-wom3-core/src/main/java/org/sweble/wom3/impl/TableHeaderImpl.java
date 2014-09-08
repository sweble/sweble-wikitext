/**
 * Copyright 2011 The Open Source Research Group,
 *                University of Erlangen-Nürnberg
 */
package org.sweble.wom3.impl;

import org.sweble.wom3.Wom3TableHeaderCell;

public class TableHeaderImpl
		extends
			TableCellBaseImpl
		implements
			Wom3TableHeaderCell
{
	private static final long serialVersionUID = 1L;
	
	// =========================================================================
	
	public TableHeaderImpl(DocumentImpl owner)
	{
		super(owner);
	}
	
	// =========================================================================
	
	@Override
	public String getWomName()
	{
		return "th";
	}
}
