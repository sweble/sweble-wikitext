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
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.sweble.wom3.impl;

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
		return setStringAttr(Attributes.ABBR, "abbr", abbr);
	}
	
	@Override
	public String getAxis()
	{
		return getStringAttr("axis");
	}
	
	@Override
	public String setAxis(String axis)
	{
		return setStringAttr(Attributes.AXIS, "axis", axis);
	}
	
	@Override
	public Wom3TableCellScope getScope()
	{
		return (Wom3TableCellScope) getAttributeNativeData("scope");
	}
	
	@Override
	public Wom3TableCellScope setScope(Wom3TableCellScope scope)
	{
		return setAttributeDirect(Attributes.SCOPE, "scope", scope);
	}
	
	@Override
	public Wom3HorizAlign getAlign()
	{
		return getAlignAttr("align");
	}
	
	@Override
	public Wom3HorizAlign setAlign(Wom3HorizAlign align)
	{
		return setAlignAttr(Attributes.ALIGN, "align", align);
	}
	
	@Override
	public Wom3TableVAlign getVAlign()
	{
		return getTableVAlignAttr("valign");
	}
	
	@Override
	public Wom3TableVAlign setVAlign(Wom3TableVAlign valign)
	{
		return setTableVAlignAttr(Attributes.VALIGN, "valign", valign);
	}
	
	@Override
	public Wom3Color getBgColor()
	{
		return getColorAttr("bgcolor");
	}
	
	@Override
	public Wom3Color setBgColor(Wom3Color color)
	{
		return setColorAttr(Attributes.BGCOLOR, "bgcolor", color);
	}
	
	@Override
	public Integer getColspan()
	{
		return getIntAttr("colspan");
	}
	
	@Override
	public Integer setColspan(Integer span) throws IllegalArgumentException
	{
		return setIntAttr(Attributes.COLSPAN, "colspan", span);
	}
	
	@Override
	public Integer getRowspan()
	{
		return getIntAttr("rowspan");
	}
	
	@Override
	public Integer setRowspan(Integer span) throws IllegalArgumentException
	{
		return setIntAttr(Attributes.ROWSPAN, "rowspan", span);
	}
	
	@Override
	public boolean isNowrap()
	{
		return getBoolAttr("nowrap");
	}
	
	@Override
	public boolean setNowrap(boolean nowrap)
	{
		return setBoolAttr(Attributes.NOWRAP, "nowrap", nowrap);
	}
	
	@Override
	public Wom3ValueWithUnit getWidth()
	{
		return getValueWithUnitAttr("width");
	}
	
	@Override
	public Wom3ValueWithUnit setWidth(Wom3ValueWithUnit width)
	{
		return setValueWithUnitAttr(Attributes.WIDTH, "width", width);
	}
	
	@Override
	public Wom3ValueWithUnit getHeight()
	{
		return getValueWithUnitAttr("height");
	}
	
	@Override
	public Wom3ValueWithUnit setHeight(Wom3ValueWithUnit height)
	{
		return setValueWithUnitAttr(Attributes.HEIGHT, "height", height);
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
	
	private static final Map<String, AttributeDescriptor> nameMap = getNameMap();
	
	private static Map<String, AttributeDescriptor> getNameMap()
	{
		Map<String, AttributeDescriptor> nameMap =
				new HashMap<String, AttributeDescriptor>();
		
		nameMap.putAll(UniversalAttributes.getNameMap());
		nameMap.put("abbr", Attributes.ABBR);
		nameMap.put("axis", Attributes.AXIS);
		nameMap.put("scope", Attributes.SCOPE);
		nameMap.put("align", Attributes.ALIGN);
		nameMap.put("valign", Attributes.VALIGN);
		nameMap.put("bgcolor", Attributes.BGCOLOR);
		nameMap.put("colspan", Attributes.COLSPAN);
		nameMap.put("rowspan", Attributes.ROWSPAN);
		nameMap.put("nowrap", Attributes.NOWRAP);
		nameMap.put("width", Attributes.WIDTH);
		nameMap.put("height", Attributes.HEIGHT);
		
		return nameMap;
	}
	
	@Override
	protected AttributeDescriptor getAttributeDescriptor(
			String namespaceUri,
			String localName,
			String qualifiedName)
	{
		return getAttrDesc(namespaceUri, localName, qualifiedName, nameMap);
	}
	
	private static enum Attributes implements AttributeDescriptor
	{
		ABBR
		{
			@Override
			public boolean verifyAndConvert(
					Backbone parent,
					NativeAndStringValuePair verified)
			{
				return true;
			}
		},
		AXIS
		{
			@Override
			public boolean verifyAndConvert(
					Backbone parent,
					NativeAndStringValuePair verified)
			{
				return true;
			}
		},
		SCOPE
		{
			@Override
			public boolean verifyAndConvert(
					Backbone parent,
					NativeAndStringValuePair verified)
			{
				return AttributeVerifiers.SCOPE.verifyAndConvert(parent, verified);
			}
		},
		ALIGN
		{
			@Override
			public boolean verifyAndConvert(
					Backbone parent,
					NativeAndStringValuePair verified)
			{
				return AttributeVerifiers.LCRJC_ALIGN.verifyAndConvert(parent, verified);
			}
		},
		VALIGN
		{
			@Override
			public boolean verifyAndConvert(
					Backbone parent,
					NativeAndStringValuePair verified)
			{
				return AttributeVerifiers.TMBB_VALIGN.verifyAndConvert(parent, verified);
			}
		},
		BGCOLOR
		{
			@Override
			public boolean verifyAndConvert(
					Backbone parent,
					NativeAndStringValuePair verified)
			{
				return AttributeVerifiers.COLOR.verifyAndConvert(parent, verified);
			}
		},
		COLSPAN
		{
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
		},
		ROWSPAN
		{
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
		},
		NOWRAP
		{
			@Override
			public boolean verifyAndConvert(
					Backbone parent,
					NativeAndStringValuePair verified)
			{
				return AttributeVerifiers.verifyAndConvertBool(parent, verified, "nowrap");
			}
		},
		WIDTH
		{
			@Override
			public boolean verifyAndConvert(
					Backbone parent,
					NativeAndStringValuePair verified)
			{
				return AttributeVerifiers.LENGTH.verifyAndConvert(parent, verified);
			}
		},
		HEIGHT
		{
			@Override
			public boolean verifyAndConvert(
					Backbone parent,
					NativeAndStringValuePair verified)
			{
				return AttributeVerifiers.LENGTH.verifyAndConvert(parent, verified);
			}
		};
		
		// =====================================================================
		
		@Override
		public boolean isRemovable()
		{
			return true;
		}
		
		@Override
		public Normalization getNormalizationMode()
		{
			return Normalization.NON_CDATA;
		}
		
		@Override
		public void customAction(
				Wom3Node parent,
				AttributeBase oldAttr,
				AttributeBase newAttr)
		{
		}
	}
}
