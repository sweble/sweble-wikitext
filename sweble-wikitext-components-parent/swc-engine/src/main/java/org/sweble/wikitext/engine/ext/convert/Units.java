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

	GIGAMETRE	(UnitType.LENGTH, 1e9,		"Gm",	"gigametre",	DefCvt.MI,	null,	null,	"gigameter"),
	MEAGAMETRE	(UnitType.LENGTH, 1e6,		"Mm",	"megametre",	DefCvt.MI,	null,	null,	"megameter"),
	KILOMETRE	(UnitType.LENGTH, 1000d,	"km",	"kilometre",	DefCvt.MI,	null,	null,	"kilometer"),
	HECTOMETRE	(UnitType.LENGTH, 100d,		"hm",	"hectometre",	DefCvt.MI,	null,	null,	"hectometer"),
	DECAMETRE	(UnitType.LENGTH, 10d,		"dam",	"decametre",	DefCvt.MI,	null,	null,	"dekameter"),
	METRE		(UnitType.LENGTH, 1d,		"m",	"metre",		DefCvt.FT_AND_IN, null,	null,	"meter"),
	DECIMETRE	(UnitType.LENGTH, 0.1,		"dm",	"decimetre",	DefCvt.IN,	null,	null,	"decimeter"),
	CENTIMETRE	(UnitType.LENGTH, 0.01,		"cm",	"centimetre",	DefCvt.IN,	null,	null,	"centimeter"),
	MILLIMETRE	(UnitType.LENGTH, 0.001,	"mm",	"millimetre",	DefCvt.IN,	null,	null,	"millimeter"),
	MICROMETRE	(UnitType.LENGTH, 1e-6,		"µm",	"micrometre",	DefCvt.IN, UnitCodes.MICROMETRE, null, "micrometer"),
	NANOMETRE	(UnitType.LENGTH, 1e-9,		"nm",	"nanometre",	DefCvt.IN,	null,	null,	"nanometer"),
	ANGSTROM	(UnitType.LENGTH, 1e-10,	"Å",	"ångström",		DefCvt.IN,	UnitCodes.ANGSTROM, null, null),
	MILE			(UnitType.LENGTH, 1609.344,	"mi",	"mile",			DefCvt.KM),
	FURLONG		(UnitType.LENGTH, 201.168,	"fur",	"furlong",		DefCvt.FT_M),
	CHAIN		(UnitType.LENGTH, 20.11684023368,	"ch",	"chain",	DefCvt.FT_M),
	ROD			(UnitType.LENGTH, 5.0292,	"rd",	"rod",			DefCvt.FT_M),
	POLE			(UnitType.LENGTH, 5.0292,	"pole",	"pole",			DefCvt.FT_M),
	PERCH		(UnitType.LENGTH, 5.0292,	"perch",	"perch",	DefCvt.FT_M),
	FATHOM		(UnitType.LENGTH, 1.8288,	"fathom",	"fathom",	DefCvt.FT_M),
	YHARD		(UnitType.LENGTH, 0.9144,	"yd",	"yard",			DefCvt.M),
	FOOT		(UnitType.LENGTH, 0.3048,	"ft",	"foot",			DefCvt.M,	"feet"),
	HAND			(UnitType.LENGTH, 0.1016,	"hand",	"hand",			DefCvt.IN_CM),
	INCH		(UnitType.LENGTH, 0.0254,	"in",	"inch",			DefCvt.MM,	"inches"),
	MICORINCH	(UnitType.LENGTH, 2.54e-8,	"µin",	"microinch",	DefCvt.NM, UnitCodes.MICROINCH, null, "microinches"),
	BANANA		(UnitType.LENGTH, 0.1778,	"banana",	"banana",	DefCvt.IN_CM),
	NAUTICAL_MILE(UnitType.LENGTH, 1852d,	"nmi",	"nautical mile", DefCvt.KM_MI),
	NAUTICAL_MILE_OLD_BRIT(UnitType.LENGTH, 1853.184,	"(Brit) nmi",	"British nautical mile", DefCvt.KM_MI, UnitCodes.NAUTICAL_MILE_OLD_BRIT, null, null),
	NAUTICAL_MILE_OLD_US(UnitType.LENGTH, 1853.24496,	"(pre‑1954 US) nmi",	"(pre-1954 US) nautical mile", DefCvt.KM_MI, UnitCodes.NAUTICAL_MILE_OLD_US, null, null),
	GIGAPARSEC(UnitType.LENGTH, 3.0856776e25,	"Gpc",	"gigaparsec",	DefCvt.GLY),
	MEGAPARSEC(UnitType.LENGTH, 3.0856776e22,	"Mpc",	"megaparsec",	DefCvt.MLY),
	KILOPARSEC(UnitType.LENGTH, 3.0856776e19,	"kpc",	"kiloparsec",	DefCvt.KLY),
	PARSEC(UnitType.LENGTH, 3.0856776e16,		"pc",	"parsec",		DefCvt.LY),
	GIGALIGHT_YEAR(UnitType.LENGTH, 9.4607304725808e24,	"Gly",	"gigalight-year", DefCvt.MPC),
	MEGALIGHT_YEAR(UnitType.LENGTH, 9.4607304725808e21,	"Mly",	"megalight-year", DefCvt.KPC),
	KILOLIGHT_YEAR(UnitType.LENGTH, 9.4607304725808e18,	"kly",	"kilolight-year", DefCvt.PC),
	LIGHT_YEAR(UnitType.LENGTH, 9460730472580800d,		"ly",	"light-year", DefCvt.AU),
	ASTRONOMICAL_UNIT(UnitType.LENGTH, 149597870691d,	"AU",	"astronomical unit", DefCvt.KM_MI),

	// masses: https://en.wikipedia.org/wiki/Template:Convert/list_of_units/mass

	KILOGRAM	(UnitType.MASS, 1d,		"kg",	"kilogram",		DefCvt.LB),
	GRAM		(UnitType.MASS, 0.001,	"g",	"gram",			DefCvt.OZ),
	DECIGRAM	(UnitType.MASS, 0.0001,	"dg",	"decigram",		DefCvt.OZ),
	POUND		(UnitType.MASS, 0.45359237, "lb",	"pound",	DefCvt.KG),
	OUNCE		(UnitType.MASS, 0.028349523125, "oz",	"ounce",	DefCvt.G),
