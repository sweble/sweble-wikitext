/**
 * Copyright 2011 The Open Source Research Group,
 *                University of Erlangen-Nürnberg
 */
package org.sweble.wom3.impl;

import org.sweble.wom3.Wom3TableBody;

public class TableBodyImpl
		extends
			TablePartitionImpl
		implements
			Wom3TableBody
{
	private static final long serialVersionUID = 1L;
	
	// =========================================================================
	
	public TableBodyImpl(DocumentImpl owner)
	{
		super(owner);
	}
	
	// =========================================================================
	
	@Override
	public String getWomName()
	{
		return "tbody";
	}
}
