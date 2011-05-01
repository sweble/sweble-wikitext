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

public enum ImageViewFormat
{
	UNRESTRAINED
	{
		@Override
		public String asKeyword()
		{
			return "";
		}
		
		@Override
		public int priority()
		{
			return 0;
		}
		
		@Override
		public boolean isFramed()
		{
			return false;
		}
	},
	FRAMELESS
	{
		@Override
		public String asKeyword()
		{
			return "frameless";
		}
		
		@Override
		public int priority()
		{
			return 1;
		}
		
		@Override
		public boolean isFramed()
		{
			return false;
		}
	},
	THUMBNAIL
	{
		@Override
		public String asKeyword()
		{
			return "thumb";
		}
		
		@Override
		public int priority()
		{
			return 2;
		}
		
		@Override
		public boolean isFramed()
		{
			return true;
		}
	},
	FRAME
	{
		@Override
		public String asKeyword()
		{
			return "frame";
		}
		
		@Override
		public int priority()
		{
			return 3;
		}
		
		@Override
		public boolean isFramed()
		{
			return true;
		}
	};
	
	private static final Object[] formatMap = new Object[] {
	        "frameless", FRAMELESS,
	        "thumb", THUMBNAIL,
	        "thumbnail", THUMBNAIL,
	        "frame", FRAME,
	};
	
	public abstract String asKeyword();
	
	public abstract int priority();
	
	public abstract boolean isFramed();
	
	public static ImageViewFormat which(String s)
	{
		if (s == null)
			throw new NullPointerException();
		
		s = s.trim().toLowerCase();
		for (int i = 0; i < formatMap.length; i += 2)
		{
			String f = (String) formatMap[i];
			if (f.equals(s))
				return (ImageViewFormat) formatMap[i + 1];
		}
		
		return null;
	}
	
	public ImageViewFormat combine(ImageViewFormat other)
	{
		return priority() > other.priority() ? this : other;
	}
}
