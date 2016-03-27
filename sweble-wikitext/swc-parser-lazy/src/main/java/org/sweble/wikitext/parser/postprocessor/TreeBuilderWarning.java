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

package org.sweble.wikitext.parser.postprocessor;

import java.io.Serializable;

import org.sweble.wikitext.parser.nodes.WtNode;

import de.fau.cs.osr.ptk.common.Warning;
import de.fau.cs.osr.ptk.common.ast.Span;

public final class TreeBuilderWarning
		extends
			Warning
		implements
			Serializable
{
	private static final long serialVersionUID = -3629058682383966718L;

	protected final WtNode trigger;

	// =========================================================================

	public TreeBuilderWarning(WtNode trigger, String message)
	{
		super(new Span(trigger.getNativeLocation(), null),
				TreeBuilder.class,
				makeMessage(trigger, message));

		this.trigger = trigger;
	}

	private static String makeMessage(WtNode trigger, String message)
	{
		return String.format("%s @ %s", message, trigger.getNodeName());
	}

	// =========================================================================

	public WtNode getTrigger()
	{
		return trigger;
	}

	// =========================================================================

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((trigger == null) ? 0 : trigger.hashCode());
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
		TreeBuilderWarning other = (TreeBuilderWarning) obj;
		if (trigger == null)
		{
			if (other.trigger != null)
				return false;
		}
		else if (!trigger.equals(other.trigger))
			return false;
		return true;
	}
}
