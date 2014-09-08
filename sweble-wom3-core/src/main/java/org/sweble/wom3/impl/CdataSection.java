/**
 * Copyright 2011 The Open Source Research Group,
 *                University of Erlangen-Nürnberg
 */
package org.sweble.wom3.impl;

import org.sweble.wom3.Wom3CdataSection;
import org.w3c.dom.Node;

public class CdataSection
		extends
			XmlTextBase
		implements
			Wom3CdataSection
{
	private static final long serialVersionUID = 1L;
	
	private String data;
	
	// =========================================================================
	
	public CdataSection(DocumentImpl owner)
	{
		super(owner);
	}
	
	public CdataSection(DocumentImpl owner, String data)
	{
		super(owner);
		this.data = data;
	}
	
	// =========================================================================
	
	@Override
	public short getNodeType()
	{
		return Node.CDATA_SECTION_NODE;
	}
	
	@Override
	public String getNodeName()
	{
		return "#cdata-section";
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
