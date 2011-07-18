package org.sweble.wikitext.engine.domconverter;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;

import org.sweble.wikitext.engine.dom.DomAttribute;
import org.sweble.wikitext.engine.dom.DomNode;
import org.sweble.wikitext.engine.dom.DomNodeType;
import org.sweble.wikitext.lazy.AstNodeTypes;
import org.sweble.wikitext.lazy.parser.XmlElement;
import org.sweble.wikitext.lazy.utils.XmlAttribute;

import de.fau.cs.osr.ptk.common.ast.AstNode;

public abstract class XmlElementAdapter
        extends
            DomBackbone
        implements
            DomAttribute
{
	private static final long serialVersionUID = 1L;
	
	private final XmlElement astNode;
	
	private XmlAttributeAdapter firstAttr;
	
	private DomBackbone firstChild;
	
	// =========================================================================
	
	public XmlElementAdapter(XmlElement astNode)
	{
		this.astNode = astNode;
	}
	
	protected XmlElement getAstNode()
	{
		return astNode;
	}
	
	// =========================================================================
	
	@Override
	public DomNodeType getNodeType()
	{
		return DomNodeType.ELEMENT;
	}
	
	@Override
	public String getText()
	{
		return null;
	}
	
	@Override
	public String getValue()
	{
		return null;
	}
	
	// =========================================================================
	
	@Override
	public void appendText(String text) throws UnsupportedOperationException
	{
		throw new UnsupportedOperationException();
	}
	
	@Override
	public String deleteText(int from, int length) throws UnsupportedOperationException, IndexOutOfBoundsException
	{
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void insertText(int at, String text) throws UnsupportedOperationException, IndexOutOfBoundsException
	{
		throw new UnsupportedOperationException();
	}
	
	@Override
	public String replaceText(String text) throws UnsupportedOperationException
	{
		throw new UnsupportedOperationException();
	}
	
	@Override
	public String replaceText(int from, int length, String text) throws UnsupportedOperationException, IndexOutOfBoundsException
	{
		throw new UnsupportedOperationException();
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
		return new SiblingCollection<DomAttribute>(firstAttr);
	}
	
	@Override
	public String getAttribute(String name)
	{
		final DomAttribute attributeNode = getAttributeNode(name);
		if (attributeNode == null)
			return null;
		return attributeNode.getValue();
	}
	
	@Override
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
	
	@Override
	public XmlAttributeAdapter removeAttribute(String name) throws UnsupportedOperationException
	{
		if (name == null)
			throw new IllegalArgumentException("Name is null.");
		
		XmlAttributeAdapter remove = getAttributeNode(name);
		if (remove == null)
			return null;
		
		removeAttribute(remove);
		
		return remove;
	}
	
	@Override
	public void removeAttributeNode(DomAttribute attr) throws UnsupportedOperationException, IllegalArgumentException
	{
		if (attr == null)
			throw new IllegalArgumentException("Attribute is null.");
		
		XmlAttributeAdapter remove = getAttributeNode(attr.getName());
		if (remove != attr)
			throw new IllegalArgumentException("Given attribute does not belong to this XML element.");
		
		removeAttribute(remove);
	}
	
	@Override
	public DomAttribute setAttribute(String name, String value) throws UnsupportedOperationException
	{
		if (name == null || value == null)
			throw new IllegalArgumentException("Name or value is null.");
		
		// TODO: Check for correct attribute name!
		// TODO: Implement!
		throw new UnsupportedOperationException();
	}
	
	@Override
	public DomAttribute setAttributeNode(DomAttribute attr) throws UnsupportedOperationException, IllegalArgumentException
	{
		if (attr == null)
			throw new IllegalArgumentException("Attribute is null.");
		
		if (!(attr instanceof DomBackbone))
			throw new IllegalArgumentException(
			        "Expected attribute of type " + DomBackbone.class.getName());
		
		final XmlAttributeAdapter newAttr = (XmlAttributeAdapter) attr;
		if (newAttr.isLinked())
			throw new IllegalStateException(
			        "Node is still child of another DOM node.");
		
		XmlAttributeAdapter oldAttr = getAttributeNode(attr.getName());
		
		replaceAttribute(oldAttr, newAttr);
		
		return (DomAttribute) oldAttr;
	}
	
	// =========================================================================
	
	private void replaceAttribute(final XmlAttributeAdapter oldAttr, final XmlAttributeAdapter newAttr)
	{
		// replace in dom
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
		
		newAttr.link(this, prev, next);
		if (oldAttr == firstAttr)
			firstAttr = newAttr;
		
		// replace in ast
		// the ast can contain an attribute with the same name multiple times!
		
		String name = oldAttr.getName();
		XmlAttribute oldAstNode = oldAttr.getAstNode();
		XmlAttribute newAstNode = newAttr.getAstNode();
		
		ListIterator<AstNode> i = astNode.getXmlAttributes().listIterator();
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
					
					Object rtd = oldAstNode.getAttribute("RTD");
					if (rtd != null)
						newAstNode.setAttribute("RTD", rtd);
				}
				else
				{
					i.remove();
				}
			}
		}
	}
	
	private void removeAttribute(XmlAttributeAdapter remove)
	{
		// remove from dom
		if (remove == firstAttr)
			firstAttr = (XmlAttributeAdapter) remove.getNextSibling();
		remove.unlink();
		
		// remove from ast
		// the ast can contain an attribute with the same name multiple times!
		Iterator<AstNode> i = astNode.getXmlAttributes().iterator();
		while (i.hasNext())
		{
			AstNode node = i.next();
			if (!node.isNodeType(AstNodeTypes.NT_XML_ATTRIBUTE))
				continue;
			
			XmlAttribute attr = (XmlAttribute) node;
			if (attr.getName().equalsIgnoreCase(remove.getName()))
			{
				i.remove();
				// the dom will always refer to the last attribute
				if (attr == remove.getAstNode())
					break;
			}
		}
	}
	
	// =========================================================================
	
	@Override
	public boolean hasChildNodes()
	{
		return this.children.isEmpty() == false;
	}
	
	@Override
	public Collection<DomNode> childNodes()
	{
		return this.children;
	}
	
	@Override
	public DomNode getFirstChild()
	{
		if (this.children.isEmpty())
			return null;
		return this.children.get(0);
	}
	
	@Override
	public DomNode getLastChild()
	{
		if (this.children.isEmpty())
			return null;
		return this.children.get(this.children.size() - 1);
	}
	
	// =========================================================================
	
	@Override
	public void appendChild(DomNode child) throws UnsupportedOperationException
	{
		if (child == null)
			throw new IllegalArgumentException("Child is null.");
		
		if (this.children == Collections.EMPTY_LIST)
			this.children = new LinkedList<DomNode>();
		
	}
	
	@Override
	public void insertBefore(DomNode before, DomNode child) throws UnsupportedOperationException, IllegalArgumentException
	{
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public boolean removeChild(DomNode child) throws UnsupportedOperationException
	{
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean replaceChild(DomNode search, DomNode replace) throws UnsupportedOperationException
	{
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public String getName()
	{
		// TODO Auto-generated method stub
		return null;
	}
}
