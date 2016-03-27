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

import org.joda.time.DateTime;
import org.sweble.wom3.Wom3BulletStyle;
import org.sweble.wom3.Wom3Clear;
import org.sweble.wom3.Wom3HorizAlign;
import org.sweble.wom3.Wom3I18nDir;
import org.sweble.wom3.Wom3TableCaptionAlign;
import org.sweble.wom3.Wom3TableCellScope;
import org.sweble.wom3.Wom3TableFrame;
import org.sweble.wom3.Wom3TableRules;
import org.sweble.wom3.Wom3TableVAlign;
import org.sweble.wom3.Wom3ValueWithUnit;

public enum AttributeVerifiers implements AttributeVerificationAndConverion
{
	ID
	{
		@Override
		public boolean verifyAndConvert(
				Backbone parent,
				NativeAndStringValuePair verified)
		{
			// TODO Auto-generated method stub
			return true;
		}
	},

	NMTOKENS
	{
		@Override
		public boolean verifyAndConvert(
				Backbone parent,
				NativeAndStringValuePair verified)
		{
			// TODO Auto-generated method stub
			return true;
		}
	},

	STYLESHEET
	{
		@Override
		public boolean verifyAndConvert(
				Backbone parent,
				NativeAndStringValuePair verified)
		{
			return true;
		}
	},

	DIR
	{
		@Override
		public boolean verifyAndConvert(
				Backbone parent,
				NativeAndStringValuePair verified)
		{
			if (verified.strValue != null)
				verified.value = Toolbox.stringToDir(verified.strValue);
			else
				verified.strValue = Toolbox.dirToString((Wom3I18nDir) verified.value);
			return true;
		}
	},

	LANGUAGE_CODE
	{
		@Override
		public boolean verifyAndConvert(
				Backbone parent,
				NativeAndStringValuePair verified)
		{
			// TODO Auto-generated method stub
			return true;
		}
	},

	SCRIPT
	{
		@Override
		public boolean verifyAndConvert(
				Backbone parent,
				NativeAndStringValuePair verified)
		{
			return true;
		}
	},

	CLEAR
	{
		@Override
		public boolean verifyAndConvert(
				Backbone parent,
				NativeAndStringValuePair verified)
		{
			if (verified.strValue != null)
				verified.value = Toolbox.stringToClear(verified.strValue);
			else
				verified.strValue = Toolbox.clearToString((Wom3Clear) verified.value);
			return true;
		}
	},

	URL
	{
		@Override
		public boolean verifyAndConvert(
				Backbone parent,
				NativeAndStringValuePair verified)
		{
			if (verified.strValue != null)
				verified.value = Toolbox.stringToUrl(verified.strValue);
			else
				verified.strValue = Toolbox.urlToString((java.net.URL) verified.value);
			if (verified.strValue.isEmpty())
				throw new IllegalArgumentException("Expected non-empty URL");
			return true;
		}
	},

	DATETIME
	{
		@Override
		public boolean verifyAndConvert(
				Backbone parent,
				NativeAndStringValuePair verified)
		{
			if (verified.strValue != null)
				verified.value = Toolbox.stringToDateTime(verified.strValue);
			else
				verified.strValue = Toolbox.dateTimeToString((DateTime) verified.value);
			return true;
		}
	},

	LCR_ALIGN
	{
		@Override
		public boolean verifyAndConvert(
				Backbone parent,
				NativeAndStringValuePair verified)
		{
			if (verified.strValue != null)
				verified.value = Toolbox.stringToHorizAlign(verified.strValue);
			else
				verified.strValue = Toolbox.horizAlignToString((Wom3HorizAlign) verified.value);
			switch ((Wom3HorizAlign) verified.value)
			{
				case CHAR:
					throw new IllegalArgumentException("`char' not allowed as alignment!");
				case JUSTIFY:
					throw new IllegalArgumentException("`justify' not allowed as alignment!");
				default:
					// Allowed values
			}
			return true;
		}
	},

	LCRJ_ALIGN
	{
		@Override
		public boolean verifyAndConvert(
				Backbone parent,
				NativeAndStringValuePair verified)
		{
			if (verified.strValue != null)
				verified.value = Toolbox.stringToHorizAlign(verified.strValue);
			else
				verified.strValue = Toolbox.horizAlignToString((Wom3HorizAlign) verified.value);
			if (((Wom3HorizAlign) verified.value) == Wom3HorizAlign.CHAR)
				throw new IllegalArgumentException("`char' not allowed as alignment!");
			return true;
		}
	},

	PIXELS
	{
		@Override
		public boolean verifyAndConvert(
				Backbone parent,
				NativeAndStringValuePair verified)
		{
			if (verified.strValue != null)
				verified.value = Integer.parseInt(verified.strValue.trim());
			else
				verified.strValue = String.valueOf((Integer) verified.value);
			if (((Integer) verified.value) < 0)
				throw new IllegalArgumentException("Expected non-negative integer!");
			return true;
		}
	},

	COLOR
	{
		@Override
		public boolean verifyAndConvert(
				Backbone parent,
				NativeAndStringValuePair verified)
		{
			if (verified.strValue != null)
				verified.value = ColorImpl.valueOf(verified.strValue);
			else
				verified.strValue = verified.value.toString();
			return true;
		}
	},

	FONTSIZE
	{
		@Override
		public boolean verifyAndConvert(
				Backbone parent,
				NativeAndStringValuePair verified)
		{
			if (verified.strValue != null)
				verified.value = verifyRange(verified.strValue, 1, 7);
			else
				verified.strValue = String.valueOf(verifyRange((Integer) verified.value, 1, 7));
			return true;
		}
	},

