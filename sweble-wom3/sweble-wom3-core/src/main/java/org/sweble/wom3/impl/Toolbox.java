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

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.sweble.wom3.Wom3BulletStyle;
import org.sweble.wom3.Wom3Clear;
import org.sweble.wom3.Wom3HorizAlign;
import org.sweble.wom3.Wom3I18nDir;
import org.sweble.wom3.Wom3ImageFormat;
import org.sweble.wom3.Wom3ImageHAlign;
import org.sweble.wom3.Wom3ImageVAlign;
import org.sweble.wom3.Wom3SignatureFormat;
import org.sweble.wom3.Wom3TableCaptionAlign;
import org.sweble.wom3.Wom3TableCellScope;
import org.sweble.wom3.Wom3TableFrame;
import org.sweble.wom3.Wom3TableRules;
import org.sweble.wom3.Wom3TableVAlign;
import org.sweble.wom3.Wom3Unit;
import org.sweble.wom3.Wom3ValueWithUnit;

import de.fau.cs.osr.utils.WrappedException;
import de.fau.cs.osr.utils.XmlGrammar;

public class Toolbox
{
	@SuppressWarnings("unchecked")
	public static <T> T expectType(Class<T> type, Object obj)
	{
		if (obj != null && !type.isInstance(obj))
			throw new IllegalArgumentException(
					"Expected object of type " + type.getName() + "!");
		return (T) obj;
	}

	@SuppressWarnings("unchecked")
	public static <T> T expectType(Class<T> type, Object obj, String argName)
	{
		if (obj != null && !type.isInstance(obj))
			throw new IllegalArgumentException(
					"Expected argument `" + argName + "' to be of type " + type.getName() + "!");
		return (T) obj;
	}

	// =========================================================================

	public static void checkValidXmlName(String name)
	{
		if (name == null)
			throw new NullPointerException("Name cannot be null");

		if (!XmlGrammar.xmlName().matcher(name).matches())
			throw new IllegalArgumentException("Not a valid XML Name");
	}

	// =========================================================================

	public static void checkValidCommentText(String text)
	{
		if (text == null)
			throw new NullPointerException("Text cannot be null");

		if (!XmlGrammar.xmlCommentText().matcher(text).matches())
			throw new IllegalArgumentException("Not a valid XML Comment text");
	}

	// =========================================================================

	private static final String INVALID_NOWIKI_RX_STR = "</nowiki\\s*>";

	private static final AtomicReference<Pattern> INVALID_NOWIKI_RX = new AtomicReference<Pattern>();

	public static Pattern getInvalidNowikiRx()
	{
		Pattern value = INVALID_NOWIKI_RX.get();
		if (value == null)
		{
			synchronized (INVALID_NOWIKI_RX)
			{
				value = INVALID_NOWIKI_RX.get();
				if (value == null)
				{
					Pattern actualValue = Pattern.compile(INVALID_NOWIKI_RX_STR);
					value = actualValue;
					INVALID_NOWIKI_RX.set(value);
				}
			}
		}
		return value;
	}

	public static void checkValidNowikiText(String text)
	{
		if (text == null)
			throw new NullPointerException("Text cannot be null");

		if (Toolbox.getInvalidNowikiRx().matcher(text).find())
			throw new IllegalArgumentException("Not a valid nowiki text");
	}

	// =========================================================================

	private static final String VALID_TARGET_RX_STR =
			"(?:[^\\u0000-\\u001F\\u007F\\uFFFD<>{}|\\[\\]]+)";

	private static final AtomicReference<Pattern> VALID_TARGET_RX = new AtomicReference<Pattern>();

	public static Pattern getValidTargetRx()
	{
		Pattern value = VALID_TARGET_RX.get();
		if (value == null)
		{
			synchronized (VALID_TARGET_RX)
			{
				value = VALID_TARGET_RX.get();
				if (value == null)
				{
					Pattern actualValue = Pattern.compile(VALID_TARGET_RX_STR);
					value = actualValue;
					VALID_TARGET_RX.set(value);
				}
			}
		}
		return value;
	}

	public static void checkValidTarget(String target)
	{
		if (target == null)
			throw new UnsupportedOperationException("Cannot remove target attribute");

		if (!getValidTargetRx().matcher(target).matches())
			throw new IllegalArgumentException("Invalid target");
	}

	// =========================================================================

	private static final String VALID_TITLE_RX_STR =
			"(?:[^\\u0000-\\u001F\\u007F\\uFFFD<>{}|\\[\\]/]+)";

