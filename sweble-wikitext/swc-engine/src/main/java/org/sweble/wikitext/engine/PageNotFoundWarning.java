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

package org.sweble.wikitext.engine;

import org.sweble.wikitext.parser.nodes.WtNode;

public class PageNotFoundWarning
		extends
			OffendingNodeWarning
{
	private static final long serialVersionUID = 1L;

	private final PageTitle title;

	// =========================================================================

	public PageNotFoundWarning(
			WarningSeverity severity,
			String origin,
			WtNode titleNode,
			PageTitle title)
	{
		super(titleNode, severity, origin, makeMessage(title));
		this.title = title;
	}

	public PageNotFoundWarning(
			WarningSeverity severity,
			Class<?> origin,
			WtNode titleNode,
			PageTitle title)
	{
		super(titleNode, severity, origin, makeMessage(title));
		this.title = title;
	}

	private static String makeMessage(PageTitle title)
	{
		return "The given text `" + title.getDenormalizedFullTitle() + "' " +
				"does not constitute a valid page name";
	}

	public PageTitle getTitle()
	{
		return title;
	}

	// =========================================================================

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		PageNotFoundWarning other = (PageNotFoundWarning) obj;
		if (title == null)
		{
			if (other.title != null)
				return false;
		}
		else if (!title.equals(other.title))
			return false;
		return true;
	}
}
