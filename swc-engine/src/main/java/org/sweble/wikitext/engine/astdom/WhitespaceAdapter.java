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
package org.sweble.wikitext.engine.astdom;

import org.sweble.wikitext.engine.dom.DomNodeType;
import org.sweble.wikitext.engine.dom.DomText;
import org.sweble.wikitext.lazy.parser.Whitespace;

import de.fau.cs.osr.ptk.common.ast.AstNode;
import de.fau.cs.osr.ptk.common.ast.Text;

public class WhitespaceAdapter
        extends
            DomBackbone
        implements
            DomText
{
	private static final long serialVersionUID = 1L;
	
	private String text;
	
	// =========================================================================
	
	public WhitespaceAdapter(Whitespace astNode)
	{
		super(astNode);
		
		text = "";
		for (AstNode n : astNode.getContent())
		{
			Text t = (Text) n;
			text += t.getContent();
		}
	}
	
	// =========================================================================
	
	@Override
	public String getNodeName()
	{
		return "text";
	}
	
	@Override
	public DomNodeType getNodeType()
	{
		return DomNodeType.TEXT;
	}
	
	@Override
	public String getText()
	{
		return text;
	}
	
	@Override
	public String getValue()
	{
		return getText();
	}
}