	private static final AtomicReference<Pattern> VALID_TITLE_RX = new AtomicReference<Pattern>();

	public static Pattern getValidTitleRx()
	{
		Pattern value = VALID_TITLE_RX.get();
		if (value == null)
		{
			synchronized (VALID_TITLE_RX)
			{
				value = VALID_TITLE_RX.get();
				if (value == null)
				{
					Pattern actualValue = Pattern.compile(VALID_TITLE_RX_STR);
					value = actualValue;
					VALID_TITLE_RX.set(value);
				}
			}
		}
		return value;
	}

	public static void checkValidTitle(String title)
			throws UnsupportedOperationException,
			IllegalArgumentException
	{
		if (title == null)
			throw new UnsupportedOperationException("Cannot remove attribute `title'");

		if (!getValidTitleRx().matcher(title).matches())
			throw new IllegalArgumentException("Invalid title: `" + title + "'");
	}

	// =========================================================================

	public static String checkValidNamespace(String namespace)
			throws IllegalArgumentException
	{
		if (namespace == null || namespace.isEmpty())
			return null;

		if (!getValidTitleRx().matcher(namespace).matches())
			throw new IllegalArgumentException("Invalid namespace");

		return namespace;
	}

	// =========================================================================

	private static final String VALID_PATH_RX_STR =
			"(?:(?:" + VALID_TITLE_RX_STR + "/)*" + VALID_TITLE_RX_STR + "?)";

	private static final AtomicReference<Pattern> VALID_PATH_RX = new AtomicReference<Pattern>();

	public static Pattern getValidPathRx()
	{
		Pattern value = VALID_PATH_RX.get();
		if (value == null)
		{
			synchronized (VALID_PATH_RX)
			{
				value = VALID_PATH_RX.get();
				if (value == null)
				{
					Pattern actualValue = Pattern.compile(VALID_PATH_RX_STR);
					value = actualValue;
					VALID_PATH_RX.set(value);
				}
			}
		}
		return value;
	}

	/**
	 * Checks for a valid path expression and removes a trailing slash if
	 * present.
	 * 
	 * @param path
	 *            The path to check.
	 * @return The path stripped of a trailing slash if present.
	 * @throws IllegalArgumentException
	 */
	public static String checkValidPath(String path)
			throws IllegalArgumentException
	{
		if (path == null || path.isEmpty())
			return null;

		if (path == null || !getValidPathRx().matcher(path).matches())
			throw new IllegalArgumentException("Invalid path: `" + path + "'");

		int l = path.length() - 1;
		return (path.charAt(l) == '/') ? path.substring(0, l) : path;
	}

	// =========================================================================

	public static void checkValidCategory(String category)
			throws UnsupportedOperationException, IllegalArgumentException
	{
		if (category == null)
			throw new UnsupportedOperationException("Cannot remove attribute `category'");

		if (!getValidTitleRx().matcher(category).matches())
			throw new IllegalArgumentException("Invalid category");
	}

	// =========================================================================

	public static DateTime stringToDateTime(String datetime)
	{
		DateTimeFormatter parser = ISODateTimeFormat.dateTimeParser();
		return parser.parseDateTime(datetime);
	}

	public static String dateTimeToString(DateTime datetime)
	{
		DateTimeFormatter formatter = ISODateTimeFormat.dateTime();
		return datetime.toString(formatter);
	}

	// =========================================================================

	public static String signatureFormatToString(Wom3SignatureFormat format)
	{
		switch (format)
		{
			case TIMESTAMP:
				return "timestamp";
			case USER:
				return "user";
			case USER_TIMESTAMP:
				return "user-timestamp";
			default:
				throw new InternalError();
		}
	}

	public static Wom3SignatureFormat stringToSignatureFormat(String value)
	{
		if (value.equals("timestamp"))
		{
			return Wom3SignatureFormat.TIMESTAMP;
		}
		else if (value.equals("user"))
		{
			return Wom3SignatureFormat.USER;
		}
		else if (value.equals("user-timestamp"))
		{
			return Wom3SignatureFormat.USER_TIMESTAMP;
		}
		else
		{
			throw new IllegalArgumentException(value);
		}
	}

	// =========================================================================

	public static Wom3ImageFormat stringToImageFormat(String value)
	{
		if (value.equals("unrestrained"))
		{
			return Wom3ImageFormat.UNRESTRAINED;
		}
		else if (value.equals("frameless"))
		{
			return Wom3ImageFormat.FRAMELESS;
		}
		else if (value.equals("thumbnail"))
		{
			return Wom3ImageFormat.THUMBNAIL;
		}
		else if (value.equals("frame"))
		{
			return Wom3ImageFormat.FRAME;
		}
		else
		{
			throw new IllegalArgumentException(value);
		}
	}

