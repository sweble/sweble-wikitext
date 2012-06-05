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

package org.sweble.wikitext.engine.ext;

import org.sweble.wikitext.engine.OffendingNodeWarning;
import org.sweble.wikitext.lazy.parser.MagicWord;
import org.sweble.wikitext.lazy.parser.WarningSeverity;

public final class NotYetImplementedMagicWordWarning
		extends
			OffendingNodeWarning
{
	private static final long serialVersionUID = 1L;
	
	private final UnimplementedMagicWord umw;
	
	// =========================================================================
	
	public NotYetImplementedMagicWordWarning(
			UnimplementedMagicWord umw,
			MagicWord magicWord)
	{
		super(magicWord, WarningSeverity.FATAL, umw.getClass(), makeMessage(umw));
		this.umw = umw;
	}
	
	private static String makeMessage(UnimplementedMagicWord upfn)
	{
		return "The magic word `" + upfn.getName() + "' " +
				"is not yet implemented!";
	}
	
	// =========================================================================
	
	public UnimplementedMagicWord getUpfn()
	{
		return umw;
	}
	
	// =========================================================================
	
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((umw == null) ? 0 : umw.hashCode());
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
		NotYetImplementedMagicWordWarning other = (NotYetImplementedMagicWordWarning) obj;
		if (umw == null)
		{
			if (other.umw != null)
				return false;
		}
		else if (!umw.equals(other.umw))
			return false;
		return true;
	}
}
