/**
 * Copyright 2011 The Open Source Research Group,
 *                University of Erlangen-Nürnberg
 */
package org.sweble.wom3.impl;

import org.sweble.wom3.Wom3Abbr;

public class AbbrImpl
		extends
			BackboneWomElemWithUnivAttrs
		implements
			Wom3Abbr
{
	private static final long serialVersionUID = 1L;
	
	// =========================================================================
	
	public AbbrImpl(DocumentImpl owner)
	{
		super(owner);
	}
	
	// =========================================================================
	
	@Override
	public String getWomName()
	{
		return "abbr";
	}
}
