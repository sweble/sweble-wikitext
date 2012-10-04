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
import org.sweble.wikitext.parser.nodes.WtIgnored;
import org.sweble.wikitext.parser.nodes.WtNode;
import org.sweble.wikitext.parser.nodes.WtNodeList;
import org.sweble.wikitext.parser.nodes.WtText;
import org.sweble.wikitext.parser.nodes.XmlCharRef;
import org.sweble.wikitext.parser.nodes.WtXmlComment;
import org.sweble.wikitext.parser.nodes.XmlEntityRef;
import org.sweble.wikitext.parser.preprocessor.ProtectedText;

import de.fau.cs.osr.ptk.common.AstVisitor;
import de.fau.cs.osr.utils.Tuple;
import de.fau.cs.osr.utils.Tuple2;

public class StringConverterPartial
{
	public static Tuple2<String, WtNodeList> convert(WtNode astNode)
	{
		return convert(astNode, null, StringConverter.DEFAULT_OPTIONS);
	}
	
	@SuppressWarnings("unchecked")
	public static Tuple2<String, WtNodeList> convert(
			WtNode astNode,
			ParserConfig resolver,
			int options)
	{
		ConverterVisitor converter =
				new ConverterVisitor(options, resolver);
		
		return (Tuple2<String, WtNodeList>) converter.go(astNode);
	}
	
	// =========================================================================
	
	protected static final class ConverterVisitor
			extends
				AstVisitor<WtNode>
	{
		private final StringBuilder result = new StringBuilder();
		
		private final ParserConfig entityResolver;
		
		private final int options;
		
		private WtNode failedOnNode = null;
		
		// =====================================================================
		
		public ConverterVisitor(int options, ParserConfig resolver)
		{
			this.entityResolver = resolver;
			this.options = options;
			
			if (resolver != null && !opt(StringConverter.RESOLVE_ENTITY_REF))
				throw new IllegalArgumentException(
						"If a resolver instance is given the option RESOLVE_ENTITY_REF is required");
			
			if (resolver == null && opt(StringConverter.RESOLVE_ENTITY_REF))
				throw new IllegalArgumentException(
						"If the option RESOLVE_ENTITY_REF is given a resolver instance is required");
		}
		
		// =====================================================================
		
		@Override
		protected Object after(WtNode node, Object result)
		{
			WtNodeList tail = null;
			if (failedOnNode != null)
			{
				if (failedOnNode.isNodeType(WtNode.NT_NODE_LIST))
				{
					tail = (WtNodeList) failedOnNode;
				}
				else
				{
					tail = new WtNodeList(failedOnNode);
				}
			}
			
			return Tuple.from(this.result.toString(), tail);
		}
		
		// =====================================================================
		
		@Override
		public Object visitNotFound(WtNode node)
		{
			failedOnNode = node;
			return node;
		}
		
		public void visit(WtNodeList n)
		{
			Iterator<WtNode> i = n.iterator();
			while (i.hasNext())
			{
				WtNode c = i.next();
				dispatch(c);
				
				if (failedOnNode != null)
				{
					failedOnNode = new WtNodeList();
					
					failedOnNode.add(c);
					while (i.hasNext())
						failedOnNode.add(i.next());
					
					break;
				}
			}
		}
		
		public void visit(XmlCharRef n)
		{
			if (opt(StringConverter.RESOLVE_CHAR_REF))
			{
				result.append(Character.toChars(n.getCodePoint()));
			}
			else
			{
				if (opt(StringConverter.FAIL_ON_UNRESOLVED_XML_ENTITY))
				{
					failedOnNode = n;
				}
				else
				{
					result.append("&#");
					result.append(n.getCodePoint());
					result.append(';');
				}
			}
		}
		
		public void visit(XmlEntityRef n)
		{
			String replacement = null;
			if (opt(StringConverter.RESOLVE_ENTITY_REF))
				replacement = entityResolver.resolveXmlEntity(n.getName());
			
			if (replacement == null)
			{
				if (opt(StringConverter.FAIL_ON_UNRESOLVED_XML_ENTITY))
				{
					failedOnNode = n;
				}
				else
				{
					result.append('&');
					result.append(n.getName());
					result.append(';');
				}
			}
			else
			{
				result.append(replacement);
			}
		}
		
		public void visit(WtText n)
		{
			result.append(n.getContent());
		}
		
		public void visit(ProtectedText n)
		{
			if (!opt(StringConverter.FAIL_ON_PROTECTED_TEXT))
				result.append(n.getContent());
		}
		
		public void visit(WtXmlComment n)
		{
			if (opt(StringConverter.FAIL_ON_XML_COMMENTS))
				failedOnNode = n;
		}
		
		public void visit(WtIgnored n)
		{
			if (opt(StringConverter.FAIL_ON_IGNORED))
				failedOnNode = n;
		}
		
		// =====================================================================
		
		private boolean opt(int x)
		{
			return (options & x) == x;
		}
	}
}