	public static String imageFormatToString(Wom3ImageFormat format)
	{
		if (format == null)
			return null;
		switch (format)
		{
			case FRAME:
				return "frame";
			case FRAMELESS:
				return "frameless";
			case THUMBNAIL:
				return "thumbnail";
			case UNRESTRAINED:
				return "unrestrained";
			default:
				throw new InternalError();
		}
	}

	// =========================================================================

	public static Wom3ImageHAlign stringToImageHAlign(String value)
	{
		if (value.equals("center"))
		{
			return Wom3ImageHAlign.CENTER;
		}
		else if (value.equals("default"))
		{
			return Wom3ImageHAlign.DEFAULT;
		}
		else if (value.equals("left"))
		{
			return Wom3ImageHAlign.LEFT;
		}
		else if (value.equals("none"))
		{
			return Wom3ImageHAlign.NONE;
		}
		else if (value.equals("right"))
		{
			return Wom3ImageHAlign.RIGHT;
		}
		else
		{
			throw new IllegalArgumentException(value);
		}
	}

	public static String imageHAlignToString(Wom3ImageHAlign halign)
	{
		if (halign == null)
			return null;
		switch (halign)
		{
			case CENTER:
				return "center";
			case DEFAULT:
				return "default";
			case LEFT:
				return "left";
			case NONE:
				return "none";
			case RIGHT:
				return "right";
			default:
				throw new InternalError();
		}
	}

	// =========================================================================

	public static Wom3ImageVAlign stringToImageVAlign(String value)
	{
		if (value.equals("baseline"))
		{
			return Wom3ImageVAlign.BASELINE;
		}
		else if (value.equals("bottom"))
		{
			return Wom3ImageVAlign.BOTTOM;
		}
		else if (value.equals("middle"))
		{
			return Wom3ImageVAlign.MIDDLE;
		}
		else if (value.equals("sub"))
		{
			return Wom3ImageVAlign.SUB;
		}
		else if (value.equals("super"))
		{
			return Wom3ImageVAlign.SUPER;
		}
		else if (value.equals("text-bottom"))
		{
			return Wom3ImageVAlign.TEXT_BOTTOM;
		}
		else if (value.equals("text-top"))
		{
			return Wom3ImageVAlign.TEXT_TOP;
		}
		else if (value.equals("top"))
		{
			return Wom3ImageVAlign.TOP;
		}
		else
		{
			throw new IllegalArgumentException(value);
		}
	}

	public static String imageVAlignToString(Wom3ImageVAlign valign)
	{
		if (valign == null)
			return null;
		switch (valign)
		{
			case BASELINE:
				return "baseline";
			case BOTTOM:
				return "bottom";
			case MIDDLE:
				return "middle";
			case SUB:
				return "sub";
			case SUPER:
				return "super";
			case TEXT_BOTTOM:
				return "text-bottom";
			case TEXT_TOP:
				return "text-top";
			case TOP:
				return "top";
			default:
				throw new InternalError();
		}
	}

	// =========================================================================

	public static Wom3I18nDir stringToDir(String value)
	{
		if (value.equalsIgnoreCase("ltr"))
		{
			return Wom3I18nDir.LTR;
		}
		else if (value.equalsIgnoreCase("rtl"))
		{
			return Wom3I18nDir.RTL;
		}
		else
		{
			throw new IllegalArgumentException(value);
		}
	}

	public static String dirToString(Wom3I18nDir dir)
	{
		if (dir == null)
			return null;
		switch (dir)
		{
			case LTR:
				return "ltr";
			case RTL:
				return "rtl";
			default:
				throw new InternalError();
		}
	}

	// =========================================================================

	public static Wom3Clear stringToClear(String value)
	{
		if (value.equalsIgnoreCase("all"))
		{
			return Wom3Clear.ALL;
		}
		else if (value.equalsIgnoreCase("left"))
		{
			return Wom3Clear.LEFT;
		}
		else if (value.equalsIgnoreCase("none"))
		{
			return Wom3Clear.NONE;
		}
		else if (value.equalsIgnoreCase("right"))
		{
			return Wom3Clear.RIGHT;
		}
		else
		{
			throw new IllegalArgumentException(value);
		}
	}

