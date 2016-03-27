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
import java.util.List;

public class Page
		implements
			Serializable
{
	private static final long serialVersionUID = 1L;

	private final BigInteger id;

	private final BigInteger namespace;

	private final String title;

	private final String redirectTitle;

	private final List<Revision> revisions;

	// =========================================================================

	public Page(
			BigInteger id,
			BigInteger namespace,
			String title,
			String redirectTitle,
			List<Revision> revisions)
	{
		super();
		this.id = id;
		this.namespace = namespace;
		this.title = title;
		this.redirectTitle = redirectTitle;
		this.revisions = revisions;
	}

	// =========================================================================

	public BigInteger getId()
	{
		return id;
	}

	public BigInteger getNamespace()
	{
		return namespace;
	}

	public String getTitle()
	{
		return title;
	}

	public boolean isRedirect()
	{
		return redirectTitle != null;
	}

	public String getRedirectTitle()
	{
		return redirectTitle;
	}

	public List<Revision> getRevisions()
	{
		return revisions;
	}

	// =========================================================================

	@Override
	public String toString()
	{
		return "Page [id=" + id + ", namespace=" + namespace + ", title=" + title + ", redirectTitle=" + redirectTitle + ", revisions=" + revisions + "]";
	}
}
