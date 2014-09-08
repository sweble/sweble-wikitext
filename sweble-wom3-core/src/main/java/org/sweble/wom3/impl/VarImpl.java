/**
 * Copyright 2011 The Open Source Research Group,
 *                University of Erlangen-Nürnberg
 */
package org.sweble.wom3.impl;

import org.sweble.wom3.Wom3Var;

public class VarImpl
		extends
			BackboneWomElemWithUnivAttrs
		implements
			Wom3Var
{
	private static final long serialVersionUID = 1L;
	
	// =========================================================================
	
	public VarImpl(DocumentImpl owner)
	{
		super(owner);
	}
	
	// =========================================================================
	
	@Override
	public String getWomName()
	{
		return "var";
	}
}
