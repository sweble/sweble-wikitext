package org.sweble.wom3.swcadapter.nodes;

import java.util.Collection;

public interface SwcTransclusion
		extends
			SwcNode
{
	SwcName getName();
	
	SwcName setName(SwcName name);
	
	SwcArg getArgument(int index);
	
	SwcArg getArgument(String name);
	
	Collection<SwcArg> getArguments();
}
