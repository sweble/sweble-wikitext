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
package org.sweble.wikitext.dumpreader.model;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.xml.datatype.XMLGregorianCalendar;

import org.joda.time.DateTime;

public class DumpConverter
{
	public Page convertPage(Object page) throws UnsupportedDumpFormat
	{
		if (page instanceof org.sweble.wikitext.dumpreader.export_0_5.PageType)
			return convertToPage_0_5((org.sweble.wikitext.dumpreader.export_0_5.PageType) page);

		else if (page instanceof org.sweble.wikitext.dumpreader.export_0_6.PageType)
			return convertToPage_0_6((org.sweble.wikitext.dumpreader.export_0_6.PageType) page);

		else if (page instanceof org.sweble.wikitext.dumpreader.export_0_7.PageType)
			return convertToPage_0_7((org.sweble.wikitext.dumpreader.export_0_7.PageType) page);

		else if (page instanceof org.sweble.wikitext.dumpreader.export_0_8.PageType)
			return convertToPage_0_8((org.sweble.wikitext.dumpreader.export_0_8.PageType) page);

		else if (page instanceof org.sweble.wikitext.dumpreader.export_0_9.PageType)
			return convertToPage_0_9((org.sweble.wikitext.dumpreader.export_0_9.PageType) page);

		else if (page instanceof org.sweble.wikitext.dumpreader.export_0_10.PageType)
			return convertToPage_0_10((org.sweble.wikitext.dumpreader.export_0_10.PageType) page);

		else
			throw new UnsupportedDumpFormat();
	}

	public Revision convertRevision(Object rev) throws UnsupportedDumpFormat
	{
		if (rev instanceof org.sweble.wikitext.dumpreader.export_0_5.RevisionType)
			return convertToRevision_0_5((org.sweble.wikitext.dumpreader.export_0_5.RevisionType) rev);

		else if (rev instanceof org.sweble.wikitext.dumpreader.export_0_6.RevisionType)
			return convertToRevision_0_6((org.sweble.wikitext.dumpreader.export_0_6.RevisionType) rev);

		else if (rev instanceof org.sweble.wikitext.dumpreader.export_0_7.RevisionType)
			return convertToRevision_0_7((org.sweble.wikitext.dumpreader.export_0_7.RevisionType) rev);

		else if (rev instanceof org.sweble.wikitext.dumpreader.export_0_8.RevisionType)
			return convertToRevision_0_8((org.sweble.wikitext.dumpreader.export_0_8.RevisionType) rev);

		else if (rev instanceof org.sweble.wikitext.dumpreader.export_0_9.RevisionType)
			return convertToRevision_0_9((org.sweble.wikitext.dumpreader.export_0_9.RevisionType) rev);

		else if (rev instanceof org.sweble.wikitext.dumpreader.export_0_10.RevisionType)
			return convertToRevision_0_10((org.sweble.wikitext.dumpreader.export_0_10.RevisionType) rev);

		else
			throw new UnsupportedDumpFormat();
	}

	// =========================================================================

	private Page convertToPage_0_5(
			org.sweble.wikitext.dumpreader.export_0_5.PageType page)
	{
		List<Revision> revisions = new ArrayList<Revision>();
		List<Object> items = page.getRevisionOrUploadOrLogitem();
		for (Object item : items)
		{
			if (item instanceof org.sweble.wikitext.dumpreader.export_0_5.RevisionType)
			{
				revisions.add(convertToRevision_0_5(item));
			}
			// We're ignoring:
			// - UploadItem
			// - LogItem
		}

		// We're ignoring:
		// - getDiscussionthreadinginfo()
		// - getRestrictions()

		return new Page(
				page.getId(),
				null,
				page.getTitle(),
				(page.getRedirect() != null) ? "" : null,
				revisions);
	}

