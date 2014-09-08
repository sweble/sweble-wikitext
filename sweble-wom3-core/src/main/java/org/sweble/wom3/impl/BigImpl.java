/**
 * Copyright 2011 The Open Source Research Group,
 *                University of Erlangen-Nürnberg
 */
package org.sweble.wom3.impl;

import org.sweble.wom3.Wom3Big;

public class BigImpl
		extends
			BackboneWomElemWithUnivAttrs
		implements
			Wom3Big
{
	private static final long serialVersionUID = 1L;
	
	// =========================================================================
	
	public BigImpl(DocumentImpl owner)
	{
		super(owner);
	}
	
	// =========================================================================
	
	@Override
	public String getWomName()
	{
		return "big";
	}
}
