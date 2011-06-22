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

public class DomOnlyAttributes
{
	private XmlAttributeAdapter firstAttr;
	
	// =========================================================================
	
	public Collection<WomAttribute> getAttributes()
	{
		return new SiblingCollection<WomAttribute>(firstAttr);
	}
	
	public String getAttribute(String name)
	{
		final WomAttribute attributeNode = getAttributeNode(name);
		if (attributeNode == null)
			return null;
		return attributeNode.getValue();
	}
	
	public XmlAttributeAdapter getAttributeNode(String name)
	{
		if (name != null)
		{
			WomAttribute i = firstAttr;
			while (i != null)
			{
				if (i.getName().equalsIgnoreCase(name))
					return (XmlAttributeAdapter) i;
				
				i = (WomAttribute) i.getNextSibling();
			}
		}
		return null;
	}
	
	public XmlAttributeAdapter removeAttribute(String name)
	{
		if (name == null)
			throw new IllegalArgumentException("Argument `name' is null.");
		
		XmlAttributeAdapter remove = getAttributeNode(name);
		if (remove == null)
			return null;
		
		removeAttribute(remove);
		
		return remove;
	}
	
	public void removeAttributeNode(WomAttribute attr) throws IllegalArgumentException
	{
		if (attr == null)
			throw new IllegalArgumentException("Argument `attr' is null.");
		
		if (attr.getParent() != this)
			throw new IllegalArgumentException("Given attribute `attr' is not an attribute of this XML element.");
		
		removeAttribute((XmlAttributeAdapter) attr);
	}
	
	public XmlAttributeAdapter setAttribute(String name, String value, DomBackbone parent)
	{
		if (name == null)
			throw new IllegalArgumentException("Argument `name' is null.");
		
		if (value == null)
			return removeAttribute(name);
		
		XmlAttributeAdapter attr = new XmlAttributeAdapter(name, value);
		
		return setAttributeNode(attr, parent);
	}
	
	public XmlAttributeAdapter setAttributeNode(WomAttribute attr, DomBackbone parent) throws IllegalArgumentException
	{
		if (attr == null)
			throw new IllegalArgumentException("Argument `attr' is null.");
		
		if (!(attr instanceof XmlAttributeAdapter))
			throw new IllegalArgumentException(
			        "Expected argument `attr' be of type " + XmlAttributeAdapter.class.getName());
		
		final XmlAttributeAdapter newAttr = (XmlAttributeAdapter) attr;
		if (newAttr.isLinked())
			throw new IllegalStateException(
			        "Given attribute `attr' is still attribute of another DOM node.");
		
		XmlAttributeAdapter oldAttr = getAttributeNode(attr.getName());
		
		replaceAttribute(oldAttr, newAttr, parent);
		
		return oldAttr;
	}
	
	// =========================================================================
	
	private void removeAttribute(XmlAttributeAdapter remove)
	{
		if (remove == firstAttr)
			firstAttr = (XmlAttributeAdapter) remove.getNextSibling();
		remove.unlink();
	}
	
	private void replaceAttribute(final XmlAttributeAdapter oldAttr, final XmlAttributeAdapter newAttr, DomBackbone parent)
	{
		DomBackbone prev = null;
		DomBackbone next = null;
		if (oldAttr != null)
		{
			prev = oldAttr.getPrevSibling();
			next = oldAttr.getNextSibling();
			oldAttr.unlink();
		}
		else if (firstAttr != null)
		{
			prev = firstAttr;
			while (prev.getNextSibling() != null)
				prev = (DomBackbone) prev.getNextSibling();
		}
		
		newAttr.link(parent, prev, next);
		if (oldAttr == firstAttr)
			firstAttr = newAttr;
	}
}