	private Revision convertToRevision_0_5(Object item)
	{
		org.sweble.wikitext.dumpreader.export_0_5.RevisionType revision =
				(org.sweble.wikitext.dumpreader.export_0_5.RevisionType) item;

		org.sweble.wikitext.dumpreader.export_0_5.ContributorType contributor =
				revision.getContributor();

		org.sweble.wikitext.dumpreader.export_0_5.CommentType comment =
				revision.getComment();

		org.sweble.wikitext.dumpreader.export_0_5.TextType text =
				revision.getText();

		Contributor contributor_ = null;
		String contributorIp = null;
		if (contributor != null)
		{
			if (contributor.getIp() == null)
			{
				contributor_ = new Contributor(
						contributor.getId(),
						contributor.getDeleted() == org.sweble.wikitext.dumpreader.export_0_5.DeletedFlagType.DELETED,
						contributor.getUsername());
			}
			else
			{
				contributorIp = contributor.getIp();
			}
		}

		boolean commentDeleted = false;
		String commentText = null;
		if (comment != null)
		{
			commentDeleted =
					comment.getDeleted() == org.sweble.wikitext.dumpreader.export_0_5.DeletedFlagType.DELETED;
			commentText = comment.getValue();
		}

		boolean textDeleted = false;
		String textText = null;
		if (text != null)
		{
			// We ignore:
			// - getBytes
			// - getId
			// - getSpace

			textDeleted =
					text.getDeleted() == org.sweble.wikitext.dumpreader.export_0_5.DeletedFlagType.DELETED;
			textText = text.getValue();
		}

		DateTime revisionTimestampDt = null;
		XMLGregorianCalendar revisionTimestamp = revision.getTimestamp();
		if (revisionTimestamp != null)
			revisionTimestampDt = new DateTime(revisionTimestamp.toGregorianCalendar());

		return new Revision(
				revision.getId(),
				new BigInteger("-1"),
				revisionTimestampDt,
				revision.getMinor() != null,
				contributor_,
				contributorIp,
				commentText,
				commentDeleted,
				textText,
				null,
				textDeleted,
				null,
				null);
	}

	// =========================================================================

	private Page convertToPage_0_6(
			org.sweble.wikitext.dumpreader.export_0_6.PageType page)
	{
		List<Revision> revisions = new ArrayList<Revision>();
		List<Object> items = page.getRevisionOrUploadOrLogitem();
		for (Object item : items)
		{
			if (item instanceof org.sweble.wikitext.dumpreader.export_0_6.RevisionType)
			{
				revisions.add(convertToRevision_0_6(item));
			}
			// We're ignoring:
			// - UploadItem
			// - LogItem
		}

		// We're ignoring:
		// - getDiscussionthreadinginfo()
		// - getRestrictions()
		// - getSha1()

		String redirectTitle = null;
		if (page.getRedirect() != null)
			redirectTitle = page.getRedirect().getTitle();

		return new Page(
				page.getId(),
				page.getNs(),
				page.getTitle(),
				redirectTitle,
				revisions);
	}

	private Revision convertToRevision_0_6(Object item)
	{
		org.sweble.wikitext.dumpreader.export_0_6.RevisionType revision =
				(org.sweble.wikitext.dumpreader.export_0_6.RevisionType) item;

		org.sweble.wikitext.dumpreader.export_0_6.ContributorType contributor =
				revision.getContributor();

		org.sweble.wikitext.dumpreader.export_0_6.CommentType comment =
				revision.getComment();

		org.sweble.wikitext.dumpreader.export_0_6.TextType text =
				revision.getText();

		Contributor contributor_ = null;
		String contributorIp = null;
		if (contributor != null)
		{
			if (contributor.getIp() == null)
			{
				contributor_ = new Contributor(
						contributor.getId(),
						contributor.getDeleted() == org.sweble.wikitext.dumpreader.export_0_6.DeletedFlagType.DELETED,
						contributor.getUsername());
			}
			else
			{
				contributorIp = contributor.getIp();
			}
		}

		boolean commentDeleted = false;
		String commentText = null;
		if (comment != null)
		{
			commentDeleted =
					comment.getDeleted() == org.sweble.wikitext.dumpreader.export_0_6.DeletedFlagType.DELETED;
			commentText = comment.getValue();
		}

		boolean textDeleted = false;
		String textText = null;
		if (text != null)
		{
			// We ignore:
			// - getBytes
			// - getId
			// - getSpace

			textDeleted =
					text.getDeleted() == org.sweble.wikitext.dumpreader.export_0_6.DeletedFlagType.DELETED;
			textText = text.getValue();
		}

		DateTime revisionTimestampDt = null;
		XMLGregorianCalendar revisionTimestamp = revision.getTimestamp();
		if (revisionTimestamp != null)
			revisionTimestampDt = new DateTime(revisionTimestamp.toGregorianCalendar());

		return new Revision(
				revision.getId(),
				new BigInteger("-1"),
				revisionTimestampDt,
				revision.getMinor() != null,
				contributor_,
				contributorIp,
				commentText,
				commentDeleted,
				textText,
				revision.getSha1(),
				textDeleted,
				null,
				null);
	}

