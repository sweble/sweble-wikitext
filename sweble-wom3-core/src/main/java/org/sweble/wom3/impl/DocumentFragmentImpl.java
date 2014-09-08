/**
 * Copyright 2011 The Open Source Research Group,
 *                University of Erlangen-Nürnberg
 */
package org.sweble.wom3.impl;

import org.sweble.wom3.Wom3DocumentFragment;
import org.w3c.dom.Node;

public class DocumentFragmentImpl
		extends
			BackboneWithChildren
		implements
			Wom3DocumentFragment
{
	private static final long serialVersionUID = 1L;
	
	// =========================================================================
	
	public DocumentFragmentImpl(DocumentImpl owner)
	{
		super(owner);
	}
	
	// =========================================================================
	
	@Override
	public String getNodeName()
	{
		return "#document-fragment";
	}
	
	@Override
	public short getNodeType()
	{
		return Node.DOCUMENT_FRAGMENT_NODE;
	}
	
	@Override
	public Backbone getParentNode()
	{
		return null;
	}
	
	// =========================================================================
	
	@Override
	protected void allowsInsertion(Backbone prev, Backbone child)
	{
	}
	
	@Override
	protected void allowsRemoval(Backbone child)
	{
	}
	
	@Override
	protected void allowsReplacement(Backbone oldChild, Backbone newChild)
	{
	}
	
	@Override
	protected void childInserted(Backbone prev, Backbone added)
	{
	}
	
	@Override
	protected void childRemoved(Backbone prev, Backbone removed)
	{
	}
}
