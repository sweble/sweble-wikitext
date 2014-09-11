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

import org.sweble.wom3.Wom3HorizAlign;
import org.sweble.wom3.Wom3Node;
import org.sweble.wom3.Wom3TableCellBase;
import org.sweble.wom3.Wom3TableColumn;
import org.sweble.wom3.Wom3TablePartition;
import org.sweble.wom3.Wom3TableRow;
import org.sweble.wom3.Wom3TableVAlign;

public abstract class TablePartitionImpl
		extends
			BackboneWomElemWithCoreAttrs
		implements
			Wom3TablePartition
{
	private static final long serialVersionUID = 1L;
	
	private transient TableField tableField = null;
	
	// =========================================================================
	
	public TablePartitionImpl(DocumentImpl owner)
	{
		super(owner);
	}
	
	// =========================================================================
	
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
	public Wom3TableVAlign setTableVAlign(Wom3TableVAlign valign)
	{
		return setTableVAlignAttr(Attributes.VALIGN, "valign", valign);
	}
	
	// =========================================================================
	
	@Override
	public int getNumCols()
	{
		checkAttachedToTable();
		return tf().getNumCols();
	}
	
	@Override
	public int getNumRows()
	{
		checkAttachedToTable();
		return tf().getNumRows();
	}
	
	@Override
	public Wom3TableRow getRow(int row) throws IndexOutOfBoundsException
	{
		checkAttachedToTable();
		return tf().getRow(row);
	}
	
	@Override
	public Wom3TableColumn getCol(int col) throws IndexOutOfBoundsException
	{
		checkAttachedToTable();
		return new TableColumnImpl(this, col);
	}
	
	@Override
	public Wom3TableCellBase getCell(int row, int col) throws IndexOutOfBoundsException
	{
		checkAttachedToTable();
		return tf().getCell(row, col);
	}
	
	// =========================================================================
	
	protected TableField tf()
	{
		if (tableField == null)
			tableField = new TableField(this);
		return tableField;
	}
	
	protected void invalidate()
	{
		tableField = null;
	}
	
	protected void checkAttachedToTable()
	{
		if (!isAttachedToTable())
			throw new IllegalStateException("Table partition not part of a table");
	}
	
	public boolean isAttachedToTable()
	{
		return getParentNode() instanceof TableImpl;
	}
	
	// =========================================================================
	
	@Override
	public void childInserted(Backbone prev, Backbone added)
	{
		invalidate();
	}
	
	@Override
	public void childRemoved(Backbone prev, Backbone removed)
	{
		invalidate();
	}
	
	// =========================================================================
	
	private static final Map<String, AttributeDescriptor> nameMap = getNameMap();
	
	private static Map<String, AttributeDescriptor> getNameMap()
	{
		Map<String, AttributeDescriptor> nameMap =
				new HashMap<String, AttributeDescriptor>();
		
		nameMap.putAll(UniversalAttributes.getNameMap());
		nameMap.put("align", Attributes.ALIGN);
		nameMap.put("valign", Attributes.VALIGN);
		
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
