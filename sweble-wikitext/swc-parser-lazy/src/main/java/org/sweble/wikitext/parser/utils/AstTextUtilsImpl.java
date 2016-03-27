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

import java.util.Iterator;

import org.sweble.wikitext.parser.ParserConfig;
import org.sweble.wikitext.parser.nodes.WikitextNodeFactory;
import org.sweble.wikitext.parser.nodes.WtContentNode;
import org.sweble.wikitext.parser.nodes.WtIgnored;
import org.sweble.wikitext.parser.nodes.WtNode;
import org.sweble.wikitext.parser.nodes.WtNodeList;
import org.sweble.wikitext.parser.nodes.WtText;
import org.sweble.wikitext.parser.nodes.WtXmlCharRef;
import org.sweble.wikitext.parser.nodes.WtXmlComment;
import org.sweble.wikitext.parser.nodes.WtXmlEntityRef;

public class AstTextUtilsImpl
		implements
			AstTextUtils
{
	protected final ParserConfig parserConfig;

	// =========================================================================

	public AstTextUtilsImpl(ParserConfig parserConfig)
	{
		this.parserConfig = parserConfig;
	}

	// =========================================================================

	@Override
	public String astToText(WtNode node) throws StringConversionException
	{
		return astToText(node, new SimpleStringConverter());
	}

	@Override
	public String astToText(WtNode node, int... options) throws StringConversionException
	{
		return astToText(node, new SimpleStringConverter(options));
	}

	protected String astToText(WtNode node, SimpleStringConverter sc) throws StringConversionException
	{
		/* We will not resolve content nodes into strings unless the specified 
		 * node itself is a content node.
		 */
		int nodeType = node.getNodeType();
		if (node instanceof WtContentNode)
			nodeType = WtNode.NT_NODE_LIST;

		sc.dispatch(node, nodeType);
		return sc.toString();
	}

	@Override
	public PartialConversion astToTextPartial(WtNode node)
	{
		return astToTextPartial(node, new PartialStringConverter());
	}

	@Override
	public PartialConversion astToTextPartial(WtNode node, int... options)
	{
		return astToTextPartial(node, new PartialStringConverter(options));
	}

	protected PartialConversion astToTextPartial(
			WtNode node,
			final PartialStringConverter sc)
	{
		/* We will not resolve content nodes into strings unless the specified 
		 * node itself is a content node.
		 */
		int nodeType = node.getNodeType();
		if (node instanceof WtContentNode)
			nodeType = WtNode.NT_NODE_LIST;

		try
		{
			sc.dispatch(node, nodeType);
		}
		catch (StringConversionException e)
		{
			// The specified node is not a container/list and could not be 
			// converted.
			sc.tail = nf().list(node);
		}

		return new PartialConversion()
		{
			@Override
			public String getText()
			{
				return sc.toString();
			}

			@Override
			public WtNodeList getTail()
			{
				return sc.tail;
			}
		};
	}

	// =========================================================================

	protected WikitextNodeFactory nf()
	{
		return this.parserConfig.getNodeFactory();
	}

	// =========================================================================

	protected static class SimpleStringConverter
	{
		protected boolean failOnUnresolvedEntityRef;

		protected StringBuilder b = new StringBuilder();

		public SimpleStringConverter()
		{
			failOnUnresolvedEntityRef = false;
		}

		public SimpleStringConverter(int[] options)
		{
			this();
			for (int option : options)
				setOption(option);
		}

		protected void setOption(int option)
		{
			switch (option)
			{
				case FAIL_ON_UNRESOLVED_ENTITY_REF:
					failOnUnresolvedEntityRef = true;
					break;
				default:
					throw new IllegalArgumentException("Unknown option");
			}
		}

		public String toString()
		{
			return b.toString();
		}

		public void dispatch(WtNode node, int nodeType) throws StringConversionException
		{
			switch (nodeType)
			{
				case WtNode.NT_NODE_LIST:
					visit((WtNodeList) node);
					break;
				case WtNode.NT_TEXT:
					visit((WtText) node);
					break;
				case WtNode.NT_XML_CHAR_REF:
					visit((WtXmlCharRef) node);
					break;
				case WtNode.NT_XML_ENTITY_REF:
					visit((WtXmlEntityRef) node);
					break;
				case WtNode.NT_XML_COMMENT:
					visit((WtXmlComment) node);
					break;
				case WtNode.NT_IGNORED:
					visit((WtIgnored) node);
					break;
				default:
					visitNotFound(node);
					break;
			}
		}

		protected void visitNotFound(WtNode node) throws StringConversionException
		{
			throw new StringConversionException(node);
		}

		protected void visit(WtIgnored node)
		{
			// Ignore
		}

		protected void visit(WtXmlComment node)
		{
			// Ignore
		}

		protected void visit(WtXmlEntityRef node) throws StringConversionException
		{
			if (node.getResolved() == null)
			{
				if (failOnUnresolvedEntityRef)
					throw new StringConversionException(node);

				b.append('&');
				b.append(node.getName());
				b.append(';');
			}
			else
			{
				b.append(node.getResolved());
			}
		}

		protected void visit(WtXmlCharRef node)
		{
			b.append(Character.toChars(node.getCodePoint()));
		}

		protected void visit(WtText node)
		{
			b.append(node.getContent());
		}

		protected void visit(WtNodeList node) throws StringConversionException
		{
			for (WtNode n : node)
				dispatch(n, n.getNodeType());
		}
	}

	// =========================================================================

	protected class PartialStringConverter
			extends
				SimpleStringConverter
	{
		WtNodeList tail = nf().emptyList();

		public PartialStringConverter()
		{
		}

		public PartialStringConverter(int[] options)
		{
			super(options);
		}

		@Override
		public void visit(WtNodeList node) throws StringConversionException
		{
			Iterator<WtNode> i = node.iterator();
			while (i.hasNext())
			{
				WtNode c = i.next();
				try
				{
					dispatch(c, c.getNodeType());
				}
				catch (StringConversionException e)
				{
					tail = nf().list();
					tail.add(c);
					while (i.hasNext())
						tail.add(i.next());

					return;
				}
			}
		}
	}
}
