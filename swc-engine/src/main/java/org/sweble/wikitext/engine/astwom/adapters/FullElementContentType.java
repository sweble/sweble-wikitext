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

import java.util.Iterator;

import org.sweble.wikitext.engine.astwom.AstToWomNodeFactory;
import org.sweble.wikitext.engine.astwom.ChildManagerBase;
import org.sweble.wikitext.engine.astwom.WomBackbone;
import org.sweble.wikitext.engine.wom.WomNode;
import org.sweble.wikitext.parser.AstNodeTypes;
import org.sweble.wikitext.parser.nodes.WtNode;
import org.sweble.wikitext.parser.nodes.WtNodeList;

public enum FullElementContentType
{
	/**
	 * MIXED: <any>*
	 */
	MIXED_ANY
	{
		@Override
		public void parseContent(
				AstToWomNodeFactory factory,
				ChildManagerBase childManager,
				WomBackbone parent,
				WtNodeList container,
				WtNode from,
				WtNode to)
		{
			throw new UnsupportedOperationException("Not yet supported");
		}
	},
	
	/**
	 * -
	 */
	SHOULD_BE_EMPTY
	{
		@Override
		public void parseContent(
				AstToWomNodeFactory factory,
				ChildManagerBase childManager,
				WomBackbone parent,
				WtNodeList container,
				WtNode from,
				WtNode to)
		{
			MIXED_INLINE.parseContent(factory, childManager, parent, container, from, to);
		}
	},
	
	/**
	 * MIXED: [Preprocessor elements]*
	 */
	MIXED_PREPROCESSOR
	{
		@Override
		public void parseContent(
				AstToWomNodeFactory factory,
				ChildManagerBase childManager,
				WomBackbone parent,
				WtNodeList container,
				WtNode from,
				WtNode to)
		{
			MIXED_INLINE.parseContent(factory, childManager, parent, container, from, to);
		}
	},
	
	/**
	 * [Block elements]*
	 */
	BLOCK_ELEMENTS
	{
		@Override
		public void parseContent(
				AstToWomNodeFactory factory,
				ChildManagerBase childManager,
				WomBackbone parent,
				WtNodeList container,
				WtNode from,
				WtNode to)
		{
			/*
			 * It doesn't make sense to enforce strict block semantics since 
			 * Wikitext doesn't require them ..
			 */
			MIXED_INLINE.parseContent(factory, childManager, parent, container, from, to);
			
			/*
			switch (node.getNodeType())
			{
				case WtNode.NT_IGNORED:
				case WtNode.NT_NEWLINE:
				{
					break;
				}
				case WtNode.NT_TEXT:
				{
					if (!StringUtils.isWhitespace(((WtText) node).getContent()))
						throw new AssertionError("Non-whitespace text in non-inline scope");
					
					break;
				}
				case WtNode.NT_XML_CHAR_REF:
				case WtNode.NT_XML_ENTITY_REF:
				{
					throw new AssertionError("Non-whitespace text in non-inline scope");
				}
				default:
				{
					WomNode child = factory.create(container, node);
					if (child != null)
						childManager.appendChild(child, parent, null);
					break;
				}
			}
			*/
		}
	},
	
	/**
	 * MIXED: [Inline elements]*
	 */
	MIXED_INLINE
	{
		@Override
		public void parseContent(
				AstToWomNodeFactory factory,
				ChildManagerBase childManager,
				WomBackbone parent,
				WtNodeList container,
				WtNode from,
				WtNode to)
		{
			WtNode n = null;
			Iterator<WtNode> i = container.iterator();
			
			// Find from node
			if (from != null)
			{
				while (i.hasNext())
				{
					n = i.next();
					if (n == from)
						break;
				}
			}
			else
			{
				if (i.hasNext())
					n = i.next();
			}
			
			// Nothing to process
			if (n == null)
				return;
			
			loop: while (true)
			{
				if (n == to)
					break;
				
				switch (n.getNodeType())
				{
					case WtNode.NT_IGNORED:
						break;
					
					case WtNode.NT_TEXT:
					case WtNode.NT_NEWLINE:
					case WtNode.NT_XML_CHAR_REF:
					case WtNode.NT_XML_ENTITY_REF:
					{
						n = buildInlineText(factory, childManager, parent, container, n, i, to);
						if (n != null)
							continue loop;
						
						break;
					}
					default:
					{
						WomNode child = factory.create(container, n);
						if (child != null)
							childManager.appendChild(child, parent, null);
						break;
					}
				}
				
				if (!i.hasNext())
					break loop;
				n = i.next();
			}
		}
	},
	
