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
import java.util.Iterator;

import org.sweble.wikitext.engine.astwom.adapters.TextAdapter;
import org.sweble.wikitext.engine.wom.WomNode;
import org.sweble.wikitext.lazy.AstNodeTypes;

import de.fau.cs.osr.ptk.common.ast.AstNode;
import de.fau.cs.osr.ptk.common.ast.NodeList;
import de.fau.cs.osr.ptk.common.ast.Text;
import de.fau.cs.osr.utils.StringUtils;

public abstract class FullElement
		extends
			AttributeSupportingElement
{
	private static final long serialVersionUID = 1L;
	
	// =========================================================================
	
	public FullElement(AstNode astNode)
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
	protected ChildManager getChildManager()
	{
		return null;
	}
	
	protected void setChildManager(ChildManager childManager)
	{
		throw new InternalError();
	}
	
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
	protected final ChildManager getChildManagerForModificationOrFail()
	{
		ChildManager childManager = getChildManager();
		ChildManager modifyable = childManager.modifyable();
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
	public NodeList getAstChildContainer()
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
	protected NodeList addAstChildrenSupport()
	{
		return null;
	}
	
	protected final NodeList getAstChildContainerOrAddSupport()
	{
		NodeList childContainer = getAstChildContainer();
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
		ChildManager children = getChildManager();
		if (children == null)
			return false;
		return children.hasChildNodes();
	}
	
	@Override
	public final Collection<WomNode> childNodes()
	{
		ChildManager children = getChildManager();
		if (children == null)
			return Collections.emptyList();
		return children.childNodes();
	}
	
	@Override
	public final WomNode getFirstChild()
	{
		ChildManager children = getChildManager();
		if (children == null)
			return null;
		return children.getFirstChild();
	}
	
	@Override
	public final WomNode getLastChild()
	{
		ChildManager children = getChildManager();
		if (children == null)
			return null;
		return children.getLastChild();
	}
	
	@Override
	public final void appendChild(WomNode child)
	{
		ChildManager children = getChildManagerForModificationOrFail();
		children.appendChild(child, this, getAstChildContainerOrAddSupport());
	}
	
	@Override
	public final void insertBefore(WomNode before, WomNode child)
			throws IllegalArgumentException
	{
		ChildManager children = getChildManagerForModificationOrFail();
		children.insertBefore(before, child, this, getAstChildContainerOrAddSupport());
	}
	
	@Override
	public final void removeChild(WomNode child)
	{
		// Don't use getAstChildContainerOrAddSupport()!
		// If the given child is not a child of this node then the caller is
		// removing something that's not there and the call to removeChild will
		// eventually fail anyway.
		ChildManager children = getChildManagerForModificationOrFail();
		children.removeChild(child, this, getAstChildContainer());
	}
	
	@Override
	public final void replaceChild(WomNode search, WomNode replace)
	{
		// Don't use getAstChildContainerOrAddSupport()!
		// If the given child is not a child of this node then the caller is
		// replacing something that's not there and the call to replaceChild 
		// will eventually fail anyway.
		ChildManager children = getChildManagerForModificationOrFail();
		children.replaceChild(search, replace, this, getAstChildContainer());
	}
	
	// =========================================================================
	
	@Override
	public final void replaceAstChild(AstNode oldNode, AstNode newNode)
	{
		NodeList container = getAstChildContainer();
		if (container == null)
			throw new InternalError("Can't replace a child if I ain't got no container...");
		
		Toolbox.replaceAstNode(container, oldNode, newNode);
	}
	
	protected void addContent(
			AstToWomNodeFactory womNodeFactory,
			NodeList container,
			ChildManager childManager,
			boolean isInlineScope)
	{
		Iterator<AstNode> i = container.iterator();
		while (i.hasNext())
		{
			AstNode n = i.next();
			
			{
				switch (n.getNodeType())
				{
					case AstNodeTypes.NT_IGNORED:
						break;
					
					case AstNode.NT_TEXT:
					case AstNodeTypes.NT_XML_CHAR_REF:
					case AstNodeTypes.NT_XML_ENTITY_REF:
						if (isInlineScope)
						{
							buildComplexText(womNodeFactory, container, childManager, i, n);
							break;
						}
						else
						{
							if (!StringUtils.isWhitespace(((Text) n).getContent()))
								throw new AssertionError("Non-whitespace text in non-inline scope");
							
							break;
						}
						
					case AstNodeTypes.NT_NEWLINE:
						if (isInlineScope)
							buildComplexText(womNodeFactory, container, childManager, i, n);
						break;
					
					default:
					{
						WomNode child = womNodeFactory.create(container, n);
						if (child != null)
							childManager.appendChild(child, this, null);
						break;
					}
				}
			}
		}
	}
	
	private void buildComplexText(
			AstToWomNodeFactory womNodeFactory,
			NodeList container,
			ChildManager childManager,
			Iterator<AstNode> i,
			AstNode firstText)
	{
		TextAdapter complexText =
				(TextAdapter) womNodeFactory.create(container, firstText);
		
		WomNode notText = null;
		
		while (i.hasNext())
		{
			AstNode n = i.next();
			switch (n.getNodeType())
			{
				case AstNode.NT_TEXT:
				case AstNodeTypes.NT_IGNORED:
				case AstNodeTypes.NT_NEWLINE:
				case AstNodeTypes.NT_XML_CHAR_REF:
				case AstNodeTypes.NT_XML_ENTITY_REF:
					complexText.append(womNodeFactory, n);
					continue;
					
				default:
					WomNode child = womNodeFactory.create(container, n);
					if (child == null)
					{
						// Skip invisible nodes (category links, magic words, ...)
						continue;
					}
					else
					{
						notText = child;
					}
					break;
			}
			break;
		}
		
		childManager.appendChild(complexText, this, null);
		
		if (notText != null)
			childManager.appendChild(notText, this, null);
	}

	protected final void addContent(AstToWomNodeFactory womNodeFactory, NodeList content)
	{
		if (content == null)
			throw new NullPointerException();
		
		if (!content.isEmpty())
			addContent(
					womNodeFactory, 
					content, 
					getChildManagerForModificationOrFail(),
					true);
	}
}
