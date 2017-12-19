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

/**
 * The following SI Units are used as base for conversions.
 *
 * length				metre		m
 * mass					kilogram	kg
 * time					second		s
 * electric current		ampere		A
 * temperature			kelvin		K
 * amount of substance	mole		mol
 * luminous intensity	candela		cd
 */
public enum Units
{
	// lengths: https://en.wikipedia.org/wiki/Template:Convert/list_of_units/length

	GIGAMETRE	(1e9,		"Gm",	"gigametre",	DefCvt.MI,	null,	null,	"gigameter"),
	MEAGAMETRE	(1e6,		"Mm",	"megametre",	DefCvt.MI,	null,	null,	"megameter"),
	KILOMETRE	(1000d,		"km",	"kilometre",	DefCvt.MI,	null,	null,	"kilometer"),
	HECTOMETRE	(100d,		"hm",	"hectometre",	DefCvt.MI,	null,	null,	"hectometer"),
	DECAMETRE	(10d,		"dam",	"decametre",	DefCvt.MI,	null,	null,	"dekameter"),
	METRE		(1d,		"m",	"metre",		DefCvt.FT_AND_IN, null,	null,	"meter"),
	DECIMETRE	(0.1,		"dm",	"decimetre",	DefCvt.IN,	null,	null,	"decimeter"),
	CENTIMETRE	(0.01,		"cm",	"centimetre",	DefCvt.IN,	null,	null,	"centimeter"),
	MILLIMETRE	(0.001,		"mm",	"millimetre",	DefCvt.IN,	null,	null,	"millimeter"),
	MICROMETRE	(1e-6,		"µm",	"micrometre",	DefCvt.IN, UnitCodes.MICROMETRE,	null,	"micrometer"),
	NANOMETRE	(1e-9,		"nm",	"nanometre",	DefCvt.IN,	null,	null,	"nanometer"),
	ANGSTROM	(1e-10,		"Å",	"ångström",		DefCvt.IN,	UnitCodes.ANGSTROM, null, null),
	MILE			(1609.344,	"mi",	"mile",			DefCvt.KM),
	FURLONG		(201.168,	"fur",	"furlong",		DefCvt.FT_M),
	CHAIN		(20.11684023368,	"ch",	"chain",	DefCvt.FT_M),
	ROD			(5.0292,	"rd",	"rod",			DefCvt.FT_M),
	POLE			(5.0292,	"pole",	"pole",			DefCvt.FT_M),
	PERCH		(5.0292,	"perch",	"perch",	DefCvt.FT_M),
	FATHOM		(1.8288,	"fathom",	"fathom",	DefCvt.FT_M),
	YHARD		(0.9144,	"yd",	"yard",			DefCvt.M),
	FOOT		(0.3048,	"ft",	"foot",			DefCvt.M,	"feet"),
	HAND			(0.1016,	"hand",	"hand",			DefCvt.IN_CM),
	INCH		(0.0254,	"in",	"inch",			DefCvt.MM,	"inches"),
	MICORINCH	(2.54e-8,	"µin",	"microinch",	DefCvt.NM, UnitCodes.MICROINCH, null,	"microinches"),
	BANANA		(0.1778,	"banana",	"banana",	DefCvt.IN_CM),
	NAUTICAL_MILE(1852d,		"nmi",	"nautical mile", DefCvt.KM_MI),
	NAUTICAL_MILE_OLD_BRIT(1853.184,	"(Brit) nmi",	"British nautical mile", DefCvt.KM_MI, UnitCodes.NAUTICAL_MILE_OLD_BRIT,null,null),
	NAUTICAL_MILE_OLD_US(1853.24496,		"(pre‑1954 US) nmi",	"(pre-1954 US) nautical mile", DefCvt.KM_MI, UnitCodes.NAUTICAL_MILE_OLD_US, null,null),
	GIGAPARSEC(3.0856776e25,	"Gpc",	"gigaparsec",	DefCvt.GLY),
	MEGAPARSEC(3.0856776e22,	"Mpc",	"megaparsec",	DefCvt.MLY),
	KILOPARSEC(3.0856776e19,	"kpc",	"kiloparsec",	DefCvt.KLY),
	PARSEC(3.0856776e16,		"pc",	"parsec",		DefCvt.LY),
	GIGALIGHT_YEAR(9.4607304725808e24,	"Gly",	"gigalight-year", DefCvt.MPC),
	MEGALIGHT_YEAR(9.4607304725808e21,	"Mly",	"megalight-year", DefCvt.KPC),
	KILOLIGHT_YEAR(9.4607304725808e18,	"kly",	"kilolight-year", DefCvt.PC),
	LIGHT_YEAR(9460730472580800d,		"ly",	"light-year", DefCvt.AU),
	ASTRONOMICAL_UNIT(149597870691d,	"AU",	"astronomical unit", DefCvt.KM_MI),

