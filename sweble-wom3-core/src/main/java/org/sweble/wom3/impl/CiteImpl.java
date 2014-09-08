/**
 * Copyright 2011 The Open Source Research Group,
 *                University of Erlangen-Nürnberg
 */
package org.sweble.wom3.impl;

import org.sweble.wom3.Wom3Cite;

public class CiteImpl
		extends
			BackboneWomElemWithUnivAttrs
		implements
			Wom3Cite
{
	private static final long serialVersionUID = 1L;
	
	// =========================================================================
	
	public CiteImpl(DocumentImpl owner)
	{
		super(owner);
	}
	
	// =========================================================================
	
	@Override
	public String getWomName()
	{
		return "cite";
	}
}
