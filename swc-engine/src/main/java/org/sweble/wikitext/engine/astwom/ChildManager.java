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
import java.util.Collections;

import org.sweble.wikitext.engine.wom.WomNode;

import de.fau.cs.osr.ptk.common.ast.AstNode;
import de.fau.cs.osr.ptk.common.ast.NodeList;

public abstract class ChildManager
{
	private static final ChildManager emptyManager = new EmptyChildManager();
	
	public static ChildManager emptyManager()
	{
		return emptyManager;
	}
	
	private static ChildManager createManager()
	{
		return new ChildManagerThingy();
	}
	
	// =========================================================================
	
	public abstract boolean hasChildNodes();
	
	public abstract Collection<WomNode> childNodes();
	
	public abstract WomNode getFirstChild();
	
	public abstract WomNode getLastChild();
	
	public abstract void appendChild(
			WomNode child,
			WomBackbone parent,
			NodeList childContainer);
	
	public abstract void insertBefore(
			WomNode before,
			WomNode child,
			WomBackbone parent,
			NodeList childContainer) throws IllegalArgumentException;
	
	public abstract void removeChild(
			WomNode child,
			WomBackbone parent,
			NodeList childContainer);
	
	public abstract void replaceChild(
			WomNode search,
			WomNode replace,
			WomBackbone parent,
			NodeList childContainer);
	
	// =========================================================================
	
	public abstract boolean isEmptyManager();
	
	public abstract ChildManager modifyable();
	
	// =========================================================================
	
	public static final class EmptyChildManager
			extends
				ChildManager
	{
		public boolean isEmptyManager()
		{
			return true;
		}
		
		public ChildManager modifyable()
		{
			return ChildManager.createManager();
		}
		
		// =====================================================================
		
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
		public void appendChild(
				WomNode child,
				WomBackbone parent,
				NodeList childContainer)
		{
			unsupported();
		}
		
		@Override
		public void insertBefore(
				WomNode before,
				WomNode child,
				WomBackbone parent,
				NodeList childContainer) throws IllegalArgumentException
		{
			unsupported();
		}
		
		@Override
		public void removeChild(
				WomNode child,
				WomBackbone parent,
				NodeList childContainer)
		{
			unsupported();
		}
		
		@Override
		public void replaceChild(
				WomNode search,
				WomNode replace,
				WomBackbone parent,
				NodeList childContainer)
		{
			unsupported();
		}
		
		private static void unsupported()
		{
			throw new UnsupportedOperationException("Cannot modify an instance of EmptyChildManager!");
		}
	}
	
	// =========================================================================
	
	public static final class ChildManagerThingy
			extends
				ChildManager
	{
		private WomBackbone firstChild;
		
		// =====================================================================
		
		public boolean isEmptyManager()
		{
			return false;
		}
		
		public ChildManager modifyable()
		{
			return this;
		}
		
		// =====================================================================
		
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
		
		public void appendChild(
				WomNode child,
				WomBackbone parent,
				NodeList childContainer)
		{
			if (child == null)
				throw new IllegalArgumentException("Argument `child' is null.");
			
			final WomBackbone newChild =
					Toolbox.expectType(WomBackbone.class, child, "child");
			
			if (newChild.isLinked())
				throw new IllegalStateException(
						"Given node `child' is still child of another WOM node.");
			
			parent.acceptsChild(newChild);
			newChild.acceptsParent(parent);
			
			// insert into WOM
			newChild.link(parent, (WomBackbone) getLastChild(), null);
			if (firstChild == null)
				firstChild = newChild;
			
			// insert into AST, if required
			if (childContainer != null)
				Toolbox.appendAstNode(childContainer, newChild.getAstNode());
		}
		
		public void insertBefore(
				WomNode before,
				WomNode child,
				WomBackbone parent,
				NodeList childContainer) throws IllegalArgumentException
		{
			if (before == null || child == null)
				throw new IllegalArgumentException("Argument `before' and/or `child' is null.");
			
			final WomBackbone newChild =
					Toolbox.expectType(WomBackbone.class, child, "child");
			
			if (newChild.isLinked())
				throw new IllegalStateException(
						"Given argument `child' is still child of another WOM node.");
			
			if (before.getParent() != parent)
				throw new IllegalArgumentException("Given node `before' is not a child of this node.");
			WomBackbone b = (WomBackbone) before;
			
			parent.acceptsChild(newChild);
			newChild.acceptsParent(parent);
			
			// insert into WOM
			newChild.link(parent, b.getPrevSibling(), b);
			if (b == firstChild)
				firstChild = newChild;
			
			// insert into AST
			Toolbox.insertAstNode(childContainer, newChild.getAstNode(), b.getAstNode());
		}
		
		public void removeChild(
				WomNode child,
				WomBackbone parent,
				NodeList childContainer)
		{
			if (child == null)
				throw new IllegalArgumentException("Argument `child' is null.");
			
			WomBackbone remove = Toolbox.expectType(
					WomBackbone.class, child, "child");
			
			if (child.getParent() != parent)
				throw new IllegalArgumentException("Given node `child' is not a child of this node.");
			
			removeChild(remove, childContainer);
		}
		
		public void replaceChild(
				WomNode search,
				WomNode replace,
				WomBackbone parent,
				NodeList childContainer)
		{
			if (search == null || replace == null)
				throw new IllegalArgumentException("Argument `search' and/or `replace' is null.");
			
			final WomBackbone newChild =
					Toolbox.expectType(WomBackbone.class, replace, "replace");
			
			if (newChild.isLinked())
				throw new IllegalStateException(
						"Given node `replace' is still child of another WOM node.");
			
			if (search.getParent() != parent)
				throw new IllegalArgumentException("Given node `search' is not a child of this node.");
			WomBackbone oldChild = (WomBackbone) search;
			
			parent.acceptsChild(newChild);
			newChild.acceptsParent(parent);
			
			replaceChild(oldChild, newChild, parent, childContainer);
		}
		
		// =====================================================================
		
		private void removeChild(WomBackbone remove, NodeList childContainer)
		{
			// remove from WOM
			if (remove == firstChild)
				firstChild = remove.getNextSibling();
			remove.unlink();
			
			// remove from AST
			Toolbox.removeAstNode(childContainer, remove.getAstNode());
		}
		
		private void replaceChild(
				final WomBackbone oldChild,
				final WomBackbone newChild,
				WomBackbone parent,
				NodeList childContainer)
		{
			// replace in WOM
			WomBackbone prev = oldChild.getPrevSibling();
			WomBackbone next = oldChild.getNextSibling();
			oldChild.unlink();
			
			newChild.link(parent, prev, next);
			if (oldChild == firstChild)
				firstChild = newChild;
			
			// replace in AST
			AstNode oldAstNode = oldChild.getAstNode();
			AstNode newAstNode = newChild.getAstNode();
			
			Toolbox.replaceAstNode(childContainer, oldAstNode, newAstNode);
		}
	}
}
