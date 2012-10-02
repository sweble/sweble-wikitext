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

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import org.sweble.wikitext.engine.Page;
import org.sweble.wikitext.engine.astwom.AstToWomNodeFactory;
import org.sweble.wikitext.engine.astwom.AttributeDescriptor;
import org.sweble.wikitext.engine.astwom.AttributeManagerBase;
import org.sweble.wikitext.engine.astwom.CustomChildrenElement;
import org.sweble.wikitext.engine.astwom.Toolbox;
import org.sweble.wikitext.engine.astwom.WomBackbone;
import org.sweble.wikitext.engine.wom.WomBody;
import org.sweble.wikitext.engine.wom.WomCategory;
import org.sweble.wikitext.engine.wom.WomNode;
import org.sweble.wikitext.engine.wom.WomNodeType;
import org.sweble.wikitext.engine.wom.WomPage;
import org.sweble.wikitext.engine.wom.WomRedirect;
import org.sweble.wikitext.parser.nodes.InternalLink;
import org.sweble.wikitext.parser.nodes.WtContentNode;
import org.sweble.wikitext.parser.nodes.WtNodeList;

import de.fau.cs.osr.utils.Utils;

public class PageAdapter
		extends
			CustomChildrenElement
		implements
			WomPage
{
	private static final long serialVersionUID = 1L;
	
	@Getter(AccessLevel.PROTECTED)
	@Setter(AccessLevel.PROTECTED)
	private AttributeManagerBase attribManager = AttributeManagerBase.emptyManager();
	
	private RedirectAdapter redirect = null;
	
	private LinkedHashMap<String, CategoryAdapter> categories =
			new LinkedHashMap<String, CategoryAdapter>();
	
	private BodyAdapter body;
	
	// =========================================================================
	
	public PageAdapter(String namespace, String path, String title)
	{
		super(new org.sweble.wikitext.engine.Page());
		
		setAttributeUnchecked(
				Attributes.version,
				"version",
				WomNode.VERSION);
		
		setNamespace(namespace);
		setPath(path);
		setTitle(title);
		setBody(new BodyAdapter());
	}
	
	public PageAdapter(
			AstToWomNodeFactory womNodeFactory,
			Page page,
			String namespace,
			String path,
			String title)
	{
		super(page);
		
		if (womNodeFactory == null || page == null || title == null)
			throw new NullPointerException();
		
		setAttributeUnchecked(
				Attributes.version,
				"version",
				WomNode.VERSION);
		
		setNamespace(namespace);
		setPath(path);
		setTitle(title);
		setBody(new BodyAdapter(womNodeFactory, page.getContent()));
	}
	
	// =========================================================================
	
	@Override
	public String getNodeName()
	{
		return "page";
	}
	
	@Override
	public WomNodeType getNodeType()
	{
		return WomNodeType.DOCUMENT;
	}
	
	@Override
	public WtContentNode getAstNode()
	{
		return (WtContentNode) super.getAstNode();
	}
	
	// =========================================================================
	
	@Override
	public String getName()
	{
		String namespace = getNamespace();
		String path = getPath();
		String title = getTitle();
		
		String name = "";
		if (namespace != null)
		{
			name += namespace;
			name += ':';
		}
		if (path != null)
		{
			name += path;
			name += '/';
		}
		name += title;
		
		return name;
	}
	
	@Override
	public String getVersion()
	{
		return WomNode.VERSION;
	}
	
	@Override
	public String getTitle()
	{
		return getAttribute("title");
	}
	
	@Override
	public String setTitle(String title) throws IllegalArgumentException, NullPointerException
	{
		NativeOrXmlAttributeAdapter old = setAttribute(
				Attributes.title,
				"title",
				title);
		
		return (old == null) ? null : old.getValue();
	}
	
	@Override
	public String getNamespace()
	{
		return getAttribute("namespace");
	}
	
	@Override
	public String setNamespace(String namespace)
	{
		NativeOrXmlAttributeAdapter old = setAttribute(
				Attributes.namespace,
				"namespace",
				namespace);
		
		return (old == null) ? null : old.getValue();
	}
	
	@Override
	public String getPath()
	{
		return getAttribute("path");
	}
	
	@Override
	public String setPath(String path)
	{
		NativeOrXmlAttributeAdapter old = setAttribute(
				Attributes.path,
				"path",
				path);
		
		return (old == null) ? null : old.getValue();
	}
	
	// =========================================================================
	
	@Override
	public boolean isRedirect()
	{
		return redirect != null;
	}
	
	@Override
	public WomRedirect getRedirect()
	{
		return redirect;
	}
	
	@Override
	public WomRedirect setRedirect(WomRedirect redirect)
	{
		RedirectAdapter old = this.redirect;
		
		if (redirect == this.redirect)
			return redirect;
		
		RedirectAdapter newRedirect =
				Toolbox.expectType(RedirectAdapter.class, redirect);
		
		WtNodeList container = ((BodyAdapter) getBody()).getAstNode();
		
		if (this.redirect != null)
		{
			// Remove from WOM
			this.redirect.unlink();
			
			// Remove from AST
			Toolbox.removeAstNode(container, this.redirect.getAstNode());
		}
		
		this.redirect = null;
		if (redirect != null)
		{
			// Add to WOM
			this.redirect = newRedirect;
			newRedirect.link(this, null, (WomBackbone) getFirstChild());
			
			// Add to AST
			Toolbox.prependAstNode(container, newRedirect.getAstNode());
		}
		
		return old;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Collection<WomCategory> getCategories()
	{
		return (Collection<WomCategory>) ((Object) Collections.unmodifiableCollection(categories.values()));
	}
	
	@Override
	public boolean hasCategory(String name) throws NullPointerException
	{
		return categories.containsKey(name.toLowerCase());
	}
	
	@Override
	public WomCategory removeCategory(String name) throws NullPointerException
	{
		CategoryAdapter old = categories.remove(name.toLowerCase());
		if (old != null)
		{
			// Remove from WOM
			old.unlink();
			
			// Remove from AST
			old.removeCategoryAstNodes();
			
			return old;
		}
		return null;
	}
	
	@Override
	public WomCategory setCategory(String name)
	{
		return addCategory(name);
	}
	
	@Override
	public WomCategory addCategory(String name)
	{
		String lcName = name.toLowerCase();
		CategoryAdapter cat = categories.get(lcName);
		if (cat != null)
		{
			// Make sure changes in case are not lost.
			if (!name.equals(cat.getName()))
				cat.setName(name);
		}
		else
		{
			WtNodeList container = this.body.getAstNode();
			
			cat = new CategoryAdapter(container, name);
			
			// Add to WOM
			categories.put(lcName, cat);
			cat.link(this, this.body.getPrevSibling(), this.body);
			
			// Add to AST
			reAttachCategory(cat.getAstNode());
		}
		
		return cat;
	}
	
	/**
	 * Used by factory that generates the WOM from an AST.
	 * 
	 * @param container
	 *            The node that contains the AST node of the category link.
	 * @param link
	 *            The category link from the AST.
	 */
	public void setCategory(WtNodeList container, InternalLink link)
	{
		String name = CategoryAdapter.getNameFromAst(link);
		String lcName = name.toLowerCase();
		
		CategoryAdapter cat = categories.get(lcName);
		if (cat != null)
		{
			// The last category encountered in the AST determines the name.
			cat.addAstAnalog(container, link);
			cat.setNameUnchecked(name);
		}
		else
		{
			cat = new CategoryAdapter(container, link);
			
			// Add to WOM
			categories.put(lcName, cat);
			cat.link(this, this.body.getPrevSibling(), this.body);
			
			// Category is already placed in AST
		}
	}
	
	/**
	 * Called by a CategoryAdapter that is already part of this page but wants
	 * to re-attach its AST node to this page.
	 * 
	 * @param astNode
	 *            The AST node to attach.
	 * @return The container to which the ast node was attached.
	 */
	protected WtNodeList reAttachCategory(InternalLink astNode)
	{
		WtNodeList container = this.body.getAstNode();
		
		container.add(astNode);
		
		return container;
	}
	
	/**
	 * Called by the category node when the name of the category is changed on
	 * the category directly (setName, setAttribute, etc.).
	 * 
	 * @param categoryAdapter
	 *            The category adapter of the renamed category.
	 * @param oldName
	 *            The old name.
	 * @param newName
	 *            The new name.
	 */
	protected void renameCategory(
			CategoryAdapter categoryAdapter,
			String oldName,
			String newName)
	{
		if (this.categories.remove(oldName.toLowerCase()) == null)
			throw new InternalError();
		
		this.categories.put(newName.toLowerCase(), categoryAdapter);
	}
	
	@Override
	public WomBody getBody()
	{
		return body;
	}
	
	@Override
	public WomBody setBody(WomBody body) throws NullPointerException
	{
		if (body == null)
			throw new NullPointerException("Argument `body' is null");
		
		if (body == this.body)
			return this.body;
		
		BodyAdapter newBody =
				Toolbox.expectType(BodyAdapter.class, body, "body");
		
		// Remove from WOM
		BodyAdapter old = this.body;
		WomBackbone prevSibling = null;
		if (old != null)
		{
			prevSibling = old.getPrevSibling();
			old.unlink();
		}
		
		// Add to WOM
		this.body = newBody;
		this.body.link(this, prevSibling, null);
		
		// Set AST
		WtNodeList oldAstBody = null;
		if (old != null)
			oldAstBody = old.getAstNode();
		getAstNode().setContent(newBody.getAstNode());
		
		// Fix categories
		if (oldAstBody != null)
			CategoryAdapter.reAttachCategoryNodesToPage(oldAstBody);
		
		return old;
	}
	
	// =========================================================================
	
	@Override
	public WomNode getFirstChild()
	{
		if (redirect != null)
			return redirect;
		if (!categories.isEmpty())
			return categories.values().iterator().next();
		return body;
	}
	
	@Override
	public WomNode getLastChild()
	{
		return body;
	}
	
	// =========================================================================
	
	@Override
	protected AttributeDescriptor getAttributeDescriptor(String name)
	{
		Attributes d = Utils.fromString(Attributes.class, name);
		if (d != null)
			return d;
		// No other attributes are allowed.
		return null;
	}
	
	private static enum Attributes implements AttributeDescriptor
	{
		version
		{
			@Override
			public String verify(WomNode parent, String value) throws IllegalArgumentException
			{
				throw new UnsupportedOperationException(
						"Cannot alter read-only attribute `version'");
			}
			
			@Override
			public boolean isRemovable()
			{
				return false;
			}
		},
		
		title
		{
			@Override
			public String verify(WomNode parent, String value) throws IllegalArgumentException
			{
				Toolbox.checkValidTitle(value);
				return value;
			}
			
			@Override
			public boolean isRemovable()
			{
				return false;
			}
		},
		
		namespace
		{
			@Override
			public String verify(WomNode parent, String value) throws IllegalArgumentException
			{
				return Toolbox.checkValidNamespace(value);
			}
		},
		
		path
		{
			@Override
			public String verify(WomNode parent, String value) throws IllegalArgumentException
			{
				return Toolbox.checkValidPath(value);
			}
		};
		
		@Override
		public boolean isRemovable()
		{
			return true;
		}
		
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
		public void customAction(
				WomNode parent,
				NativeOrXmlAttributeAdapter oldAttr,
				NativeOrXmlAttributeAdapter newAttr)
		{
		}
	}
}
