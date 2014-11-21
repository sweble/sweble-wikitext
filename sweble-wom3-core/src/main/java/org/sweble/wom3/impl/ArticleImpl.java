/**
 * Copyright 2011 The Open Source Research Group,
 *                University of Erlangen-Nürnberg
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses/.
 */
package org.sweble.wom3.impl;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.ListIterator;
import java.util.Map;

import org.sweble.wom3.Wom3Article;
import org.sweble.wom3.Wom3Body;
import org.sweble.wom3.Wom3Category;
import org.sweble.wom3.Wom3Node;
import org.sweble.wom3.Wom3Redirect;

public class ArticleImpl
		extends
			BackboneContainer
		implements
			Wom3Article
{
	private static final long serialVersionUID = 1L;
	
	private static final ChildDescriptor[] BODY_DESCRIPTOR = {
			childDesc("redirect"),
			childDesc("category", ChildDescriptor.MULTIPLE),
			childDesc("body", ChildDescriptor.REQUIRED) };
	
	private RedirectImpl redirect = null;
	
	private SiblingRangeCollection<ArticleImpl, CategoryImpl> categories;
	
	private BodyImpl body;
	
	// =========================================================================
	
	public ArticleImpl(DocumentImpl owner)
	{
		super(owner);
		
		setAttributeDirectNoChecks("version", Wom3Node.VERSION);
		
		categories = new SiblingRangeCollection<ArticleImpl, CategoryImpl>(
				this, new SiblingCollectionsBoundIml());
	}
	
	private final class SiblingCollectionsBoundIml
			implements
				SiblingCollectionBounds,
				Serializable
	{
		private static final long serialVersionUID = 1L;
		
		@Override
		public Backbone getPred()
		{
			return redirect;
		}
		
		@Override
		public Backbone getSucc()
		{
			return body;
		}
	}
	
	// =========================================================================
	
	@Override
	public String getWomName()
	{
		return "article";
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
		return Wom3Node.VERSION;
	}
	
	@Override
	public String getTitle()
	{
		return getStringAttr("title");
	}
	
	@Override
	public String setTitle(String title) throws IllegalArgumentException, NullPointerException
	{
		return setAttributeDirect(ATTR_DESC_TITLE, "title", title);
	}
	
	@Override
	public String getNamespace()
	{
		return getStringAttr("namespace");
	}
	
	@Override
	public String setNamespace(String namespace)
	{
		return setAttributeDirect(ATTR_DESC_NAMESPACE, "namespace", namespace);
	}
	
	@Override
	public String getPath()
	{
		return getStringAttr("path");
	}
	
	@Override
	public String setPath(String path)
	{
		return setAttributeDirect(ATTR_DESC_PATH, "path", path);
	}
	
	// =========================================================================
	
	@Override
	public boolean isRedirect()
	{
		return redirect != null;
	}
	
	@Override
	public Wom3Redirect getRedirect()
	{
		return redirect;
	}
	
	@Override
	public Wom3Redirect setRedirect(Wom3Redirect redirect)
	{
		return (Wom3Redirect) replaceOrInsertBeforeOrAppend(
				this.redirect, getFirstChild(), redirect, false);
		/*
		RedirectImpl old = this.redirect;
		
		if (redirect == this.redirect)
			return redirect;
		
		if (old != null)
			replaceChild(old, redirect);
		else if (hasChildNodes())
			insertBefore(redirect, getFirstChild());
		else
			appendChild(redirect);
		
		return old;
		*/
	}
	
	// ----------------------------------------
	
	@Override
	public Collection<Wom3Category> getCategories()
	{
		@SuppressWarnings({ "unchecked", "rawtypes" })
		Collection<Wom3Category> uc =
				(Collection) Collections.unmodifiableCollection(categories);
		return uc;
	}
	
	@Override
	public boolean hasCategory(String name) throws NullPointerException
	{
		ListIterator<CategoryImpl> i = categories.listIterator();
		while (i.hasNext())
		{
			CategoryImpl cat = i.next();
			if (cat.getName().equals(name))
				return true;
		}
		return false;
	}
	
	@Override
	public Wom3Category removeCategory(String name) throws NullPointerException
	{
		if (name == null)
			throw new NullPointerException();
		ListIterator<CategoryImpl> i = categories.listIterator();
		while (i.hasNext())
		{
			CategoryImpl cat = i.next();
			if (cat.getName().equals(name))
			{
				i.remove();
				return cat;
			}
		}
		return null;
	}
	
	@Override
	public Wom3Category addCategory(String name) throws NullPointerException
	{
		ListIterator<CategoryImpl> i = categories.listIterator();
		while (i.hasNext())
		{
			CategoryImpl cat = i.next();
			if (cat.getName().equals(name))
				return cat;
		}
		CategoryImpl cat = (CategoryImpl)
				getOwnerDocument().createElementNS(Wom3Node.WOM_NS_URI, "category");
		cat.setName(name);
		i.add(cat);
		return cat;
	}
	
	// ----------------------------------------
	
	@Override
	public Wom3Body getBody()
	{
		return body;
	}
	
	@Override
	public Wom3Body setBody(Wom3Body body) throws NullPointerException
	{
		/*
		if (body == null)
			throw new NullPointerException("Argument `body' is null");
		
		if (body == this.body)
			return this.body;
		
		BodyImpl old = this.body;
		
		if (old != null)
			replaceChild(old, body);
		else
			appendChild(body);
		
		return old;
		*/
		
		return (Wom3Body) replaceOrAppend(this.body, body, true);
	}
	
	// =========================================================================
	
	@Override
	public Wom3Node cloneNode(boolean deep)
	{
		ArticleImpl newNode = (ArticleImpl) super.cloneNode(deep);
		
		newNode.categories = new SiblingRangeCollection<ArticleImpl, CategoryImpl>(
				this, new SiblingCollectionsBoundIml());
		
		return newNode;
	}
	
	// =========================================================================
	
	@Override
	protected void allowsInsertion(Backbone prev, Backbone child)
	{
		checkInsertion(prev, child, BODY_DESCRIPTOR);
	}
	
	@Override
	protected void allowsRemoval(Backbone child)
	{
		checkRemoval(child, BODY_DESCRIPTOR);
	}
	
	@Override
	protected void allowsReplacement(Backbone oldChild, Backbone newChild)
	{
		checkReplacement(oldChild, newChild, BODY_DESCRIPTOR);
	}
	
	/*
	@Override
	protected void allowsInsertion(Backbone prev, Backbone child)
	{
		if (child instanceof Wom2Redirect)
		{
			if (prev == null)
				// Can only insert as first child
				return;
		}
		else if (child instanceof WomCategory)
		{
			if ((prev instanceof WomCategory)
					|| (prev instanceof Wom2Redirect)
					|| (prev == null && redirect == null))
				// Can only insert between redirect and body
				return;
		}
		else if (child instanceof WomBody)
		{
			if (getLastChild() == null || prev == getLastChild())
				// Can only insert as last child
				return;
		}
		doesNotAcceptChild(prev, child);
	}
	
	@Override
	protected void allowsRemoval(Backbone child)
	{
		if (child != body)
			return;
		doesNotAllowRemoval(child);
	}
	
	@Override
	protected void allowsReplacement(Backbone oldChild, Backbone newChild)
	{
		if ((newChild instanceof Wom2Redirect) && (
				(oldChild == this.redirect) ||
				(this.redirect == null && oldChild == getFirstChild() && oldChild != this.body)))
			// A redirect node can replace the redirect node or the first node
			// unless the first node is the body element
			return;
		if ((newChild instanceof WomCategory) && (
				(oldChild instanceof WomCategory) ||
				(oldChild == this.redirect)))
			// A category node can replace another category node or the 
			// redirect node
			return;
		if ((newChild instanceof WomBody) && (
				(oldChild == this.body) ||
				(oldChild == getLastChild())))
			// A body node can replace the body node or the last node
			return;
		doesNotAllowReplacement(oldChild, newChild);
	}
	*/
	
	@Override
	protected void childInserted(Backbone prev, Backbone added)
	{
		if (added instanceof Wom3Redirect)
			redirect = (RedirectImpl) added;
		else if (added instanceof Wom3Body)
			body = (BodyImpl) added;
	}
	
	@Override
	protected void childRemoved(Backbone prev, Backbone removed)
	{
		if (removed == redirect)
			redirect = null;
	}
	
	// =========================================================================
	
	protected void validateCategoryNameChange(
			CategoryImpl catImpl,
			String newName)
	{
		ListIterator<CategoryImpl> i = categories.listIterator();
		while (i.hasNext())
		{
			CategoryImpl cat = i.next();
			if (cat.getName().equals(newName))
			{
				if (cat == catImpl)
					return;
				
				throw new IllegalStateException(
						"Renaming the attribute leads to name collision in parent node!");
			}
		}
	}
	
	// =========================================================================
	
	protected static final AttrDescVersion ATTR_DESC_VERSION = new AttrDescVersion();
	
	protected static final AttrDescTitle ATTR_DESC_TITLE = new AttrDescTitle();
	
	protected static final AttrDescNamespace ATTR_DESC_NAMESPACE = new AttrDescNamespace();
	
	protected static final AttrDescPath ATTR_DESC_PATH = new AttrDescPath();
	
	private static final Map<String, AttributeDescriptor> NAME_MAP = new HashMap<String, AttributeDescriptor>();
	
	static
	{
		NAME_MAP.put("version", ATTR_DESC_VERSION);
		NAME_MAP.put("namespace", ATTR_DESC_NAMESPACE);
		NAME_MAP.put("path", ATTR_DESC_PATH);
		NAME_MAP.put("title", ATTR_DESC_TITLE);
	}
	
	// =========================================================================
	
	@Override
	protected AttributeDescriptor getAttributeDescriptor(
			String namespaceUri,
			String localName,
			String qualifiedName)
	{
		return getAttrDescStrict(namespaceUri, localName, qualifiedName, NAME_MAP);
	}
	
	public static final class AttrDescVersion
			extends
				AttributeDescriptor
	{
		@Override
		public int getFlags()
		{
			return makeFlags(
					false /* removable */,
					false /* readOnly */,
					false /* customAction */,
					Normalization.NON_CDATA);
		}
		
		@Override
		public boolean verifyAndConvert(
				Backbone parent,
				NativeAndStringValuePair verified)
		{
			super.verifyAndConvert(parent, verified);
			if (!Wom3Node.VERSION.equals(verified.strValue))
				throw new UnsupportedOperationException(
						"Cannot alter read-only attribute `version'");
			return true;
		}
	}
	
	public static final class AttrDescTitle
			extends
				AttributeDescriptor
	{
		@Override
		public int getFlags()
		{
			return makeFlags(
					false /* removable */,
					false /* readOnly */,
					false /* customAction */,
					Normalization.NON_CDATA);
		}
		
		@Override
		public boolean verifyAndConvert(
				Backbone parent,
				NativeAndStringValuePair verified)
		{
			super.verifyAndConvert(parent, verified);
			Toolbox.checkValidTitle(verified.strValue);
			return true;
		}
	}
	
	public static final class AttrDescNamespace
			extends
				AttributeDescriptor
	{
		@Override
		public int getFlags()
		{
			return makeFlags(
					true /* removable */,
					false /* readOnly */,
					false /* customAction */,
					Normalization.NON_CDATA);
		}
		
		@Override
		public boolean verifyAndConvert(
				Backbone parent,
				NativeAndStringValuePair verified)
		{
			super.verifyAndConvert(parent, verified);
			verified.value =
					verified.strValue =
							Toolbox.checkValidNamespace(verified.strValue);
			return (verified.strValue != null);
		}
	}
	
	public static final class AttrDescPath
			extends
				AttributeDescriptor
	{
		@Override
		public int getFlags()
		{
			return makeFlags(
					true /* removable */,
					false /* readOnly */,
					false /* customAction */,
					Normalization.NON_CDATA);
		}
		
		@Override
		public boolean verifyAndConvert(
				Backbone parent,
				NativeAndStringValuePair verified)
		{
			super.verifyAndConvert(parent, verified);
			verified.value =
					verified.strValue =
							Toolbox.checkValidPath(verified.strValue);
			return (verified.strValue != null);
		}
	}
}
