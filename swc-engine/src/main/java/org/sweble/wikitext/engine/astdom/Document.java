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
package org.sweble.wikitext.engine.astdom;

import java.util.Collection;

import org.sweble.wikitext.engine.dom.DomAttribute;
import org.sweble.wikitext.engine.dom.DomPage;
import org.sweble.wikitext.engine.dom.DomNode;
import org.sweble.wikitext.engine.dom.DomNodeType;

import de.fau.cs.osr.ptk.common.ast.AstNode;
import de.fau.cs.osr.ptk.common.ast.ContentNode;

public class Document
        extends
            DomBackbone
        implements
            DomPage
{
	private static final long serialVersionUID = 1L;
	
	private String name;
	
	private final DomOnlyAttributes attrs = new DomOnlyAttributes();
	
	private final DomAstChildren children = new DomAstChildren();
	
	// =========================================================================
	
	public Document(AstNode astNode, String namespace, String path, String title)
	{
		super(astNode);
		
		setAttribute("namespace", namespace);
		setAttribute("path", path);
		setAttribute("title", title);
		makeName();
	}
	
	// =========================================================================
	
	@Override
	public String getName()
	{
		return name;
	}
	
	@Override
	public String getTitle()
	{
		return getAttribute("title");
	}
	
	@Override
	public String setTitle(String title)
	{
		if (title == null)
			throw new IllegalArgumentException("Argument `title' must not be null.");
		
		String old = getTitle();
		setAttribute("title", title);
		makeName();
		return old;
	}
	
	@Override
	public String getNamespace()
	{
		return getAttribute("namespace");
	}
	
	@Override
	public String setNamespace(String namespace)
	{
		String old = getNamespace();
		setAttribute("namespace", namespace);
		makeName();
		return old;
	}
	
	@Override
	public String getPath()
	{
		return getAttribute("path");
	}
	
	@Override
	public String setPath(String path)
	{
		String old = getPath();
		setAttribute("path", path);
		makeName();
		return old;
	}
	
	// =========================================================================
	
	private void makeName()
	{
		String namespace = getNamespace();
		String path = getPath();
		String title = getTitle();
		
		String name = "";
		if (namespace != null && !namespace.isEmpty())
		{
			name += namespace;
			name += ':';
		}
		if (path != null && !path.isEmpty())
		{
			name += path;
			if (!path.endsWith("/"))
				name += '/';
		}
		name += title;
		
		this.name = name;
	}
	
	// =========================================================================
	
	@Override
	public String getNodeName()
	{
		return "document";
	}
	
	@Override
	public DomNodeType getNodeType()
	{
		return DomNodeType.DOCUMENT;
	}
	
	protected ContentNode getAstNode()
	{
		return ((ContentNode) super.getAstNode());
	}
	
	// =========================================================================
	
	@Override
	public boolean supportsAttributes()
	{
		return true;
	}
	
	@Override
	public Collection<DomAttribute> getAttributes()
	{
		return attrs.getAttributes();
	}
	
	@Override
	public String getAttribute(String name)
	{
		return attrs.getAttribute(name);
	}
	
	@Override
	public XmlAttributeAdapter getAttributeNode(String name)
	{
		return attrs.getAttributeNode(name);
	}
	
	@Override
	public XmlAttributeAdapter removeAttribute(String name)
	{
		return attrs.removeAttribute(name);
	}
	
	@Override
	public void removeAttributeNode(DomAttribute attr) throws IllegalArgumentException
	{
		attrs.removeAttributeNode(attr);
	}
	
	@Override
	public XmlAttributeAdapter setAttribute(String name, String value)
	{
		return attrs.setAttribute(name, value, this);
	}
	
	@Override
	public XmlAttributeAdapter setAttributeNode(DomAttribute attr) throws IllegalArgumentException
	{
		return attrs.setAttributeNode(attr, this);
	}
	
	// =========================================================================
	
	@Override
	public boolean hasChildNodes()
	{
		return children.hasChildNodes();
	}
	
	@Override
	public Collection<DomNode> childNodes()
	{
		return children.childNodes();
	}
	
	@Override
	public DomNode getFirstChild()
	{
		return children.getFirstChild();
	}
	
	@Override
	public DomNode getLastChild()
	{
		return children.getLastChild();
	}
	
	@Override
	public void appendChild(DomNode child)
	{
		children.appendChild(child, this, getAstNode().getContent());
	}
	
	@Override
	public void insertBefore(DomNode before, DomNode child) throws IllegalArgumentException
	{
		children.insertBefore(before, child, this, getAstNode().getContent());
	}
	
	@Override
	public void removeChild(DomNode child)
	{
		children.removeChild(child, this, getAstNode().getContent());
	}
	
	@Override
	public void replaceChild(DomNode search, DomNode replace)
	{
		children.replaceChild(search, replace, this, getAstNode().getContent());
	}
}