	// masses: https://en.wikipedia.org/wiki/Template:Convert/list_of_units/mass

	KILOGRAM	(1d,		"kg",	"kilogram",		DefCvt.LB),
	GRAM		(0.001,		"g",	"gram",			DefCvt.OZ),
	DECIGRAM	(0.0001,	"dg",	"decigram",		DefCvt.OZ),
	POUND		(0.45359237, "lb",	"pound",		DefCvt.KG),
	OUNCE		(0.028349523125, "oz",	"ounce",	DefCvt.G),
//	TONNE		(1000d,		"t",	"tonne",		),
//	METRIC_TON	(1000d,		"t",	"metric ton",	),
	;

	private final double scale; // is always the SI base of the corresponding unit type
	private final String unitSymbol;
	private final String unitName;
	private final String[] unitCodes;
	private final DefCvt defaultConv;
	private final String plural;
	private final String usName;

	Units(double scale, String unitSymbol, String unitName, DefCvt defaultConv)
	{
		this(scale, unitSymbol, unitName, defaultConv, null, null, null);
	}

	Units(double scale, String unitSymbol, String unitName, DefCvt defaultConv, String plural)
	{
		this(scale, unitSymbol, unitName, defaultConv, null, plural, null);
	}

	Units(double scale, String unitSymbol, String unitName, DefCvt defaultConv, String[] unitCodes, String plural, String usName)
	{
		this.scale = scale;
		this.unitSymbol = unitSymbol;
		this.unitName = unitName;
		this.unitCodes = unitCodes;
		this.defaultConv = defaultConv;
		this.plural = plural;
		this.usName = usName;
	}

	public final double getScale()
	{
		return scale;
	}

	public final String getUnitSymbol()
	{
		return unitSymbol;
	}

	public final String getUnitName()
	{
		return unitName;
	}

	public final String[] getUnitCodes()
	{
		return unitCodes;
	}

	public final DefCvt getDefaultCvt()
	{
		return defaultConv;
	}

	/**
	 * Gets the plural form of the Unit (e.g. "feet" is the plural of "foot").
	 * When no specific plural was set, the regular name with an appended "s" is
	 * returned.
	 *
	 * @return The plural form of the Unit name.
	 */
	public final String getPluralName()
	{
		if (plural == null)
		{
			return unitName + "s";
		}
		return plural;
	}

	/**
	 * Gets the U.S. name if available (e.g. "meter" instead of "metre").
	 *
	 * @return The U.S. name or null if not available.
	 */
	public final String getUsName()
	{
		return usName;
	}

	/**
	 * Searches the Unit according to the given name.
	 *
	 * @param name The official name, symbol or alternating code for the Unit.
	 * @return The Unit corresponding to the name or null if no match was found.
	 */
	public static Units searchUnitFromName(final String name)
	{
		for (Units ut : Units.values())
		{
			if (name.equals(ut.getUnitSymbol()) || name.equals(ut.getUnitName()))
			{
				return ut;
			}

			if (ut.getUnitCodes() != null)
			{
				for (String code : ut.getUnitCodes())
				{
					if (name.equals(code))
					{
						return ut;
					}
				}
			}
		}
		return null;
	}

	private static class UnitCodes
	{
		private final static String[] MICROMETRE = {"um"};
		private final static String[] ANGSTROM = {"angstrom"};
		private final static String[] MICROINCH = {"uin"};
		private final static String[] NAUTICAL_MILE_OLD_BRIT = {"oldUKnmi", "admiralty nmi", "Brnmi", "admi"};
		private final static String[] NAUTICAL_MILE_OLD_US = {"oldUSnmi", "pre1954USnmi", "pre1954U.S.nmi"};
	}
}
