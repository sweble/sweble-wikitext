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

import org.sweble.wikitext.engine.astwom.AttributeDescriptor;
import org.sweble.wikitext.engine.astwom.AttributeManagerBase;
import org.sweble.wikitext.engine.astwom.AttributeSupportingElement;
import org.sweble.wikitext.engine.astwom.Toolbox;
import org.sweble.wikitext.engine.astwom.WomBackbone;
import org.sweble.wikitext.engine.wom.WomCategory;
import org.sweble.wikitext.engine.wom.WomNode;
import org.sweble.wikitext.engine.wom.WomNodeType;
import org.sweble.wikitext.engine.wom.WomTitle;
import org.sweble.wikitext.lazy.parser.InternalLink;
import org.sweble.wikitext.lazy.parser.LinkTitle;

import de.fau.cs.osr.ptk.common.ast.AstNode;
import de.fau.cs.osr.ptk.common.ast.NodeList;
import de.fau.cs.osr.utils.Utils;

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
	private AttributeManagerBase attribManager = AttributeManagerBase.emptyManager();
	
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
		
		setName(category);
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
	
	// =========================================================================
	
	@Override
	public String getName()
	{
		return getAttribute("name");
	}
	
	@Override
	public String setName(String name)
	{
		NativeOrXmlAttributeAdapter old = setAttribute(
				Attributes.name,
				"name",
				name);
		
		return (old == null) ? null : old.getValue();
	}
	
	@Override
	public String getCategory()
	{
		return getName();
	}
	
	@Override
	public String setCategory(String category) throws NullPointerException, IllegalArgumentException
	{
		return setName(category);
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
	
	/**
	 * Called by PageAdapter to add a new category to the AST.
	 */
	protected void addToAst(NodeList container)
	{
		if (astNodes.size() != 1 || astNodes.get(0).container != null)
			throw new InternalError();
		
		astNodes.set(0, new AstRep(container, getAstNode()));
		
		container.add(getAstNode());
	}
	
	/**
	 * Called by PageAdapter to remove a category from the AST.
	 */
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
	
	/**
	 * Called by PageAdapter to replace one category node with another in the
	 * AST.
	 */
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
	
	/**
	 * Called by PageAdapter to add another AST node mentioning this category.
	 */
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
	
	private void setAttributeFromAst()
	{
		// problem: since we set from ast we must not verify and don't perform 
		// any custom action. we must also not sync to ast, but that's not a 
		// problem since it's not done for category attributes anyway.
		//
		// the check for removability technically doesn't apply, because again,
		// we set from the ast. but it's a good sanity check and should be done
		// anyway.
		//
		// what must be done on the other hand is normalization, because we DO
		// set from ast!
		//
		// cloning Attributes.name for that purpose sucks ... is there a better 
		// way? especially since this problem will occur elsewhere too ...
		setAttribute(
				Attributes.NAME_FROM_AST,
				"name",
				getNameFromAst());
	}
	
	private String getNameFromAst()
	{
		InternalLink astNode = getAstNode();
		String name = astNode.getTarget();
		int i = name.lastIndexOf(':');
		return (i >= 0) ? name.substring(i + 1) : name;
	}
	
	private void effectNameChangeInAst(NativeOrXmlAttributeAdapter newAttr)
	{
		String newValue = newAttr.getValue();
		
		// target can be null when this method is called indirectly by the constructor.
		if (getAstNode().getTarget() != null && getNameFromAst().equals(newValue))
			return;
		
		String target = CATEGORY_NAMESPACE + newValue;
		
		// setNameInAst
		InternalLink astNode = getAstNode();
		astNode.setTarget(target);
		Toolbox.addRtData(astNode);
		
		// removeRedundantLinks
		if (astNodes.size() <= 1)
			return;
		
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
	
	private void effectNameChangeInPage(
			NativeOrXmlAttributeAdapter oldAttr,
			NativeOrXmlAttributeAdapter newAttr)
	{
		WomBackbone root = getParent();
		if (root != null)
		{
			if (!(root instanceof PageAdapter))
				throw new InternalError();
			
			PageAdapter page = (PageAdapter) root;
			page.changeCategoryName(this, oldAttr.getValue(), newAttr.getValue());
		}
	}
	
	// =========================================================================
	
	@Override
	protected AttributeDescriptor getAttributeDescriptor(String name)
	{
		Attributes d = Utils.fromString(Attributes.class, name);
		if (d != null && d != Attributes.NAME_FROM_AST)
			return d;
		// No other attributes are allowed.
		return null;
	}
	
	private static enum Attributes implements AttributeDescriptor
	{
		name
		{
			@Override
			public String verify(WomNode parent, String value) throws IllegalArgumentException
			{
				Toolbox.checkValidCategory(value);
				
				CategoryAdapter categoryAdapter = (CategoryAdapter) parent;
				if (!value.equalsIgnoreCase(categoryAdapter.getName()))
				{
					WomBackbone pageNode = categoryAdapter.getParent();
					if (pageNode != null)
					{
						if (!(pageNode instanceof PageAdapter))
							throw new InternalError();
						
						PageAdapter page = (PageAdapter) pageNode;
						if (page.hasCategory(value))
							throw new IllegalArgumentException(
									"The page is already assigned to a category called `" + value + "'");
					}
				}
				
				return value;
			}
			
			@Override
			public void customAction(
					WomNode parent,
					NativeOrXmlAttributeAdapter oldAttr,
					NativeOrXmlAttributeAdapter newAttr)
			{
				((CategoryAdapter) parent).effectNameChangeInAst(newAttr);
				((CategoryAdapter) parent).effectNameChangeInPage(oldAttr, newAttr);
			}
		},
		
		NAME_FROM_AST
		{
			@Override
			public String verify(WomNode parent, String value) throws IllegalArgumentException
			{
				return value;
			}
			
			@Override
			public void customAction(
					WomNode parent,
					NativeOrXmlAttributeAdapter oldAttr,
					NativeOrXmlAttributeAdapter newAttr)
			{
			}
		};
		
		@Override
		public boolean syncToAst()
		{
			return false;
		}
		
		@Override
		public Normalization getNormalizationMode()
		{
			return Normalization.NONE;
		}
		
		@Override
		public boolean isRemovable()
		{
			return false;
		}
	}
}
