/**
 * Copyright 2011 The Open Source Research Group,
 *                University of Erlangen-Nürnberg
 */
package org.sweble.wom3.impl;

import org.sweble.wom3.Wom3Sup;

public class SupImpl
		extends
			BackboneWomElemWithUnivAttrs
		implements
			Wom3Sup
{
	private static final long serialVersionUID = 1L;
	
	// =========================================================================
	
	public SupImpl(DocumentImpl owner)
	{
		super(owner);
	}
	
	// =========================================================================
	
	@Override
	public String getWomName()
	{
		return "sup";
	}
}
