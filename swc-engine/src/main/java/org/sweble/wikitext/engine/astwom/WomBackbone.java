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
package org.sweble.wikitext.engine.astwom;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import org.sweble.wikitext.engine.astwom.adapters.NativeOrXmlAttributeAdapter;
import org.sweble.wikitext.engine.wom.WomAttribute;
import org.sweble.wikitext.engine.wom.WomNode;
import org.sweble.wikitext.parser.nodes.WtNode;
import org.sweble.wikitext.parser.nodes.WtNodeList;

import de.fau.cs.osr.utils.StringUtils;

/*
 * @startuml AstWomOverviewClassDiagramm.svg
 * 
 * interface "Iterable<T>" as Iterable
 * interface Cloneable
 * interface Serializable
 * interface WomNode
 * 
 * abstract WomBackbone
 * abstract AttributeSupportingElement
 * abstract CustomChildrenElement
 * abstract FullElement
 * abstract ContainerElement
 * abstract NativeOrXmlElement
 * abstract NativeOrXmlElementWithUniversalAttributes
 * abstract XmlElementWithChildren
 * abstract XmlElementWithUniversalAttributes
 * 
 * Iterable <|-- WomNode
 * Cloneable <|-- WomNode
 * Serializable <|-- WomNode
 * 
 * WomNode <|-- WomBackbone
 * 
 * WomBackbone <|-- AttributeSupportingElement
 * 
 * AttributeSupportingElement <|-- CustomChildrenElement
 * AttributeSupportingElement <|-- FullElement
 * 
 * FullElement <|-- NativeOrXmlElement
 * FullElement <|-- XmlElementWithChildren
 * FullElement <|-- ContainerElement
 * 
 * NativeOrXmlElement <|-- NativeOrXmlElementWithUniversalAttributes
 * 
 * XmlElementWithChildren <|-- XmlElementWithUniversalAttributes
 * 
 * note "Comment, \n<i>EmptyTitle</i>, \n<b>NativeOrXmlAttribute</b>, \n<b>WtText</b>" as InheritFromWomBackbone
 * WomBackbone .. InheritFromWomBackbone
 * 
 * note "Category, WtRedirect" as InheritFromAttributeSupportingElement
 * AttributeSupportingElement .. InheritFromAttributeSupportingElement
 * 
 * note "Body, Default, \nElement, \nImageCaption, \nName, Title, \nValue" as InheritFromContainerElement
 * ContainerElement .. InheritFromContainerElement
 * 
 * note "WtHorizontalRule, \nParagraph" as InheritFromNativeOrXmlElement
 * NativeOrXmlElement .. InheritFromNativeOrXmlElement
 * 
 * note "WtBold, WtItalics" as InheritFromNativeOrXmlElementWithUniversalAttributes
 * NativeOrXmlElementWithUniversalAttributes .. InheritFromNativeOrXmlElementWithUniversalAttributes
 * 
 * note "Blockquote, Break, \nDel, Div, Font, Ins" as InheritFromXmlElementWithChildren
 * XmlElementWithChildren .. InheritFromXmlElementWithChildren
 * 
 * note "Abbr, Big, Center, Cite, \nCode, Dfn, Emphasize, \nKbd, Samp, Small, \nSpan, Strike, Strong, \nSub, Sup, Teletype, \nUnderline, Var" as InheritFromXmlElementWithUniversalAttributes
 * XmlElementWithUniversalAttributes .. InheritFromXmlElementWithUniversalAttributes
 * 
 * note "Arg, Page" as InheritFromCustomChildrenElement
 * CustomChildrenElement -- InheritFromCustomChildrenElement
 * 
 * @enduml
 */
