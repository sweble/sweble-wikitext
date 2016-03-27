/**
 * Copyright 2011 The Open Source Research Group,
 *                University of Erlangen-Nürnberg
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses/.
 */
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
