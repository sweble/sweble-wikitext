/**
 * Copyright 2011 The Open Source Research Group,
 *                University of Erlangen-Nürnberg
 */
package org.sweble.wom3.impl;

import org.sweble.wom3.Wom3Text;
import org.w3c.dom.Node;

public class TextImpl
		extends
			BackboneContainer
		implements
			Wom3Text
{
	private static final long serialVersionUID = 1L;
	
	// =========================================================================
	
	public TextImpl(DocumentImpl owner)
	{
		super(owner);
	}
	
	// =========================================================================
	
	@Override
	public String getWomName()
	{
		return "text";
	}
	
	@Override
	protected boolean ignoresContentWhitespace()
	{
		return false;
	}
	
	@Override
	protected void allowsInsertion(Backbone prev, Backbone child)
	{
		if (getOwnerDocument().getStrictErrorChecking())
		{
			if (child.getNodeType() != Node.TEXT_NODE)
				doesNotAllowInsertion(prev, child);
		}
	}
	
	@Override
	protected void allowsRemoval(Backbone child)
	{
	}
	
	@Override
	protected void allowsReplacement(Backbone oldChild, Backbone newChild)
	{
		if (getOwnerDocument().getStrictErrorChecking())
		{
			if (newChild.getNodeType() != Node.TEXT_NODE)
				doesNotAllowReplacement(oldChild, newChild);
		}
	}
	
	@Override
	protected AttributeDescriptor getAttributeDescriptor(
			String namespaceUri,
			String localName,
			String qualifiedName)
	{
		return getAttrDescStrict(namespaceUri, localName, qualifiedName);
	}
}
