/**
 * Copyright 2011 The Open Source Research Group,
 *                University of Erlangen-Nürnberg
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
