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

import java.util.Collection;

import org.sweble.wikitext.engine.wom.WomNode;
import org.sweble.wikitext.engine.wom.WomNodeType;

import de.fau.cs.osr.ptk.common.ast.AstNode;

public abstract class CustomChildrenElement
        extends
            AttributeSupportingElement
{
	private static final long serialVersionUID = 1L;
	
	// =========================================================================
	
	public CustomChildrenElement(AstNode astNode)
	{
		super(astNode);
	}
	
	// =========================================================================
	
	@Override
	public WomNodeType getNodeType()
	{
		return WomNodeType.ELEMENT;
	}
	
	// =========================================================================
	
	@Override
	public final boolean supportsChildren()
	{
		return true;
	}
	
	@Override
	public final boolean hasChildNodes()
	{
		return getFirstChild() != null;
	}
	
	@Override
	public final Collection<WomNode> childNodes()
	{
		return new SiblingCollection<WomNode>(getFirstChild());
	}
	
	@Override
	public final void appendChild(WomNode child) throws UnsupportedOperationException
	{
		throw new UnsupportedOperationException("This node has a fixed number of children.");
	}
	
	@Override
	public final void insertBefore(WomNode before, WomNode child) throws UnsupportedOperationException, IllegalArgumentException
	{
		throw new UnsupportedOperationException("This node has a fixed number of children.");
	}
	
	@Override
	public final void removeChild(WomNode child) throws UnsupportedOperationException, IllegalArgumentException
	{
		throw new UnsupportedOperationException("This node has a fixed number of children.");
	}
	
	@Override
	public final void replaceChild(WomNode search, WomNode replace) throws UnsupportedOperationException, IllegalArgumentException
	{
		if (search == null || replace == null)
			throw new IllegalArgumentException("Argument `search' and/or `replace' is null.");
		
		final WomBackbone newChild =
		        Toolbox.expectType(WomBackbone.class, replace, "replace");
		
		if (newChild.isLinked())
			throw new IllegalStateException(
			        "Given node `replace' is still child of another WOM node.");
		
		if (search.getParent() != this)
			throw new IllegalArgumentException("Given node `search' is not a child of this node.");
		WomBackbone oldChild = (WomBackbone) search;
		
		replaceChildImpl(newChild, oldChild);
	}
	
	// =========================================================================
	
	protected void replaceChildImpl(WomBackbone newChild, WomBackbone oldChild)
	{
		throw new UnsupportedOperationException("This node has a fixed number of children.");
	}
}
