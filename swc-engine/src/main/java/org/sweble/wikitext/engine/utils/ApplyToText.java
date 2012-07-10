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

package org.sweble.wikitext.engine.utils;

import de.fau.cs.osr.ptk.common.ast.AstNode;
import de.fau.cs.osr.ptk.common.ast.Text;

public class ApplyToText
{
	private Functor fn;
	
	// =========================================================================
	
	public ApplyToText(Functor fn)
	{
		this.fn = fn;
	}
	
	// =========================================================================
	
	public void go(AstNode arg0)
	{
		if (arg0.getNodeType() == AstNode.NT_TEXT)
		{
			apply((Text) arg0);
		}
		else
		{
			for (AstNode n : arg0)
			{
				if (n == null)
					continue;
				
				if (n.isNodeType(AstNode.NT_TEXT))
				{
					apply((Text) n);
				}
				else
				{
					go(n);
				}
			}
		}
	}
	
	private void apply(Text arg0)
	{
		arg0.setContent(fn.apply(arg0.getContent()));
	}
	
	// =========================================================================
	
	public interface Functor
	{
		public String apply(String text);
	}
}
