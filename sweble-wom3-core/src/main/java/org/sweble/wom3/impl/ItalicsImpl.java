/**
 * Copyright 2011 The Open Source Research Group,
 *                University of Erlangen-Nürnberg
 */
package org.sweble.wom3.impl;

import org.sweble.wom3.Wom3Italics;

public class ItalicsImpl
		extends
			BackboneWomElemWithUnivAttrs
		implements
			Wom3Italics
{
	private static final long serialVersionUID = 1L;
	
	// =========================================================================
	
	public ItalicsImpl(DocumentImpl owner)
	{
		super(owner);
	}
	
	// =========================================================================
	
	@Override
	public String getWomName()
	{
		return "i";
	}
}
