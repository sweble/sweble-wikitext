/**
 * Copyright 2011 The Open Source Research Group,
 *                University of Erlangen-Nürnberg
 */
package org.sweble.wom3.impl;

import org.sweble.wom3.Wom3Dfn;

public class DfnImpl
		extends
			BackboneWomElemWithUnivAttrs
		implements
			Wom3Dfn
{
	private static final long serialVersionUID = 1L;
	
	// =========================================================================
	
	public DfnImpl(DocumentImpl owner)
	{
		super(owner);
	}
	
	// =========================================================================
	
	@Override
	public String getWomName()
	{
		return "dfn";
	}
}
