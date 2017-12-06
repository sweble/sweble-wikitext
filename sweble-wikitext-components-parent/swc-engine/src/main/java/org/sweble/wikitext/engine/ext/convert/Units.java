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


public enum Units
{
	MICORINCH(39370078.74, "uni", "µin", "microinch"),
	FOOT(3.280839895, "ft", "ft", "foot"),
	METRE(1.0, "m", "m", "metre", "meter"),
	INCH(0.3048, "in", "in", "inch"),
	YHARD(0.9144, "yd", "yd", "yard"),
	ANGSTROM(0.0000000001, "angstrom", "Å", "ångström");

	private final double length; // in meters
	private final String unitCode;
	private final String unitSymbol;
	private final String unitName;
	private final String usName;

	Units(double length, String unitCode, String unitSymbol, String unitName)
	{
		this.length = length;
		this.unitCode = unitCode;
		this.unitSymbol = unitSymbol;
		this.unitName = unitName;
		this.usName = null;
	}

	Units(double length, String unitCode, String unitSymbol, String unitName, String usName)
	{
		this.length = length;
		this.unitCode = unitCode;
		this.unitSymbol = unitSymbol;
		this.unitName = unitName;
		this.usName = usName;
	}

	public final double getLength()
	{
		return length;
	}

	public final String getUnitCode()
	{
		return unitCode;
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
	 * Gets the U.S. name if available (e.g. "meter" instead of "metre").
	 * @return the U.S. name or null if not available.
	 */
	public final String getUsName()
	{
		return usName;
	}
}
