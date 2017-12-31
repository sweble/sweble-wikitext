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

import java.util.HashMap;

/**
 * The following SI Units are used as scale base for conversions.
 *<pre>
 * length               metre       m
 * mass                 kilogram    kg
 * time                 second      s
 * electric current     ampere      A
 * temperature          kelvin      K
 * amount of substance  mole        mol
 * luminous intensity   candela     cd
 * </pre>
 *
 * The complete and actual data set used from the original template (Lua) can be
 * found here: https://en.wikipedia.org/wiki/Module:Convert/data
 */
public enum Units
{
	// lengths: https://en.wikipedia.org/wiki/Template:Convert/list_of_units/length

	GIGAMETRE(UnitType.LENGTH, 1e9, "Gm", "gigametre", DefCvt.MI, new String[]{"Gm"}, null, "gigameter"),
	MEAGAMETRE(UnitType.LENGTH, 1e6, "Mm", "megametre", DefCvt.MI, new String[]{"Mm"}, null, "megameter"),
	KILOMETRE(UnitType.LENGTH, 1000d, "km", "kilometre", DefCvt.MI, new String[]{"km"}, null, "kilometer"),
	HECTOMETRE(UnitType.LENGTH, 100d, "hm", "hectometre", DefCvt.MI, new String[]{"hm"}, null, "hectometer"),
	DECAMETRE(UnitType.LENGTH, 10d, "dam", "decametre", DefCvt.MI, new String[]{"dam"}, null, "dekameter"),
	METRE(UnitType.LENGTH, 1d, "m", "metre", DefCvt.FT_AND_IN, new String[]{"m", "meters", "metres"}, null, "meter"),
	DECIMETRE(UnitType.LENGTH, 0.1, "dm", "decimetre", DefCvt.IN, null, null, "decimeter"),
	CENTIMETRE(UnitType.LENGTH, 0.01, "cm", "centimetre", DefCvt.IN, null, null, "centimeter"),
	MILLIMETRE(UnitType.LENGTH, 0.001, "mm", "millimetre", DefCvt.IN, null, null, "millimeter"),
	MICROMETRE(UnitType.LENGTH, 1e-6, "µm", "micrometre", DefCvt.IN, new String[]{"µm", "um"}, null, "micrometer"),
	NANOMETRE(UnitType.LENGTH, 1e-9, "nm", "nanometre", DefCvt.IN, null, null, "nanometer"),
	ANGSTROM(UnitType.LENGTH, 1e-10, "Å", "ångström", DefCvt.IN, new String[]{"Å", "angstrom"}, null, null),
	MILE(UnitType.LENGTH, 1609.344, "mi", "mile", DefCvt.KM),
	FURLONG(UnitType.LENGTH, 201.168, "fur", "furlong", DefCvt.FT_M),
	CHAIN(UnitType.LENGTH, 20.11684023368, "ch", "chain", DefCvt.FT_M),
	ROD(UnitType.LENGTH, 5.0292, "rd", "rod", DefCvt.FT_M),
	POLE(UnitType.LENGTH, 5.0292, "pole", "pole", DefCvt.FT_M),
	PERCH(UnitType.LENGTH, 5.0292, "perch", "perch", DefCvt.FT_M),
	FATHOM(UnitType.LENGTH, 1.8288, "fathom", "fathom", DefCvt.FT_M),
	YHARD(UnitType.LENGTH, 0.9144, "yd", "yard", DefCvt.M),
	FOOT(UnitType.LENGTH, 0.3048, "ft", "foot", DefCvt.M, "feet"),
	HAND(UnitType.LENGTH, 0.1016, "hand", "hand", DefCvt.IN_CM),
	INCH(UnitType.LENGTH, 0.0254, "in", "inch", DefCvt.MM, "inches"),
	MICORINCH(UnitType.LENGTH, 2.54e-8, "µin", "microinch", DefCvt.NM, new String[]{"µin", "uin"}, "microinches", null),
	BANANA(UnitType.LENGTH, 0.1778, "banana", "banana", DefCvt.IN_CM),
	NAUTICAL_MILE(UnitType.LENGTH, 1852d, "nmi", "nautical mile", DefCvt.KM_MI),
	NAUTICAL_MILE_OLD_BRIT(UnitType.LENGTH, 1853.184, "(Brit) nmi","British nautical mile", DefCvt.KM_MI, new String[]{"oldUKnmi", "admiralty nmi", "Brnmi", "admi"}, null, null),
	NAUTICAL_MILE_OLD_US(UnitType.LENGTH, 1853.24496, "(pre‑1954 US) nmi", "(pre-1954 US) nautical mile", DefCvt.KM_MI, new String []{"oldUSnmi", "pre1954USnmi", "pre1954U.S.nmi"}, null, null),
	GIGAPARSEC(UnitType.LENGTH, 3.0856776e25, "Gpc", "gigaparsec", DefCvt.GLY),
	MEGAPARSEC(UnitType.LENGTH, 3.0856776e22, "Mpc", "megaparsec", DefCvt.MLY),
	KILOPARSEC(UnitType.LENGTH, 3.0856776e19, "kpc", "kiloparsec", DefCvt.KLY),
	PARSEC(UnitType.LENGTH, 3.0856776e16, "pc", "parsec", DefCvt.LY),
	GIGALIGHT_YEAR(UnitType.LENGTH, 9.4607304725808e24, "Gly", "gigalight-year", DefCvt.MPC),
	MEGALIGHT_YEAR(UnitType.LENGTH, 9.4607304725808e21, "Mly", "megalight-year", DefCvt.KPC),
	KILOLIGHT_YEAR(UnitType.LENGTH, 9.4607304725808e18, "kly", "kilolight-year", DefCvt.PC),
	LIGHT_YEAR(UnitType.LENGTH, 9460730472580800d, "ly", "light-year", DefCvt.AU),
	ASTRONOMICAL_UNIT(UnitType.LENGTH, 149597870691d, "AU", "astronomical unit", DefCvt.KM_MI),

