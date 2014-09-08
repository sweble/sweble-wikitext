/**
 * Copyright 2011 The Open Source Research Group,
 *                University of Erlangen-Nürnberg
 */
package org.sweble.wom3.impl;

import org.sweble.wom3.Wom3Comment;
import org.sweble.wom3.Wom3Rtd;
import org.sweble.wom3.Wom3Text;

public class CommentImpl
		extends
			BackboneContainer
		implements
			Wom3Comment
{
	private static final long serialVersionUID = 1L;
	
	// =========================================================================
	
	public CommentImpl(DocumentImpl owner)
	{
		super(owner);
	}
	
	// =========================================================================
	
	@Override
	public String getWomName()
	{
		return "comment";
	}
	
	@Override
	protected void allowsInsertion(Backbone prev, Backbone child)
	{
		if (getOwnerDocument().getStrictErrorChecking())
		{
			if (!(child instanceof Wom3Text) && !(child instanceof Wom3Rtd))
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
			if (!(newChild instanceof Wom3Text) && !(newChild instanceof Wom3Rtd))
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
