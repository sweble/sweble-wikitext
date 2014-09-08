/**
 * Copyright 2011 The Open Source Research Group,
 *                University of Erlangen-Nürnberg
 */
package org.sweble.wom3;

import org.w3c.dom.DOMException;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public interface Wom3NamedNodeMap
		extends
			NamedNodeMap
{
	public Wom3Node getNamedItem(String name);
	
	public Wom3Node setNamedItem(Node arg) throws DOMException;
	
	public Wom3Node removeNamedItem(String name) throws DOMException;
	
	public Wom3Node item(int index);
	
	public Wom3Node getNamedItemNS(String namespaceURI, String localName) throws DOMException;
	
	public Wom3Node setNamedItemNS(Node arg) throws DOMException;
	
	public Wom3Node removeNamedItemNS(String namespaceURI, String localName) throws DOMException;
}
