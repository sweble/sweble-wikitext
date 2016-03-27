/**
 * Copyright 2011 The Open Source Research Group,
 *                University of Erlangen-Nürnberg
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses/.
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
		return getData();
	}

	@Override
	public void setTextContent(String textContent) throws DOMException
	{
		setData(textContent);
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
