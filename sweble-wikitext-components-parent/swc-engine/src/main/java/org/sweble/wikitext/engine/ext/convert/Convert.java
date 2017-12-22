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
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
	private static DecimalFormat fmt = createDecimalFormater();
	private static DecimalFormat sciFmt = createScientificFormater();

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
		ArrayList<String> strArgs;
		double value;
		try
		{
			strArgs = parseArguments(frame, args);
			value = parseNumber(strArgs.get(0));
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

		if (strArgs.size() <= 2)
		{
			String dest = convertToDefaultUnit(value, srcUnit, true);
			String unitName = (Math.abs(value) == 1d)
					? srcUnit.getUnitName() : srcUnit.getPluralName();

			String result = srcValueStr + " " + unitName + " (" + dest + ")";
			return nf().text(result);
		}

		Units destUnit = Units.searchUnitFromName(strArgs.get(2));
		if (destUnit == null)
		{
			return error("Cannot convert destination unit argument!");
		}

		double convertedValue = srcUnit.getScale() * value / destUnit.getScale();

		String srcName;
		if (Math.abs(value) == 1d)
		{
			srcName = srcUnit.getUnitName();
		} else
		{
			srcName = srcUnit.getPluralName();
		}

		String result = srcValueStr + " " + srcName
				+ " (" + formatNumberDefault(convertedValue)
				+ " " + destUnit.getUnitSymbol() + ")";
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
		if (args.size() < 2)
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

		if (!isNumberValid(strArgs.get(0)))
		{
			throw new IllegalArgumentException("The first argument is not a vaild number!");
		}

		return strArgs;
	}

	/**
	 * Checks if the given number string is probably a valid value. Since the
	 * check is only made on appearing characters, there might be the chance,
	 * that a wrong used number syntax let a conversion with
	 * {@link parseNumber()} fail anyway.
	 *
	 * @param numberStr The number as string to check.
	 * @return True if the string is most likely a valid value, otherwise false.
	 * @see parseNumber()
	 */
	protected static boolean isNumberValid(final String numberStr)
	{
		return numberStr.matches("[0-9,.e/⁄\\-\\+–]+");
	}

	/**
	 * Tries to parse a number-string into a valid double value. The following
	 * notations are supported:
	 *
	 * "12"       = 12
	 * "1,234"    = 1234
	 * "12.3e-15" = 1.23e-14
	 *
	 * Fractions:
	 * "1/2"    = 0.5
	 * "1⁄3"    = 0.33333333
	 * "2+1⁄2"  = 2.5
	 * "-2-1⁄2" = -2.5
	 * "1//2"   = 0.5
	 *
	 * @param numberStr The number as string to convert.
	 * @return The parsed number as double value.
	 * @throws NumberFormatException
	 * @see https://en.wikipedia.org/wiki/Template:Convert/doc#Numbers
	 */
	protected static double parseNumber(final String numberStr)
			throws NumberFormatException
	{
		double value;
		String number = numberStr.replace(",", ""); // remove thousand separators
		number = number.replaceAll("–", "-"); // replace all the pesky en dashes

		Matcher fractionMatcher = Pattern.compile("[/⁄]").matcher(number);
		if (fractionMatcher.find())
		{
			String[] fraction = number.split("//|[/⁄]");
			if (fraction.length != 2)
			{
				throw new NumberFormatException("Invalid fraction!");
			}

			String numerator = fraction[0];
			String denominator = fraction[1];
			int idxPlus = numerator.lastIndexOf('+');
			int idxMinus = numerator.lastIndexOf('-');

			if (idxPlus > idxMinus && idxMinus != -1)
			{
				throw new NumberFormatException(
						"Should be a number, not a expression which requires"
						+ " calculations!");
			}

			// whole number part for mixed fractions like 2 ½
			double wholeNum = 0d;

			int idx = Math.max(idxPlus, idxMinus);
			if (idx != -1)
			{
				String wholeNumStr = numerator.substring(0, idx);
				numerator = numerator.substring(idx);

				try
				{
					wholeNum = Double.parseDouble(wholeNumStr);
				} catch (NumberFormatException ex)
				{
					throw ex;
				}
			}

			try
			{
				value = wholeNum
						+ Double.parseDouble(numerator)
						/ Double.parseDouble(denominator);
			} catch (NumberFormatException ex)
			{
				throw ex;
			}
			return value;
		}

		try
		{
			value = Double.parseDouble(number);
		} catch (NumberFormatException ex)
		{
			throw ex;
		}
		return value;
	}

	/**
	 * Converts the given source unit value to its default destination unit.
	 * This is necessary when no target unit was given via arguments.
	 *
	 * @param value The value to convert.
	 * @param srcUnit The unit type of the given value.
	 * @param isAbbreviated Determines whether the unit symbol or the entire
	 * name is used as descriptor.
	 * @return The converted and default formated number as String including the
	 * unit descriptor (symbol or name), or null on error.
	 * @see convertBaseTo()
	 */
	protected static String convertToDefaultUnit(
			double value,
			Units srcUnit,
			boolean isAbbreviated)
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
			return convertBaseTo(siBasedValue, destUnitA, isAbbreviated);
		} else
		{
			if (defCvt.isMixedNotation())
			{
				int limit = defCvt.getMixedNotationLimit();
				if (value >= limit)
				{
					return convertBaseTo(siBasedValue, destUnitA, isAbbreviated);
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

					if (isAbbreviated)
					{
						majorUnitName = majorUnit.getUnitSymbol();
						minorUnitName = minorUnit.getUnitSymbol();
					} else
					{
						if (value == 1d)
						{
							majorUnitName = majorUnit.getUnitName();
							minorUnitName = minorUnit.getUnitName();
						} else
						{
							majorUnitName = majorUnit.getPluralName();
							minorUnitName = minorUnit.getPluralName();
						}
					}
					DecimalFormat fmt = new DecimalFormat("0;−#");
					return fmt.format(convertedMajorVal)
							+ " " + majorUnitName
							+ " " + fmt.format(convertedMinorVal)
							+ " " + minorUnitName;
				}
			} else
			{
				Units destUnitB = Units.searchUnitFromName(cvtUnits[1]);
				if (destUnitB == null)
				{
					return null;
				}

				return convertBaseTo(siBasedValue, destUnitA, isAbbreviated)
						+ "; " + convertBaseTo(siBasedValue, destUnitB, isAbbreviated);
			}
		}
	}

	/**
	 * Converts a SI base value to the given destination unit and returns the
	 * default formated Number with unit descriptor.
	 *
	 * @param siBaseValue The value given in a SI base unit.
	 * @param destUnit The Unite the value gets converted to.
	 * @param isAbbreviated Determines whether the unit symbol or the entire
	 * name is used as descriptor.
	 * @return The converted and default formated number as String including the
	 * unit descriptor (symbol or name), or null on error.
	 */
	protected static String convertBaseTo(
			double siBaseValue,
			Units destUnit,
			boolean isAbbreviated)
	{
		assert (destUnit != null);

		double convertedValue = siBaseValue / destUnit.getScale();
		String destNameStr;
		if (isAbbreviated)
		{
			destNameStr = destUnit.getUnitSymbol();
		} else
		{
			if (siBaseValue == 1d)
			{
				destNameStr = destUnit.getUnitName();
			} else
			{
				destNameStr = destUnit.getPluralName();
			}
		}
		return formatNumberDefault(convertedValue) + " " + destNameStr;
	}

	/**
	 * Formats the given value like Wikipedia does on default. This includes the
	 * conversion to scientific notation, adding of thousand separators and
	 * rounding on various scales (depending on the amount of digits).
	 *
	 * @param convertedValue The value to format.
	 * @return The formated value as string.
	 */
	protected static String formatNumberDefault(double convertedValue)
	{
		String convertedValStr;
		final double absValue = Math.abs(convertedValue);
		if (absValue < 1e-9)
		{
			convertedValStr = sciFmt.format(convertedValue);
		} else if (absValue < 1d)
		{
			BigDecimal bd = new BigDecimal(convertedValue);
			bd = bd.round(new MathContext(2));
			convertedValStr = bd.toPlainString().replace('\u002D', '\u2212');
		} else if (absValue < 10d)
		{
			convertedValStr = formatNumberRounded(convertedValue, 1);
		} else if (absValue < 100d)
		{
			convertedValStr = formatNumberRounded(convertedValue, 0);
		} else if (absValue < 1e9)
		{
			BigDecimal bd = new BigDecimal(convertedValue);
			bd = bd.round(new MathContext(2));
			convertedValStr = formatNumberRounded(bd.doubleValue(), 0);
		} else {
			convertedValStr = sciFmt.format(convertedValue);
		}

		return convertedValStr;
	}

	/**
	 * Rounds and formats a value to the given count of digits after the
	 * floating point.
	 * 
	 * @param convertedValue The value to format.
	 * @param digitsAfterFloatingPoint Digits behind the floating point [0..16].
	 * @return The formated value as String.
	 */
	protected static String formatNumberRounded(
			double convertedValue,
			int digitsAfterFloatingPoint)
	{
		final int MAX_DIGITS_AFTER_FLOATING_POINT = 16;
		if (digitsAfterFloatingPoint > MAX_DIGITS_AFTER_FLOATING_POINT)
		{
			digitsAfterFloatingPoint = MAX_DIGITS_AFTER_FLOATING_POINT;
		}

		fmt.setMinimumFractionDigits(digitsAfterFloatingPoint);
		fmt.setMaximumFractionDigits(digitsAfterFloatingPoint);

		return fmt.format(convertedValue);
	}

	static private DecimalFormat createDecimalFormater() {
		// uses '−' (\u2212) as minus
		DecimalFormat fmt = new DecimalFormat("0.0;−#");
		fmt.setRoundingMode(RoundingMode.HALF_UP);
		fmt.setGroupingSize(3);
		fmt.setGroupingUsed(true);
		return fmt;
	}

	static private DecimalFormat createScientificFormater() {
		DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance(Locale.US);
		symbols.setExponentSeparator("×10");
		symbols.setMinusSign('−'); // uses en dash (\u2013)

		DecimalFormat fmt = new DecimalFormat("0.0E0", symbols);
		return fmt;
	}
}
