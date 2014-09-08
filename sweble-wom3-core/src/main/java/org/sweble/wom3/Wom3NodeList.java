/**
 * Copyright 2011 The Open Source Research Group,
 *                University of Erlangen-Nürnberg
 */
package org.sweble.wom3;

import org.w3c.dom.NodeList;

public interface Wom3NodeList
		extends
			NodeList,
			Iterable<Wom3Node>
{
	public Wom3Node item(int index);
}
