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
package org.sweble.wikitext.example;

import org.sweble.wikitext.parser.nodes.WtNode;

import de.fau.cs.osr.ptk.common.AstVisitor;
import de.fau.cs.osr.ptk.common.ast.AstLocation;

public final class StripAstVisitor
		extends
			AstVisitor<WtNode>
{
	private boolean stripAllAttributes;

	private boolean stripRtdAttributes;

	private boolean stripLocations;

	// =========================================================================

	public StripAstVisitor(
			boolean stripAllAttributes,
			boolean stripRtdAttributes,
			boolean stripLocations)
	{
		this.stripAllAttributes = stripAllAttributes;
		this.stripRtdAttributes = stripRtdAttributes;
		this.stripLocations = stripLocations;
	}

	// =========================================================================

	public void visit(WtNode n)
	{
		if (stripAllAttributes)
		{
			n.clearAttributes();
		}
		else if (stripRtdAttributes)
		{
			n.removeAttribute("RTD");
		}

		if (stripLocations)
			n.setNativeLocation((AstLocation) null);

		iterate(n);
	}
}
