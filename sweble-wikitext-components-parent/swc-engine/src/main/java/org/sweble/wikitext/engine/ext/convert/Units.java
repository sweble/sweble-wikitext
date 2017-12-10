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

	GIGAMETRE	(1E9,	"Gm",	"gigameter",	null,	"gigameter"), // mi
	MEAGAMETRE	(1E6,	"Mm",	"megametre",	null,	"megameter"),
	KILOMETRE	(1000d,	"km",	"kilometre",	null,	"kilometer"),
	HECTOMETRE	(100d,	"hm",	"hectometre",	null,	"hectometer"), // ft
	DECAMETRE	(10d,	"dam",	"decametre",	null,	"dekameter"), 
	METRE		(1d,	"m",	"metre",		null,	"meter"), // ft in
	DECIMETRE	(0.1,	"dm",	"decimetre",	null,	"decimeter"), // in
	CENTIMETRE	(0.01,	"cm",	"centimetre",	null,	"centimeter"),
	MILLIMETRE	(0.001,	"mm",	"millimetre",	null,	"millimeter"),
	MICROMETRE1	(1E-6,	"µm",	"micrometre",	null,	"micrometer"),
	MICROMETRE2	(1E-6,	"um",	"micrometre",	null,	"micrometer"),
	MICRON		(1E-6,	"µm",	"micron"),
	NANOMETRE	(1E-9,	"nm",	"nanometre",	null,	"nanometer"),
	ANGSTROM1	(1E-10,	"Å",	"ångström"),
	ANGSTROM2	(1E-10,	"angstrom",	"ångström"),
	MILE			(1609.344,	"mi",	"mile"),
	FURLONG		(201.168,	"fur",	"furlong"),
	CHAIN		(20.11684023368,	"ch",	"chain"),
	ROD			(5.0292,	"rd",	"rod"),
	POLE			(5.0292,	"pole",	"pole"),
	PERCH		(5.0292,	"perch",	"perch"),
	FATHOM		(1.8288,	"fathom",	"fathom"),
	YHARD		(0.9144,	"yd",	"yard"),
	FOOT		(0.3048,	"ft",	"foot",	"feet"),
	HAND			(0.1016,	"h", "hand"),
	INCH		(0.0254,	"in",	"in",	"inch",	"inches"),
	MICORINCH	(2.54E-8,	"uin",	"µin",	"microinch",	"microinches"),
	NAUTICAL_MILE(1852d,		"nmi",	"nautical mile"),
	ASTRONOMICAL_UNIT(149597870691d,	"AU",	"astronomical unit"),
	PICA			(0.0042175176,	"pica",	"pica"),
	QUARTER		(402.336,	"qt",	"quarter",	"quarter"),

	// masses: https://en.wikipedia.org/wiki/Template:Convert/list_of_units/mass

	GRAM		(0.001,		"g",	"gram"),
	DECIGRAM	(0.0001,	"dg",	"decigram"),
	KILOGRAM	(1d,		"kg",	"kilogram"),
	POUND		(0.45359237, "pl",	"pound"),
	OUNCE		(0.028349523125, "oz",	"ounce"),
	TONNE		(1000d,		"t",	"tonne"),
	METRIC_TON	(1000d,		"t",	"metric ton"),
	;

	private final double scale; // is always the SI base of the corresponding unit type
	private final String unitSymbol;
	private final String unitName;
	private final String plural;
	private final String usName;

	Units(double scale, String unitSymbol, String unitName)
	{
		this(scale, unitSymbol, unitName, null, null);
	}

	Units(double scale, String unitSymbol, String unitName, String plural)
	{
		this(scale, unitSymbol, unitName, plural, null);
	}

	Units(double scale, String unitSymbol, String unitName, String plural, String usName)
	{
		this.scale = scale;
		this.unitSymbol = unitSymbol;
		this.unitName = unitName;
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
