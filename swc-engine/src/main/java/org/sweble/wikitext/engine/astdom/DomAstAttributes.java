package org.sweble.wikitext.engine.astdom;

import java.util.Collection;
import java.util.Iterator;
import java.util.ListIterator;

import org.sweble.wikitext.engine.dom.DomAttribute;
import org.sweble.wikitext.lazy.AstNodeTypes;
import org.sweble.wikitext.lazy.utils.XmlAttribute;

import de.fau.cs.osr.ptk.common.ast.AstNode;
import de.fau.cs.osr.ptk.common.ast.NodeList;

public class DomAstAttributes
{
	private XmlAttributeAdapter firstAttr;
	
	// =========================================================================
	
	public Collection<DomAttribute> getAttributes()
	{
		return new SiblingCollection<DomAttribute>(firstAttr);
	}
	
	public String getAttribute(String name)
	{
		final DomAttribute attributeNode = getAttributeNode(name);
		if (attributeNode == null)
			return null;
		return attributeNode.getValue();
	}
	
	public XmlAttributeAdapter getAttributeNode(String name)
	{
		if (name != null)
		{
			DomAttribute i = firstAttr;
			while (i != null)
			{
				if (i.getName().equalsIgnoreCase(name))
					return (XmlAttributeAdapter) i;
				
				i = (DomAttribute) i.getNextSibling();
			}
		}
		return null;
	}
	
	public XmlAttributeAdapter removeAttribute(String name, NodeList attrContainer)
	{
		if (name == null)
			throw new IllegalArgumentException("Argument `name' is null.");
		
		XmlAttributeAdapter remove = getAttributeNode(name);
		if (remove == null)
			return null;
		
		removeAttribute(remove, attrContainer);
		
		return remove;
	}
	
	public void removeAttributeNode(DomAttribute attr, DomBackbone parent, NodeList attrContainer) throws IllegalArgumentException
	{
		if (attr == null)
			throw new IllegalArgumentException("Argument `attr' is null.");
		
		if (attr.getParent() != parent)
			throw new IllegalArgumentException("Given attribute `attr' is not an attribute of this XML element.");
		
		removeAttribute((XmlAttributeAdapter) attr, attrContainer);
	}
	
	public XmlAttributeAdapter setAttribute(String name, String value, DomBackbone parent, NodeList attrContainer)
	{
		if (name == null || value == null)
			throw new IllegalArgumentException("Argument `name' and/or `value' is null.");
		
		XmlAttributeAdapter attr = new XmlAttributeAdapter(name, value);
		
		return setAttributeNode(attr, parent, attrContainer);
	}
	
	public XmlAttributeAdapter setAttributeNode(DomAttribute attr, DomBackbone parent, NodeList attrContainer) throws IllegalArgumentException
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
		
		replaceAttribute(oldAttr, newAttr, parent, attrContainer);
		
		return oldAttr;
	}
	
	// =========================================================================
	
	private void removeAttribute(XmlAttributeAdapter remove, NodeList attrContainer)
	{
		// remove from DOM
		if (remove == firstAttr)
			firstAttr = (XmlAttributeAdapter) remove.getNextSibling();
		remove.unlink();
		
		// remove from AST
		// the ast can contain an attribute with the same name multiple times!
		Iterator<AstNode> i = attrContainer.iterator();
		while (i.hasNext())
		{
			AstNode node = i.next();
			// there could also be garbage nodes ...
			if (!node.isNodeType(AstNodeTypes.NT_XML_ATTRIBUTE))
				continue;
			
			XmlAttribute attr = (XmlAttribute) node;
			if (attr.getName().equalsIgnoreCase(remove.getName()))
			{
				i.remove();
				// the dom will always refer to the last attribute
				if (attr == remove.getAstNode())
				{
					i = null;
					break;
				}
			}
		}
		if (i != null)
			throw new AssertionError();
	}
	
	private void replaceAttribute(final XmlAttributeAdapter oldAttr, final XmlAttributeAdapter newAttr, DomBackbone parent, NodeList attrContainer)
	{
		// replace in DOM
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
		
		// replace in AST
		// the ast can contain an attribute with the same name multiple times!
		
		String name = oldAttr.getName();
		XmlAttribute oldAstNode = oldAttr.getAstNode();
		XmlAttribute newAstNode = newAttr.getAstNode();
		
		ListIterator<AstNode> i = attrContainer.listIterator();
		while (i.hasNext())
		{
			AstNode node = i.next();
			if (!node.isNodeType(AstNodeTypes.NT_XML_ATTRIBUTE))
				continue;
			
			XmlAttribute attr = (XmlAttribute) node;
			if (attr.getName().equalsIgnoreCase(name))
			{
				if (attr == oldAstNode)
				{
					i.set(newAstNode);
					i = null;
					
					Object rtd = oldAstNode.getAttribute("RTD");
					if (rtd != null)
						newAstNode.setAttribute("RTD", rtd);
					
					// The node the DOM refers to comes always last
					break;
				}
				else
				{
					i.remove();
				}
			}
		}
		if (i != null)
			throw new AssertionError();
	}
}
