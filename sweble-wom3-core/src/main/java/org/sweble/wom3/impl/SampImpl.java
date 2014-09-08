/**
 * Copyright 2011 The Open Source Research Group,
 *                University of Erlangen-Nürnberg
 */
package org.sweble.wom3.impl;

import org.sweble.wom3.Wom3Samp;

public class SampImpl
		extends
			BackboneWomElemWithUnivAttrs
		implements
			Wom3Samp
{
	private static final long serialVersionUID = 1L;
	
	// =========================================================================
	
	public SampImpl(DocumentImpl owner)
	{
		super(owner);
	}
	
	// =========================================================================
	
	@Override
	public String getWomName()
	{
		return "samp";
	}
}
