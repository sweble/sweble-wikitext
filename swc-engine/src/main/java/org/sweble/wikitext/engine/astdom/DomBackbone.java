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

import org.sweble.wikitext.engine.dom.DomAttribute;
import org.sweble.wikitext.engine.dom.DomNode;
import org.sweble.wikitext.lazy.AstNodeTypes;
import org.sweble.wikitext.lazy.utils.XmlAttribute;

import de.fau.cs.osr.ptk.common.ast.AstNode;
import de.fau.cs.osr.ptk.common.ast.NodeList;

public abstract class DomBackbone
        implements
            DomNode
{
	private static final long serialVersionUID = 1L;
	
	private final AstNode astNode;
	
	private DomBackbone parent;
	
	private DomBackbone prevSibling;
	
	private DomBackbone nextSibling;
	
	private XmlAttributeAdapter firstAttr;
	
	private DomBackbone firstChild;
	
	// =========================================================================
	
	public DomBackbone(AstNode astNode)
	{
		this.astNode = astNode;
	}
	
	// =========================================================================
	
	protected abstract NodeList getAttributeContainer();
	
	protected abstract NodeList getChildContainer();
	
	// =========================================================================
	
	protected AstNode getAstNode()
	{
		return astNode;
	}
	
	// =========================================================================
	
	protected void setParent(DomBackbone parent)
	{
		this.parent = parent;
	}
	
	protected void setPrevSibling(DomBackbone prevSibling)
	{
		this.prevSibling = prevSibling;
	}
	
	protected void setNextSibling(DomBackbone nextSibling)
	{
		this.nextSibling = nextSibling;
	}
	
	public boolean isLinked()
	{
		return parent != null;
	}
	
	public void unlink()
	{
		parent = null;
		
		if (prevSibling != null)
			prevSibling.nextSibling = nextSibling;
		if (nextSibling != null)
			nextSibling.prevSibling = prevSibling;
		
		prevSibling = null;
		nextSibling = null;
	}
	
	public void link(DomBackbone parent, DomBackbone prevSibling, DomBackbone nextSibling)
	{
		if (isLinked())
			throw new IllegalStateException(
			        "Node is still child of another DOM node.");
		
		this.parent = parent;
		
		this.prevSibling = prevSibling;
		if (prevSibling != null)
		{
			if (prevSibling.nextSibling != nextSibling)
				throw new IllegalStateException(
				        "DOM sibling chain inconsistent.");
			
			prevSibling.nextSibling = this;
		}
		
		this.nextSibling = nextSibling;
		if (nextSibling != null)
		{
			if (nextSibling.prevSibling != prevSibling)
				throw new IllegalStateException(
				        "DOM sibling chain inconsistent.");
			
			nextSibling.prevSibling = this;
		}
	}
	
	// =========================================================================
	
	@Override
	public final DomBackbone getParent()
	{
		return parent;
	}
	
	@Override
	public final DomBackbone getPrevSibling()
	{
		return prevSibling;
	}
	
	@Override
	public final DomBackbone getNextSibling()
	{
		return nextSibling;
	}
	
	// =========================================================================
	
	@Override
	public final DomNode cloneNode()
	{
		try
		{
			return (DomNode) this.clone();
		}
		catch (CloneNotSupportedException e)
		{
			throw new AssertionError();
		}
	}
	
	@Override
	protected Object clone() throws CloneNotSupportedException
	{
		DomBackbone node = (DomBackbone) super.clone();
		node.parent = null;
		node.nextSibling = null;
		node.prevSibling = null;
		return node;
	}
	
	// =========================================================================
	
	@Override
	public String getText()
	{
		return null;
	}
	
	@Override
	public String getValue()
	{
		return null;
	}
	
	@Override
	public void appendText(String text) throws UnsupportedOperationException
	{
		throw new UnsupportedOperationException();
	}
	
	@Override
	public String deleteText(int from, int length) throws UnsupportedOperationException, IndexOutOfBoundsException
	{
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void insertText(int at, String text) throws UnsupportedOperationException, IndexOutOfBoundsException
	{
		throw new UnsupportedOperationException();
	}
	
	@Override
	public String replaceText(String text) throws UnsupportedOperationException
	{
		throw new UnsupportedOperationException();
	}
	
	@Override
	public String replaceText(int from, int length, String text) throws UnsupportedOperationException, IndexOutOfBoundsException
	{
		throw new UnsupportedOperationException();
	}
	
	// =========================================================================
	
	@Override
	public boolean supportsAttributes()
	{
		return getAttributeContainer() != null;
	}
	
	@Override
	public Collection<DomAttribute> getAttributes()
	{
		return new SiblingCollection<DomAttribute>(firstAttr);
	}
	
	@Override
	public String getAttribute(String name)
	{
		final DomAttribute attributeNode = getAttributeNode(name);
		if (attributeNode == null)
			return null;
		return attributeNode.getValue();
	}
	
	@Override
	public XmlAttributeAdapter getAttributeNode(String name)
	{
		if (name != null)
		{
			DomAttribute i = firstAttr;
			while (i != null)
			{
				if (i.getName().equalsIgnoreCase(name))
					return (XmlAttributeAdapter) i;
				
				i = (DomAttribute) i.getNextSibling();
			}
		}
		return null;
	}
	
	@Override
	public XmlAttributeAdapter removeAttribute(String name) throws UnsupportedOperationException
	{
		if (!supportsAttributes())
			throw new UnsupportedOperationException();
		
		if (name == null)
			throw new IllegalArgumentException("Argument `name' is null.");
		
		XmlAttributeAdapter remove = getAttributeNode(name);
		if (remove == null)
			return null;
		
		removeAttribute(remove);
		
		return remove;
	}
	
	@Override
	public void removeAttributeNode(DomAttribute attr) throws UnsupportedOperationException, IllegalArgumentException
	{
		if (!supportsAttributes())
			throw new UnsupportedOperationException();
		
		if (attr == null)
			throw new IllegalArgumentException("Argument `attr' is null.");
		
		if (attr.getParent() != this)
			throw new IllegalArgumentException("Given attribute `attr' is not an attribute of this XML element.");
		
		removeAttribute((XmlAttributeAdapter) attr);
	}
	
	@Override
	public XmlAttributeAdapter setAttribute(String name, String value) throws UnsupportedOperationException
	{
		if (!supportsAttributes())
			throw new UnsupportedOperationException();
		
		if (name == null || value == null)
			throw new IllegalArgumentException("Argument `name' and/or `value' is null.");
		
		XmlAttributeAdapter attr = new XmlAttributeAdapter(name, value);
		
		return setAttributeNode(attr);
	}
	
	@Override
	public XmlAttributeAdapter setAttributeNode(DomAttribute attr) throws UnsupportedOperationException, IllegalArgumentException
	{
		if (!supportsAttributes())
			throw new UnsupportedOperationException();
		
		if (attr == null)
			throw new IllegalArgumentException("Argument `attr' is null.");
		
		if (!(attr instanceof XmlAttributeAdapter))
			throw new IllegalArgumentException(
			        "Expected argument `attr' be of type " + XmlAttributeAdapter.class.getName());
		
		final XmlAttributeAdapter newAttr = (XmlAttributeAdapter) attr;
		if (newAttr.isLinked())
			throw new IllegalStateException(
			        "Given attribute `attr' is still attribute of another DOM node.");
		
		XmlAttributeAdapter oldAttr = getAttributeNode(attr.getName());
		
		replaceAttribute(oldAttr, newAttr);
		
		return oldAttr;
	}
	
	// =========================================================================
	
	private void removeAttribute(XmlAttributeAdapter remove)
	{
		// remove from DOM
		if (remove == firstAttr)
			firstAttr = (XmlAttributeAdapter) remove.getNextSibling();
		remove.unlink();
		
		// remove from AST
		// the ast can contain an attribute with the same name multiple times!
		Iterator<AstNode> i = getAttributeContainer().iterator();
		while (i.hasNext())
		{
			AstNode node = i.next();
			// there could also be garbage nodes ...
			if (!node.isNodeType(AstNodeTypes.NT_XML_ATTRIBUTE))
				continue;
			
			XmlAttribute attr = (XmlAttribute) node;
			if (attr.getName().equalsIgnoreCase(remove.getName()))
			{
				i.remove();
				// the dom will always refer to the last attribute
				if (attr == remove.getAstNode())
				{
					i = null;
					break;
				}
			}
		}
		if (i != null)
			throw new AssertionError();
	}
	
	private void replaceAttribute(final XmlAttributeAdapter oldAttr, final XmlAttributeAdapter newAttr)
	{
		// replace in DOM
		DomBackbone prev = null;
		DomBackbone next = null;
		if (oldAttr != null)
		{
			prev = oldAttr.getPrevSibling();
			next = oldAttr.getNextSibling();
			oldAttr.unlink();
		}
		else if (firstAttr != null)
		{
			prev = firstAttr;
			while (prev.getNextSibling() != null)
				prev = (DomBackbone) prev.getNextSibling();
		}
		
		newAttr.link(this, prev, next);
		if (oldAttr == firstAttr)
			firstAttr = newAttr;
		
		// replace in AST
		// the ast can contain an attribute with the same name multiple times!
		
		String name = oldAttr.getName();
		XmlAttribute oldAstNode = oldAttr.getAstNode();
		XmlAttribute newAstNode = newAttr.getAstNode();
		
		ListIterator<AstNode> i = getAttributeContainer().listIterator();
		while (i.hasNext())
		{
			AstNode node = i.next();
			if (!node.isNodeType(AstNodeTypes.NT_XML_ATTRIBUTE))
				continue;
			
			XmlAttribute attr = (XmlAttribute) node;
			if (attr.getName().equalsIgnoreCase(name))
			{
				if (attr == oldAstNode)
				{
					i.set(newAstNode);
					i = null;
					
					Object rtd = oldAstNode.getAttribute("RTD");
					if (rtd != null)
						newAstNode.setAttribute("RTD", rtd);
					
					// The node the DOM refers to comes always last
					break;
				}
				else
				{
					i.remove();
				}
			}
		}
		if (i != null)
			throw new AssertionError();
	}
	
	// =========================================================================
	
	@Override
	public boolean hasChildNodes()
	{
		return firstChild != null;
	}
	
	@Override
	public Collection<DomNode> childNodes()
	{
		return new SiblingCollection<DomNode>(firstChild);
	}
	
	@Override
	public DomNode getFirstChild()
	{
		return firstChild;
	}
	
	@Override
	public DomNode getLastChild()
	{
		DomNode child = firstChild;
		if (child == null)
			return null;
		while (child.getNextSibling() != null)
			child = child.getNextSibling();
		return child;
	}
	
	@Override
	public void appendChild(DomNode child) throws UnsupportedOperationException
	{
		if (getChildContainer() == null)
			throw new UnsupportedOperationException();
		
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
		newChild.link(this, (DomBackbone) getLastChild(), null);
		if (firstChild == null)
			firstChild = newChild;
		
		// insert into AST
		getChildContainer().add(newChild.getAstNode());
	}
	
	@Override
	public void insertBefore(DomNode before, DomNode child) throws UnsupportedOperationException, IllegalArgumentException
	{
		if (getChildContainer() == null)
			throw new UnsupportedOperationException();
		
		if (before == null || child == null)
			throw new IllegalArgumentException("Argument `before' and/or `child' is null.");
		
		if (!(child instanceof DomBackbone))
			throw new IllegalArgumentException(
			        "Expected argument `child' be of type " + DomBackbone.class.getName());
		
		final DomBackbone newChild = (DomBackbone) child;
		if (newChild.isLinked())
			throw new IllegalStateException(
			        "Given argument `child' is still child of another DOM node.");
		
		if (before.getParent() != this)
			throw new IllegalArgumentException("Given node `before' is not a child of this XML element.");
		DomBackbone b = (DomBackbone) before;
		
		// insert into DOM
		if (before == firstChild)
		{
			newChild.link(this, null, firstChild);
			firstChild = newChild;
		}
		else
		{
			newChild.link(this, b.getPrevSibling(), b);
		}
		
		// insert into AST
		ListIterator<AstNode> i = getChildContainer().listIterator();
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
	
	@Override
	public void removeChild(DomNode child) throws UnsupportedOperationException
	{
		if (getChildContainer() == null)
			throw new UnsupportedOperationException();
		
		if (child == null)
			throw new IllegalArgumentException("Argument `child' is null.");
		
		if (!(child instanceof DomBackbone))
			throw new IllegalArgumentException(
			        "Expected argument `child' be of type " + DomBackbone.class.getName());
		
		if (child.getParent() != this)
			throw new IllegalArgumentException("Given node `child' is not a child of this XML element.");
		DomBackbone remove = (DomBackbone) child;
		
		removeChild(remove);
	}
	
	@Override
	public void replaceChild(DomNode search, DomNode replace) throws UnsupportedOperationException
	{
		if (getChildContainer() == null)
			throw new UnsupportedOperationException();
		
		if (search == null || replace == null)
			throw new IllegalArgumentException("Argument `search' and/or `replace' is null.");
		
		if (!(replace instanceof DomBackbone))
			throw new IllegalArgumentException(
			        "Expected argument `replace' be of type " + DomBackbone.class.getName());
		
		final DomBackbone newChild = (DomBackbone) replace;
		if (newChild.isLinked())
			throw new IllegalStateException(
			        "Given node `replace' is still child of another DOM node.");
		
		if (search.getParent() != this)
			throw new IllegalArgumentException("Given node `search' is not a child of this XML element.");
		DomBackbone oldChild = (DomBackbone) search;
		
		replaceChild(oldChild, newChild);
	}
	
	// =========================================================================
	
	private void removeChild(DomBackbone remove)
	{
		// remove from DOM
		if (remove == firstChild)
			firstChild = remove.getNextSibling();
		remove.unlink();
		
		// remove from AST
		Iterator<AstNode> i = getChildContainer().iterator();
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
	
	private void replaceChild(final DomBackbone oldChild, final DomBackbone newChild)
	{
		// replace in DOM
		DomBackbone prev = oldChild.getPrevSibling();
		DomBackbone next = oldChild.getNextSibling();
		oldChild.unlink();
		
		newChild.link(this, prev, next);
		if (oldChild == firstChild)
			firstChild = newChild;
		
		// replace in AST
		AstNode oldAstNode = oldChild.getAstNode();
		AstNode newAstNode = newChild.getAstNode();
		
		ListIterator<AstNode> i = getChildContainer().listIterator();
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
