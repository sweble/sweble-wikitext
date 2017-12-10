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
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
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
 * @see https://en.wikipedia.org/wiki/Template:Convert/doc
 * @see https://en.wikipedia.org/wiki/Help:Convert
 */
public class Convert
		extends
			ParserFunctionBase
{
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
		if (args.size() < 2)
		{
			return error("Too few arguments!");
		}

		ArrayList<String> strArgs = new ArrayList<String>(args.size());
		for (int i = 0; i < args.size(); i++)
		{
			String tmpStr = expandArgToString(frame, args, i);
			if (tmpStr == null)
			{
				return error("Cannot convert argument to string!");
			}
			strArgs.add(tmpStr);
		}

		double value = Double.NaN;
		try
		{
			value = Double.parseDouble(strArgs.get(0));
		} catch (NumberFormatException ex)
		{
			return error("Cannot convert value argument!");
		}

		Units srcUnit = searchUnitFromName(strArgs.get(1));
		if (srcUnit == null)
		{
			return error("Cannot convert source unit argument!");
		}

		Units destUnit = null;
		if (args.size() > 2)
		{
			destUnit = searchUnitFromName(strArgs.get(2));
			if (destUnit == null)
			{
				return error("Cannot convert destination unit argument!");
			}
		}

		double convertedValue = srcUnit.getScale() * value * destUnit.getScale();

		String srcName;
		if (Math.abs(value) == 1d)
		{
			srcName = srcUnit.getUnitName();
		} else {
			srcName = srcUnit.getPluralName();
		}

		String convertedValStr;
		if (Math.abs(convertedValue) < 1E-3)
		{
			NumberFormat formatter = new DecimalFormat("0.0E0");
			convertedValStr = formatter.format(convertedValue);
			convertedValStr = convertedValStr.replace("E", "×10");
			convertedValStr = convertedValStr.replace("-", "−");
		} else if (Math.abs(convertedValue) < 10d)
		{
			convertedValStr = String.format("%.2f", convertedValue);
		} else
		{
			convertedValStr = String.format("%.1f", convertedValue);
		}

		String result = strArgs.get(0) + " " + srcName 
				+ " (" + convertedValStr + " " + destUnit.getUnitSymbol() + ")";
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

	private EngSoftErrorNode error(String msg)
	{
		return EngineRtData.set(nf().softError(
				EngineRtData.set(nf().nowiki(StringTools.escHtml(msg)))));
	}

	/**
	 * Searches the Unit according to the given name.
	 *
	 * @param name The official name or symbol for the Unit.
	 * @return The Unit corresponding to the name or null if no match was found.
	 */
	private Units searchUnitFromName(String name)
	{
			for (Units ut : Units.values())
			{
				if (name.equals(ut.getUnitSymbol())
						|| name.equals(ut.getUnitName()))
				{
					return ut;
				}
			}
			return null;
	}

	/**
	 * Checks if the given number-string is probably a valid value. Since the
	 * check is only made on appearing characters, there might be the chance
	 * that a wrong used number syntax let a conversion with {@link parseNumber}
	 * fail.
	 *
	 * @param numberStr The number as string to check.
	 * @return True if the string is most likely a valid value, otherwise false.
	 * @see parseNumber
	 */
	protected static boolean isNumberValid(String numberStr)
	{
		if (numberStr.matches("[0-9,.e/⁄\\-\\+]+"))
		{
			return true;
		}
		return false;
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
	 * @throws NumberFormatException Is thrown when the conversion failed.
	 * @see https://en.wikipedia.org/wiki/Template:Convert/doc#Numbers
	 */
	protected static double parseNumber(String numberStr) 
			throws NumberFormatException
	{
		double value;
		numberStr = numberStr.replace(",", ""); // remove thousand separators

		Matcher fractionMatcher = Pattern.compile("[/⁄]").matcher(numberStr);
		if (fractionMatcher.find())
		{
			String[] fraction = numberStr.split("//|[/⁄]");
			if (fraction.length != 2)
			{
				throw new NumberFormatException("Invalid fraction!");
			}

			String numerator = fraction[0];
			String denominator = fraction[1];
			double wholeNum = 0d; // whole number part for mixed fractions like 2½

			int idxPlus = numerator.lastIndexOf('+');
			int idxMinus = numerator.lastIndexOf('-');

			if (idxPlus > idxMinus && idxMinus != -1)
			{
				throw new NumberFormatException(
						"Should be a number, not a expression which requires"
						+ " calculations!");
			}

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
				value = wholeNum + Double.parseDouble(numerator) / Double.parseDouble(denominator);
			} catch (NumberFormatException ex)
			{
				throw ex;
			}
			return value;
		}

		try {
			value = Double.parseDouble(numberStr);
		} catch (NumberFormatException ex) {
			throw ex;
		}
		return value;
	}
}