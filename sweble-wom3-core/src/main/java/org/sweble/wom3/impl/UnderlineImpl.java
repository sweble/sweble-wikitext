/**
 * Copyright 2011 The Open Source Research Group,
 *                University of Erlangen-Nürnberg
 */
package org.sweble.wom3.impl;

import org.sweble.wom3.Wom3Underline;

public class UnderlineImpl
		extends
			BackboneWomElemWithUnivAttrs
		implements
			Wom3Underline
{
	private static final long serialVersionUID = 1L;
	
	// =========================================================================
	
	public UnderlineImpl(DocumentImpl owner)
	{
		super(owner);
	}
	
	// =========================================================================
	
	@Override
	public String getWomName()
	{
		return "u";
	}
}
