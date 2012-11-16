package org.example;

import java.math.BigInteger;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import javax.xml.datatype.XMLGregorianCalendar;

import org.sweble.wikitext.articlecruncher.Job;
import org.sweble.wikitext.articlecruncher.JobTrace;
import org.sweble.wikitext.dumpreader.export_0_6.CommentType;
import org.sweble.wikitext.dumpreader.export_0_6.ContributorType;
import org.sweble.wikitext.dumpreader.export_0_6.PageType;
import org.sweble.wikitext.dumpreader.export_0_6.RevisionType;
import org.sweble.wikitext.dumpreader.export_0_6.TextType;

public class RevisionJob
		extends
			Job
{
	private final JobTrace trace = new JobTrace();
	
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
		page.getSha1();
		
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
	
	@Override
	public JobTrace getTrace()
	{
		return trace;
	}
	
	// =========================================================================
	
	// -- page --
	
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
	
	// -- revision --
	
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
	
	// -- text --
	
	public String getTextText()
	{
		return textText;
	}
	
	public boolean isTextDeleted()
	{
		return isTextDeleted;
	}
}
