package org.sweble.wom3.impl;

import org.sweble.wom3.Wom3Node;

public interface AttributeCustomAction
{
	void customAction(
			Wom3Node parent,
			AttributeBase oldAttr,
			AttributeBase newAttr);
}
