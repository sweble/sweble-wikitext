/**
 * Copyright 2011 The Open Source Research Group,
 *                University of Erlangen-Nürnberg
 */
package org.sweble.wom3.impl;

import org.sweble.wom3.Wom3Strong;

public class StrongImpl
		extends
			BackboneWomElemWithUnivAttrs
		implements
			Wom3Strong
{
	private static final long serialVersionUID = 1L;
	
	// =========================================================================
	
	public StrongImpl(DocumentImpl owner)
	{
		super(owner);
	}
	
	// =========================================================================
	
	@Override
	public String getWomName()
	{
		return "strong";
	}
}
