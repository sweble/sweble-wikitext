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

import java.util.HashMap;
import java.util.Map;

public abstract class EventAttributes
{
	public static final AttrDescOnKeyPress ATTR_DESC_ONCLICK = new AttrDescOnKeyPress();

	public static final AttrDescOnKeyPress ATTR_DESC_ONDBLCLICK = new AttrDescOnKeyPress();

	public static final AttrDescOnKeyPress ATTR_DESC_ONKEYDOWN = new AttrDescOnKeyPress();

	public static final AttrDescOnKeyPress ATTR_DESC_ONKEYPRESS = new AttrDescOnKeyPress();

	public static final AttrDescOnKeyPress ATTR_DESC_ONKEYUP = new AttrDescOnKeyPress();

	public static final AttrDescOnKeyPress ATTR_DESC_ONMOUSEDOWN = new AttrDescOnKeyPress();

	public static final AttrDescOnKeyPress ATTR_DESC_ONMOUSEMOVE = new AttrDescOnKeyPress();

	public static final AttrDescOnKeyPress ATTR_DESC_ONMOUSEOUT = new AttrDescOnKeyPress();

	public static final AttrDescOnKeyPress ATTR_DESC_ONMOUSEOVER = new AttrDescOnKeyPress();

	public static final AttrDescOnKeyPress ATTR_DESC_ONMOUSEUP = new AttrDescOnKeyPress();

	// =========================================================================

	public static final class AttrDescOnClick
			extends
				AttributeDescriptor
	{
		@Override
		public int getFlags()
		{
			return makeFlags(
					true /* removable */,
					false /* readOnly */,
					false /* customAction */,
					Normalization.NON_CDATA);
		}

		@Override
		public boolean verifyAndConvert(
				Backbone parent,
				NativeAndStringValuePair verified)
		{
			return AttributeVerifiers.SCRIPT.verifyAndConvert(parent, verified);
		}
	}

	// =========================================================================

	public static final class AttrDescOnDblClick
			extends
				AttributeDescriptor
	{
		@Override
		public int getFlags()
		{
			return makeFlags(
					true /* removable */,
					false /* readOnly */,
					false /* customAction */,
					Normalization.NON_CDATA);
		}

		@Override
		public boolean verifyAndConvert(
				Backbone parent,
				NativeAndStringValuePair verified)
		{
			return AttributeVerifiers.SCRIPT.verifyAndConvert(parent, verified);
		}
	}

	// =========================================================================

	public static final class AttrDescOnKeyDown
			extends
				AttributeDescriptor
	{
		@Override
		public int getFlags()
		{
			return makeFlags(
					true /* removable */,
					false /* readOnly */,
					false /* customAction */,
					Normalization.NON_CDATA);
		}

		@Override
		public boolean verifyAndConvert(
				Backbone parent,
				NativeAndStringValuePair verified)
		{
			return AttributeVerifiers.SCRIPT.verifyAndConvert(parent, verified);
		}
	}

	// =========================================================================

	public static final class AttrDescOnKeyPress
			extends
				AttributeDescriptor
	{
		@Override
		public int getFlags()
		{
			return makeFlags(
					true /* removable */,
					false /* readOnly */,
					false /* customAction */,
					Normalization.NON_CDATA);
		}

		@Override
		public boolean verifyAndConvert(
				Backbone parent,
				NativeAndStringValuePair verified)
		{
			return AttributeVerifiers.SCRIPT.verifyAndConvert(parent, verified);
		}
	}

	// =========================================================================

	public static final class AttrDescOnKeyUp
			extends
				AttributeDescriptor
	{
		@Override
		public int getFlags()
		{
			return makeFlags(
					true /* removable */,
					false /* readOnly */,
					false /* customAction */,
					Normalization.NON_CDATA);
		}

		@Override
		public boolean verifyAndConvert(
				Backbone parent,
				NativeAndStringValuePair verified)
		{
			return AttributeVerifiers.SCRIPT.verifyAndConvert(parent, verified);
		}
	}

