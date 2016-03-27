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

public class InvalidPagenameWarning
		extends
			OffendingNodeWarning
{
	private static final long serialVersionUID = 1L;

	private final String titleString;

	// =========================================================================

	public InvalidPagenameWarning(
			WarningSeverity severity,
			String origin,
			WtNode titleNode,
			String titleString)
	{
		super(titleNode, severity, origin, makeMessage(titleString));
		this.titleString = titleString;
	}

	public InvalidPagenameWarning(
			WarningSeverity severity,
			Class<?> origin,
			WtNode titleNode,
			String titleString)
	{
		super(titleNode, severity, origin, makeMessage(titleString));
		this.titleString = titleString;
	}

	private static String makeMessage(String titleString)
	{
		return "The given text `" + titleString + "' " +
				"does not constitute a valid page name";
	}

	public String getTitleString()
	{
		return titleString;
	}

	// =========================================================================

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((titleString == null) ? 0 : titleString.hashCode());
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
		InvalidPagenameWarning other = (InvalidPagenameWarning) obj;
		if (titleString == null)
		{
			if (other.titleString != null)
				return false;
		}
		else if (!titleString.equals(other.titleString))
			return false;
		return true;
	}
}