	/**
	 * For &lt;del> and &lt;ins>
	 */
	BLOCK_OR_MIXED_INLINE
	{
		@Override
		public void parseContent(
				AstToWomNodeFactory factory,
				ChildManagerBase childManager,
				WomBackbone parent,
				WtNodeList container,
				WtNode from,
				WtNode to)
		{
			MIXED_INLINE.parseContent(factory, childManager, parent, container, from, to);
		}
	},
	
	/**
	 * MIXED: [Flow elements]*
	 */
	MIXED_FLOW
	{
		@Override
		public void parseContent(
				AstToWomNodeFactory factory,
				ChildManagerBase childManager,
				WomBackbone parent,
				WtNodeList container,
				WtNode from,
				WtNode to)
		{
			MIXED_INLINE.parseContent(factory, childManager, parent, container, from, to);
		}
	},
	
	/**
	 * MIXED: ([Inline elements] \ [Inline link elements])*
	 */
	MIXED_TITLE
	{
		@Override
		public void parseContent(
				AstToWomNodeFactory factory,
				ChildManagerBase childManager,
				WomBackbone parent,
				WtNodeList container,
				WtNode from,
				WtNode to)
		{
			MIXED_INLINE.parseContent(factory, childManager, parent, container, from, to);
		}
	};
	
	// =========================================================================
	
	/**
	 * Takes another node from the AST and maps it into the WOM
	 * 
	 * @param factory
	 *            A AstToWomNodeFactory used to create the corresponding WOM
	 *            nodes.
	 * @param childManager
	 *            The child manager of the WOM element (the parent element) that
	 *            corresponds to the AST container.
	 * @param parent
	 *            The WOM parent element.
	 * @param container
	 *            The AST container that contains the node we are about to
	 *            process.
	 * @param from
	 *            The first AST element from the container with which processing
	 *            should start.
	 * @param to
	 *            The AST element from the container at which processing should
	 *            stop. The element "to" itself is not processed.
	 */
	public abstract void parseContent(
			AstToWomNodeFactory factory,
			ChildManagerBase childManager,
			WomBackbone parent,
			WtNodeList container,
			WtNode from,
			WtNode to);
	
	// =========================================================================
	
	/**
	 * Takes another node from the AST and maps it into the WOM
	 * 
	 * @param factory
	 *            A AstToWomNodeFactory used to create the corresponding WOM
	 *            nodes.
	 * @param childManager
	 *            The child manager of the WOM element (the parent element) that
	 *            corresponds to the AST container.
	 * @param parent
	 *            The WOM parent element.
	 * @param container
	 *            The AST container that contains the node we are about to
	 *            process.
	 * @param firstNode
	 *            The first text node (not necessarily a WtText object) in the
	 *            sequence of text nodes.
	 * @param iter
	 *            An iterator pointing to the first text node.
	 * @param to
	 *            The AST element from the container at which processing should
	 *            stop. The element "to" itself is not processed.
	 * @return The node in the container that came after the last text node.
	 */
	private static WtNode buildInlineText(
			AstToWomNodeFactory factory,
			ChildManagerBase childManager,
			WomBackbone parent,
			WtNodeList container,
			WtNode firstNode,
			Iterator<WtNode> iter,
			WtNode to)
	{
		TextAdapter complexText =
				(TextAdapter) factory.create(container, firstNode);
		
		WtNode notText = null;
		
		loop: while (iter.hasNext())
		{
			WtNode n = iter.next();
			if (n == to)
				break loop;
			
			switch (n.getNodeType())
			{
				case WtNode.NT_IGNORED:
				case WtNode.NT_NEWLINE:
				case WtNode.NT_TEXT:
				case WtNode.NT_XML_CHAR_REF:
				case WtNode.NT_XML_ENTITY_REF:
				{
					complexText.append(n);
					break;
				}
				default:
				{
					notText = n;
					break loop;
				}
			}
		}
		
		childManager.appendChild(complexText, parent, null);
		
		return notText;
	}
}
