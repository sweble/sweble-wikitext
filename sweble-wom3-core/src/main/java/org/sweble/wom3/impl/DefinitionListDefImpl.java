/**
 * Copyright 2011 The Open Source Research Group,
 *                University of Erlangen-Nürnberg
 */
package org.sweble.wom3.impl;

import org.sweble.wom3.Wom3DefinitionListDef;

public class DefinitionListDefImpl
		extends
			BackboneWomElemWithUnivAttrs
		implements
			Wom3DefinitionListDef
{
	private static final long serialVersionUID = 1L;
	
	// =========================================================================
	
	public DefinitionListDefImpl(DocumentImpl owner)
	{
		super(owner);
	}
	
	// =========================================================================
	
	@Override
	public String getWomName()
	{
		return "dd";
	}
}
