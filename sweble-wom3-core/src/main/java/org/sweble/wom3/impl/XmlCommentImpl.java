/**
 * Copyright 2011 The Open Source Research Group,
 *                University of Erlangen-Nürnberg
 */
package org.sweble.wom3.impl;

import org.sweble.wom3.Wom3XmlComment;
import org.w3c.dom.Node;

public class XmlCommentImpl
		extends
			BackboneCharacterData
		implements
			Wom3XmlComment
{
	private static final long serialVersionUID = 1L;
	
	private String data;
	
	// =========================================================================
	
	public XmlCommentImpl(DocumentImpl owner, String data)
	{
		super(owner);
		setData(data);
	}
	
	// =========================================================================
	
	@Override
	public String getNodeName()
	{
		return "#comment";
	}
	
	@Override
	public short getNodeType()
	{
		return Node.COMMENT_NODE;
	}
	
	@Override
	public Backbone getParentNode()
	{
		return getParentNodeIntern();
	}
	
	@Override
	public String getData()
	{
		return data;
	}
	
	@Override
	public void setData(String data)
	{
		this.data = data;
	}
}
