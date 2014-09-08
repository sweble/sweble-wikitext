/**
 * Copyright 2011 The Open Source Research Group,
 *                University of Erlangen-Nürnberg
 */
package org.sweble.wom3.impl;

public abstract class BackboneContainer
		extends
			BackboneWomElement
{
	private static final long serialVersionUID = 1L;
	
	// =========================================================================
	
	public BackboneContainer(DocumentImpl owner)
	{
		super(owner);
	}
	
	// =========================================================================
	
	@Override
	protected void allowsInsertion(Backbone prev, Backbone child)
	{
		// TODO: Restrict accordingly
	}
	
	@Override
	protected void allowsRemoval(Backbone child)
	{
		// Usually container elements allow removal of any element
	}
	
	@Override
	protected void allowsReplacement(Backbone oldChild, Backbone newChild)
	{
		// TODO: Restrict accordingly
	}
}
