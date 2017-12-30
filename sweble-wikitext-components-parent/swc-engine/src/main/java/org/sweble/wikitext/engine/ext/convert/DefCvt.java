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
	KM("km"),
	M("m"),
	MM("mm"),
	NM("nm"),
	MI("mi"),
	FT("ft"),
	IN("in"),
	FT_AND_IN(new String[]{"ft", "in"}, true, 3),
	FT_M(new String[]{"ft", "m"}),
	KM_MI(new String[]{"km", "mi"}),
	IN_CM(new String[]{"in", "cm"}),
	GLY("Gly"),
	MLY("Mly"),
	KLY("kly"),
	LY("ly"),
	MPC("Mpc"),
	KPC("kpc"),
	PC("pc"),
	AU("AU"),

	OZ("oz"),
	LB("lb"),
	G("g"),
	KG("kg"),
	LT_ST(new String[]{"long ton", "short ton"}),
	GR("gr"),
	T("t"),
	LB_KG(new String[]{"lb", "kg"}),
	OZ_G(new String[]{"oz", "g"}),

	SQMI("sqmi"),
	ACRE("acre"),
	SQFT("sqft"),
	SQIN("sqin"),
	KM2("km2"),
	HA("ha"),
	M2("m2"),
	CM2("cm2"),
	HA_ACRE(new String[]{"ha", "acre"}),
	KM2_SQMI(new String[]{"km2", "sqmi"}),
	SQFT_M2(new String[]{"sqft", "m2"}),
	
	SQFT_PER_ACRE("sqft/acre"),
	M2_PER_HA("m2/ha"),
	;

	private final String[] units;
	private final boolean isMixedNotation;
	private final int mixedNotationLimit;

	private DefCvt(String unit)
	{
		this.units = new String[]{unit};
		this.isMixedNotation = false;
		this.mixedNotationLimit = Integer.MAX_VALUE;
	}

	private DefCvt(String[] units)
	{
		this(units, false, Integer.MAX_VALUE);
	}

	private DefCvt(String[] units, boolean isMixedNotation)
	{
		this(units, isMixedNotation, Integer.MAX_VALUE);
	}

	private DefCvt(String[] units, int limit)
	{
		this(units, false, limit);
	}

	private DefCvt(String[] units, boolean isMixedNotation, int limit)
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
	 * <pre>
	 * {{convert|1|m}} -> 1 metre (3 ft 3 in) // mixed units
	 * {{convert|2|m}} -> 2 metres (6 ft 7 in) // mixed units
	 * {{convert|3|m}} -> 3 metres (9.8 ft) // only in feet
	 * {{convert|4|m}} -> 4 metres (13 ft) // only in feet
	 * </pre>
	 * 
	 * @return The limit interpreted as an range (+/-) where the mixed notation
	 * is applied (limit excluded). The default value is Integer.MAX_VALUE.
	 */
	public final int getMixedNotationLimit()
	{
		return mixedNotationLimit;
	}
}
