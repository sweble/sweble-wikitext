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

public enum ImageHorizAlign
{
	LEFT
	{
		@Override
		public String asKeyword()
		{
			return "left";
		}
	},
	RIGHT
	{
		@Override
		public String asKeyword()
		{
			return "right";
		}
	},
	CENTER
	{
		@Override
		public String asKeyword()
		{
			return "center";
		}
	},
	NONE
	{
		@Override
		public String asKeyword()
		{
			return "";
		}
	};
	
	public abstract String asKeyword();
	
	public static ImageHorizAlign which(String s)
	{
		if (s == null)
			throw new NullPointerException();
		
		s = s.trim().toLowerCase();
		for (ImageHorizAlign h : ImageHorizAlign.values())
		{
			if (s.equals(h.asKeyword()))
				return h;
		}
		
		return null;
	}
}
