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
