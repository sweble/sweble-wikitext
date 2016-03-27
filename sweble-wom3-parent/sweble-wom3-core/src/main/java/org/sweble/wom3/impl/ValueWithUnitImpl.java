/**
 * Copyright 2011 The Open Source Research Group,
 *                University of Erlangen-Nürnberg
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses/.
 */
package org.sweble.wom3.impl;

import java.io.Serializable;

import org.sweble.wom3.Wom3Unit;
import org.sweble.wom3.Wom3ValueWithUnit;

public class ValueWithUnitImpl
		implements
			Wom3ValueWithUnit,
			Serializable
{
	private static final long serialVersionUID = 1L;

	private Wom3Unit unit;

	private float value;

	private int intValue;

	// =========================================================================

	public ValueWithUnitImpl(Wom3Unit unit, float value)
	{
		this.unit = unit;
		this.value = value;
		this.intValue = (int) value;
	}

	public ValueWithUnitImpl(Wom3Unit unit, int value)
	{
		this.unit = unit;
		this.value = value;
		this.intValue = value;
	}

	// =========================================================================

	@Override
	public Wom3Unit getUnit()
	{
		return unit;
	}

	@Override
	public float getValue()
	{
		return value;
	}

	@Override
	public int getIntValue()
	{
		return intValue;
	}

	// =========================================================================

	@Override
	public String toString()
	{
		switch (getUnit())
		{
			case PERCENT:
				return String.valueOf(getValue()) + "%";
			case PIXELS:
				return String.valueOf(getIntValue());
			default:
				throw new InternalError();
		}
	}

	// =========================================================================

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + intValue;
		result = prime * result + ((unit == null) ? 0 : unit.hashCode());
		result = prime * result + Float.floatToIntBits(value);
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ValueWithUnitImpl other = (ValueWithUnitImpl) obj;
		if (intValue != other.intValue)
			return false;
		if (unit != other.unit)
			return false;
		if (Float.floatToIntBits(value) != Float.floatToIntBits(other.value))
			return false;
		return true;
	}
}
