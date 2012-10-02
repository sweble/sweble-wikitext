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
import org.sweble.wikitext.parser.nodes.WtList;

public abstract class ChildManagerBase
{
	private static final ChildManagerBase emptyManager = new EmptyChildManager();
	
	public static ChildManagerBase emptyManager()
	{
		return emptyManager;
	}
	
	private static ChildManagerBase createManager()
	{
		return new ChildManager();
	}
	
	// =========================================================================
	
	public abstract boolean hasChildNodes();
	
	public abstract Collection<WomNode> childNodes();
	
	public abstract WomNode getFirstChild();
	
	public abstract WomNode getLastChild();
	
	public abstract void appendChild(
			WomNode child,
			WomBackbone parent,
			WtList childContainer);
	
	public abstract void insertBefore(
			WomNode before,
			WomNode child,
			WomBackbone parent,
			WtList childContainer) throws IllegalArgumentException;
	
	public abstract void removeChild(
			WomNode child,
			WomBackbone parent,
			WtList childContainer);
	
	public abstract void replaceChild(
			WomNode search,
			WomNode replace,
			WomBackbone parent,
			WtList childContainer);
	
	// =========================================================================
	
	public abstract boolean isEmptyManager();
	
	public abstract ChildManagerBase modifyable();
	
	// =========================================================================
	
	public static final class EmptyChildManager
			extends
				ChildManagerBase
	{
		public boolean isEmptyManager()
		{
			return true;
		}
		
		public ChildManagerBase modifyable()
		{
			return ChildManagerBase.createManager();
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
				WtList childContainer)
		{
			unsupported();
		}
		
		@Override
		public void insertBefore(
				WomNode before,
				WomNode child,
				WomBackbone parent,
				WtList childContainer) throws IllegalArgumentException
		{
			unsupported();
		}
		
		@Override
		public void removeChild(
				WomNode child,
				WomBackbone parent,
				WtList childContainer)
		{
			unsupported();
		}
		
		@Override
		public void replaceChild(
				WomNode search,
				WomNode replace,
				WomBackbone parent,
				WtList childContainer)
		{
			unsupported();
		}
		
		private static void unsupported()
		{
			throw new UnsupportedOperationException("Cannot modify an instance of EmptyChildManager!");
		}
	}
	
	// =========================================================================
	
	public static final class ChildManager
			extends
				ChildManagerBase
	{
		private WomBackbone firstChild;
		
		// =====================================================================
		
		public boolean isEmptyManager()
		{
			return false;
		}
		
		public ChildManagerBase modifyable()
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
		
		/*
		 * @startuml ChildManager-appendChild.svg
		 * participant "<u>newChild : WomBackbone</u>" as newChild
		 * participant "<u>: ChildManager</u>" as CM
		 * participant "<u>parent : WomBackbone</u>" as parent
		 * 
		 * [-> CM: appendChild()
		 * activate CM
		 *   
		 *   CM -> parent: acceptsChild()
		 *   activate parent
		 *     alt doesn't accept child
		 *       [<-- parent: throws
		 *     end
		 *   deactivate parent
		 *   
		 *   CM -> newChild: acceptsParent()
		 *   activate newChild
		 *     alt doesn't accept parent
		 *       [<-- newChild: throws
		 *     end
		 *   deactivate newChild
		 *   
		 *   CM -> newChild: link()
		 *   
		 *   alt childContainer != null
		 *     CM -> parent: appendToAst()
		 *   end
		 *   
		 * deactivate CM
		 * @enduml
		 */
		public void appendChild(
				WomNode child,
				WomBackbone parent,
				WtList childContainer)
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
				parent.appendToAst(childContainer, newChild.getAstNode());
		}
		
		/*
		 * @startuml ChildManager-insertBefore.svg
		 * participant "<u>child : WomBackbone</u>" as newChild
		 * participant "<u>: ChildManager</u>" as CM
		 * participant "<u>parent : WomBackbone</u>" as parent
		 * 
		 * [-> CM: insertBefore()
		 * activate CM
		 *   
		 *   CM -> parent: acceptsChild()
		 *   activate parent
		 *     alt doesn't accept child
		 *       [<-- parent: throws
		 *     end
		 *   deactivate parent
		 *   
		 *   CM -> newChild: acceptsParent()
		 *   activate newChild
		 *     alt doesn't accept parent
		 *       [<-- newChild: throws
		 *     end
		 *   deactivate newChild
		 *   
		 *   CM -> newChild: link()
		 *   
		 *   alt childContainer != null
		 *     CM -> parent: insertIntoAst()
		 *   end
		 *   
		 * deactivate CM
		 * @enduml
		 */
		public void insertBefore(
				WomNode before,
				WomNode child,
				WomBackbone parent,
				WtList childContainer) throws IllegalArgumentException
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
			WomBackbone p = (WomBackbone) before;
			
