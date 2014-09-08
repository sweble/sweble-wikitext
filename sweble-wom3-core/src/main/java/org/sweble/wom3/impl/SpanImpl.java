/**
 * Copyright 2011 The Open Source Research Group,
 *                University of Erlangen-Nürnberg
 */
package org.sweble.wom3.impl;

import org.sweble.wom3.Wom3Span;

public class SpanImpl
		extends
			BackboneWomElemWithUnivAttrs
		implements
			Wom3Span
{
	private static final long serialVersionUID = 1L;
	
	// =========================================================================
	
	public SpanImpl(DocumentImpl owner)
	{
		super(owner);
	}
	
	// =========================================================================
	
	@Override
	public String getWomName()
	{
		return "span";
	}
}
