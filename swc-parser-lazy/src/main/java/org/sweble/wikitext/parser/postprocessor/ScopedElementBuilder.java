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

package org.sweble.wikitext.parser.postprocessor;

import java.util.LinkedList;

import org.sweble.wikitext.parser.AstNodeTypes;
import org.sweble.wikitext.parser.ParserConfig;
import org.sweble.wikitext.parser.WtRtData;
import org.sweble.wikitext.parser.nodes.Table;
import org.sweble.wikitext.parser.nodes.TableCaption;
import org.sweble.wikitext.parser.nodes.TableCell;
import org.sweble.wikitext.parser.nodes.TableHeader;
import org.sweble.wikitext.parser.nodes.TableRow;
import org.sweble.wikitext.parser.nodes.WtContentNode;
import org.sweble.wikitext.parser.nodes.WtDefinitionList;
import org.sweble.wikitext.parser.nodes.WtDefinitionListDef;
import org.sweble.wikitext.parser.nodes.WtDefinitionListTerm;
import org.sweble.wikitext.parser.nodes.WtExternalLink;
import org.sweble.wikitext.parser.nodes.WtImageLink;
import org.sweble.wikitext.parser.nodes.WtInternalLink;
import org.sweble.wikitext.parser.nodes.WtListItem;
import org.sweble.wikitext.parser.nodes.WtNode;
import org.sweble.wikitext.parser.nodes.WtNodeList;
import org.sweble.wikitext.parser.nodes.WtOrderedList;
import org.sweble.wikitext.parser.nodes.WtParsedWikitextPage;
import org.sweble.wikitext.parser.nodes.WtSection;
import org.sweble.wikitext.parser.nodes.WtSemiPre;
import org.sweble.wikitext.parser.nodes.WtSemiPreLine;
import org.sweble.wikitext.parser.nodes.WtUnorderedList;
import org.sweble.wikitext.parser.nodes.WtXmlElement;
import org.sweble.wikitext.parser.nodes.WtXmlEmptyTag;
import org.sweble.wikitext.parser.nodes.WtXmlEndTag;
import org.sweble.wikitext.parser.nodes.WtXmlStartTag;
import org.sweble.wikitext.parser.parser.WtNamedXmlElement;
import org.sweble.wikitext.parser.postprocessor.ElementScopeStack.Scope;

import de.fau.cs.osr.ptk.common.AstVisitor;
import de.fau.cs.osr.utils.FmtInternalLogicError;

