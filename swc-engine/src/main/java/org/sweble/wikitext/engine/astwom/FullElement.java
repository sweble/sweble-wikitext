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

import org.sweble.wikitext.engine.astwom.adapters.CategoryAdapter;
import org.sweble.wikitext.engine.astwom.adapters.FullElementContentType;
import org.sweble.wikitext.engine.wom.WomNode;
import org.sweble.wikitext.parser.nodes.WtNode;
import org.sweble.wikitext.parser.nodes.WtNodeList;

public abstract class FullElement
		extends
			AttributeSupportingElement
{
	private static final long serialVersionUID = 1L;
	
	// =========================================================================
	
	public FullElement(WtNode astNode)
	{
		super(astNode);
	}
	
	// =========================================================================
	
	/**
	 * Get the child manager.
	 * 
	 * This method is used by child getters which won't fail when called on an
	 * element that doesn't support children.
	 * 
	 * @return The child manager or <code>null</code> if the element never
	 *         supports children in any AST incarnation.
	 */
	protected ChildManagerBase getChildManager()
	{
		return null;
	}
	
	protected void setChildManager(ChildManagerBase childManager)
	{
		throw new InternalError();
	}
	
	/*
	 * @startuml FullElement-getChildManagerForModificationOrFail.svg
	 * [-> FullElement: getChildManagerForModificationOrFail()
	 * activate FullElement
	 * 
	 *   FullElement -> "? extends FullElement": getChildManager()
	 *   activate "? extends FullElement"
	 *     FullElement <-- "? extends FullElement": childManager
	 *   deactivate "? extends FullElement"
	 *   
	 *   FullElement -> "ChildManagerBase": modifyable()
	 *   activate ChildManagerBase
	 *     FullElement <-- "ChildManagerBase": modifyableChildManager
	 *   deactivate ChildManagerBase
	 *   
	 *   alt childManager != modifyableChildManager
	 *     FullElement -> "? extends FullElement": setChildManager(\n\tmodifyableChildManager)
	 *   end
	 *   
	 *   [<-- FullElement: modifyableChildManager
	 * deactivate FullElement
	 * @enduml
	 */
	/**
	 * This method is similar to getChildManager() but fails with an exception
	 * if the element doesn't support children. It is used by children modifying
	 * methods.
	 * 
	 * @return The child manager.
	 * @throws UnsupportedOperationException
	 *             Thrown if the element never supports children in any AST
	 *             incarnation.
	 */
	protected final ChildManagerBase getChildManagerForModificationOrFail()
	{
		ChildManagerBase childManager = getChildManager();
		ChildManagerBase modifyable = childManager.modifyable();
		if (childManager != modifyable)
			setChildManager(modifyable);
		return modifyable;
	}
	
	/**
	 * Returns the AST child container node. If the AST element that backs this
	 * element doesn't support children, <code>null</code> is returned.
	 * 
	 * @return The AST child container node or <code>null</code> if the AST
	 *         element doesn't support children.
	 */
	public WtNodeList getAstChildContainer()
	{
		return null;
	}
	
	/**
	 * If AST node that backs this element doesn't support children this method
	 * can be called to convert the underlying AST node into an equivalent node
	 * that does support children. If such a conversion is not possible
	 * <code>null</code> is returned.
	 * 
	 * @return The AST child container node of the new, children supporting AST
	 *         node.
	 * @throws UnsupportedOperationException
	 *             Thrown if no conversion is possible.
	 */
	protected WtNodeList addAstChildrenSupport()
	{
		return null;
	}
	
	protected final WtNodeList getAstChildContainerOrAddSupport()
	{
		WtNodeList childContainer = getAstChildContainer();
		if (childContainer == null)
			childContainer = addAstChildrenSupport();
		return childContainer;
	}
	
	// =========================================================================
	
	@Override
	public final boolean supportsChildren()
	{
		return getChildManager() != null;
	}
	
	@Override
	public final boolean hasChildNodes()
	{
		ChildManagerBase children = getChildManager();
		if (children == null)
			return false;
		return children.hasChildNodes();
	}
	
	@Override
	public final Collection<WomNode> childNodes()
	{
		ChildManagerBase children = getChildManager();
		if (children == null)
			return Collections.emptyList();
		return children.childNodes();
	}
	
	@Override
	public final WomNode getFirstChild()
	{
		ChildManagerBase children = getChildManager();
		if (children == null)
			return null;
		return children.getFirstChild();
	}
	
	@Override
	public final WomNode getLastChild()
	{
		ChildManagerBase children = getChildManager();
		if (children == null)
			return null;
		return children.getLastChild();
	}
	
	/*
	 * @startuml FullElement-appendChild.svg
	 * [-> FullElement: appendChild()
	 * activate FullElement
	 * 
	 *   FullElement -> FullElement: getChildManagerForModificationOrFail()
	 *   activate FullElement
	 *     FullElement <-- FullElement: childManager
	 *   deactivate FullElement
	 * 
	 *   FullElement -> ChildManager: appendChild()
	 * 
	 * deactivate FullElement
	 * @enduml
	 */
	@Override
	public final void appendChild(WomNode child)
	{
		ChildManagerBase children = getChildManagerForModificationOrFail();
		children.appendChild(child, this, getAstChildContainerOrAddSupport());
	}
	
	@Override
	public final void insertBefore(WomNode before, WomNode child)
			throws IllegalArgumentException
	{
		ChildManagerBase children = getChildManagerForModificationOrFail();
		children.insertBefore(before, child, this, getAstChildContainerOrAddSupport());
	}
	
	@Override
	public final void removeChild(WomNode child)
	{
		// Don't use getAstChildContainerOrAddSupport()!
		// If the given child is not a child of this node then the caller is
		// removing something that's not there and the call to removeChild will
		// eventually fail anyway.
		ChildManagerBase children = getChildManagerForModificationOrFail();
		children.removeChild(child, this, getAstChildContainer());
	}
	
	@Override
	public final void replaceChild(WomNode search, WomNode replace)
	{
		// Don't use getAstChildContainerOrAddSupport()!
		// If the given child is not a child of this node then the caller is
		// replacing something that's not there and the call to replaceChild 
		// will eventually fail anyway.
		ChildManagerBase children = getChildManagerForModificationOrFail();
		children.replaceChild(search, replace, this, getAstChildContainer());
	}
	
	// =========================================================================
	
	@Override
	protected void appendToAst(WtNodeList container, WtNode child)
	{
		Toolbox.appendAstNode(container, child);
	}
	
	@Override
	protected void insertIntoAst(
			WtNodeList container,
			WtNode newChild,
			WtNode prevChild)
	{
		Toolbox.insertAstNode(container, newChild, prevChild);
	}
	
	@Override
	protected void replaceInAst(
			WtNodeList container,
			WomBackbone oldNode,
			WomBackbone newNode)
	{
		Toolbox.replaceAstNode(
				container,
				oldNode.getAstNode(),
				newNode.getAstNode());
		
		CategoryAdapter.reAttachCategoryNodesToPage(oldNode.getAstNode());
	}
	
	@Override
	protected void removeFromAst(WtNodeList container, WtNode removeNode)
	{
		Toolbox.removeAstNode(container, removeNode);
		
		CategoryAdapter.reAttachCategoryNodesToPage(removeNode);
	}
	
	// =========================================================================
	
	protected final void addContent(
			FullElementContentType contentType,
			AstToWomNodeFactory factory,
			WtNodeList content)
	{
		addContent(contentType, factory, content, null, null);
	}
	
	protected final void addContent(
			FullElementContentType contentType,
			AstToWomNodeFactory factory,
			WtNodeList content,
			WtNode from,
			WtNode to)
	{
		if (content.isEmpty())
			return;
		
		ChildManagerBase manager = getChildManagerForModificationOrFail();
		contentType.parseContent(factory, manager, this, content, from, to);
	}
}
