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

import org.sweble.wikitext.engine.wom.WomNode;
import org.sweble.wikitext.parser.nodes.WikitextNode;
import org.sweble.wikitext.parser.nodes.WtList;
import org.sweble.wikitext.parser.nodes.XmlElement;

/**
 * Interface for factories that create WOM nodes from AST nodes.
 */
public interface AstToWomNodeFactory
{
	public class UnsupportedXmlElement
			extends
				RuntimeException
	{
		private static final long serialVersionUID = 1L;
		
		private final XmlElement element;
		
		public UnsupportedXmlElement(XmlElement element)
		{
			this.element = element;
		}
		
		public XmlElement getElement()
		{
			return element;
		}
	}
	
	/**
	 * Create a WOM node wrapping the specified AST node.
	 * 
	 * @param container
	 *            The container in the AST that contains the node for which a
	 *            WOM node is to be created.
	 * @param node
	 *            The AST node for which a WOM node should be created.
	 * @return The created WOM node.
	 */
	public WomNode create(WtList container, WikitextNode node);
}