public class ScopedElementBuilder
		extends
			AstVisitor<WtNode>
{
	private final ParserConfig config;
	
	private final ElementScopeStack stack = new ElementScopeStack();
	
	// =========================================================================
	
	public static WtParsedWikitextPage process(
			ParserConfig config,
			WtNode ast)
	{
		return (WtParsedWikitextPage) new ScopedElementBuilder(config).go(ast);
	}
	
	// =========================================================================
	
	public ScopedElementBuilder(ParserConfig config)
	{
		this.config = config;
	}
	
	// =========================================================================
	
	@Override
	protected Object after(WtNode node, Object result)
	{
		return node;
	}
	
	// =========================================================================
	
	public void visit(WtNode n)
	{
		stack.append(n);
	}
	
	// =========================================================================
	
	public void visit(WtParsedWikitextPage n)
	{
		openScope(ScopeType.PAGE, n);
		n.exchange(processScope(n));
		closeScope(ScopeType.PAGE, n);
	}
	
	public void visit(WtExternalLink n)
	{
		openScope(ScopeType.WT_INLINE, n);
		n.getTitle().exchange(processScope(n.getTitle()));
		closeScope(ScopeType.WT_INLINE, n);
	}
	
	public void visit(WtInternalLink n)
	{
		openScope(ScopeType.WT_INLINE, n);
		n.getTitle().exchange(processScope(n.getTitle()));
		closeScope(ScopeType.WT_INLINE, n);
	}
	
	public void visit(WtImageLink n)
	{
		openScope(ScopeType.WT_INLINE, n);
		n.getTitle().exchange(processScope(n.getTitle()));
		closeScope(ScopeType.WT_INLINE, n);
	}
	
	public void visit(WtSection n)
	{
		openScope(ScopeType.WT_BLOCK, n);
		n.getHeading().exchange(processScope(n.getHeading()));
		n.getBody().exchange(processScope(n.getBody()));
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
	
	public void visit(WtDefinitionList n)
	{
		openScope(ScopeType.WT_BLOCK, n);
		iterate(n);
		closeScope(ScopeType.WT_BLOCK, n);
	}
	
	public void visit(WtDefinitionListTerm n)
	{
		n.exchange(processScope(n));
	}
	
	public void visit(WtDefinitionListDef n)
	{
		n.exchange(processScope(n));
	}
	
	public void visit(WtOrderedList n)
	{
		openScope(ScopeType.WT_BLOCK, n);
		iterate(n);
		closeScope(ScopeType.WT_BLOCK, n);
	}
	
	public void visit(WtUnorderedList n)
	{
		openScope(ScopeType.WT_BLOCK, n);
		iterate(n);
		closeScope(ScopeType.WT_BLOCK, n);
	}
	
	public void visit(WtListItem n)
	{
		n.exchange(processScope(n));
	}
	
	public void visit(WtSemiPre n)
	{
		openScope(ScopeType.WT_BLOCK, n);
		iterate(n);
		closeScope(ScopeType.WT_BLOCK, n);
	}
	
	public void visit(WtSemiPreLine n)
	{
		n.exchange(processScope(n));
	}
	
	// =========================================================================
	
	public void visit(WtXmlEmptyTag n)
	{
		if (isXmlElementAllowed(n.getName()))
		{
			openAndCloseScope(determineElementType(n), n);
		}
		else
		{
			stack.append(n);
		}
	}
	
	public void visit(WtXmlStartTag n)
	{
		String name = n.getName();
		if (isXmlElementAllowed(name))
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
	
	public void visit(WtXmlEndTag n)
	{
		String name = n.getName();
		if (isXmlElementAllowed(name))
		{
			closeScope(determineElementType(n), n);
		}
		else
		{
			stack.append(n);
		}
	}
	
	private boolean isXmlElementAllowed(String name)
	{
		if (name.isEmpty())
			return false;
		return name.charAt(0) == '@' || config.isXmlElementAllowed(name);
	}
	
	// =========================================================================
	
	private ScopeType determineElementType(WtNamedXmlElement n)
	{
		String name = n.getName().toLowerCase();
		if (name.startsWith("@"))
			name = name.substring(1);
		return config.getXmlElementType(name);
	}
	
	// =========================================================================
	
	private void openAndCloseScope(ScopeType scopeType, WtXmlEmptyTag n)
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
	
	private void openScope(ScopeType scopeType, WtNode n)
	{
		stack.push(scopeType, n, true);
		
		if (scopeType.isCloseInline())
		{
			closeBeforeBlock(scopeType.isCloseSamePred());
			
			if (scopeType.isReopenClosedInline() && !scopeType.isWikitextScope())
				reopenClosedInlineScopes();
		}
	}
	
	private void processScope(WtContentNode n)
	{
		n.setContent(processScope(n.getContent()));
	}
	
	/*
	private void processScope(WtContentNodeMarkTwo n)
	{
		n.exchange(processScope(n));
	}
	*/
	
	private WtNodeList processScope(WtNodeList n)
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
	
	private void closeScope(ScopeType scopeType, WtNode n)
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
		
		final WtNamedXmlElement closing = (WtNamedXmlElement) n;
		
		// It's an XML element and it actually matches!
		if (current.match(/*scopeType, */closing))
		{
			stack.pop();
			stack.append(close(current, (WtXmlEndTag) n));
			
			reopenClosedInlineScopes(current.getClosedInline());
			return;
		}
		
		// It's an XML element and there's no immediately matching opening tag.
		
		Scope s = findMatchingOpeningTag(scopeType, current, closing);
		if (s == null)
		{
			if (!(n instanceof WtImEndTag))
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
				closeScopesBeforeClosingTag(from, toIncl, (WtXmlEndTag) n);
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
					current.getElement() instanceof WtNamedXmlElement)
			{
				final WtNamedXmlElement e =
						(WtNamedXmlElement) current.getElement();
				
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
				WtNodeList c = null;
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
			WtXmlEndTag close_)
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
			final WtNamedXmlElement closing)
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
			WtNamedXmlElement closing)
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
			WtNamedXmlElement closing)
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
			WtNamedXmlElement closing)
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
			WtNamedXmlElement closing)
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
	
	private WtNode close(Scope s)
	{
		return close(s, null);
	}
	
	private WtNode close(Scope s, WtNode c)
	{
		WtNodeList content = s.getContent();
		if (s.getType().dropIfEmpty() && content.isEmpty())
			return null;
		
		if (content.size() == 1)
		{
			final WtNode c0 = content.get(0);
			if (s.getType().dropIfOnlyWhitespace() &&
					c0.isNodeType(AstNodeTypes.NT_WHITESPACE) &&
					s.isOpen() == false &&
					c == null)
				return c0;
		}
		
		if (s.getElement() instanceof WtImStartTag)
		{
			final WtImStartTag open = (WtImStartTag) s.getElement();
			final WtImEndTag close = (WtImEndTag) c;
			return open.getType().transform(config, open, close, content);
		}
		else
		{
			final WtXmlStartTag open = (WtXmlStartTag) s.getElement();
			final WtXmlEndTag close = (WtXmlEndTag) c;
			return createElement(
					open,
					content,
					close,
					s.isOpen());
		}
	}
	
	// =========================================================================
	
	private WtXmlElement createEmptyElement(WtXmlEmptyTag empty)
	{
		return createEmptyElement(empty, empty.getXmlAttributes(), true);
	}
	
	private WtXmlElement createEmptyElement(WtXmlStartTag open)
	{
		return createEmptyElement(open, open.getXmlAttributes(), false);
	}
	
	private WtXmlElement createEmptyElement(
			WtNamedXmlElement e,
			WtNodeList attrs,
			boolean wasEmpty)
	{
		WtXmlElement element = new WtXmlElement(
				e.getName(),
				true,
				attrs,
				new WtNodeList());
		
		if (config.isGatherRtData())
		{
			WtRtData rtd = new WtRtData(3);
			WtRtData rtdEmpty = e.getRtd();
			if (rtdEmpty == null)
			{
				rtd.setField(0, '<', e.getName());
				rtd.setField(1, wasEmpty ? " />" : '>');
			}
			else
			{
				rtd.setField(0, rtdEmpty.getField(0));
				rtd.setField(1, rtdEmpty.getField(1));
			}
			element.setRtd(rtd);
		}
		
		return element;
	}
	
	private WtXmlElement createElement(
			WtXmlStartTag open,
			WtNodeList body,
			WtXmlEndTag close,
			boolean hasOpen)
	{
		WtXmlElement e = new WtXmlElement(
				open.getName(),
				false,
				open.getXmlAttributes(),
				body);
		
		if (config.isGatherRtData())
		{
			WtRtData rtd = new WtRtData(3);
			if (hasOpen)
			{
				WtRtData rtdOpen = open.getRtd();
				if (rtdOpen == null)
				{
					rtd.setField(0, '<', open.getName());
					rtd.setField(1, '>');
				}
				else
				{
					rtd.setField(0, rtdOpen.getField(0));
					rtd.setField(1, rtdOpen.getField(1));
				}
			}
			
			if (close != null)
			{
				WtRtData rtdClose = close.getRtd();
				if (rtdClose == null)
				{
					rtd.setField(2, "</", open.getName(), '>');
				}
				else
				{
					rtd.setField(2, rtdClose.getField(0));
				}
			}
			e.setRtd(rtd);
		}
		
		return e;
	}
}