	// masses: https://en.wikipedia.org/wiki/Template:Convert/list_of_units/mass

	KILOGRAM(UnitType.MASS, 1d, "kg", "kilogram", DefCvt.LB),
	GRAM(UnitType.MASS, 0.001, "g", "gram", DefCvt.OZ),
	DECIGRAM(UnitType.MASS, 0.0001, "dg", "decigram", DefCvt.OZ),
	MILLIGRAM(UnitType.MASS, 1e-6, "mg", "milligram", DefCvt.GR),
	MICROGRAM(UnitType.MASS, 1e-9, "µg", "microgram", DefCvt.GR, new String[]{"µg", "ug", "mcg"}, null, null),
	TONNE(UnitType.MASS, 1000d, "t", "tonne", DefCvt.LT_ST),
	METRIC_TON(UnitType.MASS, 1000d, "t", "metric ton", DefCvt.LT_ST, new String[]{"MT"}, null, null),
	LONG_TON(UnitType.MASS, 1016.0469088, "long ton", "long ton", DefCvt.T, new String[]{"LT"}, null, null),
	SHORT_TON(UnitType.MASS, 907.18474, "short ton", "short ton", DefCvt.T, new String[]{"ST"}, null, null),
	LONG_HUNDREDWEIGHT(UnitType.MASS, 50.80234544, "long&nbsp;cwt", "long hundredweight", DefCvt.LB_KG, new String[]	{"long cwt"}, null, null),
	SHORT_HUNDREDWEIGHT(UnitType.MASS, 45.359237, "short&nbsp;cwt", "short hundredweight", DefCvt.LB_KG, new String[]	{"short cwt"}, null, null),
	LONG_QUARTER(UnitType.MASS, 12.70058636, "long&nbsp;qtr", "long quarter", DefCvt.LB_KG, new String[]	{"long qtr"}, null, null),
	SHORT_QUARTER(UnitType.MASS, 11.33980925, "short&nbsp;qtr", "short quarter", DefCvt.LB_KG, new String[]	{"short qtr"}, null, null),
	STONE(UnitType.MASS, 6.35029318, "st", "stone", DefCvt.LB_KG),
	POUND(UnitType.MASS, 0.45359237, "lb", "pound", DefCvt.KG),
	OUNCE(UnitType.MASS, 0.028349523125, "oz", "ounce", DefCvt.G),
	DRACHM(UnitType.MASS, 0.001771845195, "drachm", "drachm", DefCvt.G, new String[]{"dram"}, null, null),
	GRAIN(UnitType.MASS, 6.479891e-5, "gr", "grain", DefCvt.G),
	TROY_POUND(UnitType.MASS, 0.3732417216, "troy pound", "troy pound", DefCvt.LB_KG),
	TROY_OUNCE(UnitType.MASS, 0.0311034768, "ozt", "troy ounce", DefCvt.OZ_G),
	PENNYWEIGHT(UnitType.MASS, 0.00155517384, "dwt", "pennyweight", DefCvt.OZ_G),
	CARAT(UnitType.MASS, 0.0002, "carat", "carat", DefCvt.G),

	// areas: https://en.wikipedia.org/wiki/Template:Convert/list_of_units/area

