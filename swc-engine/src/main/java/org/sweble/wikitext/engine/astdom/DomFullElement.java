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
import org.sweble.wikitext.engine.dom.DomNode;

import de.fau.cs.osr.ptk.common.ast.AstNode;
import de.fau.cs.osr.ptk.common.ast.NodeList;

public abstract class DomFullElement
        extends
            DomBackbone
{
	private static final long serialVersionUID = 1L;
	
	private final DomAstAttributes attrs = new DomAstAttributes();
	
	private final DomAstChildren children = new DomAstChildren();
	
	// =========================================================================
	
	public DomFullElement(AstNode astNode)
	{
		super(astNode);
	}
	
	// =========================================================================
	
	protected abstract NodeList getAttributeContainer();
	
	protected abstract NodeList getChildContainer();
	
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
		return attrs.removeAttribute(name, getAttributeContainer());
	}
	
	@Override
	public void removeAttributeNode(DomAttribute attr) throws IllegalArgumentException
	{
		attrs.removeAttributeNode(attr, this, getAttributeContainer());
	}
	
	@Override
	public XmlAttributeAdapter setAttribute(String name, String value)
	{
		return attrs.setAttribute(name, value, this, getAttributeContainer());
	}
	
	@Override
	public XmlAttributeAdapter setAttributeNode(DomAttribute attr) throws IllegalArgumentException
	{
		return attrs.setAttributeNode(attr, this, getAttributeContainer());
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
		children.appendChild(child, this, getChildContainer());
	}
	
	@Override
	public void insertBefore(DomNode before, DomNode child) throws IllegalArgumentException
	{
		children.insertBefore(before, child, this, getChildContainer());
	}
	
	@Override
	public void removeChild(DomNode child)
	{
		children.removeChild(child, this, getChildContainer());
	}
	
	@Override
	public void replaceChild(DomNode search, DomNode replace)
	{
		children.replaceChild(search, replace, this, getChildContainer());
	}
}
