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
import org.sweble.wom3.Wom3Paragraph;

public class ParagraphImpl
		extends
			BackboneWomElemWithCoreAttrs
		implements
			Wom3Paragraph
{
	private static final long serialVersionUID = 1L;

	// =========================================================================

	public ParagraphImpl(DocumentImpl owner)
	{
		super(owner);
		setTopGapAttr(0);
		setBottomGapAttr(0);
	}

	// =========================================================================

	@Override
	public String getWomName()
	{
		return "p";
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
		return setAlignAttr(CommonAttributeDescriptors.ATTR_DESC_ALIGN_LCRJ, "align", align);
	}

	@Override
	public int getTopGap()
	{
		return getIntAttr("topgap");
	}

	@Override
	public int setTopGap(int lines)
	{
		return setTopGapAttr(lines);
	}

	private Integer setTopGapAttr(int lines)
	{
		return setIntAttr(ATTR_DESC_TOPGAP, "topgap", lines);
	}

	@Override
	public int getBottomGap()
	{
		return getIntAttr("bottomgap");
	}

	@Override
	public int setBottomGap(int lines)
	{
		return setBottomGapAttr(lines);
	}

	private Integer setBottomGapAttr(int lines)
	{
		return setIntAttr(ATTR_DESC_BOTTOMGAP, "bottomgap", lines);
	}

	// =========================================================================

	protected static final AttributeDescriptor ATTR_DESC_TOPGAP = new AttrDescTopGap();

	protected static final AttributeDescriptor ATTR_DESC_BOTTOMGAP = new AttrDescBottomGap();

	private static final Map<String, AttributeDescriptor> NAME_MAP = new HashMap<String, AttributeDescriptor>();

	static
	{
		NAME_MAP.putAll(UniversalAttributes.getNameMap());
		NAME_MAP.put("align", CommonAttributeDescriptors.ATTR_DESC_ALIGN_LCRJ);
		NAME_MAP.put("topgap", ATTR_DESC_TOPGAP);
		NAME_MAP.put("bottomgap", ATTR_DESC_BOTTOMGAP);
	}

	@Override
	protected AttributeDescriptor getAttributeDescriptor(
			String namespaceUri,
			String localName,
			String qualifiedName)
	{
		return getAttrDesc(namespaceUri, localName, qualifiedName, NAME_MAP);
	}

	public static final class AttrDescTopGap
			extends
				AttributeDescriptor
	{
		@Override
		public int getFlags()
		{
			return makeFlags(
					false /* removable */,
					false /* readOnly */,
					false /* customAction */,
					Normalization.NON_CDATA);
		}

		@Override
		public boolean verifyAndConvert(
				Backbone parent,
				NativeAndStringValuePair verified)
		{
			return AttributeVerifiers.NUMBER.verifyAndConvert(parent, verified);
		}
	}

	public static final class AttrDescBottomGap
			extends
				AttributeDescriptor
	{
		@Override
		public int getFlags()
		{
			return makeFlags(
					false /* removable */,
					false /* readOnly */,
					false /* customAction */,
					Normalization.NON_CDATA);
		}

		@Override
		public boolean verifyAndConvert(
				Backbone parent,
				NativeAndStringValuePair verified)
		{
			return AttributeVerifiers.NUMBER.verifyAndConvert(parent, verified);
		}
	}
}
