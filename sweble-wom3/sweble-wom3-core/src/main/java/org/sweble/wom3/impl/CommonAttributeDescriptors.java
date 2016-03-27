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

public class CommonAttributeDescriptors
{
	public static final AttributeDescriptor ATTR_DESC_ALIGN_LCR = new AttrDescAlignLcr();

	public static final AttributeDescriptor ATTR_DESC_ALIGN_LCRJ = new AttrDescAlignLcrj();

	public static final AttributeDescriptor ATTR_DESC_ALIGN_LCRJC = new AttrDescAlignLcrjc();

	public static final AttributeDescriptor ATTR_DESC_ALIGN_TBLR = new AttrDescAlignTblr();

	public static final AttributeDescriptor ATTR_DESC_ALIGN_TMBB = new AttrDescAlignTmbb();

	public static final AttributeDescriptor ATTR_DESC_BGCOLOR = new AttrDescAlignBgColor();

	public static final AttributeDescriptor ATTR_DESC_CITE = new AttrDescUrl();

	public static final AttributeDescriptor ATTR_DESC_URL = new AttrDescUrl();

	public static final AttributeDescriptor ATTR_DESC_CLEAR = new AttrDescClear();

	public static final AttributeDescriptor ATTR_DESC_COLOR = new AttrDescColor();

	public static final AttributeDescriptor ATTR_DESC_COMPACT = new AttrDescCompact();

	public static final AttributeDescriptor ATTR_DESC_DATETIME = new AttrDescDatetime();

	public static final AttributeDescriptor ATTR_DESC_DID = new AttrDescDId();

	public static final AttributeDescriptor ATTR_DESC_FACE = new AttrDescFace();

	public static final AttributeDescriptor ATTR_DESC_FONT_SIZE = new AttrDescFontSize();

	public static final AttributeDescriptor ATTR_DESC_GENERIC = new AttrDescGeneric();

	public static final AttributeDescriptor ATTR_DESC_HEIGHT_LENGTH = new AttrDescHeightLength();

	public static final AttributeDescriptor ATTR_DESC_NOSHADE = new AttrDescNoShade();

	public static final AttributeDescriptor ATTR_DESC_SIZE = new AttrDescSize();

	public static final AttributeDescriptor ATTR_DESC_WIDTH_LENGTH = new AttrDescWidthLength();

	// =========================================================================

	public static final class AttrDescAlignLcr
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
			return AttributeVerifiers.LCR_ALIGN.verifyAndConvert(parent, verified);
		}
	}

	// =========================================================================

	public static final class AttrDescAlignLcrj
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
			return AttributeVerifiers.LCRJ_ALIGN.verifyAndConvert(parent, verified);
		}
	}

	// =========================================================================

	public static final class AttrDescAlignLcrjc
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
			return AttributeVerifiers.LCRJC_ALIGN.verifyAndConvert(parent, verified);
		}
	}

	// =========================================================================

	public static final class AttrDescAlignTblr
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
			return AttributeVerifiers.TBLR_ALIGN.verifyAndConvert(parent, verified);
		}
	}

	// =========================================================================

	public static final class AttrDescAlignTmbb
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
			return AttributeVerifiers.TMBB_VALIGN.verifyAndConvert(parent, verified);
		}
	}

	// =========================================================================

	public static final class AttrDescAlignBgColor
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
			return AttributeVerifiers.COLOR.verifyAndConvert(parent, verified);
		}
	}

	// =========================================================================

	public static final class AttrDescUrl
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
			return AttributeVerifiers.URL.verifyAndConvert(parent, verified);
		}
	}

	// =========================================================================

	public static final class AttrDescClear
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
			return AttributeVerifiers.CLEAR.verifyAndConvert(parent, verified);
		}
	}

	// =========================================================================

	public static final class AttrDescColor
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
			return AttributeVerifiers.COLOR.verifyAndConvert(parent, verified);
		}
	}

	// =========================================================================

	public static final class AttrDescCompact
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
			return AttributeVerifiers.verifyAndConvertBool(parent, verified, "compact");
		}
	}

	// =========================================================================

	public static final class AttrDescDatetime
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
			return AttributeVerifiers.DATETIME.verifyAndConvert(parent, verified);
		}
	}

	// =========================================================================

	public static final class AttrDescDId
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

	// =========================================================================

	public static final class AttrDescFace
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

	// =========================================================================

	public static final class AttrDescGeneric
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

	// =========================================================================

	public static final class AttrDescHeightLength
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

	// =========================================================================

	public static final class AttrDescNoShade
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
			return AttributeVerifiers.verifyAndConvertBool(parent, verified, "noshade");
		}
	}

	// =========================================================================

	public static final class AttrDescFontSize
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
			return AttributeVerifiers.FONTSIZE.verifyAndConvert(parent, verified);
		}
	}

	// =========================================================================

	public static final class AttrDescSize
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
			return AttributeVerifiers.NUMBER.verifyAndConvert(parent, verified);
		}
	}

	// =========================================================================

	public static final class AttrDescWidthLength
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
}
