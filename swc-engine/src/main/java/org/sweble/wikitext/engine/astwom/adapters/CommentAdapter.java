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
package org.sweble.wikitext.engine.astwom.adapters;

import org.sweble.wikitext.engine.astwom.Toolbox;
import org.sweble.wikitext.engine.astwom.WomBackbone;
import org.sweble.wikitext.engine.wom.WomComment;
import org.sweble.wikitext.engine.wom.WomNodeType;
import org.sweble.wikitext.lazy.preprocessor.XmlComment;

public class CommentAdapter
		extends
			WomBackbone
		implements
			WomComment
{
	private static final long serialVersionUID = 1L;
	
	// =========================================================================
	
	public CommentAdapter(String text)
	{
		super(new XmlComment());
		setValue(text);
	}
	
	public CommentAdapter(XmlComment astNode)
	{
		super(astNode);
	}
	
	// =========================================================================
	
	@Override
	public String getNodeName()
	{
		return "comment";
	}
	
	@Override
	public WomNodeType getNodeType()
	{
		return WomNodeType.COMMENT;
	}
	
	@Override
	public XmlComment getAstNode()
	{
		return (XmlComment) super.getAstNode();
	}
	
	@Override
	public String getValue()
	{
		return getAstNode().getContent();
	}
	
	@Override
	public String setValue(String text) throws IllegalArgumentException, NullPointerException
	{
		String old = getValue();
		Toolbox.checkValidCommentText(text);
		getAstNode().setContent(text);
		Toolbox.addRtData(getAstNode());
		return old;
	}
}
