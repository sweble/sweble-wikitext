package org.sweble.wom3.impl;

import org.sweble.wom3.Wom3Node;

public class RuntimeAttributeDescriptor
		extends
			AttributeDescriptor
{
	private final String name;
	
	private final int flags;
	
	private final AttributeCustomAction customActionFn;
	
	private final AttributeVerificationAndConverion verifyAndConvertFn;
	
	public RuntimeAttributeDescriptor(
			String name,
			boolean removable,
			boolean readOnly,
			AttributeVerificationAndConverion verifyAndConvert,
			AttributeCustomAction customAction,
			Normalization normalization)
	{
		this.name = name;
		this.flags = makeFlags(
				removable,
				readOnly,
				(customAction != null),
				normalization);
		this.customActionFn = customAction;
		this.verifyAndConvertFn = verifyAndConvert;
	}
	
	public String getName()
	{
		return name;
	}
	
	@Override
	public int getFlags()
	{
		return flags;
	}
	
	@Override
	public boolean verifyAndConvert(
			Backbone parent,
			NativeAndStringValuePair verified)
	{
		return (verifyAndConvertFn != null) ?
				verifyAndConvertFn.verifyAndConvert(parent, verified) :
				super.verifyAndConvert(parent, verified);
	}
	
	@Override
	public void customAction(
			Wom3Node parent,
			AttributeBase oldAttr,
			AttributeBase newAttr)
	{
		customActionFn.customAction(parent, oldAttr, newAttr);
	}
}
