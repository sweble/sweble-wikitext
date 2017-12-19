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


public enum DefCvt
{
	KM(Consts.KM),
	M(Consts.M),
	MM(Consts.MM),
	NM(Consts.NM),
	MI(Consts.MI),
	FT(Consts.FT),
	IN(Consts.IN),
	FT_AND_IN(Consts.FT_IN, true, 3),
	FT_M(Consts.FT_M),
	KM_MI(Consts.KM_MI),
	IN_CM(Consts.IN_CM),
	GLY(Consts.GLY),
	MLY(Consts.MLY),
	KLY(Consts.KLY),
	LY(Consts.LY),
	MPC(Consts.MPC),
	KPC(Consts.KPC),
	PC(Consts.PC),
	AU(Consts.AU),

	OZ(Consts.OZ),
	LB(Consts.LB),
	G(Consts.G),
	KG(Consts.KG),
	LT_ST(Consts.LT_ST),
	;

	private final String[] units;
	private final boolean isMixedNotation;
	private final int mixedNotationLimit;

	DefCvt(String[] units)
	{
		this(units, false, Integer.MAX_VALUE);
	}

	DefCvt(String[] units, boolean isMixedNotation)
	{
		this(units, isMixedNotation, Integer.MAX_VALUE);
	}

	DefCvt(String[] units, int limit)
	{
		this(units, false, limit);
	}

	DefCvt(String[] units, boolean isMixedNotation, int limit)
	{
		this.units = units;
		this.isMixedNotation = isMixedNotation;
		this.mixedNotationLimit = limit;
	}

	public final String[] getUnits()
	{
		return units;
	}
	
	/**
	 * Determines if the result of the conversion is represented in mixed units
	 * e. g. "1 metre (3 ft 3 in)".
	 *
	 * @return true if the conversion output uses mixed units, otherwise false.
	 */
	public final boolean isMixedNotation()
	{
		return isMixedNotation;
	}

	/**
	 * Gives the limit of the base value where the mixed notation is applied to.
	 *
	 * Example with limit 3:
	 * {{convert|1|m}} -> 1 metre (3 ft 3 in) // mixed units
	 * {{convert|2|m}} -> 2 metres (6 ft 7 in) // mixed units
	 * {{convert|3|m}} -> 3 metres (9.8 ft) // only in feet
	 * {{convert|4|m}} -> 4 metres (13 ft) // only in feet
	 *
	 * @return The limit interpreted as an range (+/-) where the mixed notation
	 * is applied (limit excluded). The default value is Integer.MAX_VALUE.
	 */
	public final int getMixedNotationLimit()
	{
		return mixedNotationLimit;
	}

	/**
	 * Definitions of default conversions when no explicit target unit is given.
	 */
	private static class Consts
	{
		private final static String[] KM = {"km"};
		private final static String[] M = {"m"};
		private final static String[] MM = {"mm"};
		private final static String[] MI = {"mi"};
		private final static String[] FT = {"ft"};
		private final static String[] IN = {"in"};
		private final static String[] FT_IN = {"ft", "in"};
		private final static String[] FT_M = {"ft", "m"};
		private final static String[] KM_MI = {"km", "mi"};
		private final static String[] IN_CM = {"in", "cm"};
		private final static String[] NM = {"nm"};
		private final static String[] GLY = {"Gly"};
		private final static String[] MLY = {"Mly"};
		private final static String[] KLY = {"kly"};
		private final static String[] LY = {"ly"};
		private final static String[] MPC = {"Mpc"};
		private final static String[] KPC = {"kpc"};
		private final static String[] PC = {"pc"};
		private final static String[] AU = {"AU"};

		private final static String[] OZ = {"oz"};
		private final static String[] LB = {"lb"};
		private final static String[] KG = {"kg"};
		private final static String[] G = {"g"};
		private final static String[] LT_ST = {"long ton", "short ton"};

	}
}
