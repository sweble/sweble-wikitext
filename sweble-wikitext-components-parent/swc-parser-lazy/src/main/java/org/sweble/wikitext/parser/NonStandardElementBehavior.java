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
package org.sweble.wikitext.parser;

public enum NonStandardElementBehavior
{
	/** No behavior for this non-HTML/non-Wikitext element has been specified. */
	UNSPECIFIED,

	/** Treat like a self-closing tag (e.g. a &lt;br /> or &lt;img /> tag). */
	LIKE_BR,

	/** Treat like a block element (e.g. &lt;div>). */
	LIKE_DIV,

	/** Treat like a formatting element (e.g. &lt;b>). */
	LIKE_FORMATTING,

	/** Treat like an inline element (e.g. &lt;span>). */
	LIKE_ANY_OTHER,
}
