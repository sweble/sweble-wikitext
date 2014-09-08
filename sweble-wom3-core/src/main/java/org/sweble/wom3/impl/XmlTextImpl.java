/**
 * Copyright 2011 The Open Source Research Group,
 *                University of Erlangen-Nürnberg
 */
package org.sweble.wom3.impl;

public class XmlTextImpl
		extends
			XmlTextBase
{
	private static final long serialVersionUID = 1L;
	
	private String data;
	
	// =========================================================================
	
	public XmlTextImpl(DocumentImpl owner, String data)
	{
		super(owner);
		setData(data);
	}
	
	// =========================================================================
	
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
