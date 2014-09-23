package org.sweble.wom3.swcadapter.nodes.impl;

import org.sweble.wom3.impl.BackboneWomElement;
import org.sweble.wom3.impl.ChildDescriptor;
import org.sweble.wom3.impl.DocumentImpl;
import org.sweble.wom3.swcadapter.nodes.SwcNode;

@SuppressWarnings("serial")
public abstract class BackboneSwcElement
		extends
			BackboneWomElement
		implements
			SwcNode
{
	public BackboneSwcElement(DocumentImpl owner)
	{
		super(owner);
	}
	
	@Override
	public final String getWomName()
	{
		return getSwcName();
	}
	
	@Override
	public final String getNamespaceURI()
	{
		return SwcNode.MWW_NS_URI;
	}
	
	protected static ChildDescriptor childDesc(String tag)
	{
		return childDesc(SwcNode.MWW_NS_URI, tag, 0);
	}
	
	protected static ChildDescriptor childDesc(String tag, int flags)
	{
		return childDesc(SwcNode.MWW_NS_URI, tag, flags);
	}
	
	public abstract String getSwcName();
}
