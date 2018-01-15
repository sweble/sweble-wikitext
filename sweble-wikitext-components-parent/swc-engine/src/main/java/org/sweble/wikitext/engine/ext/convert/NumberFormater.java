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

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class NumberFormater
{
	private static final DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance(Locale.US);

	// uses '−' (\u2212) as minus
	private static final DecimalFormat sciFmt = new DecimalFormat("0.0E0;−#", symbols);
	private static final DecimalFormat fmt = new DecimalFormat("#0;−#", symbols);
	static
	{
		fmt.setRoundingMode(RoundingMode.HALF_UP);
		fmt.setGroupingSize(3);
		fmt.setGroupingUsed(true);
	}

	private NumberFormater()
	{
		throw new UnsupportedOperationException();
	}

	public static String formatScientific(double number)
	{
		String[] split = sciFmt.format(number).split("E");
		String supExp = asSuperscriptNumber(split[1]);
		return split[0] + "×10" + supExp;
	}

	public static String formatRegular(double number)
	{
		return fmt.format(number);
	}

	public static String formatRegular(BigDecimal number)
	{
		return fmt.format(number);
	}

	/**
	 * Rounds and formats a value to the given count of digits after the
	 * floating point.
	 * 
	 * @param convertedValue The value to format.
	 * @param digitsAfterFloatingPoint Digits behind the floating point [0..16].
	 * @return The formated value as String.
	 */
	public static String formatNumberRounded(
			double convertedValue,
			int digitsAfterFloatingPoint)
	{
		final int MAX_DIGITS_AFTER_FLOATING_POINT = 16;
		if (digitsAfterFloatingPoint > MAX_DIGITS_AFTER_FLOATING_POINT)
		{
			digitsAfterFloatingPoint = MAX_DIGITS_AFTER_FLOATING_POINT;
		}

		DecimalFormat tmpFmt = (DecimalFormat) fmt.clone();
		tmpFmt.setMinimumFractionDigits(digitsAfterFloatingPoint);
		tmpFmt.setMaximumFractionDigits(digitsAfterFloatingPoint);

		return tmpFmt.format(convertedValue);
	}

	/**
	 * Formats the given value like Wikipedia does on default. This includes the
	 * conversion to scientific notation, adding of thousand separators and
	 * rounding on various scales (depending on the amount of digits).
	 *
	 * @param convertedValue The value to format.
	 * @param sigFig The count of significant figures to round.
	 * @return The formated value as string.
	 */
	protected static String formatNumberDefault(
			double convertedValue,
			int sigFig)
	{
		String convertedValStr;
		final double absValue = Math.abs(convertedValue);
		if (absValue < 1e-9 || absValue > 1e9)
		{
			convertedValStr = formatScientific(convertedValue);
		} else
		{
			BigDecimal bd = new BigDecimal(convertedValue);
			int prec = bd.precision() - bd.scale();
			if (sigFig > prec)
			{
				convertedValStr = formatNumberRounded(convertedValue, sigFig - prec);
			} else
			{
				bd = bd.round(new MathContext(sigFig, RoundingMode.HALF_UP));
				convertedValStr = formatRegular(bd);
			}
		}
		return convertedValStr;
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
	 * Replaces all digit characters and the minus-sing with the corresponding
	 * Unicode superscript symbols.
	 *
	 * @param numStr The String containing the numbers.
	 * @return The number string with the substituted characters.
	 */
	public static String asSuperscriptNumber(String numStr)
	{
		numStr = numStr.replaceAll("0", "⁰"); // \u2070
		numStr = numStr.replaceAll("1", "¹"); // \u00B9
		numStr = numStr.replaceAll("2", "²"); // \u00B2
		numStr = numStr.replaceAll("3", "³"); // \u00B3
		numStr = numStr.replaceAll("4", "⁴"); // \u2074
		numStr = numStr.replaceAll("5", "⁵"); // \u2075
		numStr = numStr.replaceAll("6", "⁶"); // \u2076
		numStr = numStr.replaceAll("7", "⁷"); // \u2077
		numStr = numStr.replaceAll("8", "⁸"); // \u2078
		numStr = numStr.replaceAll("9", "⁹"); // \u2079
		numStr = numStr.replaceAll("-", "⁻"); // \u207B
		return numStr;
	}

	/**
	 * Replaces all digit characters and the minus-sing with the corresponding
	 * Unicode subscript symbols.
	 *
	 * @param numStr The String containing the numbers.
	 * @return The number string with the substituted characters.
	 */
	public static String asSubscriptNumber(String numStr)
	{
		numStr = numStr.replaceAll("0", "₀"); // \u2080
		numStr = numStr.replaceAll("1", "₁"); // \u2081
		numStr = numStr.replaceAll("2", "₂"); // \u2082
		numStr = numStr.replaceAll("3", "₃"); // \u2083
		numStr = numStr.replaceAll("4", "₄"); // \u2084
		numStr = numStr.replaceAll("5", "₅"); // \u2085
		numStr = numStr.replaceAll("6", "₆"); // \u2086
		numStr = numStr.replaceAll("7", "₇"); // \u2087
		numStr = numStr.replaceAll("8", "₈"); // \u2088
		numStr = numStr.replaceAll("9", "₉"); // \u2089
		numStr = numStr.replaceAll("-", "₋"); // \u208B
		return numStr;
	}
}
