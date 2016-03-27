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

import java.util.HashMap;
import java.util.Map;

import org.sweble.wom3.Wom3Color;

public class ColorImpl
		implements
			Wom3Color
{
	private static final long serialVersionUID = 1L;

	private char r;

	private char g;

	private char b;

	// =========================================================================

	public ColorImpl(int r, int g, int b)
	{
		this.r = (char) r;
		this.g = (char) g;
		this.b = (char) b;
	}

	@Override
	public int getRed()
	{
		return this.r;
	}

	@Override
	public int getGreen()
	{
		return this.g;
	}

	@Override
	public int getBlue()
	{
		return this.b;
	}

	@Override
	public String toString()
	{
		return String.format("#%02X%02X%02X", (int) r, (int) g, (int) b);
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + b;
		result = prime * result + g;
		result = prime * result + r;
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
		ColorImpl other = (ColorImpl) obj;
		if (b != other.b)
			return false;
		if (g != other.g)
			return false;
		if (r != other.r)
			return false;
		return true;
	}

	// =========================================================================

	private static final Map<String, ColorImpl> namedColors;

	static
	{
		namedColors = new HashMap<String, ColorImpl>();

		namedColors.put("aliceblue", new ColorImpl(0xF0, 0xF8, 0xFF));
		namedColors.put("antiquewhite", new ColorImpl(0xFA, 0xEB, 0xD7));
		namedColors.put("aqua", new ColorImpl(0x00, 0xFF, 0xFF));
		namedColors.put("aquamarine", new ColorImpl(0x7F, 0xFF, 0xD4));
		namedColors.put("azure", new ColorImpl(0xF0, 0xFF, 0xFF));
		namedColors.put("beige", new ColorImpl(0xF5, 0xF5, 0xDC));
		namedColors.put("bisque", new ColorImpl(0xFF, 0xE4, 0xC4));
		namedColors.put("black", new ColorImpl(0x00, 0x00, 0x00));
		namedColors.put("blanchedalmond", new ColorImpl(0xFF, 0xEB, 0xCD));
		namedColors.put("blue", new ColorImpl(0x00, 0x00, 0xFF));
		namedColors.put("blueviolet", new ColorImpl(0x8A, 0x2B, 0xE2));
		namedColors.put("brown", new ColorImpl(0xA5, 0x2A, 0x2A));
		namedColors.put("burlywood", new ColorImpl(0xDE, 0xB8, 0x87));
		namedColors.put("cadetblue", new ColorImpl(0x5F, 0x9E, 0xA0));
		namedColors.put("chartreuse", new ColorImpl(0x7F, 0xFF, 0x00));
		namedColors.put("chocolate", new ColorImpl(0xD2, 0x69, 0x1E));
		namedColors.put("coral", new ColorImpl(0xFF, 0x7F, 0x50));
		namedColors.put("cornflowerblue", new ColorImpl(0x64, 0x95, 0xED));
		namedColors.put("cornsilk", new ColorImpl(0xFF, 0xF8, 0xDC));
		namedColors.put("crimson", new ColorImpl(0xDC, 0x14, 0x3C));
		namedColors.put("cyan", new ColorImpl(0x00, 0xFF, 0xFF));
		namedColors.put("darkblue", new ColorImpl(0x00, 0x00, 0x8B));
		namedColors.put("darkcyan", new ColorImpl(0x00, 0x8B, 0x8B));
		namedColors.put("darkgoldenrod", new ColorImpl(0xB8, 0x86, 0x0B));
		namedColors.put("darkgray", new ColorImpl(0xA9, 0xA9, 0xA9));
		namedColors.put("darkgrey", new ColorImpl(0xA9, 0xA9, 0xA9));
		namedColors.put("darkgreen", new ColorImpl(0x00, 0x64, 0x00));
		namedColors.put("darkkhaki", new ColorImpl(0xBD, 0xB7, 0x6B));
		namedColors.put("darkmagenta", new ColorImpl(0x8B, 0x00, 0x8B));
		namedColors.put("darkolivegreen", new ColorImpl(0x55, 0x6B, 0x2F));
		namedColors.put("darkorange", new ColorImpl(0xFF, 0x8C, 0x00));
		namedColors.put("darkorchid", new ColorImpl(0x99, 0x32, 0xCC));
		namedColors.put("darkred", new ColorImpl(0x8B, 0x00, 0x00));
		namedColors.put("darksalmon", new ColorImpl(0xE9, 0x96, 0x7A));
		namedColors.put("darkseagreen", new ColorImpl(0x8F, 0xBC, 0x8F));
		namedColors.put("darkslateblue", new ColorImpl(0x48, 0x3D, 0x8B));
		namedColors.put("darkslategray", new ColorImpl(0x2F, 0x4F, 0x4F));
		namedColors.put("darkslategrey", new ColorImpl(0x2F, 0x4F, 0x4F));
		namedColors.put("darkturquoise", new ColorImpl(0x00, 0xCE, 0xD1));
		namedColors.put("darkviolet", new ColorImpl(0x94, 0x00, 0xD3));
		namedColors.put("deeppink", new ColorImpl(0xFF, 0x14, 0x93));
		namedColors.put("deepskyblue", new ColorImpl(0x00, 0xBF, 0xFF));
		namedColors.put("dimgray", new ColorImpl(0x69, 0x69, 0x69));
		namedColors.put("dimgrey", new ColorImpl(0x69, 0x69, 0x69));
		namedColors.put("dodgerblue", new ColorImpl(0x1E, 0x90, 0xFF));
		namedColors.put("firebrick", new ColorImpl(0xB2, 0x22, 0x22));
		namedColors.put("floralwhite", new ColorImpl(0xFF, 0xFA, 0xF0));
		namedColors.put("forestgreen", new ColorImpl(0x22, 0x8B, 0x22));
		namedColors.put("fuchsia", new ColorImpl(0xFF, 0x00, 0xFF));
		namedColors.put("gainsboro", new ColorImpl(0xDC, 0xDC, 0xDC));
		namedColors.put("ghostwhite", new ColorImpl(0xF8, 0xF8, 0xFF));
		namedColors.put("gold", new ColorImpl(0xFF, 0xD7, 0x00));
		namedColors.put("goldenrod", new ColorImpl(0xDA, 0xA5, 0x20));
		namedColors.put("gray", new ColorImpl(0x80, 0x80, 0x80));
		namedColors.put("grey", new ColorImpl(0x80, 0x80, 0x80));
		namedColors.put("green", new ColorImpl(0x00, 0x80, 0x00));
		namedColors.put("greenyellow", new ColorImpl(0xAD, 0xFF, 0x2F));
		namedColors.put("honeydew", new ColorImpl(0xF0, 0xFF, 0xF0));
		namedColors.put("hotpink", new ColorImpl(0xFF, 0x69, 0xB4));
		namedColors.put("indianred", new ColorImpl(0xCD, 0x5C, 0x5C));
		namedColors.put("indigo", new ColorImpl(0x4B, 0x00, 0x82));
		namedColors.put("ivory", new ColorImpl(0xFF, 0xFF, 0xF0));
		namedColors.put("khaki", new ColorImpl(0xF0, 0xE6, 0x8C));
		namedColors.put("lavender", new ColorImpl(0xE6, 0xE6, 0xFA));
		namedColors.put("lavenderblush", new ColorImpl(0xFF, 0xF0, 0xF5));
		namedColors.put("lawngreen", new ColorImpl(0x7C, 0xFC, 0x00));
		namedColors.put("lemonchiffon", new ColorImpl(0xFF, 0xFA, 0xCD));
		namedColors.put("lightblue", new ColorImpl(0xAD, 0xD8, 0xE6));
		namedColors.put("lightcoral", new ColorImpl(0xF0, 0x80, 0x80));
		namedColors.put("lightcyan", new ColorImpl(0xE0, 0xFF, 0xFF));
		namedColors.put("lightgoldenrodyellow", new ColorImpl(0xFA, 0xFA, 0xD2));
		namedColors.put("lightgray", new ColorImpl(0xD3, 0xD3, 0xD3));
		namedColors.put("lightgrey", new ColorImpl(0xD3, 0xD3, 0xD3));
		namedColors.put("lightgreen", new ColorImpl(0x90, 0xEE, 0x90));
		namedColors.put("lightpink", new ColorImpl(0xFF, 0xB6, 0xC1));
		namedColors.put("lightsalmon", new ColorImpl(0xFF, 0xA0, 0x7A));
		namedColors.put("lightseagreen", new ColorImpl(0x20, 0xB2, 0xAA));
		namedColors.put("lightskyblue", new ColorImpl(0x87, 0xCE, 0xFA));
		namedColors.put("lightslategray", new ColorImpl(0x77, 0x88, 0x99));
		namedColors.put("lightslategrey", new ColorImpl(0x77, 0x88, 0x99));
		namedColors.put("lightsteelblue", new ColorImpl(0xB0, 0xC4, 0xDE));
		namedColors.put("lightyellow", new ColorImpl(0xFF, 0xFF, 0xE0));
		namedColors.put("lime", new ColorImpl(0x00, 0xFF, 0x00));
		namedColors.put("limegreen", new ColorImpl(0x32, 0xCD, 0x32));
		namedColors.put("linen", new ColorImpl(0xFA, 0xF0, 0xE6));
		namedColors.put("magenta", new ColorImpl(0xFF, 0x00, 0xFF));
		namedColors.put("maroon", new ColorImpl(0x80, 0x00, 0x00));
		namedColors.put("mediumaquamarine", new ColorImpl(0x66, 0xCD, 0xAA));
		namedColors.put("mediumblue", new ColorImpl(0x00, 0x00, 0xCD));
		namedColors.put("mediumorchid", new ColorImpl(0xBA, 0x55, 0xD3));
		namedColors.put("mediumpurple", new ColorImpl(0x93, 0x70, 0xDB));
		namedColors.put("mediumseagreen", new ColorImpl(0x3C, 0xB3, 0x71));
		namedColors.put("mediumslateblue", new ColorImpl(0x7B, 0x68, 0xEE));
		namedColors.put("mediumspringgreen", new ColorImpl(0x00, 0xFA, 0x9A));
		namedColors.put("mediumturquoise", new ColorImpl(0x48, 0xD1, 0xCC));
		namedColors.put("mediumvioletred", new ColorImpl(0xC7, 0x15, 0x85));
		namedColors.put("midnightblue", new ColorImpl(0x19, 0x19, 0x70));
		namedColors.put("mintcream", new ColorImpl(0xF5, 0xFF, 0xFA));
		namedColors.put("mistyrose", new ColorImpl(0xFF, 0xE4, 0xE1));
		namedColors.put("moccasin", new ColorImpl(0xFF, 0xE4, 0xB5));
		namedColors.put("navajowhite", new ColorImpl(0xFF, 0xDE, 0xAD));
		namedColors.put("navy", new ColorImpl(0x00, 0x00, 0x80));
		namedColors.put("oldlace", new ColorImpl(0xFD, 0xF5, 0xE6));
		namedColors.put("olive", new ColorImpl(0x80, 0x80, 0x00));
		namedColors.put("olivedrab", new ColorImpl(0x6B, 0x8E, 0x23));
		namedColors.put("orange", new ColorImpl(0xFF, 0xA5, 0x00));
		namedColors.put("orangered", new ColorImpl(0xFF, 0x45, 0x00));
		namedColors.put("orchid", new ColorImpl(0xDA, 0x70, 0xD6));
		namedColors.put("palegoldenrod", new ColorImpl(0xEE, 0xE8, 0xAA));
		namedColors.put("palegreen", new ColorImpl(0x98, 0xFB, 0x98));
		namedColors.put("paleturquoise", new ColorImpl(0xAF, 0xEE, 0xEE));
		namedColors.put("palevioletred", new ColorImpl(0xDB, 0x70, 0x93));
		namedColors.put("papayawhip", new ColorImpl(0xFF, 0xEF, 0xD5));
		namedColors.put("peachpuff", new ColorImpl(0xFF, 0xDA, 0xB9));
		namedColors.put("peru", new ColorImpl(0xCD, 0x85, 0x3F));
		namedColors.put("pink", new ColorImpl(0xFF, 0xC0, 0xCB));
		namedColors.put("plum", new ColorImpl(0xDD, 0xA0, 0xDD));
		namedColors.put("powderblue", new ColorImpl(0xB0, 0xE0, 0xE6));
		namedColors.put("purple", new ColorImpl(0x80, 0x00, 0x80));
		namedColors.put("red", new ColorImpl(0xFF, 0x00, 0x00));
		namedColors.put("rosybrown", new ColorImpl(0xBC, 0x8F, 0x8F));
		namedColors.put("royalblue", new ColorImpl(0x41, 0x69, 0xE1));
		namedColors.put("saddlebrown", new ColorImpl(0x8B, 0x45, 0x13));
		namedColors.put("salmon", new ColorImpl(0xFA, 0x80, 0x72));
		namedColors.put("sandybrown", new ColorImpl(0xF4, 0xA4, 0x60));
		namedColors.put("seagreen", new ColorImpl(0x2E, 0x8B, 0x57));
		namedColors.put("seashell", new ColorImpl(0xFF, 0xF5, 0xEE));
		namedColors.put("sienna", new ColorImpl(0xA0, 0x52, 0x2D));
		namedColors.put("silver", new ColorImpl(0xC0, 0xC0, 0xC0));
		namedColors.put("skyblue", new ColorImpl(0x87, 0xCE, 0xEB));
		namedColors.put("slateblue", new ColorImpl(0x6A, 0x5A, 0xCD));
		namedColors.put("slategray", new ColorImpl(0x70, 0x80, 0x90));
		namedColors.put("slategrey", new ColorImpl(0x70, 0x80, 0x90));
		namedColors.put("snow", new ColorImpl(0xFF, 0xFA, 0xFA));
		namedColors.put("springgreen", new ColorImpl(0x00, 0xFF, 0x7F));
		namedColors.put("steelblue", new ColorImpl(0x46, 0x82, 0xB4));
		namedColors.put("tan", new ColorImpl(0xD2, 0xB4, 0x8C));
		namedColors.put("teal", new ColorImpl(0x00, 0x80, 0x80));
		namedColors.put("thistle", new ColorImpl(0xD8, 0xBF, 0xD8));
		namedColors.put("tomato", new ColorImpl(0xFF, 0x63, 0x47));
		namedColors.put("turquoise", new ColorImpl(0x40, 0xE0, 0xD0));
		namedColors.put("violet", new ColorImpl(0xEE, 0x82, 0xEE));
		namedColors.put("wheat", new ColorImpl(0xF5, 0xDE, 0xB3));
		namedColors.put("white", new ColorImpl(0xFF, 0xFF, 0xFF));
		namedColors.put("whitesmoke", new ColorImpl(0xF5, 0xF5, 0xF5));
		namedColors.put("yellow", new ColorImpl(0xFF, 0xFF, 0x00));
		namedColors.put("yellowgreen", new ColorImpl(0x9A, 0xCD, 0x32));
	}

	public static ColorImpl valueOf(String color)
	{
		color = color.trim();
		String c = color.toLowerCase();

		try
		{
			if (c.startsWith("#"))
			{
				if (c.length() == 4)
				{
					int r = (hexToInt(c.charAt(1)) * 0xFF) / 0xF;
					int g = (hexToInt(c.charAt(2)) * 0xFF) / 0xF;
					int b = (hexToInt(c.charAt(3)) * 0xFF) / 0xF;
					return new ColorImpl(r, g, b);
				}
				else if (c.length() == 7)
				{
					int r = hexToInt(c.charAt(1)) * 0x10 + hexToInt(c.charAt(2));
					int g = hexToInt(c.charAt(3)) * 0x10 + hexToInt(c.charAt(4));
					int b = hexToInt(c.charAt(5)) * 0x10 + hexToInt(c.charAt(6));
					return new ColorImpl(r, g, b);
				}
			}
			else
			{
				ColorImpl named = namedColors.get(c);
				if (named != null)
					return named;
			}
		}
		catch (IllegalArgumentException e)
		{
		}

		throw new IllegalArgumentException("Not a valid color: `" + color + "'");
	}

	private static int hexToInt(char ch)
	{
		if (ch >= '0' && ch <= '9')
			return ch - '0';
		else if (ch >= 'a' && ch <= 'f')
			return ch - 'a' + 0x0A;
		else
			throw new IllegalArgumentException();
	}
}
