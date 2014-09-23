package org.sweble.wom3.swcadapter.nodes.impl;

import org.sweble.wom3.impl.Backbone;
import org.sweble.wom3.impl.DocumentImpl;
import org.sweble.wom3.swcadapter.nodes.SwcNode;

@SuppressWarnings("serial")
public abstract class BackboneSwcContainer
		extends
			BackboneSwcElement
		implements
			SwcNode
{
	public BackboneSwcContainer(DocumentImpl owner)
	{
		super(owner);
	}
	
	@Override
	protected void allowsInsertion(Backbone prev, Backbone child)
	{
		// Restrict accordingly
	}
	
	@Override
	protected void allowsRemoval(Backbone child)
	{
		// Usually container elements allow removal of any element
	}
	
	@Override
	protected void allowsReplacement(Backbone oldChild, Backbone newChild)
	{
		// Restrict accordingly
	}
}
