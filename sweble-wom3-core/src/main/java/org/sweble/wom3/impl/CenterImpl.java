/**
 * Copyright 2011 The Open Source Research Group,
 *                University of Erlangen-Nürnberg
 */
package org.sweble.wom3.impl;

import org.sweble.wom3.Wom3Center;

public class CenterImpl
		extends
			BackboneWomElemWithUnivAttrs
		implements
			Wom3Center
{
	private static final long serialVersionUID = 1L;
	
	// =========================================================================
	
	public CenterImpl(DocumentImpl owner)
	{
		super(owner);
	}
	
	// =========================================================================
	
	@Override
	public String getWomName()
	{
		return "center";
	}
}
