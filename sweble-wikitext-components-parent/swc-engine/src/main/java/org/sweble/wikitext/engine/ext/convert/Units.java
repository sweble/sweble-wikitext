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
	// absorbed radiation dose
	GRAY(UnitType.ABSORBED_RADIATION_DOSE, 1d, "Gy", "gray", DefCvt.RAD, new String[]{"Gy"}),
	RAD(UnitType.ABSORBED_RADIATION_DOSE, 0.01, "rad", "rad", DefCvt.GY),

	// acceleration
	CENTIMETRE_PER_SEC_SQ(UnitType.ACCELERATION, 0.01, "cm/s<sup>2</sup>", "centimetre per second squared", DefCvt.FT_PER_S2, new String[]{"cm/s2"}, "centimetres per second squared", "centimeters per second squared"),
	FOOT_PER_SEC_SQ(UnitType.ACCELERATION, 0.3048, "ft/s<sup>2</sup>", "foot per second squared", DefCvt.M_PER_S2, new String[]{"ft/s2"}, "feet per second squared", null),
	STANDARD_GRAVITY(UnitType.ACCELERATION, 9.80665, "g<sub>0</sub>", "standard gravity", DefCvt.M_PER_S2, new String[]{"g0"}, "standard gravities", null),
	KILOMETRE_PER_H_PER_SEC(UnitType.ACCELERATION, 0.27777777777777779, "km/(h⋅s)", "kilometre per hour per second", DefCvt.MPH_PER_S, new String[]{"km/hs, km/h/s"}, "kilometres per hour per second", "kilometers per hour per second"),
	KILOMETRE_PER_SEC_SQ(UnitType.ACCELERATION, 1000d, "km/s<sup>2</sup>", "kilometre per second squared", DefCvt.MPH_PER_S, new String[] {"km/s2"}, "kilometres per second squared", "kilometers per second squared"),
	METRE_PER_SEC_SQ(UnitType.ACCELERATION, 1d, "m/s<sup>2</sup>", "metre per second squared", DefCvt.FT_PER_S2, new String[]{"m/s2"}, "metres per second squared", "meters per second squared"),
	MILE_PER_H_PER_SEC(UnitType.ACCELERATION, 0.44704, "mph/s", "mile per hour per second", DefCvt.KM_PER_HS, new String[]{"mph/s"}, "miles per hour per second", null),

	// area: https://en.wikipedia.org/wiki/Template:Convert/list_of_units/area
	SQUARE_KILOMETRE(UnitType.AREA, 1e6, "km<sup>2</sup>", "square kilometre", DefCvt.SQMI, new String[]{"km2", "km²", "sqkm"}, null, "square kilometer"),
	SQUARE_HECTOMETRE(UnitType.AREA, 1e4, "hm<sup>2</sup>", "square hectometre", DefCvt.ACRE, new String[]{"hm2", "hm²"}, null, "square hectometer"),
	SQUARE_DECAMETRE(UnitType.AREA, 100d, "dam<sup>2</sup>", "square decametre", DefCvt.SQFT, new String[]{"dam2", "dam²"}, null, "square dekameter"),
	SQUARE_METRE(UnitType.AREA, 1d, "m<sup>2</sup>", "square metre", DefCvt.SQFT, new String[]{"m2", "m²", "sqm"}, null, "square meter"),
	SQUARE_DECIMETRE(UnitType.AREA, 0.01, "dm<sup>2</sup>", "square decimetre", DefCvt.SQIN, new String[]{"dm2", "dm²"}, null, "square decimeter"),
	SQUARE_CENTIMETRE(UnitType.AREA, 0.0001, "cm<sup>2</sup>", "square centimetre", DefCvt.SQIN, new String[]{"cm2", "cm²"} , null, "square centimeter"),
	SQUARE_MILLIMETRE(UnitType.AREA, 1e-6, "mm<sup>2</sup>", "square millimetre", DefCvt.SQIN, new String[]{"mm2", "mm²"}, null, "square millimeter"),

	THOUSAND_SQFT(UnitType.AREA, 92.90304, "1000&nbsp;sq&nbsp;ft", "thousand square feet", DefCvt.M2, null, "thousand square feet", null),
	ARE(UnitType.AREA, 100d, "a", "are", DefCvt.SQFT, new String[]{"a"}),
	ACRE(UnitType.AREA, 4046.8564224, "acres", "acre", DefCvt.HA, new String[]{"acre-sing", "acres"}),
	ARPENT(UnitType.AREA, 3418.89, "arpent", "arpent", DefCvt.HA, new String[]{"sq arp"}),
	CUERDA(UnitType.AREA, 3930.395625, "cda", "cuerda", DefCvt.HA_ACRE),
	DAA(UnitType.AREA, 1000d, "daa", "decare", DefCvt.KM2_SQMI, new String[]{"daa"}),
	DUNAM(UnitType.AREA, 1000d, "dunam", "dunam", DefCvt.KM2_SQMI),
	DUNUM(UnitType.AREA, 1000d, "dunum", "dunum", DefCvt.KM2_SQMI),
	HECTARE(UnitType.AREA, 1e4, "ha", "hectare", DefCvt.ACRE),
	IRISH_ACRE(UnitType.AREA, 6555.2385024, "Irish&nbsp;acres", "Irish acre", DefCvt.HA),
	PONDEMAAT(UnitType.AREA, 3674.363358816, "pond", "pondemaat", DefCvt.M2, new String[]{"pond"}),
	PYEONG(UnitType.AREA, 3.3057851239669422, "pyeong", "pyeong", DefCvt.M2),
	RAI(UnitType.AREA, 1600d, "rai", "rai", DefCvt.M2),
	ROOD(UnitType.AREA, 1011.7141056, "rood", "rood", DefCvt.SQFT_M2),
	SQUARE_FOOT(UnitType.AREA, 0.09290304, "sq&nbsp;ft", "square foot", DefCvt.M2, new String[]{"sqft", "ft2"}, "square feet", null),
	SQUARE_FOOT2(UnitType.AREA, 0.09290304, "sq&nbsp;ft", "square foot", DefCvt.M2, new String[]{"sqfoot", "foot2"}, "square foot", null),
	SQUARE_INCH(UnitType.AREA, 0.00064516, "sq&nbsp;in", "square inch", DefCvt.CM2, new String[]{"sqin", "in2"}, "square inches", null),
	SQUARE_NAUTICAL_MILE(UnitType.AREA, 3429904d, "sq&nbsp;nmi", "square nautical mile", DefCvt.KM2_SQMI, new String[]{"nmi2"}),
	SQUARE_MILE(UnitType.AREA, 2589988.110336, "sq&nbsp;mi", "square mile", DefCvt.KM2, new String[]{"sqmi", "mi2"}),
	SQUARE_PERCH(UnitType.AREA, 25.29285264, "perch", "perches", DefCvt.M2),
	SQUARE_VERST(UnitType.AREA, 1138062.24, "square verst", "square verst", DefCvt.KM2_SQMI, new String[]{"sqverst", "verst2"}),
	SQUARE_YARD(UnitType.AREA, 0.83612736, "sq&nbsp;yd", "square yard", DefCvt.M2, new String[]{"sqyd", "yd2"}),
	TSUBO(UnitType.AREA, 3.3057851239669422, "tsubo", "tsubo", DefCvt.M2, null, "tsubo", null),

	// area per unit area
	M2_PER_HA(UnitType.AREA_PER_UNIT_AREA, 0.0001, "m<sup>2</sup>/ha", "square metre per hectare", DefCvt.SQFT_PER_ACRE, new String[]{"m2/ha"}, "square metres per hectare", "square meters per hectare"),
	SQFT_PER_ACRE(UnitType.AREA_PER_UNIT_AREA, 2.295684113865932e-5, "sq&nbsp;ft/acre", "square foot per acre", DefCvt.M2_PER_HA, null, "square feet per acre", null),

	// cent
	CENT(UnitType.CENT, 1d, "¢", "cent", DefCvt.CENT, new String[]{"¢"}),

	// charge
	AMPERE_HOUR(UnitType.CHARGE, 3600d, "A⋅h", "ampere-hour", DefCvt.COULOMB, new String[]{"A.h"}),
	COULOMB(UnitType.CHARGE, 1d, "C", "coulomb", DefCvt.E),
	E(UnitType.CHARGE, 1.602176487e-19, "e", "elementary charge", DefCvt.COULOMB, new String[]{"e"}),

	// chemical amount
	GRAM_MOLE(UnitType.CHEMICAL_AMOUNT, 1d, "g&#8209;mol", "gram-mole", DefCvt.LBMOL, new String[]{"g-mol"}),
	GRAM_MOLE2(UnitType.CHEMICAL_AMOUNT, 1d, "gmol", "gram-mole", DefCvt.LBMOL, new String[]{"gmol"}),
	KILOMOLE(UnitType.CHEMICAL_AMOUNT, 1000d, "kmol", "kilomole", DefCvt.LBMOL, new String[]{"kmol"}),
	POUND_MOLE(UnitType.CHEMICAL_AMOUNT, 453.59237, "lb&#8209;mol", "pound-mole", DefCvt.MOL, new String[]{"lb-mol"}),
	POUND_MOLE2(UnitType.CHEMICAL_AMOUNT, 453.59237, "lbmol", "pound-mole", DefCvt.MOL, new String[]{"lbmol"}),
	MOLE(UnitType.CHEMICAL_AMOUNT, 1d, "mol", "mole", DefCvt.LBMOL, new String[]{"mol"}),

	// CO2 per unit volume
	KILOGRAM_PER_LITRE(UnitType.CO2_PER_UNIT_VOLUME, 1000d, "kg(CO<sub>2</sub>)/L", "kilogram per litre", DefCvt.LB_CO2_PER_US_GAL, new String[]{"kgCO2/L"}, "kilograms per litre", "kilograms per liter"),
	POUNT_PER_US_GAL(UnitType.CO2_PER_UNIT_VOLUME, 119.82642731689663, "lbCO2/US&nbsp;gal", "pound per US gallon", DefCvt.KG_CO2_PER_L, new String[]{"lbCO2/USgal"}, "pounds per US gallon", null),

	// density
	GRAMM_PER_CUBIC_DM(UnitType.DENSITY, 1d, "g/dm<sup>3</sup>", "gram per cubic decimetre", DefCvt.KG_PER_M3, new String[]{"g/dm3"}, "grams per cubic decimetre", "grams per cubic decimeter"),
	GRAM_PER_LITRE(UnitType.DENSITY, 1d, "g/L", "gram per litre", DefCvt.LB_PER_CUIN, new String[]{"g/L"}, "grams per litre", "grams per liter"),
	GRAM_PER_ML(UnitType.DENSITY, 1000d, "g/mL", "gram per millilitre", DefCvt.LB_PER_CUIN, new String[]{"g/mL", "g/ml"}, "grams per millilitre", "grams per milliliter"),
	KILOGRAM_PER_DM3(UnitType.DENSITY, 1000d, "kg/dm<sup>3</sup>", "kilogram per cubic decimetre", DefCvt.LB_CUFT, new String[]{"kg/dm3"}, "kilograms per cubic decimetre", "kilograms per cubic decimeter"),
	KILOGRAMM_PER_LITRE(UnitType.DENSITY, 1000d, "kg/L", "kilogram per litre", DefCvt.LB_PER_US_GAL, new String[]{"kg/L", "kg/l"}, "kilograms per litre", "kilograms per liter"),
	KILOGRAMM_PER_CUBIC_M(UnitType.DENSITY, 1d, "kg/m<sup>3</sup>", "kilogram per cubic metre", DefCvt.LB_PER_CUYD, new String[]{"kg/m3"}, "kilograms per cubic metre", "kilograms per cubic meter"),
	POUND_PER_CUBIC_FT(UnitType.DENSITY, 16.018463373960142, "lb/cu&nbsp;ft", "pound per cubic foot", DefCvt.G_PER_CM3, new String[]{"lb/cuft", "lb/ft3"}, "pounds per cubic foot", null),
	POUND_PER_CUBIC_IN(UnitType.DENSITY, 27679.904710203122, "lb/cu&nbsp;in", "pound per cubic inch", DefCvt.G_PER_CM3, new String[]{"lb/cuin"}, "pounds per cubic inch", null),
	POUND_PER_CUBIC_IN2(UnitType.DENSITY, 27679.904710203122, "lb/cu&thinsp;in", "pound per cubic inch", DefCvt.G_PER_CM3, new String[]{"lb/in3"}, "pounds per cubic inch", null),
	POUND_PER_CUBIC_YD(UnitType.DENSITY, 0.5932764212577829, "lb/cu&nbsp;yd", "pound per cubic yard", DefCvt.KG_PER_M3, new String[]{"lb/cuyd", "lb/yd3"}, "pounds per cubic yard", null),
	POUND_PER_IMP_GAL(UnitType.DENSITY, 99.776372663101697, "lb/imp&nbsp;gal", "pound per imperial gallon", DefCvt.KG_PER_L, new String[]{"lb/impgal"}, "pounds per imperial gallon", null),
	POUND_PER_US_GAL(UnitType.DENSITY, 119.82642731689663, "lb/U.S.&nbsp;gal", "pound per U.S. gallon", DefCvt.KG_PER_L, new String[]{"lb/U.S.gal"}, "pounds per U.S. gallon", null),
	POUND_PER_US_GAL2(UnitType.DENSITY, 119.82642731689663, "lb/US&nbsp;gal", "pound per US gallon", DefCvt.KG_PER_L, new String[]{"lb/USgal"}, "pounds per US gallon", null),
	POUND_PER_US_BUSHEL(UnitType.DENSITY, 12.871859780974471, "lb/US&nbsp;bu", "pound per US bushel", DefCvt.KG_PER_M3, new String[]{"lb/USbu"}, "pounds per US bushel", null),
	POUND_MASS_PER_CUBIC_IN(UnitType.DENSITY, 27679.904710203122, "lbm/cu&thinsp;in", "pound mass per cubic inch", DefCvt.G_PER_CM3, new String[]{"lbm/cuin"}, "pounds mass per cubic inch", null),
	MILLIGRAM_PER_LITRE(UnitType.DENSITY, 0.001, "mg/L", "milligram per litre", DefCvt.LB_PER_CUIN, new String[]{"mg/L"}, "milligrams per litre", "milligrams per liter"),
	OUNCE_PER_CUBIC_IN(UnitType.DENSITY, 1729.9940443876951, "oz/cu&nbsp;in", "ounce per cubic inch", DefCvt.G_PER_CM3, new String[]{"oz/cuin", "oz/in3"}, "ounces per cubic inch", null),

	// length: https://en.wikipedia.org/wiki/Template:Convert/list_of_units/length
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
	FOOT(UnitType.LENGTH, 0.3048, "ft", "foot", DefCvt.M, null, "feet", null),
	HAND(UnitType.LENGTH, 0.1016, "hand", "hand", DefCvt.IN_CM),
	INCH(UnitType.LENGTH, 0.0254, "in", "inch", DefCvt.MM, null, "inches", null),
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
			if (!NAME_CODE_MAP.containsKey(unit.getName()))
			{
				NAME_CODE_MAP.put(unit.getName(), unit);
			}

			if (unit.usName != null)
			{
				NAME_CODE_MAP.put(unit.usName, unit);
			}

			if (unit.altCodes != null)
			{
				for (final String altCode : unit.altCodes)
				{
					assert (!NAME_CODE_MAP.containsKey(altCode)) 
							: "Alternative Name Code \"" + altCode + "\" should "
							+ "be unique, but was already added in Map before!";

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
			String[] altCodes)
	{
		this(type, scale, symbol, name, defCvt, altCodes, null, null);
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
		CO2_PER_UNIT_VOLUME,
		DENSITY,
		LENGTH,
		MASS,
	}
}
