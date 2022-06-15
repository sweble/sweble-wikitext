package org.sweble.wikitext.dumpreader;

import de.fau.cs.osr.utils.StringTools;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sweble.wikitext.dumpreader.export_0_11.CaseType;
import org.sweble.wikitext.dumpreader.export_0_11.CommentType;
import org.sweble.wikitext.dumpreader.export_0_11.ContributorType;
import org.sweble.wikitext.dumpreader.export_0_11.MediaWikiType;
import org.sweble.wikitext.dumpreader.export_0_11.NamespaceType;
import org.sweble.wikitext.dumpreader.export_0_11.PageType;
import org.sweble.wikitext.dumpreader.export_0_11.RevisionType;
import org.sweble.wikitext.dumpreader.export_0_11.SiteInfoType;
import org.sweble.wikitext.dumpreader.export_0_11.TextType;

import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventLocator;
import javax.xml.datatype.XMLGregorianCalendar;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.URL;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class TestDumpReader_0_11 {
    private Logger logger;

    @Before
    public void setUp()
    {
        logger = LoggerFactory.getLogger(getClass());
    }

    @Test
    public void testExport() throws Throwable
    {
        URL resource = getClass().getResource("/input-0.11.xml");
        String path = StringTools.decodeUsingDefaultCharset(resource.getFile());
        final File file = new File(path);

        DumpReader dr = null;
        FileInputStream is = null;
        try
        {
            is = new FileInputStream(file);
            dr = new DumpReaderExtension(file, is, logger, file);
            dr.unmarshal();
        }
        finally
        {
            try
            {
                if (dr != null)
                    dr.close();
            }
            finally
            {
                if (is != null)
                    is.close();
            }
        }
    }

    private static final class DumpReaderExtension
            extends
            DumpReader
    {
        private final File file;

        private DumpReaderExtension(
                File dumpFile,
                InputStream is,
                Logger logger,
                File file)
                throws Exception
        {
            super(is, null, dumpFile.getAbsolutePath(), logger, true);
            this.file = file;
        }

        @Override
        protected void processPage(Object mediaWiki, Object page)
        {
            {
                MediaWikiType mw =
                        (MediaWikiType) mediaWiki;

                assertEquals("0.11", mw.getVersion());

                {
                    SiteInfoType siteinfo = mw.getSiteinfo();
                    assertEquals("BASE", siteinfo.getBase());
                    assertEquals(CaseType.FIRST_LETTER, siteinfo.getCase());
                    assertEquals("GENERATOR", siteinfo.getGenerator());
                    assertEquals("SITENAME", siteinfo.getSitename());
                    assertEquals("DBNAME", siteinfo.getDbname());

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
            assertEquals(BigInteger.valueOf(0), p.getNs());

            {
                List<Object> items = p.getRevisionOrUpload();
                assertEquals(1, items.size());

                {
                    RevisionType item = (RevisionType) items.get(0);

                    assertEquals(BigInteger.valueOf(123456789), item.getId());

                    assertEquals(BigInteger.valueOf(987654321), item.getParentid());

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

                    assertNotNull(item.getSha1());
                    assertEquals("abcdfeghijklmnopqrstuvwxyz01234", new String(item.getSha1().getBytes()));

                    assertNotNull(item.getModel());
                    assertEquals("wikitext", item.getModel());

                    assertNotNull(item.getFormat());
                    assertEquals("text/x-wiki", item.getFormat());

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
        protected boolean processEvent(
                ValidationEvent ve,
                ValidationEventLocator vel)
        {
            Assert.fail(String.format(
                    "%s:%d:%d: %s",
                    file.getAbsolutePath(),
                    vel.getLineNumber(),
                    vel.getColumnNumber(),
                    ve.getMessage()));

            return super.processEvent(ve, vel);
        }
    }
}
