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

package org.sweble.wikitext.engine.ext.core;

import org.sweble.wikitext.engine.config.ParserFunctionGroup;
import org.sweble.wikitext.engine.config.WikiConfig;

public class CorePfnBehaviorSwitches
		extends
			ParserFunctionGroup
{
	private static final long serialVersionUID = 1L;

	// =========================================================================

	protected CorePfnBehaviorSwitches(WikiConfig wikiConfig)
	{
		super("Core - Variables - Behavior Switches");
	}

	public static CorePfnBehaviorSwitches group(WikiConfig wikiConfig)
	{
		return new CorePfnBehaviorSwitches(wikiConfig);
	}

	// =========================================================================
	// ==
	// == WtTable of contents
	// == -----------------
	// == TODO: __NOTOC__
	// == TODO: __FORCETOC__
	// == TODO: __TOC__
	// ==
	// == Editing
	// == -------
	// == TODO: __NOEDITSECTION__
	// == TODO: __NEWSECTIONLINK__
	// == TODO: __NONEWSECTIONLINK__
	// ==
	// == Categories
	// == ----------
	// == TODO: __NOGALLERY__
	// == TODO: __HIDDENCAT__
	// ==
	// == Language conversion
	// == -------------------
	// == TODO: __NOCONTENTCONVERT__, __NOCC__
	// == TODO: __NOTITLECONVERT__, __NOTC__
	// ==
	// == Other
	// == -----
	// == TODO: __START__
	// == TODO: __END__
	// == TODO: __INDEX__
	// == TODO: __NOINDEX__
	// == TODO: __STATICREDIRECT__
	// ==
	// =========================================================================
}
