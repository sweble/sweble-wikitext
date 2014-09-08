/**
 * Copyright 2011 The Open Source Research Group,
 *                University of Erlangen-Nürnberg
 */
package org.sweble.wom3.impl;

import org.sweble.wom3.Wom3Kbd;

public class KbdImpl
		extends
			BackboneWomElemWithUnivAttrs
		implements
			Wom3Kbd
{
	private static final long serialVersionUID = 1L;
	
	// =========================================================================
	
	public KbdImpl(DocumentImpl owner)
	{
		super(owner);
	}
	
	// =========================================================================
	
	@Override
	public String getWomName()
	{
		return "kbd";
	}
}
