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

package org.sweble.wikitext.lazy.postprocessor;

import java.util.LinkedList;

import org.sweble.wikitext.lazy.AstNodeTypes;
import org.sweble.wikitext.lazy.ParserConfigInterface;
import org.sweble.wikitext.lazy.parser.DefinitionDefinition;
import org.sweble.wikitext.lazy.parser.DefinitionList;
import org.sweble.wikitext.lazy.parser.DefinitionTerm;
import org.sweble.wikitext.lazy.parser.Enumeration;
import org.sweble.wikitext.lazy.parser.EnumerationItem;
import org.sweble.wikitext.lazy.parser.ExternalLink;
import org.sweble.wikitext.lazy.parser.ImageLink;
import org.sweble.wikitext.lazy.parser.InternalLink;
import org.sweble.wikitext.lazy.parser.Itemization;
import org.sweble.wikitext.lazy.parser.ItemizationItem;
import org.sweble.wikitext.lazy.parser.LazyParsedPage;
import org.sweble.wikitext.lazy.parser.NamedXmlElement;
import org.sweble.wikitext.lazy.parser.RtData;
import org.sweble.wikitext.lazy.parser.Section;
import org.sweble.wikitext.lazy.parser.SemiPre;
import org.sweble.wikitext.lazy.parser.SemiPreLine;
import org.sweble.wikitext.lazy.parser.Table;
import org.sweble.wikitext.lazy.parser.TableCaption;
import org.sweble.wikitext.lazy.parser.TableCell;
import org.sweble.wikitext.lazy.parser.TableHeader;
import org.sweble.wikitext.lazy.parser.TableRow;
import org.sweble.wikitext.lazy.parser.XmlElement;
import org.sweble.wikitext.lazy.parser.XmlElementClose;
import org.sweble.wikitext.lazy.parser.XmlElementEmpty;
import org.sweble.wikitext.lazy.parser.XmlElementOpen;
import org.sweble.wikitext.lazy.postprocessor.ElementScopeStack.Scope;
import org.sweble.wikitext.lazy.utils.TextUtils;

import de.fau.cs.osr.ptk.common.AstVisitor;
import de.fau.cs.osr.ptk.common.ast.AstNode;
import de.fau.cs.osr.ptk.common.ast.ContentNode;
import de.fau.cs.osr.ptk.common.ast.NodeList;
import de.fau.cs.osr.utils.FmtInternalLogicError;

