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

import de.fau.cs.osr.ptk.common.ast.Span;

public class LooksLikeWarning
		extends
			WikitextWarning
{
	private static final long serialVersionUID = 1L;

	private final String looksLikeWhat;

	// =========================================================================

	public LooksLikeWarning(
			Span span,
			WarningSeverity severity,
			String origin,
			String looksLikeWhat,
			String message)
	{
		super(span, severity, origin, makeMessage(message, looksLikeWhat));
		this.looksLikeWhat = looksLikeWhat;
	}

	public LooksLikeWarning(
			Span span,
			WarningSeverity severity,
			Class<?> origin,
			String looksLikeWhat,
			String message)
	{
		super(span, severity, origin, makeMessage(message, looksLikeWhat));
		this.looksLikeWhat = looksLikeWhat;
	}

	private static String makeMessage(String message, String looksLikeWhat)
	{
		return "This looks like a " + looksLikeWhat + ", however " + message;
	}

	// =========================================================================

	public String getLooksLikeWhat()
	{
		return looksLikeWhat;
	}

	// =========================================================================

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((looksLikeWhat == null) ? 0 : looksLikeWhat.hashCode());
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
		LooksLikeWarning other = (LooksLikeWarning) obj;
		if (looksLikeWhat == null)
		{
			if (other.looksLikeWhat != null)
				return false;
		}
		else if (!looksLikeWhat.equals(other.looksLikeWhat))
			return false;
		return true;
	}
}
