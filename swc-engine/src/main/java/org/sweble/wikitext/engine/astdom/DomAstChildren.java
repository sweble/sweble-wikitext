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
package org.sweble.wikitext.engine.astdom;

import java.util.Collection;
import java.util.Iterator;
import java.util.ListIterator;

import org.sweble.wikitext.engine.wom.WomNode;

import de.fau.cs.osr.ptk.common.ast.AstNode;
import de.fau.cs.osr.ptk.common.ast.NodeList;

public class DomAstChildren
{
	private DomBackbone firstChild;
	
	// =========================================================================
	
	public boolean hasChildNodes()
	{
		return firstChild != null;
	}
	
	public Collection<WomNode> childNodes()
	{
		return new SiblingCollection<WomNode>(firstChild);
	}
	
	public WomNode getFirstChild()
	{
		return firstChild;
	}
	
	public WomNode getLastChild()
	{
		WomNode child = firstChild;
		if (child == null)
			return null;
		while (child.getNextSibling() != null)
			child = child.getNextSibling();
		return child;
	}
	
	public void appendChild(WomNode child, DomBackbone parent, NodeList childContainer)
	{
		if (child == null)
			throw new IllegalArgumentException("Argument `child' is null.");
		
		if (!(child instanceof DomBackbone))
			throw new IllegalArgumentException(
			        "Expected argument `child' be of type " + DomBackbone.class.getName());
		
		final DomBackbone newChild = (DomBackbone) child;
		if (newChild.isLinked())
			throw new IllegalStateException(
			        "Given node `child' is still child of another DOM node.");
		
		// insert into DOM
		newChild.link(parent, (DomBackbone) getLastChild(), null);
		if (firstChild == null)
			firstChild = newChild;
		
		// insert into AST
		childContainer.add(newChild.getAstNode());
	}
	
	public void insertBefore(WomNode before, WomNode child, DomBackbone parent, NodeList childContainer) throws IllegalArgumentException
	{
		if (before == null || child == null)
			throw new IllegalArgumentException("Argument `before' and/or `child' is null.");
		
		if (!(child instanceof DomBackbone))
			throw new IllegalArgumentException(
			        "Expected argument `child' be of type " + DomBackbone.class.getName());
		
		final DomBackbone newChild = (DomBackbone) child;
		if (newChild.isLinked())
			throw new IllegalStateException(
			        "Given argument `child' is still child of another DOM node.");
		
		if (before.getParent() != parent)
			throw new IllegalArgumentException("Given node `before' is not a child of this node.");
		DomBackbone b = (DomBackbone) before;
		
		// insert into DOM
		if (before == firstChild)
		{
			newChild.link(parent, null, firstChild);
			firstChild = newChild;
		}
		else
		{
			newChild.link(parent, b.getPrevSibling(), b);
		}
		
		// insert into AST
		ListIterator<AstNode> i = childContainer.listIterator();
		while (i.hasNext())
		{
			AstNode n = i.next();
			if (n == b.getAstNode())
			{
				i.previous();
				i.add(newChild.getAstNode());
				i = null;
				break;
			}
		}
		
		if (i != null)
			throw new AssertionError();
	}
	
	public void removeChild(WomNode child, DomBackbone parent, NodeList childContainer)
	{
		if (child == null)
			throw new IllegalArgumentException("Argument `child' is null.");
		
		if (!(child instanceof DomBackbone))
			throw new IllegalArgumentException(
			        "Expected argument `child' be of type " + DomBackbone.class.getName());
		
		if (child.getParent() != parent)
			throw new IllegalArgumentException("Given node `child' is not a child of this node.");
		DomBackbone remove = (DomBackbone) child;
		
		removeChild(remove, childContainer);
	}
	
	public void replaceChild(WomNode search, WomNode replace, DomBackbone parent, NodeList childContainer)
	{
		if (search == null || replace == null)
			throw new IllegalArgumentException("Argument `search' and/or `replace' is null.");
		
		if (!(replace instanceof DomBackbone))
			throw new IllegalArgumentException(
			        "Expected argument `replace' be of type " + DomBackbone.class.getName());
		
		final DomBackbone newChild = (DomBackbone) replace;
		if (newChild.isLinked())
			throw new IllegalStateException(
			        "Given node `replace' is still child of another DOM node.");
		
		if (search.getParent() != parent)
			throw new IllegalArgumentException("Given node `search' is not a child of this node.");
		DomBackbone oldChild = (DomBackbone) search;
		
		replaceChild(oldChild, newChild, parent, childContainer);
	}
	
	// =========================================================================
	
	private void removeChild(DomBackbone remove, NodeList childContainer)
	{
		// remove from DOM
		if (remove == firstChild)
			firstChild = remove.getNextSibling();
		remove.unlink();
		
		// remove from AST
		Iterator<AstNode> i = childContainer.iterator();
		while (i.hasNext())
		{
			AstNode node = i.next();
			if (node == remove.getAstNode())
			{
				i.remove();
				i = null;
				break;
			}
		}
		if (i != null)
			throw new AssertionError();
	}
	
	private void replaceChild(final DomBackbone oldChild, final DomBackbone newChild, DomBackbone parent, NodeList childContainer)
	{
		// replace in DOM
		DomBackbone prev = oldChild.getPrevSibling();
		DomBackbone next = oldChild.getNextSibling();
		oldChild.unlink();
		
		newChild.link(parent, prev, next);
		if (oldChild == firstChild)
			firstChild = newChild;
		
		// replace in AST
		AstNode oldAstNode = oldChild.getAstNode();
		AstNode newAstNode = newChild.getAstNode();
		
		ListIterator<AstNode> i = childContainer.listIterator();
		while (i.hasNext())
		{
			AstNode node = i.next();
			if (node == oldAstNode)
			{
				i.remove();
				i.add(newAstNode);
				i = null;
				break;
			}
		}
		if (i != null)
			throw new AssertionError();
	}
}
