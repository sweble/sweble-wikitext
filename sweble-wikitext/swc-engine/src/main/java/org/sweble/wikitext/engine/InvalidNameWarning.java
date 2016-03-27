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

public class InvalidNameWarning
		extends
			OffendingNodeWarning
{
	private static final long serialVersionUID = 1L;

	private static final String message =
			"The wikitext cannot be converted into a plain name";

	// =========================================================================

	public InvalidNameWarning(
			WarningSeverity severity,
			String origin,
			WtNode name)
	{
		super(name, severity, origin, message);
	}

	public InvalidNameWarning(
			WarningSeverity severity,
			Class<?> origin,
			WtNode name)
	{
		super(name, severity, origin, message);
	}
}
