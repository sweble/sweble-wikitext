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

import org.sweble.wikitext.engine.wom.WomAttribute;
import org.sweble.wikitext.engine.wom.WomNode;

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
	public Collection<WomAttribute> getAttributes()
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
	public void removeAttributeNode(WomAttribute attr) throws IllegalArgumentException
	{
		attrs.removeAttributeNode(attr, this, getAttributeContainer());
	}
	
	@Override
	public XmlAttributeAdapter setAttribute(String name, String value)
	{
		return attrs.setAttribute(name, value, this, getAttributeContainer());
	}
	
	@Override
	public XmlAttributeAdapter setAttributeNode(WomAttribute attr) throws IllegalArgumentException
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
	public Collection<WomNode> childNodes()
	{
		return children.childNodes();
	}
	
	@Override
	public WomNode getFirstChild()
	{
		return children.getFirstChild();
	}
	
	@Override
	public WomNode getLastChild()
	{
		return children.getLastChild();
	}
	
	@Override
	public void appendChild(WomNode child)
	{
		children.appendChild(child, this, getChildContainer());
	}
	
	@Override
	public void insertBefore(WomNode before, WomNode child) throws IllegalArgumentException
	{
		children.insertBefore(before, child, this, getChildContainer());
	}
	
	@Override
	public void removeChild(WomNode child)
	{
		children.removeChild(child, this, getChildContainer());
	}
	
	@Override
	public void replaceChild(WomNode search, WomNode replace)
	{
		children.replaceChild(search, replace, this, getChildContainer());
	}
}
