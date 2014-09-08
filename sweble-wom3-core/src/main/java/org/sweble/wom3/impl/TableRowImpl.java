/**
 * Copyright 2011 The Open Source Research Group,
 *                University of Erlangen-Nürnberg
 */
package org.sweble.wom3.impl;

import java.util.HashMap;
import java.util.Map;

import org.sweble.wom3.Wom3Color;
import org.sweble.wom3.Wom3HorizAlign;
import org.sweble.wom3.Wom3Node;
import org.sweble.wom3.Wom3TableCellBase;
import org.sweble.wom3.Wom3TableRow;
import org.sweble.wom3.Wom3TableVAlign;

public class TableRowImpl
		extends
			BackboneWomElemWithCoreAttrs
		implements
			Wom3TableRow
{
	private static final long serialVersionUID = 1L;
	
	private int rowIndex;
	
	// =========================================================================
	
	public TableRowImpl(DocumentImpl owner)
	{
		super(owner);
	}
	
	// =========================================================================
	
	@Override
	public String getWomName()
	{
		return "tr";
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
	
	// =========================================================================
	
	@Override
	public int getRowIndex()
	{
		checkAttachedToTable();
		return rowIndex;
	}
	
	@Override
	public int getNumCols()
	{
		checkAttachedToTable();
		return tf().getNumCols();
	}
	
	@Override
	public Wom3TableCellBase getCell(int col) throws IndexOutOfBoundsException
	{
		checkAttachedToTable();
		return tf().getCell(rowIndex, col);
	}
	
	// =========================================================================
	
	protected void checkAttachedToTable()
	{
		if (!isAttachedToTable())
			throw new IllegalStateException("Row not part of a table");
	}
	
	protected boolean isAttachedToTable()
	{
		return getParentNode() instanceof TablePartitionImpl && getTablePartition().isAttachedToTable();
	}
	
	protected TableField tf()
	{
		return getTablePartition().tf();
	}
	
	protected TablePartitionImpl getTablePartition()
	{
		return (TablePartitionImpl) getParentNode();
	}
	
	protected void setCoords(int rowIndex)
	{
		this.rowIndex = rowIndex;
	}
	
	// =========================================================================
	
	@Override
	public void childInserted(Backbone prev, Backbone added)
	{
		if (isAttachedToTable())
			getTablePartition().invalidate();
	}
	
	@Override
	public void childRemoved(Backbone prev, Backbone removed)
	{
		if (isAttachedToTable())
			getTablePartition().invalidate();
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
		nameMap.put("bgcolor", Attributes.BGCOLOR);
		
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
