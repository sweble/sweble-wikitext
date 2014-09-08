/**
 * Copyright 2011 The Open Source Research Group,
 *                University of Erlangen-Nürnberg
 */
package org.sweble.wom3;

import org.w3c.dom.Document;

public interface Wom3Document
		extends
			Wom3Node,
			Document
{
	@Override
	public Wom3ElementNode getDocumentElement();
}
