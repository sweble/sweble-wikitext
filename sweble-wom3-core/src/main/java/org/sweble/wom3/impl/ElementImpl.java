/**
 * Copyright 2011 The Open Source Research Group,
 *                University of Erlangen-Nürnberg
 */
package org.sweble.wom3.impl;

import org.sweble.wom3.Wom3Element;

public class ElementImpl
		extends
			BackboneElement
		implements
			Wom3Element
{
	private static final long serialVersionUID = 1L;
	
	private String name;
	
	// =========================================================================
	
	protected ElementImpl(DocumentImpl owner)
	{
		super(owner);
	}
	
	public ElementImpl(DocumentImpl owner, String name)
	{
		super(owner);
		setNodeName(name);
	}
	
	// =========================================================================
	
	protected final void setNodeName(String name)
	{
		this.name = name;
	}
	
	// =========================================================================
	
	@Override
	public String getNodeName()
	{
		return name;
	}
	
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
	protected boolean ignoresContentWhitespace()
	{
		return false;
	}
	
	@Override
	protected AttributeDescriptor getAttributeDescriptor(
			String namespaceUri,
			String localName,
			String qualifiedName)
	{
		return GenericAttributeDescriptor.get();
	}
}
