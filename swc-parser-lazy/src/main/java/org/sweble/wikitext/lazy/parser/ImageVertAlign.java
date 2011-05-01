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

package org.sweble.wikitext.lazy.parser;

public enum ImageVertAlign
{
	BASELINE
	{
		@Override
		public String asKeyword()
		{
			return "baseline";
		}
	},
	SUB
	{
		@Override
		public String asKeyword()
		{
			return "sub";
		}
	},
	SUPER
	{
		@Override
		public String asKeyword()
		{
			return "super";
		}
	},
	TOP
	{
		@Override
		public String asKeyword()
		{
			return "top";
		}
	},
	TEXT_TOP
	{
		@Override
		public String asKeyword()
		{
			return "text-top";
		}
	},
	MIDDLE
	{
		@Override
		public String asKeyword()
		{
			return "middle";
		}
	},
	BOTTOM
	{
		@Override
		public String asKeyword()
		{
			return "bottom";
		}
	},
	TEXT_BOTTOM
	{
		@Override
		public String asKeyword()
		{
			return "text-bottom";
		}
	};
	
	public abstract String asKeyword();
	
	public static ImageVertAlign which(String s)
	{
		if (s == null)
			throw new NullPointerException();
		
		s = s.trim().toLowerCase();
		for (ImageVertAlign v : ImageVertAlign.values())
		{
			if (v.asKeyword().equals(s))
				return v;
		}
		
		return null;
	}
}
