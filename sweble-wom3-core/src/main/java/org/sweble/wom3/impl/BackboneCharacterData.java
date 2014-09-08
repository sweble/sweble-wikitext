/**
 * Copyright 2011 The Open Source Research Group,
 *                University of Erlangen-Nürnberg
 */
package org.sweble.wom3.impl;

import org.w3c.dom.CharacterData;
import org.w3c.dom.DOMException;

public abstract class BackboneCharacterData
		extends
			Backbone
		implements
			CharacterData
{
	private static final long serialVersionUID = 1L;
	
	// =========================================================================
	
	public BackboneCharacterData(DocumentImpl owner)
	{
		super(owner);
	}
	
	// =========================================================================
	
	@Override
	public String getLocalName()
	{
		return null;
	}
	
	// =========================================================================
	
	@Override
	public String getNodeValue() throws DOMException
	{
		return getData();
	}
	
	@Override
	public void setNodeValue(String nodeValue) throws DOMException
	{
		setData(nodeValue);
	}
	
	@Override
	public String getTextContent() throws DOMException
	{
		return getNodeValue();
	}
	
	@Override
	public void setTextContent(String textContent) throws DOMException
	{
		setNodeValue(textContent);
	}
	
	// =========================================================================
	
	@Override
	public abstract String getData() throws DOMException;
	
	@Override
	public abstract void setData(String data) throws DOMException;
	
	@Override
	public int getLength()
	{
		return this.getData().length();
	}
	
	@Override
	public String substringData(int offset, int count) throws DOMException
	{
		return this.getData().substring(offset, offset + count);
	}
	
	@Override
	public void appendData(String arg) throws DOMException
	{
		setData(getData() + arg);
	}
	
	@Override
	public void insertData(int offset, String arg) throws DOMException
	{
		String data = getData();
		setData(data.substring(0, offset) + arg +
				data.substring(offset, data.length()));
	}
	
	@Override
	public void deleteData(int offset, int count) throws DOMException
	{
		String data = getData();
		setData(data.substring(0, offset) +
				data.substring(offset + count, data.length()));
	}
	
	@Override
	public void replaceData(int offset, int count, String arg) throws DOMException
	{
		String data = getData();
		setData(data.substring(0, offset) + arg +
				data.substring(offset + count, data.length()));
	}
}
