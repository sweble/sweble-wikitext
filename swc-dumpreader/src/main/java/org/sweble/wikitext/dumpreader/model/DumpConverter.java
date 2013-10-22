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
		}
		return new Page(
				page.getId(),
				null,
				page.getTitle(),
				page.getRedirect() != null,
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
		if (contributor != null)
		{
			contributor_ = new Contributor(
					contributor.getId(),
					contributor.getIp(),
					contributor.getDeleted() == org.sweble.wikitext.dumpreader.export_0_5.DeletedFlagType.DELETED,
					contributor.getUsername());
		}
		
		Comment comment_ = null;
		if (comment != null)
		{
			comment_ = new Comment(
					comment.getDeleted() == org.sweble.wikitext.dumpreader.export_0_5.DeletedFlagType.DELETED,
					comment.getValue());
		}
		
		Text text_ = null;
		if (text != null)
		{
			BigInteger textIdBi = null;
			String textId = text.getId();
			if (textId != null)
				textIdBi = new BigInteger(textId);
			
			text_ = new Text(
					textIdBi,
					text.getBytes(),
					text.getDeleted() == org.sweble.wikitext.dumpreader.export_0_5.DeletedFlagType.DELETED,
					text.getValue());
		}
		
		DateTime revisionTimestampDt = null;
		XMLGregorianCalendar revisionTimestamp = revision.getTimestamp();
		if (revisionTimestamp != null)
			revisionTimestampDt = new DateTime(revisionTimestamp.toGregorianCalendar());
		
		return new Revision(
				revision.getId(),
				revisionTimestampDt,
				revision.getMinor() != null,
				contributor_,
				comment_,
				text_);
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
		}
		return new Page(
				page.getId(),
				page.getNs(),
				page.getTitle(),
				page.getRedirect() != null,
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
		if (contributor != null)
		{
			contributor_ = new Contributor(
					contributor.getId(),
					contributor.getIp(),
					contributor.getDeleted() == org.sweble.wikitext.dumpreader.export_0_6.DeletedFlagType.DELETED,
					contributor.getUsername());
		}
		
		Comment comment_ = null;
		if (comment != null)
		{
			comment_ = new Comment(
					comment.getDeleted() == org.sweble.wikitext.dumpreader.export_0_6.DeletedFlagType.DELETED,
					comment.getValue());
		}
		
		Text text_ = null;
		if (text != null)
		{
			BigInteger textIdBi = null;
			String textId = text.getId();
			if (textId != null)
				textIdBi = new BigInteger(textId);
			
			text_ = new Text(
					textIdBi,
					text.getBytes(),
					text.getDeleted() == org.sweble.wikitext.dumpreader.export_0_6.DeletedFlagType.DELETED,
					text.getValue());
		}
		
		DateTime revisionTimestampDt = null;
		XMLGregorianCalendar revisionTimestamp = revision.getTimestamp();
		if (revisionTimestamp != null)
			revisionTimestampDt = new DateTime(revisionTimestamp.toGregorianCalendar());
		
		return new Revision(
				revision.getId(),
				revisionTimestampDt,
				revision.getMinor() != null,
				contributor_,
				comment_,
				text_);
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
		}
		return new Page(
				page.getId(),
				page.getNs(),
				page.getTitle(),
				page.getRedirect() != null,
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
		if (contributor != null)
		{
			contributor_ = new Contributor(
					contributor.getId(),
					contributor.getIp(),
					contributor.getDeleted() == org.sweble.wikitext.dumpreader.export_0_7.DeletedFlagType.DELETED,
					contributor.getUsername());
		}
		
		Comment comment_ = null;
		if (comment != null)
		{
			comment_ = new Comment(
					comment.getDeleted() == org.sweble.wikitext.dumpreader.export_0_7.DeletedFlagType.DELETED,
					comment.getValue());
		}
		
		Text text_ = null;
		if (text != null)
		{
			BigInteger textIdBi = null;
			String textId = text.getId();
			if (textId != null)
				textIdBi = new BigInteger(textId);
			
			text_ = new Text(
					textIdBi,
					text.getBytes(),
					text.getDeleted() == org.sweble.wikitext.dumpreader.export_0_7.DeletedFlagType.DELETED,
					text.getValue());
		}
		
		DateTime revisionTimestampDt = null;
		XMLGregorianCalendar revisionTimestamp = revision.getTimestamp();
		if (revisionTimestamp != null)
			revisionTimestampDt = new DateTime(revisionTimestamp.toGregorianCalendar());
		
		return new Revision(
				revision.getId(),
				revisionTimestampDt,
				revision.getMinor() != null,
				contributor_,
				comment_,
				text_);
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
		}
		return new Page(
				page.getId(),
				page.getNs(),
				page.getTitle(),
				page.getRedirect() != null,
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
		if (contributor != null)
		{
			contributor_ = new Contributor(
					contributor.getId(),
					contributor.getIp(),
					contributor.getDeleted() == org.sweble.wikitext.dumpreader.export_0_8.DeletedFlagType.DELETED,
					contributor.getUsername());
		}
		
		Comment comment_ = null;
		if (comment != null)
		{
			comment_ = new Comment(
					comment.getDeleted() == org.sweble.wikitext.dumpreader.export_0_8.DeletedFlagType.DELETED,
					comment.getValue());
		}
		
		Text text_ = null;
		if (text != null)
		{
			BigInteger textIdBi = null;
			String textId = text.getId();
			if (textId != null)
				textIdBi = new BigInteger(textId);
			
			text_ = new Text(
					textIdBi,
					text.getBytes(),
					text.getDeleted() == org.sweble.wikitext.dumpreader.export_0_8.DeletedFlagType.DELETED,
					text.getValue());
		}
		
		DateTime revisionTimestampDt = null;
		XMLGregorianCalendar revisionTimestamp = revision.getTimestamp();
		if (revisionTimestamp != null)
			revisionTimestampDt = new DateTime(revisionTimestamp.toGregorianCalendar());
		
		return new Revision(
				revision.getId(),
				revisionTimestampDt,
				revision.getMinor() != null,
				contributor_,
				comment_,
				text_);
	}
}