//	TONNE		(UnitType.MASS, 1000d,		"t",	"tonne",		),
//	METRIC_TON	(UnitType.MASS, 1000d,		"t",	"metric ton",	),
	;

	private final UnitType type;
	private final double scale; // is always the SI base of the corresponding unit type
	private final String unitSymbol;
	private final String unitName;
	private final String[] unitCodes;
	private final DefCvt defaultConv;
	private final String plural;
	private final String usName;

	Units(UnitType type, double scale, String unitSymbol, String unitName, DefCvt defaultConv)
	{
		this(type, scale, unitSymbol, unitName, defaultConv, null, null, null);
	}

	Units(UnitType type, double scale, String unitSymbol, String unitName, DefCvt defaultConv, String plural)
	{
		this(type, scale, unitSymbol, unitName, defaultConv, null, plural, null);
	}

	Units(UnitType type, double scale, String unitSymbol, String unitName, DefCvt defaultConv, String[] unitCodes, String plural, String usName)
	{
		this.type = type;
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

	/**
	 * Checks if two units are from the same type (e.g. UnitType.LENGTH).
	 *
	 * @param a The first unit for comparison.
	 * @param b The second unit for comparison.
	 * @return True if the type of the units are equal.
	 */
	public static boolean isSameUnitType(final Units a, final Units b)
	{
		return (a.type == b.type);
	}

	private static class UnitCodes
	{
		private final static String[] MICROMETRE = {"um"};
		private final static String[] ANGSTROM = {"angstrom"};
		private final static String[] MICROINCH = {"uin"};
		private final static String[] NAUTICAL_MILE_OLD_BRIT = {"oldUKnmi", "admiralty nmi", "Brnmi", "admi"};
		private final static String[] NAUTICAL_MILE_OLD_US = {"oldUSnmi", "pre1954USnmi", "pre1954U.S.nmi"};
	}
	
	private static enum UnitType {
		LENGTH,
		MASS,
		TIME,
		ELECTRIC_CURRENT,
		TEMPERATURE,
		AMOUNT_OF_SUBSTANCE,
		LUMINOUS_INTENSITY,
		AREA,
		DENSITY,
		ENERGY,
		FORCE,
		SPEED,
		TORQUE,
		VOLUME,
		PRESSURE,
		FUEL_EFFICIENCY,
		POWER,
		POPULATION_DENSITY,
		COST_PER_UNIT_MASS;
	}
}
