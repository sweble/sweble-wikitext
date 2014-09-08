/**
 * Copyright 2011 The Open Source Research Group,
 *                University of Erlangen-Nürnberg
 */
package org.sweble.wom3.impl;

import org.sweble.wom3.Wom3Bold;

public class BoldImpl
		extends
			BackboneWomElemWithUnivAttrs
		implements
			Wom3Bold
{
	private static final long serialVersionUID = 1L;
	
	// =========================================================================
	
	public BoldImpl(DocumentImpl owner)
	{
		super(owner);
	}
	
	// =========================================================================
	
	@Override
	public String getWomName()
	{
		return "b";
	}
}
