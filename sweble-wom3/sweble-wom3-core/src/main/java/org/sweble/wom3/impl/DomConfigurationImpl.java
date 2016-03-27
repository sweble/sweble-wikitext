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

import org.sweble.wom3.Wom3DomConfiguration;
import org.w3c.dom.DOMException;
import org.w3c.dom.DOMStringList;

public class DomConfigurationImpl
		implements
			Wom3DomConfiguration
{
	@Override
	public void setParameter(String name, Object value) throws DOMException
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public DOMStringList getParameterNames()
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public Object getParameter(String name) throws DOMException
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean canSetParameter(String name, Object value)
	{
		throw new UnsupportedOperationException();
	}
}