	// =========================================================================

	private Page convertToPage_0_7(
			org.sweble.wikitext.dumpreader.export_0_7.PageType page)
	{
		List<Revision> revisions = new ArrayList<Revision>();
		List<Object> items = page.getRevisionOrUpload();
		for (Object item : items)
		{
			if (item instanceof org.sweble.wikitext.dumpreader.export_0_7.RevisionType)
			{
				revisions.add(convertToRevision_0_7(item));
			}
			// We're ignoring:
			// - UploadItem
		}

		// We're ignoring:
		// - getDiscussionthreadinginfo()
		// - getRestrictions()

		String redirectTitle = null;
		if (page.getRedirect() != null)
			redirectTitle = page.getRedirect().getTitle();

		return new Page(
				page.getId(),
				page.getNs(),
				page.getTitle(),
				redirectTitle,
				revisions);
	}

	private Revision convertToRevision_0_7(Object item)
	{
		org.sweble.wikitext.dumpreader.export_0_7.RevisionType revision =
				(org.sweble.wikitext.dumpreader.export_0_7.RevisionType) item;

		org.sweble.wikitext.dumpreader.export_0_7.ContributorType contributor =
				revision.getContributor();

		org.sweble.wikitext.dumpreader.export_0_7.CommentType comment =
				revision.getComment();

		org.sweble.wikitext.dumpreader.export_0_7.TextType text =
				revision.getText();

		Contributor contributor_ = null;
		String contributorIp = null;
		if (contributor != null)
		{
			if (contributor.getIp() == null)
			{
				contributor_ = new Contributor(
						contributor.getId(),
						contributor.getDeleted() == org.sweble.wikitext.dumpreader.export_0_7.DeletedFlagType.DELETED,
						contributor.getUsername());
			}
			else
			{
				contributorIp = contributor.getIp();
			}
		}

		boolean commentDeleted = false;
		String commentText = null;
		if (comment != null)
		{
			commentDeleted =
					comment.getDeleted() == org.sweble.wikitext.dumpreader.export_0_7.DeletedFlagType.DELETED;
			commentText = comment.getValue();
		}

		boolean textDeleted = false;
		String textText = null;
		if (text != null)
		{
			// We ignore:
			// - getBytes
			// - getId
			// - getSpace

			textDeleted =
					text.getDeleted() == org.sweble.wikitext.dumpreader.export_0_7.DeletedFlagType.DELETED;
			textText = text.getValue();
		}

		DateTime revisionTimestampDt = null;
		XMLGregorianCalendar revisionTimestamp = revision.getTimestamp();
		if (revisionTimestamp != null)
			revisionTimestampDt = new DateTime(revisionTimestamp.toGregorianCalendar());

		return new Revision(
				revision.getId(),
				revision.getParentid(),
				revisionTimestampDt,
				revision.getMinor() != null,
				contributor_,
				contributorIp,
				commentText,
				commentDeleted,
				textText,
				revision.getSha1(),
				textDeleted,
				null,
				null);
	}

	// =========================================================================

	private Page convertToPage_0_8(
			org.sweble.wikitext.dumpreader.export_0_8.PageType page)
	{
		List<Revision> revisions = new ArrayList<Revision>();
		List<Object> items = page.getRevisionOrUpload();
		for (Object item : items)
		{
			if (item instanceof org.sweble.wikitext.dumpreader.export_0_8.RevisionType)
			{
				revisions.add(convertToRevision_0_8(item));
			}
			// We're ignoring:
			// - UploadItem
		}

		// We're ignoring:
		// - getDiscussionthreadinginfo()
		// - getRestrictions()

		String redirectTitle = null;
		if (page.getRedirect() != null)
			redirectTitle = page.getRedirect().getTitle();

		return new Page(
				page.getId(),
				page.getNs(),
				page.getTitle(),
				redirectTitle,
				revisions);
	}

