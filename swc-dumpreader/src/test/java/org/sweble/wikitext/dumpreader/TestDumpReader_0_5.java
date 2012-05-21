package org.sweble.wikitext.dumpreader;

import static org.junit.Assert.*;

import java.io.File;
import java.math.BigInteger;
import java.net.URL;
import java.util.List;

import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventLocator;
import javax.xml.datatype.XMLGregorianCalendar;

import junit.framework.Assert;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.sweble.wikitext.dumpreader.export_0_5.CaseType;
import org.sweble.wikitext.dumpreader.export_0_5.CommentType;
import org.sweble.wikitext.dumpreader.export_0_5.ContributorType;
import org.sweble.wikitext.dumpreader.export_0_5.MediaWikiType;
import org.sweble.wikitext.dumpreader.export_0_5.NamespaceType;
import org.sweble.wikitext.dumpreader.export_0_5.PageType;
import org.sweble.wikitext.dumpreader.export_0_5.RevisionType;
import org.sweble.wikitext.dumpreader.export_0_5.SiteInfoType;
import org.sweble.wikitext.dumpreader.export_0_5.TextType;

public class TestDumpReader_0_5
{
	private Logger logger;
	
	@Before
	public void setUp()
	{
		logger = Logger.getLogger(getClass());
	}
	
	@Test
	public void testExport() throws Throwable
	{
		URL resource = getClass().getResource("/input-0.5.xml");
		
		final File file = new File(resource.getFile());
		
		DumpReader dr = new DumpReader(file, logger)
		{
			@Override
			protected void processPage(Object mediaWiki, Object page)
			{
				{
					MediaWikiType mw =
							(MediaWikiType) mediaWiki;
					
					assertEquals("0.5", mw.getVersion());
					
					{
						SiteInfoType siteinfo = mw.getSiteinfo();
						assertEquals("BASE", siteinfo.getBase());
						assertEquals(CaseType.FIRST_LETTER, siteinfo.getCase());
						assertEquals("GENERATOR", siteinfo.getGenerator());
						assertEquals("SITENAME", siteinfo.getSitename());
						
						{
							List<NamespaceType> namespaces = siteinfo.getNamespaces().getNamespace();
							assertEquals(3, namespaces.size());
							
							{
								NamespaceType ns = namespaces.get(0);
								assertEquals(BigInteger.valueOf(-1), ns.getKey());
								assertEquals("NEGATIVE", ns.getValue());
								assertEquals(CaseType.FIRST_LETTER, ns.getCase());
							}
							
							{
								NamespaceType ns = namespaces.get(1);
								assertEquals(BigInteger.valueOf(0), ns.getKey());
								assertEquals("", ns.getValue());
								assertEquals(CaseType.FIRST_LETTER, ns.getCase());
							}
							
							{
								NamespaceType ns = namespaces.get(2);
								assertEquals(BigInteger.valueOf(+1), ns.getKey());
								assertEquals("POSITIVE", ns.getValue());
								assertEquals(CaseType.FIRST_LETTER, ns.getCase());
							}
						}
					}
				}
				
				PageType p = (PageType) page;
				assertEquals("TITLE", p.getTitle());
				assertNull(p.getDiscussionthreadinginfo());
				assertEquals(BigInteger.valueOf(10), p.getId());
				assertNotNull(p.getRedirect());
				assertNull(p.getRestrictions());
				
				{
					List<Object> items = p.getRevisionOrUploadOrLogitem();
					assertEquals(1, items.size());
					
					{
						RevisionType item = (RevisionType) items.get(0);
						assertEquals(BigInteger.valueOf(123456789), item.getId());
						assertNotNull(item.getMinor());
						
						{
							CommentType comment = item.getComment();
							assertNull(comment.getDeleted());
							assertEquals("COMMENT", comment.getValue());
						}
						
						{
							ContributorType contrib = item.getContributor();
							assertNull(contrib.getDeleted());
							assertEquals(BigInteger.valueOf(987654321), contrib.getId());
							assertNull(contrib.getIp());
							assertEquals("USERNAME", contrib.getUsername());
						}
						
						{
							TextType text = item.getText();
							assertNull(text.getBytes());
							assertNull(text.getDeleted());
							assertNull(text.getId());
							assertEquals("TEXT", text.getValue());
						}
						
						{
							XMLGregorianCalendar ts = item.getTimestamp();
							assertEquals(2012, ts.getYear());
							assertEquals(5, ts.getMonth());
							assertEquals(21, ts.getDay());
							assertEquals(11, ts.getHour());
							assertEquals(11, ts.getMinute());
							assertEquals(11, ts.getSecond());
							assertEquals(0, ts.getTimezone());
						}
					}
				}
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
	}
}
