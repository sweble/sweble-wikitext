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
import org.sweble.wikitext.lazy.parser.XmlElement;
import org.sweble.wikitext.lazy.utils.XmlEntityResolver;

import de.fau.cs.osr.ptk.common.ast.AstNode;
import de.fau.cs.osr.ptk.common.ast.NodeList;

public interface WomNodeFactory
        extends
            XmlEntityResolver
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
	
	//public WomNode create(AstNode node);
	
	public WomNode create(NodeList container, AstNode node);
}
