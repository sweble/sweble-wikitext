/**
 * Copyright 2011 The Open Source Research Group,
 *                University of Erlangen-Nürnberg
 */
package org.sweble.wom3.impl;

import org.sweble.wom3.Wom3Small;

public class SmallImpl
		extends
			BackboneWomElemWithUnivAttrs
		implements
			Wom3Small
{
	private static final long serialVersionUID = 1L;
	
	// =========================================================================
	
	public SmallImpl(DocumentImpl owner)
	{
		super(owner);
	}
	
	// =========================================================================
	
	@Override
	public String getWomName()
	{
		return "small";
	}
}