	SQUARE_KILOMETRE(UnitType.AREA, 1e6, "km<sup>2</sup>", "square kilometre", DefCvt.SQMI, new String[]{"km2", "km²", "sqkm"}, null, "square kilometer"),
	SQUARE_HECTOMETRE(UnitType.AREA, 1e4, "hm<sup>2</sup>", "square hectometre", DefCvt.ACRE, new String[]{"hm2", "hm²"}, null, "square hectometer"),
	SQUARE_DECAMETRE(UnitType.AREA, 100d, "dam<sup>2</sup>", "square decametre", DefCvt.SQFT, new String[]{"dam2", "dam²"}, null, "square dekameter"),
	SQUARE_METRE(UnitType.AREA, 1d, "m<sup>2</sup>", "square metre", DefCvt.SQFT, new String[]{"m2", "sqm", "m²"}, null, "square meter"),
	SQUARE_DECIMETRE(UnitType.AREA, 0.01, "dm<sup>2</sup>", "square decimetre", DefCvt.SQIN, new String[]{"dm2", "dm²"}, null, "square decimeter"),
	SQUARE_CENTIMETRE(UnitType.AREA, 0.0001, "cm<sup>2</sup>", "square centimetre", DefCvt.SQIN, new String[]{"cm2", "cm²"} , null, "square centimeter"),
	SQUARE_MILLIMETRE(UnitType.AREA, 1e-6, "mm<sup>2</sup>", "square millimetre", DefCvt.SQIN, new String[]{"mm2", "mm²"}, null, "square millimeter"),
	THOUSAND_SQFT(UnitType.AREA, 92.90304, "1000&nbsp;sq&nbsp;ft", "thousand square feet", DefCvt.M2, null, "thousand square feet", null),
	ARE(UnitType.AREA, 100d, "a", "are", DefCvt.SQFT),
	ACRE(UnitType.AREA, 4046.8564224, "acres", "acre", DefCvt.HA, new String[]{"acre-sing", "acres"}, null, null),
	ARPENT(UnitType.AREA, 3418.89, "arpent", "arpent", DefCvt.HA, new String[]{"sq arp"}, null, null),
	CUERDA(UnitType.AREA, 3930.395625, "cda", "cuerda", DefCvt.HA_ACRE),
	DAA(UnitType.AREA, 1000d, "daa", "decare", DefCvt.KM2_SQMI),
	DUNAM(UnitType.AREA, 1000d, "dunam", "dunam", DefCvt.KM2_SQMI),
	DUNUM(UnitType.AREA, 1000d, "dunum", "dunum", DefCvt.KM2_SQMI),
	HECTARE(UnitType.AREA, 1e4, "ha", "hectare", DefCvt.ACRE),
	IRISH_ACRE(UnitType.AREA, 6555.2385024, "Irish&nbsp;acres", "Irish acre", DefCvt.HA),
	PONDEMAAT(UnitType.AREA, 3674.363358816, "pond", "pondemaat", DefCvt.M2),
	PYEONG(UnitType.AREA, 3.3057851239669422, "pyeong", "pyeong", DefCvt.M2),
	RAI(UnitType.AREA, 1600d, "rai", "rai", DefCvt.M2),
	ROOD(UnitType.AREA, 1011.7141056, "rood", "rood", DefCvt.SQFT_M2),
	SQUARE_FOOT(UnitType.AREA, 0.09290304, "sq&nbsp;ft", "square foot", DefCvt.M2, new String[]{"sqft", "ft2", "sqfoot", "foot2"}, "square foot", null),
	SQUARE_INCH(UnitType.AREA, 0.00064516, "sq&nbsp;in", "square inch", DefCvt.CM2, new String[]{"sqin", "in2"}, "square inches", null),
	SQUARE_NAUTICAL_MILE(UnitType.AREA, 3429904d, "sq&nbsp;nmi", "square nautical mile", DefCvt.KM2_SQMI),
	SQUARE_MILE(UnitType.AREA, 2589988.110336, "sq&nbsp;mi", "square mile", DefCvt.KM2, new String[]{"sqmi", "mi2"}, null, null),
	SQUARE_PERCH(UnitType.AREA, 25.29285264, "perch", "perches", DefCvt.M2),
	SQUARE_VERST(UnitType.AREA, 1138062.24, "square verst", "square verst", DefCvt.KM2_SQMI),
	SQUARE_YARD(UnitType.AREA, 0.83612736, "sq&nbsp;yd", "square yard", DefCvt.M2, new String[]{"sqyd", "yd2"}, null, null),
	TSUBO(UnitType.AREA, 3.3057851239669422, "tsubo", "tsubo", DefCvt.M2, null, "tsubo", null),

