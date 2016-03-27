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

import org.sweble.wom3.Wom3XmlText;
import org.w3c.dom.DOMException;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

import de.fau.cs.osr.utils.StringTools;

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
	public boolean isContentWhitespace()
	{
		// TODO: use isElementContentWhitespace() instead!
		return StringTools.isWhitespace(getTextContent());
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
