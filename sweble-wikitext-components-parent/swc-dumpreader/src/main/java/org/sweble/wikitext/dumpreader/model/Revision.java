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

import java.io.Serializable;
import java.math.BigInteger;

import org.joda.time.DateTime;

public class Revision
		implements
			Serializable
{
	private static final long serialVersionUID = 1L;

	private final BigInteger id;

	private final BigInteger parentId;

	private final DateTime timestamp;

	private final boolean minor;

	private final Contributor contributor;

	private final String contributorIp;

	private final String commentText;

	private final boolean commentDeleted;

	private final String text;

	private final String textSha1;

	private final boolean textDeleted;

	private final String format;

	private final String model;

	// =========================================================================

	public Revision(
			BigInteger id,
			BigInteger parentId,
			DateTime timestamp,
			boolean minor,
			Contributor contributor,
			String contributorIp,
			String commentText,
			boolean commentDeleted,
			String text,
			String textSha1,
			boolean textDeleted,
			String format,
			String model)
	{
		this.id = id;
		this.parentId = parentId;
		this.timestamp = timestamp;
		this.minor = minor;
		this.contributor = contributor;
		this.contributorIp = contributorIp;
		this.commentText = commentText;
		this.commentDeleted = commentDeleted;
		this.text = text;
		this.textSha1 = textSha1;
		this.textDeleted = textDeleted;
		this.format = format;
		this.model = model;
	}

	// =========================================================================

	public BigInteger getId()
	{
		return id;
	}

	public BigInteger getParentId()
	{
		return parentId;
	}

	public DateTime getTimestamp()
	{
		return timestamp;
	}

	public boolean isMinor()
	{
		return minor;
	}

	public Contributor getContributor()
	{
		return contributor;
	}

	public String getContributorIp()
	{
		return contributorIp;
	}

	public String getCommentText()
	{
		return commentText;
	}

	public boolean isCommentDeleted()
	{
		return commentDeleted;
	}

	public String getText()
	{
		return text;
	}

	public String getTextSha1()
	{
		return textSha1;
	}

	public boolean isTextDeleted()
	{
		return textDeleted;
	}

	public String getFormat()
	{
		return format;
	}

	public String getModel()
	{
		return model;
	}

	// =========================================================================

	@Override
	public String toString()
	{
		return "Revision [id=" + id + ", parentId=" + parentId + ", timestamp=" + timestamp + ", minor=" + minor + ", contributor=" + contributor + ", contributorIp=" + contributorIp + ", commentText=" + commentText + ", commentDeleted=" + commentDeleted + ", text=" + text + ", textSha1=" + textSha1 + ", textDeleted=" + textDeleted + "]";
	}
}
