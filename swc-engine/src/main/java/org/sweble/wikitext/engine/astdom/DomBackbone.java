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
import java.util.Collections;

import org.sweble.wikitext.engine.wom.WomAttribute;
import org.sweble.wikitext.engine.wom.WomNode;

import de.fau.cs.osr.ptk.common.ast.AstNode;

public abstract class DomBackbone
        implements
            WomNode
{
	private static final long serialVersionUID = 1L;
	
	private final AstNode astNode;
	
	private DomBackbone parent;
	
	private DomBackbone prevSibling;
	
	private DomBackbone nextSibling;
	
	// =========================================================================
	
	public DomBackbone(AstNode astNode)
	{
		this.astNode = astNode;
	}
	
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
	public final WomNode cloneNode()
	{
		try
		{
			return (WomNode) this.clone();
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
		return false;
	}
	
	@Override
	public Collection<WomAttribute> getAttributes()
	{
		return Collections.emptyList();
	}
	
	@Override
	public String getAttribute(String name)
	{
		return null;
	}
	
	@Override
	public XmlAttributeAdapter getAttributeNode(String name)
	{
		return null;
	}
	
	@Override
	public XmlAttributeAdapter removeAttribute(String name) throws UnsupportedOperationException
	{
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void removeAttributeNode(WomAttribute attr) throws UnsupportedOperationException, IllegalArgumentException
	{
		throw new UnsupportedOperationException();
	}
	
	@Override
	public XmlAttributeAdapter setAttribute(String name, String value) throws UnsupportedOperationException
	{
		throw new UnsupportedOperationException();
	}
	
	@Override
	public XmlAttributeAdapter setAttributeNode(WomAttribute attr) throws UnsupportedOperationException, IllegalArgumentException
	{
		throw new UnsupportedOperationException();
	}
	
	// =========================================================================
	
	@Override
	public boolean hasChildNodes()
	{
		return false;
	}
	
	@Override
	public Collection<WomNode> childNodes()
	{
		return Collections.emptyList();
	}
	
	@Override
	public WomNode getFirstChild()
	{
		return null;
	}
	
	@Override
	public WomNode getLastChild()
	{
		return null;
	}
	
	@Override
	public void appendChild(WomNode child) throws UnsupportedOperationException
	{
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void insertBefore(WomNode before, WomNode child) throws UnsupportedOperationException, IllegalArgumentException
	{
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void removeChild(WomNode child) throws UnsupportedOperationException
	{
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void replaceChild(WomNode search, WomNode replace) throws UnsupportedOperationException
	{
		throw new UnsupportedOperationException();
	}
}
