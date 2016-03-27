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

import static org.sweble.wom3.impl.CommonAttributeDescriptors.ATTR_DESC_ALIGN_LCRJC;
import static org.sweble.wom3.impl.CommonAttributeDescriptors.ATTR_DESC_ALIGN_TMBB;
import static org.sweble.wom3.impl.CommonAttributeDescriptors.ATTR_DESC_BGCOLOR;
import static org.sweble.wom3.impl.CommonAttributeDescriptors.ATTR_DESC_HEIGHT_LENGTH;
import static org.sweble.wom3.impl.CommonAttributeDescriptors.ATTR_DESC_WIDTH_LENGTH;

import java.util.HashMap;
import java.util.Map;

import org.sweble.wom3.Wom3Color;
import org.sweble.wom3.Wom3HorizAlign;
import org.sweble.wom3.Wom3Node;
import org.sweble.wom3.Wom3TableCellBase;
import org.sweble.wom3.Wom3TableCellScope;
import org.sweble.wom3.Wom3TableVAlign;
import org.sweble.wom3.Wom3ValueWithUnit;

public abstract class TableCellBaseImpl
		extends
			BackboneWomElemWithCoreAttrs
		implements
			Wom3TableCellBase
{
	private static final long serialVersionUID = 1L;

	private int rowIndex;

	private int colIndex;

	// =========================================================================

	public TableCellBaseImpl(DocumentImpl owner)
	{
		super(owner);
	}

	// =========================================================================

	@Override
	public String getAbbr()
	{
		return getStringAttr("abbr");
	}

	@Override
	public String setAbbr(String abbr)
	{
		return setStringAttr(ATTR_DESC_ABBR, "abbr", abbr);
	}

	@Override
	public String getAxis()
	{
		return getStringAttr("axis");
	}

	@Override
	public String setAxis(String axis)
	{
		return setStringAttr(ATTR_DESC_AXIS, "axis", axis);
	}

	@Override
	public Wom3TableCellScope getScope()
	{
		return (Wom3TableCellScope) getAttributeNativeData("scope");
	}

	@Override
	public Wom3TableCellScope setScope(Wom3TableCellScope scope)
	{
		return setAttributeDirect(ATTR_DESC_SCOPE, "scope", scope);
	}

	@Override
	public Wom3HorizAlign getAlign()
	{
		return getAlignAttr("align");
	}

	@Override
	public Wom3HorizAlign setAlign(Wom3HorizAlign align)
	{
		return setAlignAttr(ATTR_DESC_ALIGN_LCRJC, "align", align);
	}

	@Override
	public Wom3TableVAlign getVAlign()
	{
		return getTableVAlignAttr("valign");
	}

	@Override
	public Wom3TableVAlign setVAlign(Wom3TableVAlign valign)
	{
		return setTableVAlignAttr(ATTR_DESC_ALIGN_TMBB, "valign", valign);
	}

	@Override
	public Wom3Color getBgColor()
	{
		return getColorAttr("bgcolor");
	}

	@Override
	public Wom3Color setBgColor(Wom3Color color)
	{
		return setColorAttr(ATTR_DESC_BGCOLOR, "bgcolor", color);
	}

	@Override
	public Integer getColspan()
	{
		return getIntAttr("colspan");
	}

	@Override
	public Integer setColspan(Integer span) throws IllegalArgumentException
	{
		return setIntAttr(ATTR_DESC_COLSPAN, "colspan", span);
	}

	@Override
	public Integer getRowspan()
	{
		return getIntAttr("rowspan");
	}

	@Override
	public Integer setRowspan(Integer span) throws IllegalArgumentException
	{
		return setIntAttr(ATTR_DESC_ROWSPAN, "rowspan", span);
	}

	@Override
	public boolean isNowrap()
	{
		return getBoolAttr("nowrap");
	}

	@Override
	public boolean setNowrap(boolean nowrap)
	{
		return setBoolAttr(ATTR_DESC_NOWRAP, "nowrap", nowrap);
	}

	@Override
	public Wom3ValueWithUnit getWidth()
	{
		return getValueWithUnitAttr("width");
	}

	@Override
	public Wom3ValueWithUnit setWidth(Wom3ValueWithUnit width)
	{
		return setValueWithUnitAttr(ATTR_DESC_WIDTH_LENGTH, "width", width);
	}

	@Override
	public Wom3ValueWithUnit getHeight()
	{
		return getValueWithUnitAttr("height");
	}

	@Override
	public Wom3ValueWithUnit setHeight(Wom3ValueWithUnit height)
	{
		return setValueWithUnitAttr(ATTR_DESC_HEIGHT_LENGTH, "height", height);
	}

	// =========================================================================

	@Override
	public int getRowIndex()
	{
		checkAttachedToTable();
		return rowIndex;
	}

	@Override
	public int getColIndex()
	{
		checkAttachedToTable();
		return colIndex;
	}

	// =========================================================================

	protected void checkAttachedToTable()
	{
		if (!isAttachedToTable())
			throw new IllegalStateException("Cell not part of a table");
	}

	protected boolean isAttachedToTable()
	{
		return getParentNode() instanceof TableRowImpl && getRow().isAttachedToTable();
	}

	protected TableRowImpl getRow()
	{
		return (TableRowImpl) getParentNode();
	}

	protected TablePartitionImpl getTablePartition()
	{
		TableRowImpl row = getRow();
		if (row == null)
			return null;
		return row.getTablePartition();
	}

	protected void invalidate()
	{
		TablePartitionImpl p = getTablePartition();
		if (p != null)
			p.invalidate();
	}

	protected void setCoords(int rowIndex, int colIndex)
	{
		this.rowIndex = rowIndex;
		this.colIndex = colIndex;
	}

	// =========================================================================

	protected static final AttributeDescriptor ATTR_DESC_ABBR = new AttrDescAbbr();

	protected static final AttributeDescriptor ATTR_DESC_AXIS = new AttrDescAxis();

	protected static final AttributeDescriptor ATTR_DESC_SCOPE = new AttrDescScope();

	protected static final AttributeDescriptor ATTR_DESC_COLSPAN = new AttrDescColSpan();

	protected static final AttributeDescriptor ATTR_DESC_ROWSPAN = new AttrDescRowSpan();

	protected static final AttributeDescriptor ATTR_DESC_NOWRAP = new AttrDescNoWrap();

	private static final Map<String, AttributeDescriptor> NAME_MAP = new HashMap<String, AttributeDescriptor>();

	static
	{
		NAME_MAP.putAll(UniversalAttributes.getNameMap());
		NAME_MAP.put("abbr", ATTR_DESC_ABBR);
		NAME_MAP.put("axis", ATTR_DESC_AXIS);
		NAME_MAP.put("scope", ATTR_DESC_SCOPE);
		NAME_MAP.put("align", ATTR_DESC_ALIGN_LCRJC);
		NAME_MAP.put("valign", ATTR_DESC_ALIGN_TMBB);
		NAME_MAP.put("bgcolor", ATTR_DESC_BGCOLOR);
		NAME_MAP.put("colspan", ATTR_DESC_COLSPAN);
		NAME_MAP.put("rowspan", ATTR_DESC_ROWSPAN);
		NAME_MAP.put("nowrap", ATTR_DESC_NOWRAP);
		NAME_MAP.put("width", ATTR_DESC_WIDTH_LENGTH);
		NAME_MAP.put("height", ATTR_DESC_HEIGHT_LENGTH);
	}

	@Override
	protected AttributeDescriptor getAttributeDescriptor(
			String namespaceUri,
			String localName,
			String qualifiedName)
	{
		return getAttrDesc(namespaceUri, localName, qualifiedName, NAME_MAP);
	}

	public static final class AttrDescAbbr
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
			return super.verifyAndConvert(parent, verified);
		}
	}

	public static final class AttrDescAxis
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
			return super.verifyAndConvert(parent, verified);
		}
	}

	public static final class AttrDescScope
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
			return AttributeVerifiers.SCOPE.verifyAndConvert(parent, verified);
		}
	}

	public static final class AttrDescColSpan
			extends
				AttributeDescriptor
	{
		@Override
		public int getFlags()
		{
			return makeFlags(
					true /* removable */,
					false /* readOnly */,
					true /* customAction */,
					Normalization.NON_CDATA);
		}

		@Override
		public boolean verifyAndConvert(
				Backbone parent,
				NativeAndStringValuePair verified)
		{
			AttributeVerifiers.NUMBER.verifyAndConvert(parent, verified);
			if (((Integer) verified.value) <= 0)
				throw new IllegalArgumentException("Illegal colspan: " + verified.strValue);
			return true;
		}

		@Override
		public void customAction(
				Wom3Node parent,
				AttributeBase oldAttr,
				AttributeBase newAttr)
		{
			((TableCellBaseImpl) parent).invalidate();
		}
	}

	public static final class AttrDescRowSpan
			extends
				AttributeDescriptor
	{
		@Override
		public int getFlags()
		{
			return makeFlags(
					true /* removable */,
					false /* readOnly */,
					true /* customAction */,
					Normalization.NON_CDATA);
		}

		@Override
		public boolean verifyAndConvert(
				Backbone parent,
				NativeAndStringValuePair verified)
		{
			AttributeVerifiers.NUMBER.verifyAndConvert(parent, verified);
			if (((Integer) verified.value) <= 0)
				throw new IllegalArgumentException("Illegal rowspan: " + verified.strValue);
			return true;
		}

		@Override
		public void customAction(
				Wom3Node parent,
				AttributeBase oldAttr,
				AttributeBase newAttr)
		{
			((TableCellBaseImpl) parent).invalidate();
		}
	}

	public static final class AttrDescNoWrap
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
			return AttributeVerifiers.verifyAndConvertBool(parent, verified, "nowrap");
		}
	}
}