public abstract class WomBackbone
		implements
			WomNode
{
	private static final long serialVersionUID = 1L;
	
	private WtNode astNode;
	
	private WomBackbone parent;
	
	private WomBackbone prevSibling;
	
	private WomBackbone nextSibling;
	
	// =========================================================================
	
	public WomBackbone(WtNode astNode)
	{
		this.astNode = astNode;
	}
	
	// =========================================================================
	
	public WtNode getAstNode()
	{
		return astNode;
	}
	
	protected WtNode setAstNode(WtNode astNode)
	{
		WtNode old = this.astNode;
		this.astNode = astNode;
		return old;
	}
	
	protected void appendToAst(WtNodeList container, WtNode child)
	{
		throw new InternalError();
	}
	
	/**
	 * @param prevChild
	 *            Cannot be null.
	 */
	protected void insertIntoAst(
			WtNodeList container,
			WtNode newChild,
			WtNode prevChild)
	{
		throw new InternalError();
	}
	
	/**
	 * IMPORTANT: The implementing class has to make sure that the removed
	 * subtree is searched for category nodes. These have to be re-attached to
	 * the page body.
	 * 
	 * @param oldNode
	 *            Cannot be null.
	 */
	protected void replaceInAst(
			WtNodeList container,
			WomBackbone oldNode,
			WomBackbone newNode)
	{
		throw new InternalError();
	}
	
	/**
	 * IMPORTANT: The implementing class has to make sure that the removed
	 * subtree is searched for category nodes. These have to be re-attached to
	 * the page body.
	 * 
	 * @param oldNode
	 *            Cannot be null.
	 */
	protected void removeFromAst(WtNodeList container, WtNode removeChild)
	{
		throw new InternalError();
	}
	
	// =========================================================================
	
	/*
	protected void setParent(WomBackbone parent)
	{
		this.parent = parent;
	}
	
	protected void setPrevSibling(WomBackbone prevSibling)
	{
		this.prevSibling = prevSibling;
	}
	
	protected void setNextSibling(WomBackbone nextSibling)
	{
		this.nextSibling = nextSibling;
	}
	*/
	
	public final boolean isLinked()
	{
		return parent != null;
	}
	
	public void unlink()
	{
		parent = null;
		
		if (prevSibling != null)
			prevSibling.nextSibling = nextSibling;
		if (nextSibling != null)
			nextSibling.prevSibling = prevSibling;
		
		prevSibling = null;
		nextSibling = null;
	}
	
	public void link(
			WomBackbone parent,
			WomBackbone prevSibling,
			WomBackbone nextSibling)
	{
		if (isLinked())
			throw new IllegalStateException(
					"Node is still child of another WOM node.");
		
		this.parent = parent;
		
		this.prevSibling = prevSibling;
		if (prevSibling != null)
		{
			if (prevSibling.nextSibling != nextSibling)
				throw new IllegalStateException(
						"WOM sibling chain inconsistent.");
			
			prevSibling.nextSibling = this;
		}
		
		this.nextSibling = nextSibling;
		if (nextSibling != null)
		{
			if (nextSibling.prevSibling != prevSibling)
				throw new IllegalStateException(
						"WOM sibling chain inconsistent.");
			
			nextSibling.prevSibling = this;
		}
	}
	
	/**
	 * Called on the parent node when a new child node is added. The parent can
	 * then check if he accepts this kind of child node or throw an exception if
	 * he doesn't.
	 * 
	 * @param child
	 *            The child node to add.
	 * @throws IllegalArgumentException
	 *             Thrown if the parent does not accept this kind of child node.
	 */
	public void acceptsChild(WomBackbone child)
	{
	}
	
	/**
	 * Called on a node when it is added as child to some parent node. The to be
	 * child node can then check if it can be added to this kind of parent node
	 * or throw an exception if it can not be added to this parent node.
	 * 
	 * @param parent
	 *            The parent node to which the node is added.
	 * @throws IllegalArgumentException
	 *             Thrown if the node cannot be added to the given parent node.
	 */
	public void acceptsParent(WomBackbone parent)
	{
	}
	
	/**
	 * Called on a node when it is removed from its parent node. The child node
	 * can then check if it allows the removal or throw an exception if it
	 * cannot be removed this way.
	 * 
	 * @param parent
	 *            The parent node from which the child is removed.
	 * @throws IllegalArgumentException
	 *             Thrown if the node cannot be removed.
	 */
	public void childAllowsRemoval(WomBackbone parent)
	{
	}
	
	// =========================================================================
	
	@Override
	public final WomBackbone getParent()
	{
		return parent;
	}
	
	@Override
	public final WomBackbone getPrevSibling()
	{
		return prevSibling;
	}
	
	@Override
	public final WomBackbone getNextSibling()
	{
		return nextSibling;
	}
	
	// =========================================================================
	
	@Override
	public final WomNode cloneNode()
	{
		try
		{
			return (WomNode) this.clone();
		}
		catch (CloneNotSupportedException e)
		{
			throw new AssertionError();
		}
	}
	
	@Override
	protected Object clone() throws CloneNotSupportedException
	{
		// FIXME: Implement automatic cloning in sub classes.
		WomBackbone node = (WomBackbone) super.clone();
		node.parent = null;
		node.nextSibling = null;
		node.prevSibling = null;
		return node;
	}
	
	// =========================================================================
	
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
	
	@Override
	public void appendText(String text) throws UnsupportedOperationException, IllegalArgumentException
	{
		doesNotSupportTextOperations();
	}
	
	@Override
	public void deleteText(int from, int length) throws UnsupportedOperationException, IndexOutOfBoundsException
	{
		doesNotSupportTextOperations();
	}
	
	@Override
	public void insertText(int at, String text) throws UnsupportedOperationException, IndexOutOfBoundsException
	{
		doesNotSupportTextOperations();
	}
	
	@Override
	public boolean replaceText(String search, String replacement) throws UnsupportedOperationException
	{
		doesNotSupportTextOperations();
		return false;
	}
	
	@Override
	public String replaceText(int from, int length, String text) throws UnsupportedOperationException, IndexOutOfBoundsException
	{
		doesNotSupportTextOperations();
		return null;
	}
	
	protected void doesNotSupportTextOperations() throws UnsupportedOperationException
	{
		throw new UnsupportedOperationException("This node does not support text operations!");
	}
	
	// =========================================================================
	
	@Override
	public boolean supportsAttributes()
	{
		return false;
	}
	
	@Override
	public Collection<WomAttribute> getAttributes()
	{
		return Collections.emptyList();
	}
	
	@Override
	public String getAttribute(String name)
	{
		return null;
	}
	
	@Override
	public NativeOrXmlAttributeAdapter getAttributeNode(String name)
	{
		return null;
	}
	
	@Override
	public NativeOrXmlAttributeAdapter removeAttribute(String name) throws UnsupportedOperationException
	{
		doesNotSupportAttributes();
		return null;
	}
	
	@Override
	public void removeAttributeNode(WomAttribute attr) throws UnsupportedOperationException, IllegalArgumentException
	{
		doesNotSupportAttributes();
	}
	
	@Override
	public NativeOrXmlAttributeAdapter setAttribute(String name, String value) throws UnsupportedOperationException
	{
		doesNotSupportAttributes();
		return null;
	}
	
	@Override
	public NativeOrXmlAttributeAdapter setAttributeNode(WomAttribute attr) throws UnsupportedOperationException, IllegalArgumentException
	{
		doesNotSupportAttributes();
		return null;
	}
	
	protected void doesNotSupportAttributes() throws UnsupportedOperationException
	{
		throw new UnsupportedOperationException("This node does not support attributes!");
	}
	
	// =========================================================================
	
	@Override
	public boolean supportsChildren()
	{
		return false;
	}
	
	@Override
	public boolean hasChildNodes()
	{
		return false;
	}
	
	@Override
	public Collection<WomNode> childNodes()
	{
		return Collections.emptyList();
	}
	
	@Override
	public WomNode getFirstChild()
	{
		return null;
	}
	
	@Override
	public WomNode getLastChild()
	{
		return null;
	}
	
	@Override
	public void appendChild(WomNode child) throws UnsupportedOperationException
	{
		doesNotSupportChildNodes();
	}
	
	@Override
	public void insertBefore(WomNode before, WomNode child) throws UnsupportedOperationException, IllegalArgumentException
	{
		doesNotSupportChildNodes();
	}
	
	@Override
	public void removeChild(WomNode child) throws UnsupportedOperationException
	{
		doesNotSupportChildNodes();
	}
	
	@Override
	public void replaceChild(WomNode search, WomNode replace) throws UnsupportedOperationException
	{
		doesNotSupportChildNodes();
	}
	
	protected void doesNotSupportChildNodes() throws UnsupportedOperationException
	{
		throw new UnsupportedOperationException("This node does not support child nodes!");
	}
	
	// =========================================================================
	
	@Override
	public Iterator<WomNode> iterator()
	{
		return new SiblingCollection<WomNode>(getFirstChild()).iterator();
	}
	
	// =========================================================================
	
	@Override
	public String toString()
	{
		return toString(new StringBuilder()).toString();
	}
	
	private StringBuilder toString(StringBuilder b)
	{
		switch (getNodeType())
		{
			case ATTRIBUTE:
				b.append(((WomAttribute) this).getName());
				b.append("=\"");
				b.append(StringUtils.escHtml(getValue()));
				b.append('"');
				break;
			
			case COMMENT:
				b.append("<!--");
				b.append(getValue());
				b.append("-->");
				break;
			
			case DOCUMENT:
			case ELEMENT:
				b.append('<');
				b.append(getNodeName());
				for (WomAttribute attr : getAttributes())
				{
					b.append(' ');
					if (attr instanceof WomBackbone)
						((WomBackbone) attr).toString(b);
					else
						b.append(attr.toString());
				}
				if (hasChildNodes())
				{
					b.append('>');
					for (WomNode child : childNodes())
					{
						if (child instanceof WomBackbone)
							((WomBackbone) child).toString(b);
						else
							b.append(child.toString());
					}
					b.append("</");
					b.append(getNodeName());
					b.append('>');
				}
				else
				{
					b.append(" />");
				}
				break;
			
			case TEXT:
				b.append(StringUtils.escHtml(getText()));
				break;
		}
		return b;
	}
}
