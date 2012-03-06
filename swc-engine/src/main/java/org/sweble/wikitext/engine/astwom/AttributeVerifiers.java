package org.sweble.wikitext.engine.astwom;

import java.util.regex.Pattern;

import org.sweble.wikitext.engine.wom.WomNode;

import de.fau.cs.osr.utils.Utils;

public enum AttributeVerifiers
{
	LCR_ALIGN
	{
		@Override
		public String verify(WomNode parent, String value)
		{
			if (!Utils.isOneOf(LCR_ALIGN_EXPECTED, value))
				throw new MustBeOneOfException(LCR_ALIGN_EXPECTED, value);
			
			return value;
		}
	},
	
	LCRJ_ALIGN
	{
		@Override
		public String verify(WomNode parent, String value)
		{
			if (!Utils.isOneOf(LCRJ_ALIGN_EXPECTED, value))
				throw new MustBeOneOfException(LCRJ_ALIGN_EXPECTED, value);
			
			return value;
		}
	},
	
	PIXELS
	{
		@Override
		public String verify(WomNode parent, String value)
		{
			if (!PIXELS_EXPECTED.matcher(value).matches())
				throw new IllegalArgumentException("Expected value of type 'Pixels' but got: " + value);
			
			return value;
		}
	},
	
	LENGTH
	{
		@Override
		public String verify(WomNode parent, String value)
		{
			if (!LENGTH_EXPECTED.matcher(value).matches())
				throw new IllegalArgumentException("Expected value of type 'Length' but got: " + value);
			
			return value;
		}
	},
	
	URL
	{
		@Override
		public String verify(WomNode parent, String value)
		{
			// TODO Auto-generated method stub
			return value;
		}
	},
	
	CLEAR
	{
		@Override
		public String verify(WomNode parent, String value)
		{
			if (!Utils.isOneOf(CLEAR_EXPECTED, value))
				throw new MustBeOneOfException(CLEAR_EXPECTED, value);
			
			return value;
		}
	},
	
	SCRIPT
	{
		@Override
		public String verify(WomNode parent, String value)
		{
			// TODO Auto-generated method stub
			return value;
		}
	},
	
	DATETIME
	{
		@Override
		public String verify(WomNode parent, String value)
		{
			// YYYY-MM-DDThh:mm:ssTZD
			// TODO Auto-generated method stub
			return value;
		}
	},
	
	COLOR
	{
		@Override
		public String verify(WomNode parent, String value)
		{
			if (!COLOR_EXPECTED.matcher(value).matches())
				throw new IllegalArgumentException("Expected value of type 'Color' but got: " + value);
			
			return value;
		}
	},
	
	FONT_SIZE
	{
		@Override
		public String verify(WomNode parent, String value)
		{
			verifyRange(value, 1, 7);
			return value;
		}
	};
	
	// =========================================================================
	
	static final Pattern LENGTH_EXPECTED =
			Pattern.compile("[-+]?(\\d+|\\d+(\\.\\d+)?%)");
	
	static final Pattern PIXELS_EXPECTED =
			Pattern.compile("-0+|\\+?\\d+");
	
	static final Pattern COLOR_EXPECTED =
			Pattern.compile("[A-Za-z]+|#[0-9A-Fa-f]{3}|#[0-9A-Fa-f]{6}");
	
	static final String[] LCR_ALIGN_EXPECTED = { "left", "center", "right" };
	
	static final String[] LCRJ_ALIGN_EXPECTED = { "left", "center", "right", "justify" };
	
	static final String[] CLEAR_EXPECTED = { "left", "all", "right", "none" };
	
	// =========================================================================
	
	public abstract String verify(WomNode parent, String value);
	
	// =========================================================================
	
	/**
	 * Throw if the given value exceeds its domain.
	 * 
	 * @param value
	 *            The value to check.
	 * @param lower
	 *            The lower bound (inclusive).
	 * @param upper
	 *            The upper bound (inclusive).
	 */
	public static int verifyRange(String valueStr, int lower, int upper)
	{
		Integer x = Integer.valueOf(valueStr);
		verifyRange(x, lower, upper);
		return x;
	}
	
	/**
	 * Throw if the given value exceeds its domain.
	 * 
	 * @param value
	 *            The value to check.
	 * @param lower
	 *            The lower bound (inclusive).
	 * @param upper
	 *            The upper bound (inclusive).
	 */
	public static void verifyRange(int value, int lower, int upper)
	{
		if (value < lower || value > upper)
			throw new IllegalArgumentException(String.format(
					"Attribute value out of bounds: %d <= (%d) <= %d.",
					lower,
					value,
					upper));
	}
}