	// =========================================================================

	public static final class AttrDescOnMouseDown
			extends
				AttributeDescriptor
	{
		@Override
		public int getFlags()
		{
			return makeFlags(
					true /* removable */,
					false /* readOnly */,
					false /* customAction */,
					Normalization.NON_CDATA);
		}

		@Override
		public boolean verifyAndConvert(
				Backbone parent,
				NativeAndStringValuePair verified)
		{
			return AttributeVerifiers.SCRIPT.verifyAndConvert(parent, verified);
		}
	}

	// =========================================================================

	public static final class AttrDescOnMouseMove
			extends
				AttributeDescriptor
	{
		@Override
		public int getFlags()
		{
			return makeFlags(
					true /* removable */,
					false /* readOnly */,
					false /* customAction */,
					Normalization.NON_CDATA);
		}

		@Override
		public boolean verifyAndConvert(
				Backbone parent,
				NativeAndStringValuePair verified)
		{
			return AttributeVerifiers.SCRIPT.verifyAndConvert(parent, verified);
		}
	}

	// =========================================================================

	public static final class AttrDescOnMouseOut
			extends
				AttributeDescriptor
	{
		@Override
		public int getFlags()
		{
			return makeFlags(
					true /* removable */,
					false /* readOnly */,
					false /* customAction */,
					Normalization.NON_CDATA);
		}

		@Override
		public boolean verifyAndConvert(
				Backbone parent,
				NativeAndStringValuePair verified)
		{
			return AttributeVerifiers.SCRIPT.verifyAndConvert(parent, verified);
		}
	}

	// =========================================================================

	public static final class AttrDescOnMouseOver
			extends
				AttributeDescriptor
	{
		@Override
		public int getFlags()
		{
			return makeFlags(
					true /* removable */,
					false /* readOnly */,
					false /* customAction */,
					Normalization.NON_CDATA);
		}

		@Override
		public boolean verifyAndConvert(
				Backbone parent,
				NativeAndStringValuePair verified)
		{
			return AttributeVerifiers.SCRIPT.verifyAndConvert(parent, verified);
		}
	}

	// =========================================================================

	public static final class AttrDescOnMouseUp
			extends
				AttributeDescriptor
	{
		@Override
		public int getFlags()
		{
			return makeFlags(
					true /* removable */,
					false /* readOnly */,
					false /* customAction */,
					Normalization.NON_CDATA);
		}

		@Override
		public boolean verifyAndConvert(
				Backbone parent,
				NativeAndStringValuePair verified)
		{
			return AttributeVerifiers.SCRIPT.verifyAndConvert(parent, verified);
		}
	}

	// =========================================================================

	private static Map<String, AttributeDescriptor> NAME_MAP;

	public synchronized static Map<String, AttributeDescriptor> getNameMap()
	{
		Map<String, AttributeDescriptor> nameMap = NAME_MAP;
		if (nameMap == null)
		{
			nameMap = new HashMap<String, AttributeDescriptor>();
			nameMap.put("onclick", ATTR_DESC_ONCLICK);
			nameMap.put("ondblclick", ATTR_DESC_ONDBLCLICK);
			nameMap.put("onkeydown", ATTR_DESC_ONKEYDOWN);
			nameMap.put("onkeypress", ATTR_DESC_ONKEYPRESS);
			nameMap.put("onkeyup", ATTR_DESC_ONKEYUP);
			nameMap.put("onmousedown", ATTR_DESC_ONMOUSEDOWN);
			nameMap.put("onmousemove", ATTR_DESC_ONMOUSEMOVE);
			nameMap.put("onmouseout", ATTR_DESC_ONMOUSEOUT);
			nameMap.put("onmouseover", ATTR_DESC_ONMOUSEOVER);
			nameMap.put("onmouseup", ATTR_DESC_ONMOUSEUP);
			NAME_MAP = nameMap;
		}
		return nameMap;
	}

	public static AttributeDescriptor getDescriptor(String name)
	{
		return getNameMap().get(name);
	}
}