	public static String clearToString(Wom3Clear clear)
	{
		if (clear == null)
			return null;
		switch (clear)
		{
			case ALL:
				return "all";
			case LEFT:
				return "left";
			case NONE:
				return "none";
			case RIGHT:
				return "right";
			default:
				throw new InternalError();
		}
	}

	// =========================================================================

	public static String urlToString(Object value)
	{
		return value.toString();
	}

	public static URL stringToUrl(String value)
	{
		try
		{
			return new URL(value);
		}
		catch (MalformedURLException e)
		{
			throw new WrappedException(e);
		}
	}

	// =========================================================================

	public static Wom3HorizAlign stringToHorizAlign(String value)
	{
		if (value.equals("center"))
		{
			return Wom3HorizAlign.CENTER;
		}
		else if (value.equals("left"))
		{
			return Wom3HorizAlign.LEFT;
		}
		else if (value.equals("right"))
		{
			return Wom3HorizAlign.RIGHT;
		}
		else if (value.equals("justify"))
		{
			return Wom3HorizAlign.JUSTIFY;
		}
		else if (value.equals("char"))
		{
			return Wom3HorizAlign.CHAR;
		}
		else
		{
			throw new IllegalArgumentException(value);
		}
	}

	public static String horizAlignToString(Wom3HorizAlign halign)
	{
		if (halign == null)
			return null;
		switch (halign)
		{
			case CENTER:
				return "center";
			case CHAR:
				return "char";
			case JUSTIFY:
				return "justify";
			case LEFT:
				return "left";
			case RIGHT:
				return "right";
			default:
				throw new InternalError();
		}
	}

	// =========================================================================

	private static final String VALID_LENGTH_RX_STR =
			"([-+]?\\d+)|(?:([-+]?\\d+(?:\\.\\d+)?)%)";

	private static final AtomicReference<Pattern> VALID_LENGTH_RX = new AtomicReference<Pattern>();

	public static Pattern getValidLengthRx()
	{
		Pattern value = VALID_LENGTH_RX.get();
		if (value == null)
		{
			synchronized (VALID_LENGTH_RX)
			{
				value = VALID_LENGTH_RX.get();
				if (value == null)
				{
					Pattern actualValue = Pattern.compile(VALID_LENGTH_RX_STR);
					value = actualValue;
					VALID_LENGTH_RX.set(value);
				}
			}
		}
		return value;
	}

	public static Wom3ValueWithUnit stringToLength(String value)
	{
		Matcher m = getValidLengthRx().matcher(value);
		if (m.matches())
		{
			if (m.group(1) != null)
			{
				return new ValueWithUnitImpl(Wom3Unit.PIXELS, Integer.parseInt(m.group(1)));
			}
			else
			{
				return new ValueWithUnitImpl(Wom3Unit.PERCENT, Float.parseFloat(m.group(2)));
			}
		}
		else
		{
			throw new IllegalArgumentException("Not a length value: `" + value + "'");
		}
	}

	public static String lengthToString(Wom3ValueWithUnit value)
	{
		return value.toString();
	}

	// =========================================================================

	public static Wom3BulletStyle stringToBulletStyle(String value)
	{
		if (value.equals("circle"))
		{
			return Wom3BulletStyle.CIRCLE;
		}
		else if (value.equals("disc"))
		{
			return Wom3BulletStyle.DISC;
		}
		else if (value.equals("square"))
		{
			return Wom3BulletStyle.SQUARE;
		}
		else
		{
			throw new IllegalArgumentException(value);
		}
	}

	public static String bulletStyleToString(Wom3BulletStyle value)
	{
		if (value == null)
			return null;
		switch (value)
		{
			case CIRCLE:
				return "circle";
			case DISC:
				return "disc";
			case SQUARE:
				return "square";
			default:
				throw new InternalError();
		}
	}

	// =========================================================================

	public static Wom3TableVAlign stringToTableVAlign(String value)
	{
		if (value.equals("baseline"))
		{
			return Wom3TableVAlign.BASELINE;
		}
		else if (value.equals("bottom"))
		{
			return Wom3TableVAlign.BOTTOM;
		}
		else if (value.equals("middle"))
		{
			return Wom3TableVAlign.MIDDLE;
		}
		else if (value.equals("top"))
		{
			return Wom3TableVAlign.TOP;
		}
		else
		{
			throw new IllegalArgumentException(value);
		}
	}

	public static String tableVAlignToString(Wom3TableVAlign value)
	{
		if (value == null)
			return null;
		switch (value)
		{
			case BASELINE:
				return "baseline";
			case BOTTOM:
				return "bottom";
			case MIDDLE:
				return "middle";
			case TOP:
				return "top";
			default:
				throw new InternalError();
		}
	}