	private Revision convertToRevision_0_8(Object item)
	{
		org.sweble.wikitext.dumpreader.export_0_8.RevisionType revision =
				(org.sweble.wikitext.dumpreader.export_0_8.RevisionType) item;

		org.sweble.wikitext.dumpreader.export_0_8.ContributorType contributor =
				revision.getContributor();

		org.sweble.wikitext.dumpreader.export_0_8.CommentType comment =
				revision.getComment();

		org.sweble.wikitext.dumpreader.export_0_8.TextType text =
				revision.getText();

		Contributor contributor_ = null;
		String contributorIp = null;
		if (contributor != null)
		{
			if (contributor.getIp() == null)
			{
				contributor_ = new Contributor(
						contributor.getId(),
						contributor.getDeleted() == org.sweble.wikitext.dumpreader.export_0_8.DeletedFlagType.DELETED,
						contributor.getUsername());
			}
			else
			{
				contributorIp = contributor.getIp();
			}
		}

		boolean commentDeleted = false;
		String commentText = null;
		if (comment != null)
		{
			commentDeleted =
					comment.getDeleted() == org.sweble.wikitext.dumpreader.export_0_8.DeletedFlagType.DELETED;
			commentText = comment.getValue();
		}

		boolean textDeleted = false;
		String textText = null;
		if (text != null)
		{
			// We ignore:
			// - getBytes
			// - getId
			// - getSpace

			textDeleted =
					text.getDeleted() == org.sweble.wikitext.dumpreader.export_0_8.DeletedFlagType.DELETED;
			textText = text.getValue();
		}

		DateTime revisionTimestampDt = null;
		XMLGregorianCalendar revisionTimestamp = revision.getTimestamp();
		if (revisionTimestamp != null)
			revisionTimestampDt = new DateTime(revisionTimestamp.toGregorianCalendar());

		/*
		if (!"text/x-wiki".equalsIgnoreCase(revision.getFormat()))
			throw new UnsupportedDumpFormat("revision/format = " + revision.getFormat());
		
		if (!"wikitext".equalsIgnoreCase(revision.getModel()))
			throw new UnsupportedDumpFormat("revision/model = " + revision.getModel());
		*/

		String format = revision.getFormat();
		String model = revision.getModel();

		return new Revision(
				revision.getId(),
				revision.getParentid(),
				revisionTimestampDt,
				revision.getMinor() != null,
				contributor_,
				contributorIp,
				commentText,
				commentDeleted,
				textText,
				revision.getSha1(),
				textDeleted,
				format,
				model);
	}

	private Page convertToPage_0_9(
			org.sweble.wikitext.dumpreader.export_0_9.PageType page)
	{
		List<Revision> revisions = new ArrayList<Revision>();
		List<Object> items = page.getRevisionOrUpload();
		for (Object item : items)
		{
			if (item instanceof org.sweble.wikitext.dumpreader.export_0_9.RevisionType)
			{
				revisions.add(convertToRevision_0_9(item));
			}
			// We're ignoring:
			// - UploadItem
		}

		// We're ignoring:
		// - getDiscussionthreadinginfo()
		// - getRestrictions()

		String redirectTitle = null;
		if (page.getRedirect() != null)
			redirectTitle = page.getRedirect().getTitle();

		return new Page(
				page.getId(),
				page.getNs(),
				page.getTitle(),
				redirectTitle,
				revisions);

	}

	private Revision convertToRevision_0_9(Object item)
	{
		org.sweble.wikitext.dumpreader.export_0_9.RevisionType revision =
				(org.sweble.wikitext.dumpreader.export_0_9.RevisionType) item;

		org.sweble.wikitext.dumpreader.export_0_9.ContributorType contributor =
				revision.getContributor();

		org.sweble.wikitext.dumpreader.export_0_9.CommentType comment =
				revision.getComment();

		org.sweble.wikitext.dumpreader.export_0_9.TextType text =
				revision.getText();

		Contributor contributor_ = null;
		String contributorIp = null;
		if (contributor != null)
		{
			if (contributor.getIp() == null)
			{
				contributor_ = new Contributor(
						contributor.getId(),
						contributor.getDeleted() == org.sweble.wikitext.dumpreader.export_0_9.DeletedFlagType.DELETED,
						contributor.getUsername());
			}
			else
			{
				contributorIp = contributor.getIp();
			}
		}

		boolean commentDeleted = false;
		String commentText = null;
		if (comment != null)
		{
			commentDeleted =
					comment.getDeleted() == org.sweble.wikitext.dumpreader.export_0_9.DeletedFlagType.DELETED;
			commentText = comment.getValue();
		}

		boolean textDeleted = false;
		String textText = null;
		if (text != null)
		{
			// We ignore:
			// - getBytes
			// - getId
			// - getSpace

			textDeleted =
					text.getDeleted() == org.sweble.wikitext.dumpreader.export_0_9.DeletedFlagType.DELETED;
			textText = text.getValue();
		}

		DateTime revisionTimestampDt = null;
		XMLGregorianCalendar revisionTimestamp = revision.getTimestamp();
		if (revisionTimestamp != null)
			revisionTimestampDt = new DateTime(revisionTimestamp.toGregorianCalendar());

		/*
		if (!"text/x-wiki".equalsIgnoreCase(revision.getFormat()))
			throw new UnsupportedDumpFormat("revision/format = " + revision.getFormat());

		if (!"wikitext".equalsIgnoreCase(revision.getModel()))
			throw new UnsupportedDumpFormat("revision/model = " + revision.getModel());
		*/

		String format = revision.getFormat();
		String model = revision.getModel();

		return new Revision(
				revision.getId(),
				revision.getParentid(),
				revisionTimestampDt,
				revision.getMinor() != null,
				contributor_,
				contributorIp,
				commentText,
				commentDeleted,
				textText,
				revision.getSha1(),
				textDeleted,
				format,
				model);

	}