public class ScopedElementBuilder
		extends
			AstVisitor
{
	private final ParserConfigInterface config;
	
	private final ElementScopeStack stack = new ElementScopeStack();
	
	// =========================================================================
	
	public static LazyParsedPage process(
			ParserConfigInterface config,
			AstNode ast)
	{
		return (LazyParsedPage) new ScopedElementBuilder(config).go(ast);
	}
	
	// =========================================================================
	
	public ScopedElementBuilder(ParserConfigInterface config)
	{
		this.config = config;
	}
	
	// =========================================================================
	
	@Override
	protected Object after(AstNode node, Object result)
	{
		return node;
	}
	
	// =========================================================================
	
	public void visit(AstNode n)
	{
		stack.append(n);
	}
	
	// =========================================================================
	
	public void visit(LazyParsedPage n)
	{
		openScope(ScopeType.PAGE, n);
		processScope(n);
		closeScope(ScopeType.PAGE, n);
	}
	
	public void visit(ExternalLink n)
	{
		openScope(ScopeType.WT_INLINE, n);
		n.setTitle(processScope(n.getTitle()));
		closeScope(ScopeType.WT_INLINE, n);
	}
	
	public void visit(InternalLink n)
	{
		openScope(ScopeType.WT_INLINE, n);
		processScope(n.getTitle());
		closeScope(ScopeType.WT_INLINE, n);
	}
	
	public void visit(ImageLink n)
	{
		openScope(ScopeType.WT_INLINE, n);
		processScope(n.getTitle());
		closeScope(ScopeType.WT_INLINE, n);
	}
	
	public void visit(Section n)
	{
		openScope(ScopeType.WT_BLOCK, n);
		n.setTitle(processScope(n.getTitle()));
		n.setBody(processScope(n.getBody()));
		closeScope(ScopeType.WT_BLOCK, n);
	}
	
	public void visit(Table n)
	{
		openScope(ScopeType.WT_TABLE, n);
		n.setBody(processScope(n.getBody()));
		closeScope(ScopeType.WT_TABLE, n);
	}
	
	public void visit(TableCaption n)
	{
		openScope(ScopeType.WT_TABLE_ITEM, n);
		n.setBody(processScope(n.getBody()));
		closeScope(ScopeType.WT_TABLE_ITEM, n);
	}
	
	public void visit(TableRow n)
	{
		openScope(ScopeType.WT_TABLE_ITEM, n);
		n.setBody(processScope(n.getBody()));
		closeScope(ScopeType.WT_TABLE_ITEM, n);
	}
	
	public void visit(TableHeader n)
	{
		openScope(ScopeType.WT_TABLE_ITEM, n);
		n.setBody(processScope(n.getBody()));
		closeScope(ScopeType.WT_TABLE_ITEM, n);
	}
	
	public void visit(TableCell n)
	{
		openScope(ScopeType.WT_TABLE_ITEM, n);
		n.setBody(processScope(n.getBody()));
		closeScope(ScopeType.WT_TABLE_ITEM, n);
	}
	
	public void visit(DefinitionList n)
	{
		openScope(ScopeType.WT_BLOCK, n);
		iterate(n.getContent());
		closeScope(ScopeType.WT_BLOCK, n);
	}
	
	public void visit(DefinitionTerm n)
	{
		processScope(n);
	}
	
	public void visit(DefinitionDefinition n)
	{
		processScope(n);
	}
	
	public void visit(Enumeration n)
	{
		openScope(ScopeType.WT_BLOCK, n);
		iterate(n.getContent());
		closeScope(ScopeType.WT_BLOCK, n);
	}
	
	public void visit(EnumerationItem n)
	{
		processScope(n);
	}
	
	public void visit(Itemization n)
	{
		openScope(ScopeType.WT_BLOCK, n);
		iterate(n.getContent());
		closeScope(ScopeType.WT_BLOCK, n);
	}
	
	public void visit(ItemizationItem n)
	{
		processScope(n);
	}
	
	public void visit(SemiPre n)
	{
		openScope(ScopeType.WT_BLOCK, n);
		iterate(n.getContent());
		closeScope(ScopeType.WT_BLOCK, n);
	}
	
	public void visit(SemiPreLine n)
	{
		processScope(n);
	}
	
	// =========================================================================
	
	public void visit(XmlElementEmpty n)
	{
		if (n instanceof IntermediateTag ||
				config.isXmlElementAllowed(n.getName()))
		{
			openAndCloseScope(determineElementType(n), n);
		}
		else
		{
			stack.append(n);
		}
	}
	
	public void visit(XmlElementOpen n)
	{
		String name = n.getName();
		if (n instanceof IntermediateTag || config.isXmlElementAllowed(name))
		{
			if (config.isXmlElementEmptyOnly(name))
			{
				stack.append(createEmptyElement(n));
			}
			else
			{
				openScope(determineElementType(n), n);
			}
		}
		else
		{
			stack.append(n);
		}
	}
	
	public void visit(XmlElementClose n)
	{
		String name = n.getName();
		if (n instanceof IntermediateTag || config.isXmlElementAllowed(name))
		{
			closeScope(determineElementType(n), n);
		}
		else
		{
			stack.append(n);
		}
	}
	
	// =========================================================================
	
	private ScopeType determineElementType(NamedXmlElement n)
	{
		String name = n.getName().toLowerCase();
		if (name.startsWith("@"))
			name = name.substring(1);
		return config.getXmlElementType(name);
	}
	
	// =========================================================================
	
	private void openAndCloseScope(ScopeType scopeType, XmlElementEmpty n)
	{
		Scope current = null;
		if (scopeType.isCloseInline())
		{
			stack.push(scopeType, n, true);
			
			closeBeforeBlock(scopeType.isCloseSamePred());
			
			current = stack.top();
			stack.pop();
		}
		
		stack.append(createEmptyElement(n));
		
		if (current != null)
			reopenClosedInlineScopes(current.getClosedInline());
	}
	
	private void openScope(ScopeType scopeType, AstNode n)
	{
		stack.push(scopeType, n, true);
		
		if (scopeType.isCloseInline())
		{
			closeBeforeBlock(scopeType.isCloseSamePred());
			
			if (scopeType.isReopenClosedInline() && !scopeType.isWikitextScope())
				reopenClosedInlineScopes();
		}
	}
	
	private void processScope(ContentNode n)
	{
		n.setContent(processScope(n.getContent()));
	}
	
	private NodeList processScope(NodeList n)
	{
		Scope container = stack.top();
		
		stack.push(ScopeType.WT_ELEMENT_BODY, n, true);
		Scope current = stack.top();
		
		if (container.getType().isReopenClosedInline())
			reopenClosedInlineScopes(container.clearClosedInline());
		
		iterate(n);
		
		while (stack.top() != current)
		{
			Scope s = stack.top();
			if (s.getType().isWikitextScope())
				throw new FmtInternalLogicError();
			
			if (s.getType().isReopen() && container.getType().isPropagateOut())
			{
				container.addClosedInline(s);
			}
			else if (!s.getType().isPropagateOut())
			{
				container.clearClosedInline();
			}
			// Table and Special scopes are just closed and won't be continued
			
			stack.top().previous().append(close(s));
			stack.pop();
		}
		
		stack.pop();
		return current.getContent();
	}
	
	private void closeScope(ScopeType scopeType, AstNode n)
	{
		Scope current = stack.top();
		
		// It's a native Wikitext element
		if (scopeType.isWikitextScope())
		{
			// These must match, otherwise our algorithm is wrong :(
			if (current.getElement() != n || current.getType() != scopeType)
				throw new FmtInternalLogicError();
			
			// The actual contents of the various scopes the element might have
			// will have been processed by now in the processScope() method.
			stack.pop();
			if (scopeType != ScopeType.PAGE)
				stack.append(n);
			
			reopenClosedInlineScopes(current.getClosedInline());
			return;
		}
		
		final NamedXmlElement closing = (NamedXmlElement) n;
		
		// It's an XML element and it actually matches!
		if (current.match(/*scopeType, */closing))
		{
			stack.pop();
			stack.append(close(current, (XmlElementClose) n));
			
			reopenClosedInlineScopes(current.getClosedInline());
			return;
		}
		
		// It's an XML element and there's no immediately matching opening tag.
		
		Scope s = findMatchingOpeningTag(scopeType, current, closing);
		if (s == null)
		{
			if (!(n instanceof ImTagClose))
				stack.append(n);
			return;
		}
		
		// Now traverse the stack from the opening tag to the closing tag.
		// If in between, we hit a Wikitext scope, we have to process the
		// span up to the Wikitext scope first and then continue inside the 
		// Wikitext scope.
		Scope from = s;
		while (true)
		{
			s = s.next();
			if (s == null)
			{
				// This call also reopens closed scopes after the closing tag n
				Scope toIncl = stack.top();
				closeScopesBeforeClosingTag(from, toIncl, (XmlElementClose) n);
				break;
			}
			else if (s.getType().isWikitextScope())
			{
				Scope toIncl = s.previous();
				Scope insert = s.next();
				closeScopesBeforeClosingTag(from, toIncl, insert);
				s = insert;
				
				from = insert.next();
				if (from == null)
					break;
			}
		}
	}
	
	private void closeBeforeBlock(boolean closeSamePrec)
	{
		Scope current = stack.top();
		
		boolean isRecordClosed =
				current.getType().isContinueClosed() ||
						current.getType().isReopenClosedInline();
		
		Scope i = current;
		while (i.hasPrevious())
		{
			Scope s = i.previous();
			if (s.getType().isClosableBeforeBlock())
			{
				if (s.getType().isReopen() && isRecordClosed)
					current.addClosedInline(s);
				
				s.previous().append(close(s));
				stack.drop(s);
				continue;
			}
			else if (closeSamePrec &&
					current.getElement() instanceof NamedXmlElement)
			{
				final NamedXmlElement e =
						(NamedXmlElement) current.getElement();
				
				if (s.match(/*current.getType(), */e))
				{
					s.previous().append(close(s));
					stack.drop(s);
				}
			}
			
			break;
		}
	}
	
	private void closeScopesBeforeClosingTag(
			Scope from,
			Scope toIncl,
			Scope reopenIn)
	{
		Scope i = toIncl;
		while (true)
		{
			// Only reopen closable inline scopes
			if (i.getType().isReopen())
			{
				NodeList c = null;
				if (!reopenIn.getContent().isEmpty())
					c = reopenIn.clearContent();
				
				Scope s = stack.insertAfter(
						reopenIn, i.getType(), i.getElement(), false);
				
				if (c != null)
					s.setContent(c);
			}
			
			i.previous().append(close(i));
			stack.drop(i);
			
			if (i == from)
				break;
			
			i = i.previous();
		}
	}
	
	private void closeScopesBeforeClosingTag(
			Scope from,
			Scope toIncl,
			XmlElementClose close_)
	{
		LinkedList<Scope> reopen = null;
		
		boolean propagateOut = from.getType().isPropagateOut();
		
		Scope i = toIncl;
		while (true)
		{
			if (i != from)
			{
				// Only reopen closable inline scopes
				if (i.getType().isReopen() && propagateOut)
				{
					if (reopen == null)
						reopen = new LinkedList<Scope>();
					reopen.addFirst(i);
				}
				
				i.previous().append(close(i));
				
				stack.pop();
			}
			else
			{
				// Reopen closed inline elements that were not propagated into the
				// element we are about to close (usually tables)
				if (reopen == null)
					reopen = i.clearClosedInline();
				if (i.getClosedInline() != null)
					reopen.addAll(0, i.getClosedInline());
				
				if (i.match(close_))
				{
					i.previous().append(close(i, close_));
				}
				else
				{
					i.previous().append(close(i));
					i.previous().append(close_);
				}
				
				stack.pop();
				break;
			}
			
			i = i.previous();
		}
		
		if (from.getType().isContinueClosed())
			reopenClosedInlineScopes(reopen);
	}
	
	private void reopenClosedInlineScopes()
	{
		Scope current = stack.top();
		
		LinkedList<Scope> closedInline = current.getClosedInline();
		if (closedInline != null)
		{
			reopenClosedInlineScopes(closedInline);
			current.clearClosedInline();
		}
	}
	
	private void reopenClosedInlineScopes(LinkedList<Scope> closedInline)
	{
		if (closedInline != null)
		{
			for (Scope s : closedInline)
				stack.push(s.getType(), s.getElement(), false);
		}
	}
	
	private Scope findMatchingOpeningTag(
			ScopeType scopeType,
			Scope current,
			final NamedXmlElement closing)
	{
		if (scopeType.isInlineScope())
			return findMatchingInlineOpeningTag(scopeType, current, closing);
		
		if (scopeType.isTableScope())
			return findMatchingTableOpeningTag(scopeType, current, closing);
		
		if (scopeType.isBlockScope())
			return findMatchingBlockOpeningTag(scopeType, current, closing);
		
		return findMatchingSpecialOpeningTag(scopeType, current, closing);
	}
	
	private Scope findMatchingInlineOpeningTag(
			ScopeType scopeType,
			Scope s,
			NamedXmlElement closing)
	{
		do
		{
			if (s.getType().isBlockScope())
				break;
			
			s = s.previous();
			if (s.match(/*scopeType, */closing))
				return s;
		} while (s.hasPrevious());
		return null;
	}
	
	private Scope findMatchingTableOpeningTag(
			ScopeType scopeType,
			Scope s,
			NamedXmlElement closing)
	{
		do
		{
			s = s.previous();
			if (s.match(/*scopeType, */closing))
				return s;
		} while (s.hasPrevious());
		return null;
	}
	
	private Scope findMatchingBlockOpeningTag(
			ScopeType scopeType,
			Scope s,
			NamedXmlElement closing)
	{
		do
		{
			if (s.getType().isBlockScope())
				break;
			
			s = s.previous();
			if (s.match(/*scopeType, */closing))
				return s;
		} while (s.hasPrevious());
		return null;
	}
	
	private Scope findMatchingSpecialOpeningTag(
			ScopeType scopeType,
			Scope s,
			NamedXmlElement closing)
	{
		do
		{
			if (s.getType().isTableScope())
				break;
			
			s = s.previous();
			if (s.match(/*scopeType, */closing))
				return s;
		} while (s.hasPrevious());
		return null;
	}
	
	private AstNode close(Scope s)
	{
		return close(s, null);
	}
	
	private AstNode close(Scope s, AstNode c)
	{
		NodeList content = s.getContent();
		if (s.getType().dropIfEmpty() && content.isEmpty())
			return null;
		
		if (content.size() == 1)
		{
			final AstNode c0 = content.get(0);
			if (s.getType().dropIfOnlyWhitespace() &&
					c0.isNodeType(AstNodeTypes.NT_WHITESPACE) &&
					s.isOpen() == false &&
					c == null)
				return c0;
		}
		
		if (s.getElement() instanceof ImTagOpen)
		{
			final ImTagOpen open = (ImTagOpen) s.getElement();
			final ImTagClose close = (ImTagClose) c;
			return open.getType().transform(config, open, close, content);
		}
		else
		{
			final XmlElementOpen open = (XmlElementOpen) s.getElement();
			final XmlElementClose close = (XmlElementClose) c;
			return createElement(
					open,
					content,
					close,
					s.isOpen());
		}
	}
	
	// =========================================================================
	
	private XmlElement createEmptyElement(XmlElementEmpty empty)
	{
		return createEmptyElement(empty, empty.getXmlAttributes(), true);
	}
	
	private XmlElement createEmptyElement(XmlElementOpen open)
	{
		return createEmptyElement(open, open.getXmlAttributes(), false);
	}
	
	private XmlElement createEmptyElement(
			NamedXmlElement e,
			NodeList attrs,
			boolean wasEmpty)
	{
		XmlElement element = new XmlElement(
				e.getName(),
				true,
				attrs,
				new NodeList());
		
		if (config.isGatherRtData())
		{
			Object[] rtd0;
			Object[] rtd1;
			
			RtData rtd = (RtData) e.getAttribute("RTD");
			if (rtd == null)
			{
				rtd0 = TextUtils.joinRt('<', e.getName());
				rtd1 = TextUtils.joinRt(wasEmpty ? " />" : '>');
			}
			else
			{
				rtd0 = rtd.getRts()[0];
				rtd1 = rtd.getRts()[1];
			}
			
			TextUtils.addRtData(element, rtd0, rtd1, null);
		}
		
		return element;
	}
	
	private XmlElement createElement(
			XmlElementOpen open,
			NodeList body,
			XmlElementClose close,
			boolean hasOpen)
	{
		XmlElement e = new XmlElement(
				open.getName(),
				false,
				open.getXmlAttributes(),
				body);
		
		if (config.isGatherRtData())
		{
			Object[] oRtd0 = null;
			Object[] oRtd1 = null;
			Object[] cRtd0 = null;
			
			if (hasOpen)
			{
				RtData a = (RtData) open.getAttribute("RTD");
				if (a == null)
				{
					oRtd0 = TextUtils.joinRt('<', open.getName());
					oRtd1 = TextUtils.joinRt('>');
				}
				else
				{
					oRtd0 = a.getRts()[0];
					oRtd1 = a.getRts()[1];
				}
			}
			
			if (close != null)
			{
				RtData b = (RtData) close.getAttribute("RTD");
				if (b != null)
				{
					cRtd0 = b.getRts()[0];
				}
				else
				{
					cRtd0 = TextUtils.joinRt("</", open.getName(), '>');
				}
			}
			
			TextUtils.addRtData(e, oRtd0, oRtd1, cRtd0);
		}
		
		return e;
	}
}
