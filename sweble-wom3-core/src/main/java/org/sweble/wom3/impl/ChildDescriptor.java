/**
 * Copyright 2011 The Open Source Research Group,
 *                University of Erlangen-Nürnberg
 */
package org.sweble.wom3.impl;

import org.sweble.wom3.Wom3Node;

public class ChildDescriptor
{
	public static final int REQUIRED = 1;
	
	public static final int MULTIPLE = 2;
	
	private final String namespaceUri;
	
	private final String tag;
	
	private final boolean required;
	
	private final boolean multiple;
	
	// =========================================================================
	
	public ChildDescriptor(String namespaceUri, String tag, int flags)
	{
		this.namespaceUri = namespaceUri;
		this.tag = tag;
		this.required = (flags & REQUIRED) == REQUIRED;
		this.multiple = (flags & MULTIPLE) == MULTIPLE;
	}
	
	// =========================================================================
	
	public String getTag()
	{
		return tag;
	}
	
	public boolean matches(Wom3Node n)
	{
		return tag.equals(n.getNodeName()) &&
				((namespaceUri == null) || (namespaceUri.equals(n.getNamespaceURI())));
	}
	
	public boolean isRequired()
	{
		return required;
	}
	
	public boolean isMultiple()
	{
		return multiple;
	}
}
