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

package org.sweble.wikitext.dumpreader;

import java.lang.reflect.Method;

import de.fau.cs.osr.utils.WrappedException;

public enum ExportSchemaVersion
{
	V0_5
	{
		@Override
		public String getSchema()
		{
			return "/export-0.5.xsd";
		}
		
		@Override
		public String getContextPath()
		{
			return "org.sweble.wikitext.dumpreader.export_0_5";
		}
		
		@Override
		public Class<?> getMediaWikiType()
		{
			return org.sweble.wikitext.dumpreader.export_0_5.MediaWikiType.class;
		}
	},
	
	// =========================================================================
	
	V0_6
	{
		@Override
		public String getSchema()
		{
			return "/export-0.6-fixed.xsd";
		}
		
		@Override
		public String getContextPath()
		{
			return "org.sweble.wikitext.dumpreader.export_0_6";
		}
		
		@Override
		public Class<?> getMediaWikiType()
		{
			return org.sweble.wikitext.dumpreader.export_0_6.MediaWikiType.class;
		}
	},
	
	// =========================================================================
	
	V0_7
	{
		@Override
		public String getSchema()
		{
			return "/export-0.7.xsd";
		}
		
		@Override
		public String getContextPath()
		{
			return "org.sweble.wikitext.dumpreader.export_0_7";
		}
		
		@Override
		public Class<?> getMediaWikiType()
		{
			return org.sweble.wikitext.dumpreader.export_0_7.MediaWikiType.class;
		}
	},
	
	// =========================================================================
	
	V0_8
	{
		@Override
		public String getSchema()
		{
			return "/export-0.8.xsd";
		}
		
		@Override
		public String getContextPath()
		{
			return "org.sweble.wikitext.dumpreader.export_0_8";
		}
		
		@Override
		public Class<?> getMediaWikiType()
		{
			return org.sweble.wikitext.dumpreader.export_0_8.MediaWikiType.class;
		}
	};
	
	// =========================================================================
	
	public abstract String getSchema();
	
	public abstract String getContextPath();
	
	public abstract Class<?> getMediaWikiType();
	
	// =========================================================================
	
	public void setPageListener(Object target, PageListener pageListener)
	{
		Class<?> mwType = getMediaWikiType();
		if (mwType.isInstance(target))
		{
			try
			{
				Method m = mwType.getMethod("setPageListener", PageListener.class);
				m.invoke(target, pageListener);
			}
			catch (Exception e)
			{
				throw new WrappedException(e);
			}
		}
	}
}
