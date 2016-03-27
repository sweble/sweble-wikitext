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

package org.sweble.wikitext.example;

import java.math.BigInteger;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import javax.xml.datatype.XMLGregorianCalendar;

import org.sweble.wikitext.articlecruncher.Job;
import org.sweble.wikitext.dumpreader.export_0_10.CommentType;
import org.sweble.wikitext.dumpreader.export_0_10.ContributorType;
import org.sweble.wikitext.dumpreader.export_0_10.PageType;
import org.sweble.wikitext.dumpreader.export_0_10.RevisionType;
import org.sweble.wikitext.dumpreader.export_0_10.TextType;

public class RevisionJob
		extends
			Job
{
	// -- page info --

	private final BigInteger pageId;

	private final BigInteger pageNamespace;

	private final String pageTitle;

	private final String pageRedirect;

	// -- revision info --

	private final BigInteger id;

	private final boolean minor;

	private final Calendar timestamp;

	// -- text info --

	private final String textText;

	private final boolean isTextDeleted;

	// =========================================================================

	public RevisionJob(PageType page, RevisionType rev)
	{
		page.getDiscussionthreadinginfo();

		this.pageId = page.getId();

		this.pageNamespace = page.getNs();

		if (page.getRedirect() != null)
		{
			this.pageRedirect = page.getRedirect().getTitle();
		}
		else
		{
			this.pageRedirect = null;
		}

		page.getRestrictions();
		rev.getSha1();

		this.pageTitle = page.getTitle();

		if (rev.getComment() != null)
		{
			CommentType comment = rev.getComment();
			comment.getDeleted();
			comment.getValue();
		}

		if (rev.getContributor() != null)
		{
			ContributorType contributor = rev.getContributor();
			contributor.getDeleted();
			contributor.getId();
			contributor.getIp();
			contributor.getUsername();
		}

		this.id = rev.getId();

		this.minor = (rev.getMinor() != null);

		rev.getSha1();

		if (rev.getText() != null)
		{
			TextType text = rev.getText();
			text.getBytes();
			this.isTextDeleted = text.getDeleted() != null;
			text.getSpace();
			this.textText = text.getValue();
		}
		else
		{
			this.isTextDeleted = false;
			this.textText = null;
		}

		if (rev.getTimestamp() != null)
		{
			XMLGregorianCalendar ts = rev.getTimestamp();

			this.timestamp = new GregorianCalendar();
			this.timestamp.setTimeZone(ts.getTimeZone(0));
			this.timestamp.set(
					ts.getYear(),
					ts.getMonth(),
					ts.getDay(),
					ts.getHour(),
					ts.getMinute(),
					ts.getSecond());
		}
		else
		{
			this.timestamp = new GregorianCalendar();
			this.timestamp.setTimeZone(TimeZone.getTimeZone("UST"));
			this.timestamp.setTimeInMillis(0);
		}
	}

	// =========================================================================
	// page

	public BigInteger getPageId()
	{
		return pageId;
	}

	public BigInteger getPageNamespace()
	{
		return pageNamespace;
	}

	public String getPageTitle()
	{
		return pageTitle;
	}

	public String getPageRedirect()
	{
		return pageRedirect;
	}

	// =========================================================================
	// revision

	public BigInteger getId()
	{
		return id;
	}

	public boolean isMinor()
	{
		return minor;
	}

	public Calendar getTimestamp()
	{
		return timestamp;
	}

	// =========================================================================
	// text

	public String getTextText()
	{
		return textText;
	}

	public boolean isTextDeleted()
	{
		return isTextDeleted;
	}
}
