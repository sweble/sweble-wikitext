/**
 * Copyright 2011 The Open Source Research Group,
 *                University of Erlangen-Nürnberg
 */
package org.sweble.wom3.impl;

import org.sweble.wom3.Wom3Strike;

public class StrikeImpl
		extends
			BackboneWomElemWithUnivAttrs
		implements
			Wom3Strike
{
	private static final long serialVersionUID = 1L;
	
	// =========================================================================
	
	public StrikeImpl(DocumentImpl owner)
	{
		super(owner);
	}
	
	// =========================================================================
	
	@Override
	public String getWomName()
	{
		return "strike";
	}
}
