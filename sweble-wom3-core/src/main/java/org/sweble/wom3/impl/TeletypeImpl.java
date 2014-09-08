/**
 * Copyright 2011 The Open Source Research Group,
 *                University of Erlangen-Nürnberg
 */
package org.sweble.wom3.impl;

import org.sweble.wom3.Wom3Teletype;

public class TeletypeImpl
		extends
			BackboneWomElemWithUnivAttrs
		implements
			Wom3Teletype
{
	private static final long serialVersionUID = 1L;
	
	// =========================================================================
	
	public TeletypeImpl(DocumentImpl owner)
	{
		super(owner);
	}
	
	// =========================================================================
	
	@Override
	public String getWomName()
	{
		return "tt";
	}
}
