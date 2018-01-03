/**
 * Copyright 2011 The Open Source Research Group,
 *                University of Erlangen-Nürnberg
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.sweble.wikitext.engine.ext.convert;

import de.fau.cs.osr.utils.StringTools;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.sweble.wikitext.engine.ExpansionFrame;
import org.sweble.wikitext.engine.ParserFunctionBase;
import org.sweble.wikitext.engine.config.WikiConfig;
import org.sweble.wikitext.engine.nodes.EngSoftErrorNode;
import org.sweble.wikitext.engine.nodes.EngineRtData;
import org.sweble.wikitext.parser.nodes.WtNode;
import org.sweble.wikitext.parser.utils.StringConversionException;

/**
 * Template which converts a measurement unit into another. (e.g.
 * {{convert|1|m}} -> "1 metre (3 ft 3 in)")
 *
 * @see https://en.wikipedia.org/wiki/Template:Convert/doc
 * @see https://en.wikipedia.org/wiki/Help:Convert
 */
public class Convert
		extends
		ParserFunctionBase
{
	private static int MIN_ARGS = 2;
	private static int DEFAULT_SIG_FIG = 2;
	private int sigFig = DEFAULT_SIG_FIG; // significant figures

	/**
	 * The different abbreviation modes:
	 *
	 * <pre>
	 * "out" (def): 2 metres (6 ft 7 in)
	 * "on"       : 2 m (6 ft 7 in)
	 * "unit"     : 2 m (6 ft 7 in)
	 * "in"       : 2 m (6 feet 7 inches)
	 * "none"     : 2 metres (6 feet 7 inches)
	 * "off"      : 2 metres (6 feet 7 inches)
	 * "values"   : 2 (6 ft 7 in)
	 * "~"        : 2 metres [m] (6 ft 7 in)
	 * </pre>
	 */
	static enum AbbreviationMode {OUT, ON, UNIT, IN, NONE, OFF, VALUES, TILDE};
	private AbbreviationMode abbrMode = AbbreviationMode.OUT;

	private boolean isUsNameUsed = false;

	public Convert()
	{
		super("convert");
	}

	public Convert(WikiConfig wikiConfig)
	{
		super(wikiConfig, "convert");
	}

	@Override
	public WtNode invoke(
			WtNode pnf,
			ExpansionFrame frame,
			List<? extends WtNode> args)
	{
		sigFig = DEFAULT_SIG_FIG;
		isUsNameUsed = false;
		abbrMode = AbbreviationMode.OUT;

		ArrayList<String> strArgs;
		double value;
		try
		{
			strArgs = parseArguments(frame, args);
			value = NumberFormater.parseNumber(strArgs.get(0));
		} catch (Exception ex)
		{
			return error(ex.getMessage());
		}

		Units srcUnit = Units.searchUnitFromName(strArgs.get(1));
		if (srcUnit == null)
		{
			return error("Cannot convert source unit argument!");
		}

		// Caution: Wikipedia uses '−' (\u2212) as minus sign!
		String srcValueStr = strArgs.get(0).replaceAll("-", "−");

		if (strArgs.size() == MIN_ARGS)
		{
			String dest = convertToDefaultUnit(value, srcUnit, abbrMode, sigFig, isUsNameUsed);
			String srcUnitName = getSourceUnitName(
					srcUnit,
					abbrMode,
					!(Math.abs(value) == 1d),
					isUsNameUsed);

			if (srcUnitName == null) {
				return error("Cannot determinate output format!");
			}

			String result = srcValueStr + " " + srcUnitName + "(" + dest + ")";
			return nf().text(result);
		}

		Units destUnit = Units.searchUnitFromName(strArgs.get(2));
		if (destUnit == null)
		{
			return error("Cannot convert destination unit argument!");
		}

		if (!Units.isSameUnitType(srcUnit, destUnit))
		{
			return error("Cannot convert units with different types!");
		}

		double convertedValue = srcUnit.getScale() * value / destUnit.getScale();
		String srcUnitName = getSourceUnitName(srcUnit,
				abbrMode,
				!(Math.abs(value) == 1d),
				isUsNameUsed);

		if (srcUnitName == null) {
			return error("Cannot determinate output format!");
		}

		String destUnitName = getDestUnitName(destUnit,
				abbrMode,
				!(Math.abs(convertedValue) == 1d),
				isUsNameUsed);

		String result = srcValueStr + " " + srcUnitName
				+ "(" + NumberFormater.formatNumberDefault(convertedValue, sigFig)
				+ destUnitName + ")";
		return nf().text(result);
	}

	private String expandArgToString(
			ExpansionFrame preprocessorFrame,
			List<? extends WtNode> args,
			final int index)
	{
		WtNode arg = preprocessorFrame.expand(args.get(index));

		tu().trim(arg);

		String format = null;
		try
		{
			format = tu().astToText(arg).trim();
		} catch (StringConversionException e1)
		{
		}
		return format;
	}

	private EngSoftErrorNode error(final String msg)
	{
		return EngineRtData.set(nf().softError(
				EngineRtData.set(nf().nowiki(StringTools.escHtml(msg)))));
	}

	/**
	 * Parses the given arguments into a String list and also resolves some
	 * formating parameters.
	 *
	 * @param frame
	 * @param args
	 * @throws IllegalArgumentException
	 */
	private ArrayList<String> parseArguments(
			final ExpansionFrame frame,
			final List<? extends WtNode> args)
			throws IllegalArgumentException
	{
		if (args.size() < MIN_ARGS)
		{
			throw new IllegalArgumentException("Too few arguments!");
		}

		ArrayList<String> strArgs = new ArrayList<String>(args.size());
		for (int i = 0; i < args.size(); i++)
		{
			String tmpStr = expandArgToString(frame, args, i);
			if (tmpStr == null)
			{
				throw new IllegalArgumentException("Cannot convert argument to string!");
			}
			strArgs.add(tmpStr);
		}

		if (!NumberFormater.isNumberValid(strArgs.get(0)))
		{
			throw new IllegalArgumentException("The first argument is not a vaild number!");
		}

		ArrayList<String> cleanArgs = new ArrayList<String>(strArgs.size());
		cleanArgs.add(strArgs.get(0));

		for (int i = 1; i < strArgs.size(); i++)
		{
			final String arg = strArgs.get(i);
			if (arg.contains("="))
			{
				final String[] spl = arg.split("=");
				final String opt = StringUtils.trim(spl[0]);
				final String param = StringUtils.trim(spl[1]);
				if (opt.equals("abbr"))
				{
					if (param.equals("in"))
					{
						abbrMode = AbbreviationMode.IN;
					} else if (param.equals("off") || param.equals("none"))
					{
						abbrMode = AbbreviationMode.OFF; // same as "none"
					} else if (param.equals("on"))
					{
						abbrMode = AbbreviationMode.ON;
					} else if (param.equals("out"))
					{
						abbrMode = AbbreviationMode.OUT;
					} else if (param.equals("unit"))
					{
						abbrMode = AbbreviationMode.UNIT;
					} else if (param.equals("values"))
					{
						abbrMode = AbbreviationMode.VALUES;
					} else if (param.equals("~"))
					{
						abbrMode = AbbreviationMode.TILDE;
					} else
					{
						throw new IllegalArgumentException("Invalid abbreviation parameter!");
					}
				} else if (opt.equals("sigfig"))
				{
					sigFig = Integer.parseInt(param);
					if (sigFig <= 0)
					{
						throw new IllegalArgumentException("\"sigfig=\" needs a positive integer!");
					}
				} else if (opt.equals("sp"))
				{
					isUsNameUsed = param.equals("us");
				} else
				{
					throw new IllegalArgumentException("Unknow argument!");
				}
			} else
			{
				// TODO: do something useful with the value of significant digits.
//				int sigDig;
//				try {
//					sigDig = Integer.parseInt(arg);
//				} catch (NumberFormatException ex) {
//					// not a proper number, so ignore it and go on
//					continue;
//				}

				// TODO: search for other options
				cleanArgs.add(arg);
			}
		}
		return cleanArgs;
	}

	/**
	 * Converts the given source unit value to its default destination unit.
	 * This is necessary when no target unit was given via arguments.
	 *
	 * @param value The value to convert.
	 * @param srcUnit The unit type of the given value.
	 * @param abbreviationMode Determines the way to describe the unit (symbol
	 * or name).
	 * @param sigFig  The count of significant figures to round.
	 * @param isUsNameUsed
	 * @return The converted and default formated number as String including the
	 * unit descriptor (symbol or name), or null on error.
	 * @see convertBaseTo()
	 */
	protected static String convertToDefaultUnit(
			double value,
			Units srcUnit,
			AbbreviationMode abbreviationMode,
			int sigFig,
			boolean isUsNameUsed)
	{
		assert (srcUnit != null);

		final double siBasedValue = srcUnit.getScale() * value;
		final DefCvt defCvt = srcUnit.getDefaultCvt();
		final String[] cvtUnits = defCvt.getUnits();
		final Units destUnitA = Units.searchUnitFromName(cvtUnits[0]);
		if (destUnitA == null)
		{
			return null;
		}

		if (cvtUnits.length <= 1)
		{
			return convertBaseTo(siBasedValue, destUnitA, abbreviationMode, sigFig, isUsNameUsed);
		} else
		{
			if (defCvt.isMixedNotation())
			{
				int limit = defCvt.getMixedNotationLimit();
				if (value >= limit)
				{
					return convertBaseTo(siBasedValue, destUnitA, abbreviationMode, sigFig, isUsNameUsed);
				} else
				{
					Units majorUnit = destUnitA;
					Units minorUnit = Units.searchUnitFromName(cvtUnits[1]);

					if (minorUnit == null)
					{
						return null;
					}

					double convertedVal = srcUnit.getScale() * value / minorUnit.getScale();
					double transitScale = majorUnit.getScale() / minorUnit.getScale();
					double convertedMinorVal = convertedVal % transitScale;
					double convertedMajorVal = (convertedVal - convertedMinorVal) / transitScale;
					String majorUnitName;
					String minorUnitName;

					switch (abbreviationMode)
					{
						case OUT:
						case ON:
						case UNIT:
						case VALUES:
						case TILDE:
							majorUnitName = majorUnit.getSymbol();
							minorUnitName = minorUnit.getSymbol();
							break;
						case IN:
						case NONE:
						case OFF:
							if (value == 1d)
							{
								majorUnitName = majorUnit.getName();
								minorUnitName = minorUnit.getName();
							} else
							{
								majorUnitName = majorUnit.getPluralName();
								minorUnitName = minorUnit.getPluralName();
							}
							break;
						default:
							return null;
					}

					return NumberFormater.formatRegular(convertedMajorVal)
							+ " " + majorUnitName
							+ " " + NumberFormater.formatRegular(convertedMinorVal)
							+ " " + minorUnitName;
				}
			} else
			{
				Units destUnitB = Units.searchUnitFromName(cvtUnits[1]);
				if (destUnitB == null)
				{
					return null;
				}

				return convertBaseTo(siBasedValue, destUnitA, abbreviationMode, sigFig, isUsNameUsed)
						+ "; " + convertBaseTo(siBasedValue, destUnitB, abbreviationMode, sigFig, isUsNameUsed);
			}
		}
	}

	/**
	 * Converts a SI base value to the given destination unit and returns the
	 * default formated Number with unit descriptor.
	 *
	 * @param siBaseValue The value given in a SI base unit.
	 * @param destUnit The unit the value gets converted to.
	 * @param abbreviationMode Determines whether the unit symbol or the entire
	 * name is used as descriptor.
	 * @param sigFig  The count of significant figures to round.
	 * @param isUsNameUsed Determines whether the US name is used or not (e.g.
	 * "meter" instead of "metre").
	 * @return The converted and default formated number as String including the
	 * unit descriptor (symbol or name), or null on error.
	 */
	protected static String convertBaseTo(
			double siBaseValue,
			Units destUnit,
			AbbreviationMode abbreviationMode,
			int sigFig,
			boolean isUsNameUsed)
	{
		assert (destUnit != null);

		double convertedValue = siBaseValue / destUnit.getScale();
		String destNameStr = getDestUnitName(
				destUnit,
				abbreviationMode,
				!(Math.abs(convertedValue) == 1d),
				isUsNameUsed);
		if (destNameStr == null)
		{
			return null;
		}

		return NumberFormater.formatNumberDefault(convertedValue, sigFig) + destNameStr;
	}

	static private String getSourceUnitName(Units srcUnit,
			AbbreviationMode abbrMode,
			boolean isPlural,
			boolean isUsNameUsed)
	{
		String srcUnitName;
		switch (abbrMode)
		{
			case ON:
			case UNIT:
			case IN:
				if (isUsNameUsed)
				{
					srcUnitName = srcUnit.getUsName() + " ";
				} else
				{
					srcUnitName = srcUnit.getSymbol() + " ";
				}
				break;
			case OUT:
			case NONE:
			case OFF:
				if (isUsNameUsed)
				{
					if (isPlural)
					{
						srcUnitName = srcUnit.getUsName() + "s ";
					} else
					{
						srcUnitName = srcUnit.getUsName() + " ";
					}
				} else
				{
					if (isPlural)
					{
						srcUnitName = srcUnit.getPluralName() + " ";
					} else
					{
						srcUnitName = srcUnit.getName() + " ";
					}
				}
				break;
			case VALUES:
				srcUnitName = "";
				break;
			case TILDE:
				if (isPlural)
				{
					srcUnitName = srcUnit.getPluralName();
				} else
				{
					srcUnitName = srcUnit.getName();
				}
				srcUnitName += " [" + srcUnit.getSymbol()+ "] ";
				break;
			default:
				return null;
		}
		return srcUnitName;
	}

	private static String getDestUnitName(
			Units destUnit,
			AbbreviationMode abbrMode,
			boolean isPlural,
			boolean isUsNameUsed)
	{
		String destUnitName;
		switch (abbrMode)
		{
			case OUT:
			case ON:
			case UNIT:
			case TILDE:
					destUnitName = " " + destUnit.getSymbol();
				break;
			case VALUES:
				destUnitName = "";
				break;
			case IN:
			case NONE:
			case OFF:
				if (isUsNameUsed)
				{
					if (isPlural)
					{
						destUnitName = " " + destUnit.getUsName() + "s";
					} else
					{
						destUnitName = " " + destUnit.getUsName();
					}
				} else
				{
					if (isPlural)
					{
						destUnitName = " " + destUnit.getPluralName();
					} else
					{
						destUnitName = " " + destUnit.getName();
					}
				}
				break;
			default:
				return null;
		}
		return destUnitName;
	}
}
