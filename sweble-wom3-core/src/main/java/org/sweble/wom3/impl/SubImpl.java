/**
 * Copyright 2011 The Open Source Research Group,
 *                University of Erlangen-Nürnberg
 */
package org.sweble.wom3.impl;

import org.sweble.wom3.Wom3Sub;

public class SubImpl
		extends
			BackboneWomElemWithUnivAttrs
		implements
			Wom3Sub
{
	private static final long serialVersionUID = 1L;
	
	// =========================================================================
	
	public SubImpl(DocumentImpl owner)
	{
		super(owner);
	}
	
	// =========================================================================
	
	@Override
	public String getWomName()
	{
		return "sub";
	}
}
