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
package org.sweble.wikitext.engine.wom;

import java.io.Serializable;
import java.util.Collection;

/**
 * The parent interface of every node in the Wikitext Object Model.
 */
public interface WomNode
		extends
			Cloneable,
			Serializable,
			Iterable<WomNode>
{
	/**
	 * The version of the WOM standard represented by these classes.
	 */
	public static final String VERSION = "1.0";
	
	// ==[ Reflection ]=========================================================
	
	/**
	 * Returns the name of a node.
	 */
	public String getNodeName();
	
	/**
	 * Returns the type of a node.
	 */
	public WomNodeType getNodeType();
	
	// ==[ Textual content ]====================================================
	
	/**
	 * Return the text content of a text node. Returns <code>null</node> for
	 * other types of nodes. An empty text node will return the empty string and
	 * not <code>null</code>!
	 */
	public String getText();
	
	/**
	 * Return the value of a text node and other value carrying nodes. Returns
	 * <code>null</node> for other types of nodes. Attributes with an empty
	 * value or empty text nodes will return the empty string and not
	 * <code>null</code>!
	 */
	public String getValue();
	
	// ==[ Text manipulation ]==================================================
	
	/**
	 * Append text to the text of this node.
	 * 
	 * @throws UnsupportedOperationException
	 *             If this node is not a text node.
	 * @throws NullPointerException
	 *             Thrown if <code>text</code> is <code>null</code>.
	 * @throws IllegalArgumentException
	 *             Thrown if <code>text</code> contains isolated surrogates.
	 */
	public void appendText(String text) throws UnsupportedOperationException, NullPointerException, IllegalArgumentException;
	
	/**
	 * Delete a range of the text of this node.
	 * 
	 * @param from
	 *            The first character that will be deleted.
	 * @param length
	 *            The number of characters that will be deleted.
	 * 
	 * @throws UnsupportedOperationException
	 *             If this node is not a text node.
	 * @throws UnsupportedOperationException
	 *             If the operation leaves isolated surrogates in the resulting
	 *             text.
	 * @throws IndexOutOfBoundsException
	 *             If the given range is invalid.
	 */
	public void deleteText(int from, int length)
			throws UnsupportedOperationException,
			IndexOutOfBoundsException;
	
	/**
	 * Insert text at a specified position.
	 * 
	 * @throws UnsupportedOperationException
	 *             If this node is not a text node.
	 * @throws IllegalArgumentException
	 *             Thrown if <code>text</code> contains isolated surrogates or
	 *             the operation would leave isolated surrogates in the
	 *             resulting text.
	 * @throws IndexOutOfBoundsException
	 *             If the given range is invalid.
	 * @throws NullPointerException
	 *             Thrown if <code>text</code> is <code>null</code>.
	 */
	public void insertText(int at, String text)
			throws UnsupportedOperationException,
			IndexOutOfBoundsException;
	
	/**
	 * Replaces all occurrences of the given substring with another text.
	 * 
	 * @param search
	 *            The substring to replace.
	 * @param replacement
	 *            The new text that will replace the <code>search</code>
	 *            substring.
	 * @return Returns <code>true</code> if the substring was found and replaced
	 *         at least once. If the given substring is not found,
	 *         <code>false</code> is returned.
	 * @throws UnsupportedOperationException
	 *             If this node is not a text node.
	 * @throws NullPointerException
	 *             Thrown if <code>search</code> or <code>replacement</code> is
	 *             <code>null</code>.
	 */
	public boolean replaceText(String search, String replacement) throws UnsupportedOperationException;
	
	/**
	 * Replaces a specified range of the text of this node with another text.
	 * 
	 * @param from
	 *            The first character that will be replaced.
	 * @param length
	 *            The number of characters that will be replaced.
	 * @param replacement
	 *            The new text that will replace the given range of characters.
	 * 
	 * @return The replaced text.
	 * 
	 * @throws UnsupportedOperationException
	 *             If this node is not a text node.
	 * @throws IndexOutOfBoundsException
	 *             If the given range is invalid.
	 * @throws NullPointerException
	 *             Thrown if <code>replacement</code> is <code>null</code>.
	 */
	public String replaceText(int from, int length, String replacement)
			throws UnsupportedOperationException,
			IndexOutOfBoundsException;
	
	// ==[ Attributes ]=========================================================
	
	/**
	 * Returns whether this node supports attributes.
	 */
	public boolean supportsAttributes();
	
	/**
	 * Returns an immutable collection containing the XML attributes of a node.
	 * Nodes that don't support attributes will return an empty collection.
	 */
	public Collection<WomAttribute> getAttributes();
	
	/**
	 * Returns the value of an attribute node. If no attribute with the given
	 * name exists <code>null</code> is returned. Nodes that don't support
	 * attributes will return <code>null</code>.
	 * 
	 * @throws NullPointerException
	 *             Thrown if <code>name</code> is <code>null</code>.
	 */
	public String getAttribute(String name);
	
	/**
	 * Returns an attribute. If no attribute with the given name exists
	 * <code>null</code> is returned. Nodes that don't support attributes will
	 * return <code>null</code>.
	 * 
	 * @throws NullPointerException
	 *             Thrown if <code>name</code> is <code>null</code>.
	 */
	public WomAttribute getAttributeNode(String name);
	
	// ==[ Attribute modification ]=============================================
	
	/**
	 * Remove an attribute.
	 * 
	 * @return The removed attribute node or <code>null</code> if no such
	 *         attribute exists.
	 * 
	 * @throws UnsupportedOperationException
	 *             If the node does not support attributes.
	 * @throws NullPointerException
	 *             Thrown if <code>name</code> is <code>null</code>.
	 */
	public WomAttribute removeAttribute(String name)
			throws UnsupportedOperationException;
	
	/**
	 * Remove an attribute.
	 * 
	 * @throws UnsupportedOperationException
	 *             If the node does not support attributes.
	 * @throws IllegalArgumentException
	 *             If the given node is not an attribute of this node.
	 * @throws NullPointerException
	 *             Thrown if <code>attr</code> is <code>null</code>.
	 */
	public void removeAttributeNode(WomAttribute attr)
			throws UnsupportedOperationException,
			IllegalArgumentException;
	
	/**
	 * Sets an attribute node. If the attribute already exists, it will be
	 * replaced by the given attribute. Otherwise, a new attribute will be
	 * created.
	 * 
	 * @param value
	 *            Passing <code>null</code> as value will remove the attribute.
	 * 
	 * @return The old attribute or <code>null</code> if the attribute did not
	 *         exist.
	 * 
	 * @throws UnsupportedOperationException
	 *             If the node does not support attributes.
	 * @throws NullPointerException
	 *             Thrown if <code>name</code> is <code>null</code>.
	 */
	public WomAttribute setAttribute(String name, String value)
			throws UnsupportedOperationException;
	
	/**
	 * Sets an attribute node. If the attribute already exists, it will be
	 * replaced by the given attribute.
	 * 
	 * @return The old attribute or <code>null</code> if the attribute did not
	 *         exist.
	 * 
	 * @throws UnsupportedOperationException
	 *             If the node does not support attributes.
	 * @throws NullPointerException
	 *             Thrown if <code>attr</code> is <code>null</code>.
	 */
	public WomAttribute setAttributeNode(WomAttribute attr)
			throws UnsupportedOperationException;
	
	// ==[ Navigation ]=========================================================
	
	/**
	 * Return the parent node of this node.
	 */
	public WomNode getParent();
	
	/**
	 * Returns whether this node supports children.
	 */
	public boolean supportsChildren();
	
	/**
	 * Return whether this node has any children. Returns <code>false</code> if
	 * the node does not support children.
	 */
	public boolean hasChildNodes();
	
	/**
	 * Returns an immutable collection containing the children of this node.
	 * Nodes that don't support children will return an empty collection.
	 */
	public Collection<WomNode> childNodes();
	
	/**
	 * Returns the first child of this node or <code>null</code> if this node
	 * has no children or doesn't support children.
	 */
	public WomNode getFirstChild();
	
	/**
	 * Returns the last child of this node or <code>null</code> if this node has
	 * no children or doesn't support children.
	 */
	public WomNode getLastChild();
	
	/**
	 * Return the next node on the same level as this node. Returns
	 * <code>null</code> if there is no next sibling.
	 */
	public WomNode getNextSibling();
	
	/**
	 * Return the previous node on the same level as this node. Returns
	 * <code>null</code> if there is no previous sibling.
	 */
	public WomNode getPrevSibling();
	
	// ==[ Tree modification ]==================================================
	
	/**
	 * Adds a node to the end of the list of children of this node.
	 * 
	 * @throws UnsupportedOperationException
	 *             If the node does not support children.
	 * @throws NullPointerException
	 *             Thrown if <code>child</code> is <code>null</code>.
	 */
	public void appendChild(WomNode child) throws UnsupportedOperationException;
	
	/**
	 * Insert a node into the list of children of this node. The node will be
	 * inserted before another given child node.
	 * 
	 * @throws UnsupportedOperationException
	 *             If the node does not support children.
	 * @throws IllegalArgumentException
	 *             If the <code>before</code> node is not a child of this node.
	 * @throws NullPointerException
	 *             Thrown if <code>before</code> or <code>child</code> is
	 *             <code>null</code>.
	 */
	public void insertBefore(WomNode before, WomNode child)
			throws UnsupportedOperationException,
			IllegalArgumentException;
	
	/**
	 * Remove the given child node from the list of children.
	 * 
	 * @throws UnsupportedOperationException
	 *             If the node does not support children.
	 * @throws IllegalArgumentException
	 *             If the <code>child</code> node is not a child of this node.
	 * @throws NullPointerException
	 *             Thrown if <code>child</code> is <code>null</code>.
	 */
	public void removeChild(WomNode child)
			throws UnsupportedOperationException,
			IllegalArgumentException;
	
	/**
	 * Replace a given child node with another node.
	 * 
	 * @return Returns <code>true</code> if the child was replaced. If the given
	 *         node is not a child of this node, <code>false</code> is returned.
	 * 
	 * @throws UnsupportedOperationException
	 *             If the node does not support children.
	 * @throws IllegalArgumentException
	 *             If the <code>search</code> node is not a child of this node.
	 * @throws NullPointerException
	 *             Thrown if <code>search</code> or <code>replace</code> is
	 *             <code>null</code>.
	 */
	public void replaceChild(WomNode search, WomNode replace)
			throws UnsupportedOperationException,
			IllegalArgumentException;
	
	// ==[ Cloning ]============================================================
	
	/**
	 * Create a copy of this node. The created node will not be part of the WOM
	 * tree which the original node belonged to (or any other tree, until it is
	 * added to some other tree).
	 */
	public WomNode cloneNode();
}
