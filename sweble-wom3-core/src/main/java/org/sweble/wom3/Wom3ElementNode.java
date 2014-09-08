/**
 * Copyright 2011 The Open Source Research Group,
 *                University of Erlangen-Nürnberg
 */
package org.sweble.wom3;

import org.w3c.dom.DOMException;
import org.w3c.dom.Element;

public interface Wom3ElementNode
		extends
			Wom3Node,
			Element
{
	@Override
	public Wom3NodeList getElementsByTagNameNS(
			String namespaceURI,
			String localName) throws DOMException;
}
