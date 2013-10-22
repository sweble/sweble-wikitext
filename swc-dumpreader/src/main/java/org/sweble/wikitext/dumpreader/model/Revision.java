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
	
	private final DateTime timestamp;
	
	private final boolean minor;
	
	private final Contributor contributor;
	
	private final Comment comment;
	
	private final Text text;
	
	// =========================================================================
	
	public Revision(
			BigInteger id,
			DateTime timestamp,
			boolean minor,
			Contributor contributor,
			Comment comment,
			Text text)
	{
		this.id = id;
		this.timestamp = timestamp;
		this.minor = minor;
		this.contributor = contributor;
		this.comment = comment;
		this.text = text;
	}
	
	// =========================================================================
	
	public BigInteger getId()
	{
		return id;
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
	
	public Comment getComment()
	{
		return comment;
	}
	
	public Text getText()
	{
		return text;
	}
	
	// =========================================================================
	
	@Override
	public String toString()
	{
		return "Revision [id=" + id + ", timestamp=" + timestamp + ", minor=" + minor + ", contributor=" + contributor + ", comment=" + comment + ", text=" + text + "]";
	}
}
