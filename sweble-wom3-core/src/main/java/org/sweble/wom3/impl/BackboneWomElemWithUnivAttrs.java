/**
 * Copyright 2011 The Open Source Research Group,
 *                University of Erlangen-Nürnberg
 */
package org.sweble.wom3.impl;

/**
 * Base class for classes which implement XHTML elements with universal
 * attributes. The only thing left to an implementing class is to implement the
 * getNodeName() method.
 */
public abstract class BackboneWomElemWithUnivAttrs
		extends
			BackboneWomElemWithCoreAttrs
{
	private static final long serialVersionUID = 1L;
	
	// =========================================================================
	
	public BackboneWomElemWithUnivAttrs(DocumentImpl owner)
	{
		super(owner);
	}
	
	// =========================================================================
	
	@Override
	protected AttributeDescriptor getAttributeDescriptor(
			String namespaceUri,
			String localName,
			String qualifiedName)
	{
		return getAttrDesc(namespaceUri, localName, qualifiedName, UniversalAttributes.getNameMap());
	}
}