			parent.acceptsChild(newChild);
			newChild.acceptsParent(parent);
			
			// insert into WOM
			newChild.link(parent, p.getPrevSibling(), p);
			if (p == firstChild)
				firstChild = newChild;
			
			// insert into AST
			if (childContainer != null)
				parent.insertIntoAst(childContainer, newChild.getAstNode(), p.getAstNode());
		}
		
		/*
		 * @startuml ChildManager-removeChild.svg
		 * participant "<u>child : WomBackbone</u>" as child
		 * participant "<u>: ChildManager</u>" as CM
		 * participant "<u>parent : WomBackbone</u>" as parent
		 * 
		 * [-> CM: removeChild()
		 * activate CM
		 *   
		 *   CM -> child: childAllowsRemoval()
		 *   activate child
		 *     alt doesn't allow removal
		 *       [<-- parent: throws
		 *     end
		 *   deactivate child
		 *   
		 *   CM -> child: unlink()
		 *   
		 *   alt childContainer != null
		 *     CM -> parent: removeFromAst()
		 *   end
		 *   
		 * deactivate CM
		 * @enduml
		 */
		public void removeChild(
				WomNode child,
				WomBackbone parent,
				WtList childContainer)
		{
			if (child == null)
				throw new IllegalArgumentException("Argument `child' is null.");
			
			WomBackbone remove = Toolbox.expectType(
					WomBackbone.class, child, "child");
			
			if (child.getParent() != parent)
				throw new IllegalArgumentException("Given node `child' is not a child of this node.");
			
			remove.childAllowsRemoval(parent);
			
			// remove from WOM
			if (remove == firstChild)
				firstChild = remove.getNextSibling();
			remove.unlink();
			
			// remove from AST
			if (childContainer != null)
				parent.removeFromAst(childContainer, remove.getAstNode());
		}
		
		/*
		 * @startuml ChildManager-replaceChild.svg
		 * participant "<u>search : WomBackbone</u>" as search
		 * participant "<u>replace : WomBackbone</u>" as replace
		 * participant "<u>: ChildManager</u>" as CM
		 * participant "<u>parent : WomBackbone</u>" as parent
		 * 
		 * [-> CM: replaceChild()
		 * activate CM
		 *   
		 *   CM -> search: childAllowsRemoval()
		 *   activate search
		 *     alt doesn't allow removal
		 *       [<-- parent: throws
		 *     end
		 *   deactivate search
		 *   
		 *   CM -> parent: acceptsChild()
		 *   activate parent
		 *     alt doesn't accept child
		 *       [<-- parent: throws
		 *     end
		 *   deactivate parent
		 *   
		 *   CM -> replace: acceptsParent()
		 *   activate replace
		 *     alt doesn't accept parent
		 *       [<-- replace: throws
		 *     end
		 *   deactivate replace
		 *   
		 *   CM -> search: unlink()
		 *   CM -> replace: link()
		 *   
		 *   alt childContainer != null
		 *     CM -> parent: replaceInAst()
		 *   end
		 *   
		 * deactivate CM
		 * @enduml
		 */
		public void replaceChild(
				WomNode search,
				WomNode replace,
				WomBackbone parent,
				WtList childContainer)
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
			
			oldChild.childAllowsRemoval(parent);
			
			parent.acceptsChild(newChild);
			newChild.acceptsParent(parent);
			
			// replace in WOM
			WomBackbone prev = oldChild.getPrevSibling();
			WomBackbone next = oldChild.getNextSibling();
			oldChild.unlink();
			
			newChild.link(parent, prev, next);
			if (oldChild == firstChild)
				firstChild = newChild;
			
			// replace in AST
			parent.replaceInAst(childContainer, oldChild, newChild);
		}
	}
}
