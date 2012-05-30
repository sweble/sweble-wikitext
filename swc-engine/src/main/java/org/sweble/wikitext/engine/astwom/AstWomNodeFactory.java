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

package org.sweble.wikitext.engine.astwom;

import org.sweble.wikitext.engine.astwom.adapters.BoldAdapter;
import org.sweble.wikitext.engine.astwom.adapters.CommentAdapter;
import org.sweble.wikitext.engine.astwom.adapters.HorizontalRuleAdapter;
import org.sweble.wikitext.engine.astwom.adapters.ParagraphAdapter;
import org.sweble.wikitext.engine.astwom.adapters.TextAdapter;
import org.sweble.wikitext.engine.wom.WomNode;
import org.sweble.wikitext.engine.wom.WomParagraph;
import org.sweble.wikitext.lazy.utils.XmlEntityResolver;

public class AstWomNodeFactory
		implements
			WomNodeFactory
{
	private final XmlEntityResolver xmlEntityResolver;
	
	public AstWomNodeFactory(XmlEntityResolver xmlEntityResolver)
	{
		super();
		this.xmlEntityResolver = xmlEntityResolver;
	}
	
	@Override
	public TextAdapter createText(String text)
	{
		return new TextAdapter(xmlEntityResolver, text);
	}
	
	@Override
	public BoldAdapter createBold(WomNode... children)
	{
		BoldAdapter bold = new BoldAdapter();
		for (WomNode child : children)
			bold.appendChild(child);
		return bold;
	}
	
	@Override
	public CommentAdapter createComment(String text)
	{
		return new CommentAdapter(text);
	}
	
	@Override
	public HorizontalRuleAdapter createHorizontalRule()
	{
		return new HorizontalRuleAdapter();
	}
	
	@Override
	public WomParagraph createParagraph(WomNode... children)
	{
		ParagraphAdapter p = new ParagraphAdapter();
		for (WomNode child : children)
			p.appendChild(child);
		return p;
	}
}
