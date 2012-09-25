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

package org.sweble.wikitext.parser.utils;

import java.io.Writer;

import org.sweble.wikitext.parser.parser.Whitespace;

import de.fau.cs.osr.ptk.common.ast.AstNode;

public class AstPrinter
        extends
            de.fau.cs.osr.ptk.common.AstPrinter
{
	public AstPrinter(Writer writer)
	{
		super(writer);
	}
	
	public void visit(Whitespace n)
	{
		if (n.hasAttributes())
		{
			visit((AstNode) n);
		}
		else if (n.getContent().isEmpty())
		{
			indent();
			out.println("Whitespace(NO EOL: [ ])");
		}
		else
		{
			String singleLine = printNodeContentToString(n.getContent());
			
			String eolInfo = n.getHasNewline() ? "EOL" : "NO EOL";
			
			if (singleLine != null)
			{
				indent();
				out.println("Whitespace(" + eolInfo + ": [ " + singleLine + " ])");
			}
			else
			{
				indent();
				out.println("Whitespace(" + eolInfo + ": [");
				
				incIndent();
				printNodeContent(n.getContent());
				decIndent();
				
				indent();
				out.println("])");
			}
		}
	}
}
