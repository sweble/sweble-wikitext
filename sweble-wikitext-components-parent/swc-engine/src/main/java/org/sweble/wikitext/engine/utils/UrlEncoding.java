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

package org.sweble.wikitext.engine.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import de.fau.cs.osr.utils.WrappedException;

public enum UrlEncoding
{
	QUERY
	{
		@Override
		public String encode(String text)
		{
			try
			{
				return URLEncoder.encode(text, "UTF-8");
			}
			catch (UnsupportedEncodingException e)
			{
				throw new WrappedException(e);
			}
		}
	},

	WIKI
	{
		@Override
		public String encode(String text)
		{
			text = text.replace(' ', '_');
			try
			{
				text = URLEncoder.encode(text, "UTF-8");
			}
			catch (UnsupportedEncodingException e)
			{
				throw new WrappedException(e);
			}

			StringBuilder b = new StringBuilder();
			for (int i = 0; i < text.length(); ++i)
			{
				char ch = text.charAt(i);
				if (ch == '%')
				{
					char ch1 = text.charAt(++i);
					char ch2 = text.charAt(++i);

					int code = hexToDec(ch1) * 0x10 + hexToDec(ch2);
					switch (code)
					{
						case 0x21:
						case 0x24:
						case 0x28:
						case 0x29:
						case 0x2A:
						case 0x2C:
						case 0x2F:
						case 0x3A:
						case 0x3B:
						case 0x40:
							b.append((char) code);
							break;

						default:
							b.append(ch);
							b.append(ch1);
							b.append(ch2);
							break;
					}
				}
				else
					b.append(ch);
			}
			return b.toString();
		}

		private int hexToDec(char ch)
		{
			if (ch >= 'A' && ch <= 'F')
				return ch - 'A' + 0xA;
			else if (ch >= 'a' && ch <= 'f')
				return ch - 'a' + 0xA;
			else if (ch >= '0' && ch <= '9')
				return ch - '0' + 0x0;
			else
				throw new IllegalArgumentException("Not a valid hex digit!");
		}
	},

	PATH
	{
		@Override
		public String encode(String text)
		{
			try
			{
				return URLEncoder.encode(text, "UTF-8").replace("+", "%20");
			}
			catch (UnsupportedEncodingException e)
			{
				throw new WrappedException(e);
			}
		}
	};

	// =====================================================================

	public abstract String encode(String text);
}