	NUMBER
	{
		@Override
		public boolean verifyAndConvert(
				Backbone parent,
				NativeAndStringValuePair verified)
		{
			if (verified.strValue != null)
				verified.value = Integer.parseInt(verified.strValue.trim());
			else
				verified.strValue = String.valueOf((Integer) verified.value);
			if (((Integer) verified.value) < 0)
				throw new IllegalArgumentException("Expected non-negative integer!");
			return true;
		}
	},

	LENGTH
	{
		@Override
		public boolean verifyAndConvert(
				Backbone parent,
				NativeAndStringValuePair verified)
		{
			if (verified.strValue != null)
				verified.value = Toolbox.stringToLength(verified.strValue);
			else
				verified.strValue = Toolbox.lengthToString((Wom3ValueWithUnit) verified.value);
			return true;
		}
	},

	OLTYPE
	{
		@Override
		public boolean verifyAndConvert(
				Backbone parent,
				NativeAndStringValuePair verified)
		{
			return true;
		}
	},

	ULTYPE
	{
		@Override
		public boolean verifyAndConvert(
				Backbone parent,
				NativeAndStringValuePair verified)
		{
			if (verified.strValue != null)
				verified.value = Toolbox.stringToBulletStyle(verified.strValue);
			else
				verified.strValue = Toolbox.bulletStyleToString((Wom3BulletStyle) verified.value);
			return true;
		}
	},

	ITEMTYPE
	{
		@Override
		public boolean verifyAndConvert(
				Backbone parent,
				NativeAndStringValuePair verified)
		{
			return true;
		}
	},
	LCRJC_ALIGN
	{
		@Override
		public boolean verifyAndConvert(
				Backbone parent,
				NativeAndStringValuePair verified)
		{
			// We omit the "char" option since it's not supported anyway
			// by todays browsers
			return LCRJ_ALIGN.verifyAndConvert(parent, verified);
		}
	},
	TMBB_VALIGN
	{
		@Override
		public boolean verifyAndConvert(
				Backbone parent,
				NativeAndStringValuePair verified)
		{
			if (verified.strValue != null)
				verified.value = Toolbox.stringToTableVAlign(verified.strValue);
			else
				verified.strValue = Toolbox.tableVAlignToString((Wom3TableVAlign) verified.value);
			return true;
		}
	},
	TBLR_ALIGN
	{
		@Override
		public boolean verifyAndConvert(
				Backbone parent,
				NativeAndStringValuePair verified)
		{
			if (verified.strValue != null)
				verified.value = Toolbox.stringToCaptionAlign(verified.strValue);
			else
				verified.strValue = Toolbox.captionAlignToString((Wom3TableCaptionAlign) verified.value);
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
			if (verified.strValue != null)
				verified.value = Toolbox.stringToTableScope(verified.strValue);
			else
				verified.strValue = Toolbox.tableScopeToString((Wom3TableCellScope) verified.value);
			return true;
		}
	},
	FRAME
	{
		@Override
		public boolean verifyAndConvert(
				Backbone parent,
				NativeAndStringValuePair verified)
		{
			if (verified.strValue != null)
				verified.value = Toolbox.stringToTableFrame(verified.strValue);
			else
				verified.strValue = Toolbox.tableFrameToString((Wom3TableFrame) verified.value);
			return true;
		}
	},
	RULES
	{
		@Override
		public boolean verifyAndConvert(
				Backbone parent,
				NativeAndStringValuePair verified)
		{
			if (verified.strValue != null)
				verified.value = Toolbox.stringToTableRules(verified.strValue);
			else
				verified.strValue = Toolbox.tableRulesToString((Wom3TableRules) verified.value);
			return true;
		}
	},
	XML_NAME
	{
		@Override
		public boolean verifyAndConvert(
				Backbone parent,
				NativeAndStringValuePair verified)
		{
			Toolbox.checkValidXmlName(
					(verified.strValue == null) ? (String) verified.value : verified.strValue);
			return true;
		}
	};

	// =========================================================================

	public static boolean verifyAndConvertBool(
			Backbone parent,
			NativeAndStringValuePair verified,
			String attrName)
	{
		if (verified.strValue != null)
		{
			verified.value = true;
			verified.strValue = attrName;
		}
		else
			verified.strValue = attrName;
		return (Boolean) verified.value;
	}

	// =========================================================================

	/**
	 * Throw if the given value exceeds its domain.
	 * 
	 * @param valueStr
	 *            The value to check.
	 * @param lower
	 *            The lower bound (inclusive).
	 * @param upper
	 *            The upper bound (inclusive).
	 */
	public static int verifyRange(String valueStr, int lower, int upper)
	{
		Integer x = Integer.parseInt(valueStr.trim());
		verifyRange(x, lower, upper);
		return x;
	}

	/**
	 * Throw if the given value exceeds its domain.
	 * 
	 * @param value
	 *            The value to check.
	 * @param lower
	 *            The lower bound (inclusive).
	 * @param upper
	 *            The upper bound (inclusive).
	 */
	public static int verifyRange(int value, int lower, int upper)
	{
		if (value < lower || value > upper)
			throw new IllegalArgumentException(String.format(
					"Attribute value out of bounds: %d <= (%d) <= %d.",
					lower,
					value,
					upper));
		return value;
	}
}
