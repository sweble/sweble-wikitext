/**
 * Copyright 2011 The Open Source Research Group,
 *                University of Erlangen-Nürnberg
 */
package org.sweble.wom3.impl;

import org.sweble.wom3.Wom3XmlText;
import org.w3c.dom.DOMException;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

import de.fau.cs.osr.utils.StringUtils;

public abstract class XmlTextBase
		extends
			BackboneCharacterData
		implements
			Wom3XmlText
{
	private static final long serialVersionUID = 1L;
	
	// =========================================================================
	
	public XmlTextBase(DocumentImpl owner)
	{
		super(owner);
	}
	
	// =========================================================================
	
	@Override
	public String getNodeName()
	{
		return "#text";
	}
	
	@Override
	public short getNodeType()
	{
		return Node.TEXT_NODE;
	}
	
	@Override
	public Backbone getParentNode()
	{
		return getParentNodeIntern();
	}
	
	@Override
	protected boolean isContentWhitespace()
	{
		// TODO: use isElementContentWhitespace() instead!
		return StringUtils.isWhitespace(getTextContent());
	}
	
	// =========================================================================
	// 
	// DOM Level 3 Text Interface Operations
	// 
	// =========================================================================
	
	@Override
	public Text splitText(int offset) throws DOMException
	{
		// TODO: Implement
		throw new UnsupportedOperationException();
	}
	
	@Override
	public boolean isElementContentWhitespace()
	{
		// TODO: Implement
		throw new UnsupportedOperationException();
	}
	
	@Override
	public String getWholeText()
	{
		// TODO: Implement
		throw new UnsupportedOperationException();
	}
	
	@Override
	public Text replaceWholeText(String content) throws DOMException
	{
		// TODO: Implement
		throw new UnsupportedOperationException();
	}
}