	// =========================================================================

	public static Wom3TableCaptionAlign stringToCaptionAlign(String value)
	{
		if (value.equals("bottom"))
		{
			return Wom3TableCaptionAlign.BOTTOM;
		}
		else if (value.equals("left"))
		{
			return Wom3TableCaptionAlign.LEFT;
		}
		else if (value.equals("right"))
		{
			return Wom3TableCaptionAlign.RIGHT;
		}
		else if (value.equals("top"))
		{
			return Wom3TableCaptionAlign.TOP;
		}
		else
		{
			throw new IllegalArgumentException(value);
		}
	}

	public static String captionAlignToString(Wom3TableCaptionAlign value)
	{
		if (value == null)
			return null;
		switch (value)
		{
			case BOTTOM:
				return "bottom";
			case LEFT:
				return "left";
			case RIGHT:
				return "right";
			case TOP:
				return "top";
			default:
				throw new InternalError();
		}
	}

	// =========================================================================

	public static Wom3TableCellScope stringToTableScope(String value)
	{
		if (value.equals("col"))
		{
			return Wom3TableCellScope.COL;
		}
		else if (value.equals("colgroup"))
		{
			return Wom3TableCellScope.COLGROUP;
		}
		else if (value.equals("row"))
		{
			return Wom3TableCellScope.ROW;
		}
		else if (value.equals("rowgroup"))
		{
			return Wom3TableCellScope.ROWGROUP;
		}
		else
		{
			throw new IllegalArgumentException(value);
		}
	}

	public static String tableScopeToString(Wom3TableCellScope value)
	{
		if (value == null)
			return null;
		switch (value)
		{
			case COL:
				return "col";
			case COLGROUP:
				return "colgroup";
			case ROW:
				return "row";
			case ROWGROUP:
				return "rowgroup";
			default:
				throw new InternalError();
		}
	}

	// =========================================================================

	public static Wom3TableFrame stringToTableFrame(String value)
	{
		if (value.equals("above"))
		{
			return Wom3TableFrame.ABOVE;
		}
		else if (value.equals("below"))
		{
			return Wom3TableFrame.BELOW;
		}
		else if (value.equals("border"))
		{
			return Wom3TableFrame.BORDER;
		}
		else if (value.equals("box"))
		{
			return Wom3TableFrame.BOX;
		}
		else if (value.equals("hsides"))
		{
			return Wom3TableFrame.HSIDES;
		}
		else if (value.equals("lhs"))
		{
			return Wom3TableFrame.LHS;
		}
		else if (value.equals("rhs"))
		{
			return Wom3TableFrame.RHS;
		}
		else if (value.equals("void"))
		{
			return Wom3TableFrame.VOID;
		}
		else if (value.equals("vsides"))
		{
			return Wom3TableFrame.VSIDES;
		}
		else
		{
			throw new IllegalArgumentException(value);
		}
	}

	public static String tableFrameToString(Wom3TableFrame value)
	{
		if (value == null)
			return null;
		switch (value)
		{
			case ABOVE:
				return "above";
			case BELOW:
				return "below";
			case BORDER:
				return "border";
			case BOX:
				return "box";
			case HSIDES:
				return "hsides";
			case LHS:
				return "lhs";
			case RHS:
				return "rhs";
			case VOID:
				return "void";
			case VSIDES:
				return "vsides";
			default:
				throw new InternalError();
		}
	}

	// =========================================================================

	public static Wom3TableRules stringToTableRules(String value)
	{
		if (value.equals("all"))
		{
			return Wom3TableRules.ALL;
		}
		else if (value.equals("cols"))
		{
			return Wom3TableRules.COLS;
		}
		else if (value.equals("groups"))
		{
			return Wom3TableRules.GROUPS;
		}
		else if (value.equals("none"))
		{
			return Wom3TableRules.NONE;
		}
		else if (value.equals("rows"))
		{
			return Wom3TableRules.ROWS;
		}
		else
		{
			throw new IllegalArgumentException(value);
		}
	}

	public static String tableRulesToString(Wom3TableRules value)
	{
		if (value == null)
			return null;
		switch (value)
		{
			case ALL:
				return "all";
			case COLS:
				return "cols";
			case GROUPS:
				return "groups";
			case NONE:
				return "none";
			case ROWS:
				return "rows";
			default:
				throw new InternalError();
		}
	}
}
