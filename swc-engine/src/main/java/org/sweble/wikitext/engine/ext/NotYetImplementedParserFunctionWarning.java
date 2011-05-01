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
import org.sweble.wikitext.lazy.parser.WarningSeverity;
import org.sweble.wikitext.lazy.preprocessor.Template;

public final class NotYetImplementedParserFunctionWarning
        extends
            OffendingNodeWarning
{
	private static final long serialVersionUID = 1L;
	
	private final UnimplementedParserFunction upfn;
	
	// =========================================================================
	
	public NotYetImplementedParserFunctionWarning(UnimplementedParserFunction upfn, Template template)
	{
		super(template, WarningSeverity.FATAL, upfn.getClass(), makeMessage(upfn));
		this.upfn = upfn;
	}
	
	private static String makeMessage(UnimplementedParserFunction upfn)
	{
		return "The parser function `" + upfn.getName() + "' " +
		        "is not yet implemented!";
	}
	
	// =========================================================================
	
	public UnimplementedParserFunction getUpfn()
	{
		return upfn;
	}
	
	// =========================================================================
	
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((upfn == null) ? 0 : upfn.hashCode());
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
		NotYetImplementedParserFunctionWarning other = (NotYetImplementedParserFunctionWarning) obj;
		if (upfn == null)
		{
			if (other.upfn != null)
				return false;
		}
		else if (!upfn.equals(other.upfn))
			return false;
		return true;
	}
}
