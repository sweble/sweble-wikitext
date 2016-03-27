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

import static org.sweble.wom3.impl.CommonAttributeDescriptors.ATTR_DESC_ALIGN_LCR;
import static org.sweble.wom3.impl.CommonAttributeDescriptors.ATTR_DESC_BGCOLOR;
import static org.sweble.wom3.impl.CommonAttributeDescriptors.ATTR_DESC_WIDTH_LENGTH;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.sweble.wom3.Wom3Color;
import org.sweble.wom3.Wom3HorizAlign;
import org.sweble.wom3.Wom3Table;
import org.sweble.wom3.Wom3TableBody;
import org.sweble.wom3.Wom3TableCaption;
import org.sweble.wom3.Wom3TableFrame;
import org.sweble.wom3.Wom3TablePartition;
import org.sweble.wom3.Wom3TableRules;
import org.sweble.wom3.Wom3ValueWithUnit;

public class TableImpl
		extends
			BackboneWomElemWithCoreAttrs
		implements
			Wom3Table
{
	private static final long serialVersionUID = 1L;

	/* Doesn't work for tables. They are too complex (the caption can occur anywhere...)
	private static final ChildDescriptor[] BODY_DESCRIPTOR = {
			childDesc("caption"),
			descChild("tbody", ChildDescriptor.MULTIPLE) };
	*/

	private Wom3TableCaption caption;

	/*
	// TODO: A MediaWiki table may only have one body, but others might have the full set!
	private Wom3TableBody body;
	*/

	private ChildrenSubset<Wom3TablePartition> partitions =
			new ChildrenSubset<Wom3TablePartition>(1);

	// =========================================================================

	public TableImpl(DocumentImpl owner)
	{
		super(owner);
	}

	// =========================================================================

	@Override
	public String getWomName()
	{
		return "table";
	}

	// =========================================================================

	@Override
	protected void allowsInsertion(Backbone prev, Backbone child)
	{
		if (getOwnerDocument().getStrictErrorChecking())
		{
			if (BackboneChildOperationChecker.isIgnoredDefault(child))
				return;
			if ((child instanceof Wom3TableCaption) && (getCaption() == null))
				return;
			if (child instanceof Wom3TableBody)
				return;
			doesNotAllowInsertion(prev, child);
		}
	}

	@Override
	protected void allowsRemoval(Backbone child)
	{
	}

	@Override
	protected void allowsReplacement(Backbone oldChild, Backbone newChild)
	{
		if (getOwnerDocument().getStrictErrorChecking())
		{
			/**
			 * Since all children are optional and there can be any number of
			 * ignored children or partitions, any node can always be replaced
			 * by a partition or an ignored node.
			 */
			if (newChild instanceof Wom3TableBody)
				return;
			if (BackboneChildOperationChecker.isIgnoredDefault(newChild))
				return;

			/**
			 * Only one caption may be child of this node. So if we replace with
			 * a caption we must make sure that either the existing caption is
			 * replaced or that there is no caption yet.
			 */
			if ((newChild instanceof Wom3TableCaption) &&
					((oldChild == getCaption()) || (getCaption() == null)))
				return;

			doesNotAllowReplacement(oldChild, newChild);
		}
	}

	@Override
	public void childInserted(Backbone prev, Backbone added)
	{
		if (added instanceof Wom3TableCaption)
		{
			this.caption = (Wom3TableCaption) added;
		}
		else if (added instanceof Wom3TableBody)
		{
			this.partitions.insertAfter(prev, Wom3TablePartition.class, (Wom3TableBody) added);
		}
	}

	@Override
	public void childRemoved(Backbone prev, Backbone removed)
	{
		if (removed == this.caption)
		{
			this.caption = null;
		}
		else
		{
			this.partitions.remove(removed);
		}
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
		return setAlignAttr(ATTR_DESC_ALIGN_LCR, "align", align);
	}

	@Override
	public Integer getBorder()
	{
		return getIntAttr("border");
	}

	@Override
	public Integer setBorder(Integer thickness)
	{
		return setIntAttr(ATTR_DESC_BORDER, "border", thickness);
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
	public Wom3ValueWithUnit getCellPadding()
	{
		return getValueWithUnitAttr("cellpadding");
	}

	@Override
	public Wom3ValueWithUnit setCellPadding(Wom3ValueWithUnit padding)
	{
		return setValueWithUnitAttr(ATTR_DESC_CELLPADDING, "cellpadding", padding);
	}

	@Override
	public Wom3ValueWithUnit getCellSpacing()
	{
		return getValueWithUnitAttr("cellspacing");
	}

	@Override
	public Wom3ValueWithUnit setCellSpacing(Wom3ValueWithUnit spacing)
	{
		return setValueWithUnitAttr(ATTR_DESC_CELLSPACING, "cellspacing", spacing);
	}

	@Override
	public Wom3TableFrame getFrame()
	{
		return (Wom3TableFrame) getAttributeNativeData("frame");
	}

	@Override
	public Wom3TableFrame setFrame(Wom3TableFrame frame)
	{
		return setAttributeDirect(ATTR_DESC_FRAME, "frame", frame);
	}

	@Override
	public Wom3TableRules getRules()
	{
		return (Wom3TableRules) getAttributeNativeData("rules");
	}

	@Override
	public Wom3TableRules setRules(Wom3TableRules rules)
	{
		return setAttributeDirect(ATTR_DESC_RULES, "rules", rules);
	}

	@Override
	public String getSummary()
	{
		return getStringAttr("summary");
	}

	@Override
	public String setSummary(String summary)
	{
		return setStringAttr(ATTR_DESC_SUMMARY, "summary", summary);
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

	// =========================================================================

	@Override
	public Wom3TableCaption getCaption()
	{
		return caption;
	}

	@Override
	public Wom3TableCaption setCaption(Wom3TableCaption caption)
	{
		return (Wom3TableCaption) replaceOrInsertBeforeOrAppend(
				this.caption, this.partitions.getFirstOrNull(), caption, false);
	}

	@Override
	public Collection<Wom3TablePartition> getPartitions()
	{
		return Collections.unmodifiableCollection(this.partitions);
	}

	// =========================================================================

	protected static final AttributeDescriptor ATTR_DESC_BORDER = new AttrDescBorder();

	protected static final AttributeDescriptor ATTR_DESC_CELLPADDING = new AttrDescCellPadding();

	protected static final AttributeDescriptor ATTR_DESC_CELLSPACING = new AttrDescCellSpacing();

	protected static final AttributeDescriptor ATTR_DESC_FRAME = new AttrDescFrame();

	protected static final AttributeDescriptor ATTR_DESC_RULES = new AttrDescRules();

	protected static final AttributeDescriptor ATTR_DESC_SUMMARY = new AttrDescSummary();

	private static final Map<String, AttributeDescriptor> NAME_MAP = new HashMap<String, AttributeDescriptor>();

	static
	{
		NAME_MAP.putAll(UniversalAttributes.getNameMap());
		NAME_MAP.putAll(UniversalAttributes.getNameMap());
		NAME_MAP.put("align", ATTR_DESC_ALIGN_LCR);
		NAME_MAP.put("border", ATTR_DESC_BORDER);
		NAME_MAP.put("bgcolor", ATTR_DESC_BGCOLOR);
		NAME_MAP.put("cellpadding", ATTR_DESC_CELLPADDING);
		NAME_MAP.put("cellspacing", ATTR_DESC_CELLSPACING);
		NAME_MAP.put("frame", ATTR_DESC_FRAME);
		NAME_MAP.put("rules", ATTR_DESC_RULES);
		NAME_MAP.put("summary", ATTR_DESC_SUMMARY);
		NAME_MAP.put("width", ATTR_DESC_WIDTH_LENGTH);
	}

	@Override
	protected AttributeDescriptor getAttributeDescriptor(
			String namespaceUri,
			String localName,
			String qualifiedName)
	{
		return getAttrDesc(namespaceUri, localName, qualifiedName, NAME_MAP);
	}

	public static final class AttrDescBorder
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
			return AttributeVerifiers.PIXELS.verifyAndConvert(parent, verified);
		}
	}

	public static final class AttrDescCellPadding
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
			return AttributeVerifiers.LENGTH.verifyAndConvert(parent, verified);
		}
	}

	public static final class AttrDescCellSpacing
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
			return AttributeVerifiers.LENGTH.verifyAndConvert(parent, verified);
		}
	}

	public static final class AttrDescFrame
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
			return AttributeVerifiers.FRAME.verifyAndConvert(parent, verified);
		}
	}

	public static final class AttrDescRules
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
			return AttributeVerifiers.RULES.verifyAndConvert(parent, verified);
		}
	}

	public static final class AttrDescSummary
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
}
