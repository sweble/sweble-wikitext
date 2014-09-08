/**
 * Copyright 2011 The Open Source Research Group,
 *                University of Erlangen-Nürnberg
 */
package org.sweble.wom3.impl;

import org.sweble.wom3.Wom3NamedNodeMap;
import org.sweble.wom3.Wom3Node;
import org.w3c.dom.DOMException;
import org.w3c.dom.Node;

public class AttributeMap
		extends
			NodeListImpl
		implements
			Wom3NamedNodeMap
{
	private BackboneElement element;
	
	// =========================================================================
	
	public AttributeMap(BackboneElement element)
	{
		super(element);
		this.element = element;
	}
	
	// =========================================================================
	
	@Override
	public Wom3Node getNamedItem(String name)
	{
		for (Backbone n = element.getFirstAttr(); n != null; n = n.getNextSibling())
		{
			if (n.getNodeName().equals(name))
				return n;
		}
		return null;
	}
	
	@Override
	public Wom3Node getNamedItemNS(String namespaceURI, String localName) throws DOMException
	{
		if (namespaceURI.equals(Wom3Node.WOM_NS_URI))
			return getNamedItem(localName);
		return null;
	}
	
	@Override
	public int getLength()
	{
		int len = 0;
		for (Wom3Node n = element.getFirstAttr(); n != null; n = n.getNextSibling())
			++len;
		return len;
	}
	
	@Override
	public Wom3Node item(int index)
	{
		int cnt = 0;
		Wom3Node n = element.getFirstAttr();
		while (n != null && cnt++ < index)
			n = n.getNextSibling();
		return n;
	}
	
	@Override
	public Wom3Node setNamedItem(Node arg) throws DOMException
	{
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}
	
	@Override
	public Wom3Node setNamedItemNS(Node arg) throws DOMException
	{
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}
	
	@Override
	public Wom3Node removeNamedItem(String name) throws DOMException
	{
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}
	
	@Override
	public Wom3Node removeNamedItemNS(String namespaceURI, String localName) throws DOMException
	{
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}
}
