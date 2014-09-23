package org.sweble.wom3.swcadapter.nodes;

public interface SwcArg
		extends
			SwcNode
{
	boolean hasName();
	
	SwcName getName();
	
	SwcName setName(SwcName name);
	
	SwcValue getValue();
	
	SwcValue setValue(SwcValue value);
}
