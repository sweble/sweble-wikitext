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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.List;

import javax.xml.bind.JAXBException;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Test;
import org.sweble.wikitext.dumpreader.model.Contributor;
import org.sweble.wikitext.dumpreader.model.Page;
import org.sweble.wikitext.dumpreader.model.Revision;
import org.xml.sax.SAXException;

public class TestDumpReader_0_9_Fragments
{
	private static final ExportSchemaVersion EXPORT_VERSION = ExportSchemaVersion.V0_9;

	private static final String PAGE_INPUT = "/input-0.9-page.xml";

	private static final String REVISION_INPUT = "/input-0.9-revision.xml";

	// =========================================================================

	@Test
	public void testPageImportNoSchema() throws JAXBException, SAXException, IOException
	{
		DumpUnmarshaller dr = new DumpUnmarshaller(EXPORT_VERSION, false);
		compare(unmarshalPage(dr));
	}

	@Test
	public void testPageImportWithSchema() throws JAXBException, SAXException, IOException
	{
		DumpUnmarshaller dr = new DumpUnmarshaller(EXPORT_VERSION, true);
		compare(unmarshalPage(dr));
	}

	private Page unmarshalPage(DumpUnmarshaller dr) throws JAXBException, IOException
	{
		InputStream xmlInputStream = getClass().getResourceAsStream(PAGE_INPUT);
		Source source = new StreamSource(xmlInputStream);

		try
		{
			return dr.unmarshalToPage(source);
		}
		finally
		{
			xmlInputStream.close();
		}
	}

	private void compare(Page page)
	{
		assertEquals("TITLE", page.getTitle());
		assertEquals(BigInteger.valueOf(10), page.getId());
		assertTrue(page.isRedirect());
		assertEquals(BigInteger.valueOf(0), page.getNamespace());

		List<Revision> revisions = page.getRevisions();
		assertEquals(1, revisions.size());
		compare(revisions.get(0));
	}

	// =========================================================================

	@Test
	public void testRevisionImportNoSchema() throws JAXBException, SAXException, IOException
	{
		DumpUnmarshaller dr = new DumpUnmarshaller(EXPORT_VERSION, false);
		compare(unmarshalRevision(dr));
	}

	@Test
	public void testRevisionImportWithSchema() throws JAXBException, SAXException, IOException
	{
		DumpUnmarshaller dr = new DumpUnmarshaller(EXPORT_VERSION, true);
		compare(unmarshalRevision(dr));
	}

	private Revision unmarshalRevision(DumpUnmarshaller dr) throws JAXBException, IOException
	{
		InputStream xmlInputStream = getClass().getResourceAsStream(REVISION_INPUT);
		Source source = new StreamSource(xmlInputStream);

		try
		{
			return dr.unmarshalToRevision(source);
		}
		finally
		{
			xmlInputStream.close();
		}
	}

	private void compare(Revision rev)
	{
		assertEquals(BigInteger.valueOf(123456789), rev.getId());
		assertEquals(BigInteger.valueOf(987654321), rev.getParentId());
		assertTrue(rev.isMinor());

		assertFalse(rev.isCommentDeleted());
		assertEquals("COMMENT", rev.getCommentText());

		assertFalse(rev.isTextDeleted());
		assertEquals("TEXT", rev.getText());
		assertEquals("abcdfeghijklmnopqrstuvwxyz01234", rev.getTextSha1());

		assertEquals("text/x-wiki", rev.getFormat());

		assertEquals("wikitext", rev.getModel());

		assertNull(rev.getContributorIp());

		{
			Contributor contrib = rev.getContributor();
			assertFalse(contrib.isDeleted());
			assertEquals(BigInteger.valueOf(987654321), contrib.getId());
			assertEquals("USERNAME", contrib.getUsername());
		}

		{
			DateTime ts = rev.getTimestamp();
			assertEquals(2012, ts.getYear());
			assertEquals(5, ts.getMonthOfYear());
			assertEquals(21, ts.getDayOfMonth());
			assertEquals(11, ts.getHourOfDay());
			assertEquals(11, ts.getMinuteOfHour());
			assertEquals(11, ts.getSecondOfMinute());
			assertEquals(DateTimeZone.UTC, ts.getZone());
		}
	}
}
