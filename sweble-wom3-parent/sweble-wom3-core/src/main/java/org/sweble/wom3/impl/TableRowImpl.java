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

import java.util.HashMap;
import java.util.Map;

import org.sweble.wom3.Wom3Color;
import org.sweble.wom3.Wom3HorizAlign;
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

	private static final Map<String, AttributeDescriptor> NAME_MAP = new HashMap<String, AttributeDescriptor>();

	static
	{
		NAME_MAP.putAll(UniversalAttributes.getNameMap());
		NAME_MAP.put("align", ATTR_DESC_ALIGN_LCRJC);
		NAME_MAP.put("valign", ATTR_DESC_ALIGN_TMBB);
		NAME_MAP.put("bgcolor", ATTR_DESC_BGCOLOR);
	}

	@Override
	protected AttributeDescriptor getAttributeDescriptor(
			String namespaceUri,
			String localName,
			String qualifiedName)
	{
		return getAttrDesc(namespaceUri, localName, qualifiedName, NAME_MAP);
	}
}
