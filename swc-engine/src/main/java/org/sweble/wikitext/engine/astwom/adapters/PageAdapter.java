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

import de.fau.cs.osr.ptk.common.ast.ContentNode;
import de.fau.cs.osr.ptk.common.ast.NodeList;
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
	
	public PageAdapter(
			AstToWomNodeFactory womNodeFactory,
			Page page,
			String title)
	{
		this(womNodeFactory, page, null, null, title);
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
	public ContentNode getAstNode()
	{
		return (ContentNode) super.getAstNode();
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
		
		NodeList container = ((BodyAdapter) getBody()).getAstNode();
		
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
			old.removeFromAst();
			
			return old;
		}
		return null;
	}
	
	@Override
	public WomCategory setCategory(String name) throws NullPointerException
	{
		String lcName = name.toLowerCase();
		CategoryAdapter cat = categories.get(lcName);
		if (cat != null)
		{
			// Make sure changes in case are not lost.
			if (!name.equals(cat.getName()))
				cat.setName(name);
			return cat;
		}
		
		cat = new CategoryAdapter(name);
		
		// Add to WOM
		categories.put(lcName, cat);
		cat.link(this, this.body.getPrevSibling(), this.body);
		
		// Add to AST
		// FIXME: Is it that simple? Think of interleaving text and such ...
		cat.addToAst(this.body.getAstNode());
		
		return cat;
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
		getAstNode().setContent(newBody.getAstNode());
		
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
	protected void replaceChildImpl(WomBackbone newChild, WomBackbone oldChild)
	{
		if (oldChild == redirect)
		{
			setRedirect(Toolbox.expectType(WomRedirect.class, newChild));
		}
		else if (oldChild == body)
		{
			setBody(Toolbox.expectType(WomBody.class, newChild));
		}
		else
		{
			CategoryAdapter replacement =
					Toolbox.expectType(CategoryAdapter.class, newChild);
			
			CategoryAdapter replacee = (CategoryAdapter) oldChild;
			
			// Remove old from WOM
			if (categories.remove(replacee.getName().toLowerCase()) == null)
				throw new InternalError();
			WomBackbone prevSibling = replacee.getPrevSibling();
			WomBackbone nextSibling = replacee.getNextSibling();
			replacee.unlink();
			
			// Add new to WOM
			categories.put(replacement.getName().toLowerCase(), replacement);
			replacement.link(this, prevSibling, nextSibling);
			
			// Fix AST
			replacee.replaceInAst(replacement);
		}
	}
	
	/**
	 * Add a category to this page gathered while traversing an AST.
	 * 
	 * <strong>For INTERNAL use only!</strong>
	 */
	public void registerCategory(CategoryAdapter newCat)
	{
		String name = newCat.getName();
		String lcName = name.toLowerCase();
		
		CategoryAdapter cat = categories.get(lcName);
		if (cat != null)
		{
			cat.addRedundantOccurance(newCat);
		}
		else
		{
			// Add to WOM
			categories.put(lcName, newCat);
			newCat.link(this, this.body.getPrevSibling(), this.body);
		}
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
		public void customAction(WomNode parent, String value)
		{
		}
	}
}