	M2_PER_HA(UnitType.AREA_PER_UNIT_AREA, 0.0001, "m<sup>2</sup>/ha", "square metre per hectare", DefCvt.SQFT_PER_ACRE, null, "square metres per hectare", "square meters per hectare"),
	SQFT_PER_ACRE(UnitType.AREA_PER_UNIT_AREA, 2.295684113865932e-5, "sq&nbsp;ft/acre", "square foot per acre", DefCvt.M2_PER_HA, null, "square feet per acre", null),
	;

	private final UnitType type; /// The type of the unit (e.g. length, mass, etc.)
	private final double scale; /// Scale factor based on the underlying SI unit type.
	private final String symbol; /// The unit symbol which may contain HTML formating (e.g. "m<sup>2</sup>").
	private final String name; /// The official name of the unit.
	private final String[] altCodes; /// Alternating codes and names of the unit. [can be null]
	private final DefCvt defaultConv; /// Default unit to convert to when no target was explicitly given.
	private final String plural; /// Plural form of the name (e.g. "feet" instead of "foot") [can be null]
	private final String usName; /// U.S. spelling of the name (e.g. "meter" instead of "metre") [can be null]

	/**
	 * Static look-up table for fast access on various unit names and
	 * identifiers. Do NOT use the unit symbol for look-up, since this string
	 * may contain HTML-tags for the final output. If the symbol should also be
	 * covered by the HashMap, add another entry in the corresponding altCodes
	 * array.
	 */
	private static final HashMap<String, Units> NAME_CODE_MAP = new HashMap<String, Units>();
	static
	{
		for (final Units unit : Units.values())
		{
			NAME_CODE_MAP.put(unit.getName(), unit);

			if (unit.usName != null)
			{
				NAME_CODE_MAP.put(unit.usName, unit);
			}

			if (unit.altCodes != null)
			{
				for (final String altCode : unit.altCodes)
				{
					NAME_CODE_MAP.put(altCode, unit);
				}
			}
		}
	}

	private Units(
			UnitType type,
			double scale,
			String symbol,
			String name,
			DefCvt defCvt)
	{
		this(type, scale, symbol, name, defCvt, null, null, null);
	}

	private Units(
			UnitType type,
			double scale,
			String symbol,
			String name,
			DefCvt defCvt,
			String plural)
	{
		this(type, scale, symbol, name, defCvt, null, plural, null);
	}

	/**
	 * Constructor.
	 *
	 * @param type The type of the unit (e.g. length, mass, etc.)
	 * @param scale Scale factor based on the underlying SI unit type.
	 * @param symbol The unit symbol which may contain HTML formating (e.g.
	 * "m<sup>2</sup>").
	 * @param name The official name of the unit.
	 * @param defCvt Default unit to convert to when no target was explicitly
	 * given.
	 * @param altCodes Alternating codes and names of the unit [can be null].
	 * @param plural Plural form of the name (e.g. "feet" instead of "foot")
	 * [can be null]
	 * @param usName U.S. spelling of the name (e.g. "meter" instead of "metre")
	 * [can be null]
	 */
	private Units(
			UnitType type,
			double scale,
			String symbol,
			String name,
			DefCvt defCvt,
			String[] altCodes,
			String plural,
			String usName)
	{
		assert(type != null);
		assert(symbol != null);
		assert(name != null);
		assert(defCvt != null);

		this.type = type;
		this.scale = scale;
		this.symbol = symbol;
		this.name = name;
		this.altCodes = altCodes;
		this.defaultConv = defCvt;
		this.plural = plural;
		this.usName = usName;
	}

	public final double getScale()
	{
		return scale;
	}

	public final String getSymbol()
	{
		return symbol;
	}

	public final String getName()
	{
		return name;
	}

	public final String[] getAltCodes()
	{
		return altCodes;
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
			return name + "s";
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
		if(NAME_CODE_MAP.containsKey(name))
		{
			return NAME_CODE_MAP.get(name);
		}

		for (Units ut : Units.values())
		{
			if (name.equals(ut.getSymbol()))
			{
				return ut;
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

	private static enum UnitType {
		ABSORBED_RADIATION_DOSE,
		ACCELERATION,
		AREA,
		AREA_PER_UNIT_AREA,
		CENT,
		CHARGE,
		CHEMICAL_AMOUNT,
		LENGTH,
		MASS,
		TIME,
		ELECTRIC_CURRENT,
		TEMPERATURE,
		AMOUNT_OF_SUBSTANCE,
		LUMINOUS_INTENSITY,
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
