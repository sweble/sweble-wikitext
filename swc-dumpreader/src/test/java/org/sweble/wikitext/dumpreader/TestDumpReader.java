package org.sweble.wikitext.dumpreader;

import java.io.File;

import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventLocator;

import junit.framework.Assert;

import org.apache.log4j.Logger;
import org.junit.Test;

public class TestDumpReader
{
	@Test
	public void testExport05() throws Throwable
	{
		/*
		URL resource = getClass().getResource("/input-0.5.xml");
		
		File file = new File(resource.getFile());
		*/
		
		final File file = new File("/home/hannes/downloads/dump/enwiki-20120211-pages-meta-current1.xml-p000000010p000010000");
		
		Logger logger = Logger.getLogger(getClass());
		
		DumpReader dr = new DumpReader(file, logger)
		{
			@Override
			protected void processPage(Object mediaWiki, Object page)
			{
				org.sweble.wikitext.dumpreader.export_0_5.MediaWikiType mw =
						(org.sweble.wikitext.dumpreader.export_0_5.MediaWikiType) mediaWiki;
				
				org.sweble.wikitext.dumpreader.export_0_5.PageType p =
						(org.sweble.wikitext.dumpreader.export_0_5.PageType) page;
				
				Assert.assertNotNull(mw);
				Assert.assertNotNull(p);
			}
			
			@Override
			protected boolean handleEvent(
					ValidationEvent ve,
					ValidationEventLocator vel) throws Exception
			{
				Assert.fail(String.format(
						"%s:%d:%d: %s",
						file.getAbsolutePath(),
						vel.getLineNumber(),
						vel.getColumnNumber(),
						ve.getMessage()));
				
				return super.handleEvent(ve, vel);
			}
		};
		
		dr.unmarshal();
		System.out.println(dr.getParsedCount());
	}
	
	@Test
	public void testExport06() throws Throwable
	{
		/*
		URL resource = getClass().getResource("/input-0.6.xml");
		
		File file = new File(resource.getFile());
		*/
		
		final File file = new File("/home/hannes/downloads/dump/enwiki-20120307-pages-meta-current1.xml-p000000010p000010000");
		
		Logger logger = Logger.getLogger(getClass());
		
		DumpReader dr = new DumpReader(file, logger)
		{
			@Override
			protected void processPage(Object mediaWiki, Object page)
			{
				org.sweble.wikitext.dumpreader.export_0_6.MediaWikiType mw =
						(org.sweble.wikitext.dumpreader.export_0_6.MediaWikiType) mediaWiki;
				
				org.sweble.wikitext.dumpreader.export_0_6.PageType p =
						(org.sweble.wikitext.dumpreader.export_0_6.PageType) page;
				
				Assert.assertNotNull(mw);
				Assert.assertNotNull(p);
			}
			
			@Override
			protected boolean handleEvent(
					ValidationEvent ve,
					ValidationEventLocator vel) throws Exception
			{
				Assert.fail(String.format(
						"%s:%d:%d: %s",
						file.getAbsolutePath(),
						vel.getLineNumber(),
						vel.getColumnNumber(),
						ve.getMessage()));
				
				return super.handleEvent(ve, vel);
			}
		};
		
		dr.unmarshal();
		System.out.println(dr.getParsedCount());
	}
}
