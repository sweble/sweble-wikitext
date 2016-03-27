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
		public String getFragmentsSchema()
		{
			return "/export-0.5-fragments.xsd";
		}

		@Override
		public String getContextPath()
		{
			return "org.sweble.wikitext.dumpreader.export_0_5";
		}

		@Override
		public String getMediaWikiNamespace()
		{
			return "http://www.mediawiki.org/xml/export-0.5/";
		}

		@Override
		public Class<?> getMediaWikiType()
		{
			return org.sweble.wikitext.dumpreader.export_0_5.MediaWikiType.class;
		}

		@Override
		public Class<?> getPageType()
		{
			return org.sweble.wikitext.dumpreader.export_0_5.PageType.class;
		}

		@Override
		public Class<?> getRevisionType()
		{
			return org.sweble.wikitext.dumpreader.export_0_5.RevisionType.class;
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
		public String getFragmentsSchema()
		{
			return "/export-0.6-fixed-fragments.xsd";
		}

		@Override
		public String getContextPath()
		{
			return "org.sweble.wikitext.dumpreader.export_0_6";
		}

		@Override
		public String getMediaWikiNamespace()
		{
			return "http://www.mediawiki.org/xml/export-0.6/";
		}

		@Override
		public Class<?> getMediaWikiType()
		{
			return org.sweble.wikitext.dumpreader.export_0_6.MediaWikiType.class;
		}

		@Override
		public Class<?> getPageType()
		{
			return org.sweble.wikitext.dumpreader.export_0_6.PageType.class;
		}

		@Override
		public Class<?> getRevisionType()
		{
			return org.sweble.wikitext.dumpreader.export_0_6.RevisionType.class;
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
		public String getFragmentsSchema()
		{
			return "/export-0.7-fragments.xsd";
		}

		@Override
		public String getContextPath()
		{
			return "org.sweble.wikitext.dumpreader.export_0_7";
		}

		@Override
		public String getMediaWikiNamespace()
		{
			return "http://www.mediawiki.org/xml/export-0.7/";
		}

		@Override
		public Class<?> getMediaWikiType()
		{
			return org.sweble.wikitext.dumpreader.export_0_7.MediaWikiType.class;
		}

		@Override
		public Class<?> getPageType()
		{
			return org.sweble.wikitext.dumpreader.export_0_7.PageType.class;
		}

		@Override
		public Class<?> getRevisionType()
		{
			return org.sweble.wikitext.dumpreader.export_0_7.RevisionType.class;
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
		public String getFragmentsSchema()
		{
			return "/export-0.8-fragments.xsd";
		}

		@Override
		public String getContextPath()
		{
			return "org.sweble.wikitext.dumpreader.export_0_8";
		}

		@Override
		public String getMediaWikiNamespace()
		{
			return "http://www.mediawiki.org/xml/export-0.8/";
		}

		@Override
		public Class<?> getMediaWikiType()
		{
			return org.sweble.wikitext.dumpreader.export_0_8.MediaWikiType.class;
		}

		@Override
		public Class<?> getPageType()
		{
			return org.sweble.wikitext.dumpreader.export_0_8.PageType.class;
		}

		@Override
		public Class<?> getRevisionType()
		{
			return org.sweble.wikitext.dumpreader.export_0_8.RevisionType.class;
		}
	},

	// =========================================================================

	V0_9
	{
		@Override
		public String getSchema()
		{
			return "/export-0.9.xsd";
		}

		@Override
		public String getFragmentsSchema()
		{
			return "/export-0.9-fragments.xsd";
		}

		@Override
		public String getContextPath()
		{
			return "org.sweble.wikitext.dumpreader.export_0_9";
		}

		@Override
		public String getMediaWikiNamespace()
		{
			return "http://www.mediawiki.org/xml/export-0.9/";
		}

		@Override
		public Class<?> getMediaWikiType()
		{
			return org.sweble.wikitext.dumpreader.export_0_9.MediaWikiType.class;
		}

		@Override
		public Class<?> getPageType()
		{
			return org.sweble.wikitext.dumpreader.export_0_9.PageType.class;
		}

		@Override
		public Class<?> getRevisionType()
		{
			return org.sweble.wikitext.dumpreader.export_0_9.RevisionType.class;
		}
	},

	// =========================================================================

	V0_10
	{
		@Override
		public String getSchema()
		{
			return "/export-0.10.xsd";
		}

		@Override
		public String getFragmentsSchema()
		{
			return "/export-0.10-fragments.xsd";
		}

		@Override
		public String getContextPath()
		{
			return "org.sweble.wikitext.dumpreader.export_0_10";
		}

		@Override
		public String getMediaWikiNamespace()
		{
			return "http://www.mediawiki.org/xml/export-0.10/";
		}

		@Override
		public Class<?> getMediaWikiType()
		{
			return org.sweble.wikitext.dumpreader.export_0_10.MediaWikiType.class;
		}

		@Override
		public Class<?> getPageType()
		{
			return org.sweble.wikitext.dumpreader.export_0_10.PageType.class;
		}

		@Override
		public Class<?> getRevisionType()
		{
			return org.sweble.wikitext.dumpreader.export_0_10.RevisionType.class;
		}
	};

	// =========================================================================

	public abstract String getSchema();

	public abstract String getFragmentsSchema();

	public abstract String getContextPath();

	public abstract String getMediaWikiNamespace();

	public abstract Class<?> getMediaWikiType();

	public abstract Class<?> getPageType();

	public abstract Class<?> getRevisionType();

	// =========================================================================

	public void setPageListener(Object target, DumpReaderListener listener)
	{
		try
		{
			Method m = null;
			if (getMediaWikiType().isInstance(target))
			{
				m = getMediaWikiType().getMethod(
						"setPageListener",
						DumpReaderListener.class);
			}
			else if (getPageType().isInstance(target))
			{
				m = getPageType().getMethod(
						"setRevisionListener",
						DumpReaderListener.class);
			}

			if (m != null)
				m.invoke(target, listener);
		}
		catch (Exception e)
		{
			throw new WrappedException(e);
		}
	}
}
