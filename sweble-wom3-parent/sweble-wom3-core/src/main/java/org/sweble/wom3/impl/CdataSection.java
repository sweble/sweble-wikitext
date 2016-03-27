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
		assertWritableOnDocument();
		this.data = data;
	}
}
