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

import org.sweble.wikitext.parser.WikitextWarning;
import org.sweble.wikitext.parser.nodes.WtNode;
import org.sweble.wikitext.parser.utils.WtRtDataPrinter;

import de.fau.cs.osr.ptk.common.ast.Span;

public abstract class OffendingNodeWarning
		extends
			WikitextWarning
{
	private static final long serialVersionUID = 1L;

	private final WtNode node;

	// =========================================================================

	public OffendingNodeWarning(
			WtNode node,
			WarningSeverity severity,
			String origin,
			String message)
	{
		super(makeSpan(node), severity, origin, message);
		this.node = node;
	}

	public OffendingNodeWarning(
			WtNode node,
			WarningSeverity severity,
			Class<?> origin,
			String message)
	{
		super(makeSpan(node), severity, origin, message);
		this.node = node;
	}

	private static Span makeSpan(WtNode node)
	{
		return new Span(
				node.getNativeLocation(),
				WtRtDataPrinter.print(node));
	}

	// =========================================================================

	public WtNode getNode()
	{
		return node;
	}

	// =========================================================================

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((node == null) ? 0 : node.hashCode());
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
		OffendingNodeWarning other = (OffendingNodeWarning) obj;
		if (node == null)
		{
			if (other.node != null)
				return false;
		}
		else if (!node.equals(other.node))
			return false;
		return true;
	}
}
