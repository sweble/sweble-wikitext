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

package org.sweble.wikitext.parser.parser;

public final class LinkTargetException
		extends
			Exception
{
	private static final long serialVersionUID = 1L;

	private final String title;

	private final Reason reason;

	private String offendingSubstring;

	// =========================================================================

	public LinkTargetException(Reason reason, String title)
	{
		super(makeMessage(reason, title, null));
		this.reason = reason;
		this.title = title;
	}

	public LinkTargetException(
			Reason reason,
			String title,
			String offendingSubstring)
	{
		super(makeMessage(reason, title, offendingSubstring));
		this.reason = reason;
		this.title = title;
		this.offendingSubstring = offendingSubstring;
	}

	// =========================================================================

	public String getTitle()
	{
		return title;
	}

	public Reason getReason()
	{
		return reason;
	}

	public String getOffendingSubstring()
	{
		return offendingSubstring;
	}

	private static String makeMessage(
			Reason reason,
			String title,
			String offendingSubstring)
	{
		return String.format(reason.getDescription(), title, offendingSubstring);
	}

	// =========================================================================

	public static enum Reason
	{
		EMPTY_TARGET("Target string must not be empty (or contain only characters that are reduced to whitespace)"),
		INVALID_ENTITIES("The title `%1$s' contains invalid entities: %2$s"),
		ONLY_NAMESPACE("A namespace alone is not a valid link target: %1$s"),
		NO_ARTICLE_TITLE("Title part of target string is empty: %1$s"),
		IW_IW_LINK("An interwiki name cannot be followed by another interwiki name `%2$s' in target `%1$s'"),
		TALK_NS_IW_LINK("The Talk namespace in a link target may not be followed by another namespace or interwiki name `%2$s' in target `%1$s'");

		private String description;

		private Reason(String description)
		{
			this.description = description;
		}

		public String getDescription()
		{
			return description;
		}
	}
}
