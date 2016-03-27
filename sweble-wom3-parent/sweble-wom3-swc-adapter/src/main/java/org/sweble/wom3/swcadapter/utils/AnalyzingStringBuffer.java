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
package org.sweble.wom3.swcadapter.utils;

public class AnalyzingStringBuffer
{
	private final StringBuffer sb;

	private boolean onlyWhitespaceSoFar = true;

	private int newlinesSinceLastNonWhitespace = 0;

	// =========================================================================

	public AnalyzingStringBuffer()
	{
		this.sb = new StringBuffer();
	}

	public AnalyzingStringBuffer(int capacity)
	{
		this.sb = new StringBuffer(capacity);
	}

	public AnalyzingStringBuffer(String str)
	{
		this.sb = new StringBuffer(str);
	}

	public AnalyzingStringBuffer(CharSequence seq)
	{
		this.sb = new StringBuffer(seq);
	}

	// =========================================================================

	public int length()
	{
		return sb.length();
	}

	public char charAt(int index)
	{
		return sb.charAt(index);
	}

	public StringBuffer append(String str)
	{
		checkString(str);
		return sb.append(str);
	}

	public void discard(int wmPosBeforeChildren)
	{
		sb.delete(wmPosBeforeChildren, sb.length());
		recountNewlinesSinceLastNonWhitespace();
	}

	public void rollback(int count)
	{
		int length = sb.length();
		sb.delete(length - count, length);
		recountNewlinesSinceLastNonWhitespace();
	}

	@Override
	public String toString()
	{
		return sb.toString();
	}

	// =========================================================================

	public int newlineCount()
	{
		return newlinesSinceLastNonWhitespace;
	}

	public boolean hadNewline()
	{
		return newlineCount() > 0;
	}

	public boolean noContentYet()
	{
		return onlyWhitespaceSoFar;
	}

	public boolean hadSpaceAfterLastNewline()
	{
		int l = sb.length() - 1;
		if (l < 0)
			throw new RuntimeException("Should not happen!");
		switch (sb.charAt(l))
		{
			case ' ':
			case '\t':
				return true;
			case '\n':
				return false;
			default:
				throw new RuntimeException("Should not happen!");
		}
	}

	public int countNewlinesSince(int wmPosBeforeListItem)
	{
		int count = 0;
		int length = sb.length();
		for (int i = wmPosBeforeListItem; i < length; ++i)
		{
			if (sb.charAt(i) == '\n')
				count += 1;
		}
		return count;
	}

	private void recountNewlinesSinceLastNonWhitespace()
	{
		newlinesSinceLastNonWhitespace = 0;
		for (int i = sb.length() - 1; i >= 0; --i)
		{
			char ch = sb.charAt(i);
			switch (ch)
			{
				case '\n':
					++newlinesSinceLastNonWhitespace;
					break;

				case ' ':
				case '\t':
					break;

				default:
					return;
			}
		}
		onlyWhitespaceSoFar = true;
	}

	// =========================================================================

	protected void checkString(String str)
	{
		int len = str.length();
		for (int i = 0; i < len; ++i)
		{
			char ch = str.charAt(i);
			switch (ch)
			{
				case '\n':
					++newlinesSinceLastNonWhitespace;
					break;

				case ' ':
				case '\t':
					break;

				default:
					onlyWhitespaceSoFar = false;
					newlinesSinceLastNonWhitespace = 0;
					break;
			}
		}
	}
}
