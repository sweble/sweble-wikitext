/**
 * Copyright 2011 The Open Source Research Group,
 *                University of Erlangen-Nürnberg
 */
package org.sweble.wom3.impl;

import org.sweble.wom3.Wom3Emphasize;

public class EmphasizeImpl
		extends
			BackboneWomElemWithUnivAttrs
		implements
			Wom3Emphasize
{
	private static final long serialVersionUID = 1L;
	
	// =========================================================================
	
	public EmphasizeImpl(DocumentImpl owner)
	{
		super(owner);
	}
	
	// =========================================================================
	
	@Override
	public String getWomName()
	{
		return "em";
	}
}
