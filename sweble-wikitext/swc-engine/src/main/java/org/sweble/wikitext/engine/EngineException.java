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

import org.sweble.wikitext.engine.nodes.EngLogProcessingPass;

public final class EngineException
		extends
			Exception
{
	private static final long serialVersionUID = 1L;

	private final PageTitle pageTitle;

	private EngLogProcessingPass log;

	// =========================================================================

	public EngineException(
			PageTitle pageTitle,
			String message,
			Throwable cause)
	{
		this(pageTitle, message, cause, null);
	}

	public EngineException(
			PageTitle pageTitle,
			String message,
			Throwable cause,
			EngLogProcessingPass log)
	{
		super(makeMessage(pageTitle, message), unwrap(cause));
		this.pageTitle = pageTitle;
		this.log = log;
	}

	// =========================================================================

	private static String makeMessage(PageTitle pageTitle, String message)
	{
		return String.format("%s (%s)", message, pageTitle.getDenormalizedFullTitle());
	}

	private static Throwable unwrap(Throwable t)
	{
		while (t instanceof ExpansionException)
			t = t.getCause();
		return t;
	}

	// =========================================================================

	public void attachLog(EngLogProcessingPass log)
	{
		if (this.log != null)
			throw new IllegalStateException("Log already attached!");

		this.log = log;
	}

	public PageTitle getPageTitle()
	{
		return pageTitle;
	}

	public EngLogProcessingPass getLog()
	{
		return log;
	}
}
