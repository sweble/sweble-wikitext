/**
 * Copyright 2011 The Open Source Research Group,
 *                University of Erlangen-Nürnberg
 */
package org.sweble.wom3.serialization;

public class NamespaceException
		extends
			RuntimeException
{
	private static final long serialVersionUID = 1L;
	
	public NamespaceException()
	{
	}
	
	public NamespaceException(String message, Throwable cause)
	{
		super(message, cause);
	}
	
	public NamespaceException(String message)
	{
		super(message);
	}
	
	public NamespaceException(Throwable cause)
	{
		super(cause);
	}
}
