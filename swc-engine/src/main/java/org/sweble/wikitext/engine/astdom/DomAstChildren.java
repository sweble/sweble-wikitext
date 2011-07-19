package org.sweble.wikitext.engine.astdom;

import java.util.Collection;
import java.util.Iterator;
import java.util.ListIterator;

import org.sweble.wikitext.engine.dom.DomNode;

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
	
	public Collection<DomNode> childNodes()
	{
		return new SiblingCollection<DomNode>(firstChild);
	}
	
	public DomNode getFirstChild()
	{
		return firstChild;
	}
	
	public DomNode getLastChild()
	{
		DomNode child = firstChild;
		if (child == null)
			return null;
		while (child.getNextSibling() != null)
			child = child.getNextSibling();
		return child;
	}
	
	public void appendChild(DomNode child, DomBackbone parent, NodeList childContainer)
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
	
	public void insertBefore(DomNode before, DomNode child, DomBackbone parent, NodeList childContainer) throws IllegalArgumentException
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
	
	public void removeChild(DomNode child, DomBackbone parent, NodeList childContainer)
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
	
	public void replaceChild(DomNode search, DomNode replace, DomBackbone parent, NodeList childContainer)
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
