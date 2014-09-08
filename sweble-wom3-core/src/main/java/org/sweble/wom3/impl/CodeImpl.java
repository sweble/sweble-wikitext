/**
 * Copyright 2011 The Open Source Research Group,
 *                University of Erlangen-Nürnberg
 */
package org.sweble.wom3.impl;

import org.sweble.wom3.Wom3Code;

public class CodeImpl
		extends
			BackboneWomElemWithUnivAttrs
		implements
			Wom3Code
{
	private static final long serialVersionUID = 1L;
	
	// =========================================================================
	
	public CodeImpl(DocumentImpl owner)
	{
		super(owner);
	}
	
	// =========================================================================
	
	@Override
	public String getWomName()
	{
		return "code";
	}
}
