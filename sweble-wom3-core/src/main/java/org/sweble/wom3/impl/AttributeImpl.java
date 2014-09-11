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

/**
 * DOM v1 Attribute Implementation (no namespace awareness)
 */
public class AttributeImpl
		extends
			AttributeBase
{
	private static final long serialVersionUID = 1L;
	
	/**
	 * This value must always be normalized.
	 */
	private String strValue;
	
	private Object value;
	
	// =========================================================================
	
	/**
	 * For AttributeNsImpl only!
	 */
	AttributeImpl(DocumentImpl owner)
	{
		super(owner);
	}
	
	protected AttributeImpl(DocumentImpl owner, String name)
	{
		super(owner);
		setName(name);
	}
	
	// =========================================================================
	
	@Override
	public String getValue()
	{
		return strValue;
	}
	
	public void setValue(Object value, String strValue)
	{
		this.value = (value != null) ? value : strValue;
		this.strValue = (strValue != null) ? strValue : (String) value;
	}
	
	protected Object getNativeValue()
	{
		return value;
	}
	
	// =========================================================================
	
	@Override
	protected Object clone() throws CloneNotSupportedException
	{
		AttributeImpl newNode = (AttributeImpl) super.clone();
		/* FIXME: This is horribly flawed....
		 * One cannot clone arbitrary objects, but if we just copy the other objects
		 * value, non-immutable values can have bad side effects since a change in one would affect all clones as well...
		if (this.value != null)
			newNode.value = this.value.clone();
		*/
		return newNode;
	}
}
