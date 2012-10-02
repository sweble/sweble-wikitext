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

import static org.sweble.wikitext.parser.utils.AstBuilder.astList;
import static org.sweble.wikitext.parser.utils.AstBuilder.astText;
import static org.sweble.wikitext.parser.utils.AstBuilder.astXmlAttrib;

import org.sweble.wikitext.engine.astwom.Toolbox;
import org.sweble.wikitext.parser.AstNodeTypes;
import org.sweble.wikitext.parser.nodes.WtNode;
import org.sweble.wikitext.parser.nodes.XmlAttribute;
import org.sweble.wikitext.parser.nodes.XmlElement;

public class SoftErrorNode
		extends
			XmlElement
{
	private static final long serialVersionUID = 1L;
	
	public SoftErrorNode(String message)
	{
		this(astText(message));
	}
	
	public SoftErrorNode(WtNode content)
	{
		// We don't store the content as AST node of the error node. This way
		// we prevent visitors from trying to do something to the error node.
		//
		// this(RtWikitextPrinter.print(content));
		// setAttribute("node", content);
		
		super("strong",
				false,
				astList(astXmlAttrib()
						.withName("class")
						.withValue("error")
						.build()),
				astList(content));
		
		Toolbox.addRtData(this);
	}
	
	public SoftErrorNode(WtNode content, Exception e)
	{
		this(content);
		setAttribute("exception", e);
	}
	
	public void addCssClass(String cssClass)
	{
		for (WtNode attrib : this.getXmlAttributes())
		{
			if (attrib == null || !(attrib instanceof XmlAttribute))
				continue;
			XmlAttribute a = (XmlAttribute) attrib;
			if (!a.getName().equals("class"))
				continue;
			a.setValue(astList(a.getValue(), astText(" " + cssClass)));
		}
	}
	
	@Override
	public int getNodeType()
	{
		return AstNodeTypes.NT_ERROR;
	}
}