	private Page convertToPage_0_10(
			org.sweble.wikitext.dumpreader.export_0_10.PageType page)
	{
		List<Revision> revisions = new ArrayList<Revision>();
		List<Object> items = page.getRevisionOrUpload();
		for (Object item : items)
		{
			if (item instanceof org.sweble.wikitext.dumpreader.export_0_10.RevisionType)
			{
				revisions.add(convertToRevision_0_10(item));
			}
			// We're ignoring:
			// - UploadItem
		}

		// We're ignoring:
		// - getDiscussionthreadinginfo()
		// - getRestrictions()

		String redirectTitle = null;
		if (page.getRedirect() != null)
			redirectTitle = page.getRedirect().getTitle();

		return new Page(
				page.getId(),
				page.getNs(),
				page.getTitle(),
				redirectTitle,
				revisions);

	}

	private Revision convertToRevision_0_10(Object item)
	{
		org.sweble.wikitext.dumpreader.export_0_10.RevisionType revision =
				(org.sweble.wikitext.dumpreader.export_0_10.RevisionType) item;

		org.sweble.wikitext.dumpreader.export_0_10.ContributorType contributor =
				revision.getContributor();

		org.sweble.wikitext.dumpreader.export_0_10.CommentType comment =
				revision.getComment();

		org.sweble.wikitext.dumpreader.export_0_10.TextType text =
				revision.getText();

		Contributor contributor_ = null;
		String contributorIp = null;
		if (contributor != null)
		{
			if (contributor.getIp() == null)
			{
				contributor_ = new Contributor(
						contributor.getId(),
						contributor.getDeleted() == org.sweble.wikitext.dumpreader.export_0_10.DeletedFlagType.DELETED,
						contributor.getUsername());
			}
			else
			{
				contributorIp = contributor.getIp();
			}
		}

		boolean commentDeleted = false;
		String commentText = null;
		if (comment != null)
		{
			commentDeleted =
					comment.getDeleted() == org.sweble.wikitext.dumpreader.export_0_10.DeletedFlagType.DELETED;
			commentText = comment.getValue();
		}

		boolean textDeleted = false;
		String textText = null;
		if (text != null)
		{
			// We ignore:
			// - getBytes
			// - getId
			// - getSpace

			textDeleted =
					text.getDeleted() == org.sweble.wikitext.dumpreader.export_0_10.DeletedFlagType.DELETED;
			textText = text.getValue();
		}

		DateTime revisionTimestampDt = null;
		XMLGregorianCalendar revisionTimestamp = revision.getTimestamp();
		if (revisionTimestamp != null)
			revisionTimestampDt = new DateTime(revisionTimestamp.toGregorianCalendar());

		/*
		if (!"text/x-wiki".equalsIgnoreCase(revision.getFormat()))
			throw new UnsupportedDumpFormat("revision/format = " + revision.getFormat());

		if (!"wikitext".equalsIgnoreCase(revision.getModel()))
			throw new UnsupportedDumpFormat("revision/model = " + revision.getModel());
		*/

		String format = revision.getFormat();
		String model = revision.getModel();

		return new Revision(
				revision.getId(),
				revision.getParentid(),
				revisionTimestampDt,
				revision.getMinor() != null,
				contributor_,
				contributorIp,
				commentText,
				commentDeleted,
				textText,
				revision.getSha1(),
				textDeleted,
				format,
				model);
	}
}
