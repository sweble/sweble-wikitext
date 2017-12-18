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

	GIGAMETRE	(1E9,		"Gm",	"gigametre",	DefCvt.MI,	null,	"gigameter"),
	MEAGAMETRE	(1E6,		"Mm",	"megametre",	DefCvt.MI,	null,	"megameter"),
	KILOMETRE	(1000d,		"km",	"kilometre",	DefCvt.MI,	null,	"kilometer"),
	HECTOMETRE	(100d,		"hm",	"hectometre",	DefCvt.MI,	null,	"hectometer"),
	DECAMETRE	(10d,		"dam",	"decametre",	DefCvt.MI,	null,	"dekameter"),
	METRE		(1d,		"m",	"metre",		DefCvt.FT_AND_IN,	null,	"meter"),
	DECIMETRE	(0.1,		"dm",	"decimetre",	DefCvt.IN,	null,	"decimeter"),
	CENTIMETRE	(0.01,		"cm",	"centimetre",	DefCvt.IN,	null,	"centimeter"),
	MILLIMETRE	(0.001,		"mm",	"millimetre",	DefCvt.IN,	null,	"millimeter"),
	MICROMETRE1	(1E-6,		"µm",	"micrometre",	DefCvt.IN,	null,	"micrometer"),
	MICROMETRE2	(1E-6,		"um",	"micrometre",	DefCvt.IN,	null,	"micrometer"),
	NANOMETRE	(1E-9,		"nm",	"nanometre",	DefCvt.IN,	null,	"nanometer"),
	ANGSTROM1	(1E-10,		"Å",	"ångström",		DefCvt.IN),
	ANGSTROM2	(1E-10,		"angstrom",	"ångström",	DefCvt.IN),
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
	MICORINCH1	(2.54E-8,	"µin",	"microinch",	DefCvt.NM,	"microinches"),
	MICORINCH2	(2.54E-8,	"uin",	"microinch",	DefCvt.NM,	"microinches"),
//	NAUTICAL_MILE(1852d,		"nmi",	"nautical mile"),
//	ASTRONOMICAL_UNIT(149597870691d,	"AU",	"astronomical unit"),
//	PICA			(0.0042175176,	"pica",	"pica"),
//	QUARTER		(402.336,	"qt",	"quarter",	"quarter"),

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
	private final DefCvt defaultConv;
	private final String plural;
	private final String usName;

	Units(double scale, String unitSymbol, String unitName, DefCvt defaultConv)
	{
		this(scale, unitSymbol, unitName, defaultConv, null, null);
	}

	Units(double scale, String unitSymbol, String unitName, DefCvt defaultConv, String plural)
	{
		this(scale, unitSymbol, unitName, defaultConv, plural, null);
	}

	Units(double scale, String unitSymbol, String unitName, DefCvt defaultConv, String plural, String usName)
	{
		this.scale = scale;
		this.unitSymbol = unitSymbol;
		this.unitName = unitName;
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
}
