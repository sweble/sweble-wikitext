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
package org.sweble.wikitext.engine.astwom.adapters;

import java.util.LinkedList;
import java.util.ListIterator;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import org.sweble.wikitext.engine.astwom.AttributeManager;
import org.sweble.wikitext.engine.astwom.AttributeSupportingElement;
import org.sweble.wikitext.engine.astwom.WomBackbone;
import org.sweble.wikitext.engine.astwom.Toolbox;
import org.sweble.wikitext.engine.wom.WomCategory;
import org.sweble.wikitext.engine.wom.WomNodeType;
import org.sweble.wikitext.engine.wom.WomTitle;
import org.sweble.wikitext.lazy.parser.InternalLink;
import org.sweble.wikitext.lazy.parser.LinkTitle;
import org.sweble.wikitext.lazy.utils.TextUtils;

import de.fau.cs.osr.ptk.common.ast.AstNode;
import de.fau.cs.osr.ptk.common.ast.NodeList;

public class CategoryAdapter
        extends
            AttributeSupportingElement
        implements
            WomCategory
{
	private static final String CATEGORY_NAMESPACE = "Category:";
	
	private static final long serialVersionUID = 1L;
	
	private static final WomTitle EMPTY_TITLE = new EmptyTitleAdapter();
	
	@Getter(AccessLevel.PROTECTED)
	@Setter(AccessLevel.PROTECTED)
	private AttributeManager attribManager = AttributeManager.emptyManager();
	
	protected static final class AstRep
	{
		public final NodeList container;
		
		public final InternalLink category;
		
		public AstRep(NodeList container, InternalLink category)
		{
			super();
			this.container = container;
			this.category = category;
		}
	}
	
	private LinkedList<AstRep> astNodes = new LinkedList<AstRep>();
	
	// =========================================================================
	
	public CategoryAdapter(String category)
	{
		super(new InternalLink("", null, new LinkTitle(), ""));
		
		astNodes.add(new AstRep(null, getAstNode()));
		
		setCategory(category);
	}
	
	public CategoryAdapter(NodeList container, InternalLink category)
	{
		super(category);
		
		if (category == null)
			throw new NullPointerException();
		
		astNodes.add(new AstRep(container, getAstNode()));
		
		setAttributeFromAst();
	}
	
	// =========================================================================
	
	@Override
	public String getNodeName()
	{
		return "category";
	}
	
	@Override
	public InternalLink getAstNode()
	{
		return (InternalLink) super.getAstNode();
	}
	
	@Override
	public WomNodeType getNodeType()
	{
		return WomNodeType.ELEMENT;
	}
	
	@Override
	protected String checkAttributeValue(String name, String value) throws IllegalArgumentException, UnsupportedOperationException
	{
		if (name.equalsIgnoreCase("category"))
		{
			String checkedValue = checkValidCategory(value);
			setNameInAst(checkedValue);
			return checkedValue;
		}
		else
		{
			throw new IllegalArgumentException("Attribute `" + name + "' not supported by this node");
		}
	}
	
	// =========================================================================
	
	@Override
	public String getCategory()
	{
		return getAttribute("category");
	}
	
	@Override
	public String setCategory(String category)
	{
		category = checkValidCategory(category);
		
		// Fix AST
		removeRedundantLinks();
		setNameInAst(category);
		
		// Fix WOM
		NativeOrXmlAttributeAdapter old = setAttributeUnchecked("category", category);
		
		return (old == null) ? null : old.getValue();
	}
	
	@Override
	public WomTitle getLinkTitle()
	{
		return EMPTY_TITLE;
	}
	
	@Override
	public String getLinkTarget()
	{
		return getAstNode().getTarget();
	}
	
	// =========================================================================
	
	protected void addToAst(NodeList container)
	{
		if (astNodes.size() != 1 || astNodes.get(0).container != null)
			throw new InternalError();
		
		astNodes.set(0, new AstRep(container, getAstNode()));
		
		container.add(getAstNode());
	}
	
	protected void removeFromAst()
	{
		for (AstRep rep : astNodes)
		{
			if (rep.container == null)
				throw new InternalError();
			if (!rep.container.remove(getAstNode()))
				throw new InternalError();
		}
		astNodes.clear();
		astNodes.add(new AstRep(null, getAstNode()));
	}
	
	protected void replaceInAst(CategoryAdapter replacement)
	{
		if (replacement.astNodes.size() != 1)
			throw new InternalError();
		if (replacement.astNodes.get(0).container != null)
			throw new InternalError();
		
		InternalLink astNode = getAstNode();
		for (AstRep rep : astNodes)
		{
			if (rep.container == null)
				throw new InternalError();
			if (rep.category == astNode)
			{
				boolean success = false;
				ListIterator<AstNode> i = rep.container.listIterator();
				InternalLink rplAstNode = replacement.getAstNode();
				while (i.hasNext())
				{
					AstNode node = i.next();
					if (node == astNode)
					{
						i.set(rplAstNode);
						success = true;
						break;
					}
				}
				
				if (!success)
					throw new InternalError();
				
				replacement.astNodes.set(0, new AstRep(rep.container, rplAstNode));
			}
			else
			{
				if (!rep.container.remove(astNode))
					throw new InternalError();
			}
		}
		astNodes.clear();
		astNodes.add(new AstRep(null, astNode));
	}
	
	protected void addRedundantOccurance(CategoryAdapter newCat)
	{
		if (newCat.astNodes.size() != 1)
			throw new InternalError();
		
		NodeList container = newCat.astNodes.get(0).container;
		if (container == null)
			throw new InternalError();
		
		InternalLink astNode = newCat.getAstNode();
		if (astNode != newCat.astNodes.get(0).category)
			throw new InternalError();
		
		astNodes.add(new AstRep(container, astNode));
		
		// Always keep the last
		setAstNode(astNode);
		setAttributeFromAst();
	}
	
	// =========================================================================
	
	private void setNameInAst(String category)
	{
		InternalLink astNode = getAstNode();
		String target = CATEGORY_NAMESPACE + category;
		astNode.setTarget(target);
		TextUtils.addRtData(
		        astNode,
		        TextUtils.joinRt("[[", target),
		        TextUtils.joinRt("]]"));
	}
	
	private void setAttributeFromAst()
	{
		InternalLink category = getAstNode();
		
		String name = category.getTarget();
		int i = name.lastIndexOf(':');
		if (i >= 0)
			name = name.substring(i + 1);
		
		setAttributeUnchecked("category", name);
	}
	
	private void removeRedundantLinks()
	{
		if (astNodes.size() <= 1)
			return;
		
		InternalLink astNode = getAstNode();
		ListIterator<AstRep> i = astNodes.listIterator();
		while (i.hasNext())
		{
			AstRep rep = i.next();
			if (rep.category != astNode)
			{
				i.remove();
				rep.container.remove(rep.category);
			}
		}
	}
	
	private String checkValidCategory(String category)
	{
		String checkedCategory = Toolbox.checkValidCategory(category);
		
		if (!checkedCategory.equalsIgnoreCase(getCategory()))
		{
			WomBackbone parent = getParent();
			if (parent != null)
			{
				if (!(parent instanceof PageAdapter))
					throw new InternalError();
				
				PageAdapter page = (PageAdapter) parent;
				if (page.hasCategory(checkedCategory))
					throw new IllegalArgumentException(
					        "The page is already assigned to a category called `" + category + "'");
			}
		}
		
		return checkedCategory;
	}
}
