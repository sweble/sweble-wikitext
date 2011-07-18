package org.sweble.wikitext.engine.dom;

import java.io.Serializable;
import java.util.Collection;

public interface DomNode
        extends
            Cloneable,
            Serializable
{
	// ==[ Reflection ]=========================================================
	
	/**
	 * Returns the name of a node.
	 */
	public String getNodeName();
	
	/**
	 * Returns the type of a node.
	 */
	public DomNodeType getNodeType();
	
	// ==[ Textual content ]====================================================
	
	/**
	 * Return the text content of a node. Returns <code>null</node> for other 
	 * types of nodes. An empty text node will return the empty string and not 
	 * <code>null</code>!
	 */
	public String getText();
	
	/**
	 * Return the value of a text node or an attribute node. Returns
	 * <code>null</node> for other types of nodes. Attributes with an empty 
	 * value or empty text nodes will return the empty string and not 
	 * <code>null</code>!
	 */
	public String getValue();
	
	// ==[ Attributes ]=========================================================
	
	/**
	 * Append text to the text of this node.
	 * 
	 * @throws UnsupportedOperationException
	 *             If this node is not a text node.
	 */
	public void appendText(String text) throws UnsupportedOperationException;
	
	/**
	 * Delete a range of the text of this node.
	 * 
	 * @param from
	 *            The first character that will be deleted.
	 * @param length
	 *            The number of characters that will be deleted.
	 * 
	 * @return The deleted text.
	 * 
	 * @throws UnsupportedOperationException
	 *             If this node is not a text node.
	 * @throws IndexOutOfBoundsException
	 *             If the given range is invalid.
	 */
	public String deleteText(int from, int length) throws UnsupportedOperationException, IndexOutOfBoundsException;
	
	/**
	 * Insert text at a specified position.
	 * 
	 * @throws UnsupportedOperationException
	 *             If this node is not a text node.
	 * @throws IndexOutOfBoundsException
	 *             If the given range is invalid.
	 */
	public void insertText(int at, String text) throws UnsupportedOperationException, IndexOutOfBoundsException;
	
	/**
	 * Replaces the text of this node with another text.
	 * 
	 * @return The replaced text.
	 * 
	 * @throws UnsupportedOperationException
	 *             If this node is not a text node.
	 */
	public String replaceText(String text) throws UnsupportedOperationException;
	
	/**
	 * Replaces a specified range of the text of this node with another text.
	 * 
	 * @param from
	 *            The first character that will be replaced.
	 * @param length
	 *            The number of characters that will be replaced.
	 * @param text
	 *            The new text that will replace the given range of characters.
	 * 
	 * @return The replaced text.
	 * 
	 * @throws UnsupportedOperationException
	 *             If this node is not a text node.
	 * @throws IndexOutOfBoundsException
	 *             If the given range is invalid.
	 */
	public String replaceText(int from, int length, String text) throws UnsupportedOperationException, IndexOutOfBoundsException;
	
	// ==[ Attributes ]=========================================================
	
	/**
	 * Returns whether this node supports attributes.
	 */
	public boolean supportsAttributes();
	
	/**
	 * Return a collection containing the XML attributes of a node. Nodes that
	 * don't support attributes will return an empty collection.
	 */
	public Collection<DomAttribute> getAttributes();
	
	/**
	 * Return the value of an attribute node. If no attribute with the given
	 * name exists <code>null</code> is returned. Nodes that don't support
	 * attributes will return an empty collection.
	 */
	public String getAttribute(String name);
	
	/**
	 * Return an attribute. If no attribute with the given name exists
	 * <code>null</code> is returned. Nodes that don't support attributes will
	 * return an empty collection.
	 */
	public DomAttribute getAttributeNode(String name);
	
	// ==[ Attribute modification ]=============================================
	
	/**
	 * Remove an attribute.
	 * 
	 * @return The removed attribute node or <code>null</code> if no such
	 *         attribute exists.
	 * 
	 * @throws UnsupportedOperationException
	 *             If the DOM node does not support attributes.
	 */
	public DomAttribute removeAttribute(String name) throws UnsupportedOperationException;
	
	/**
	 * Remove an attribute.
	 * 
	 * @throws UnsupportedOperationException
	 *             If the DOM node does not support attributes.
	 * @throws IllegalArgumentException
	 *             If the given node is not an attribute of this node.
	 */
	public void removeAttributeNode(DomAttribute attr) throws UnsupportedOperationException, IllegalArgumentException;
	
	/**
	 * Sets an attribute node. If the attribute already exists, it will be
	 * replaced by the given attribute. Otherwise, a new attribute will be
	 * created.
	 * 
	 * @return The old attribute or <code>null</code> if the attribute did not
	 *         exist.
	 * 
	 * @throws UnsupportedOperationException
	 *             If the DOM node does not support attributes.
	 */
	public DomAttribute setAttribute(String name, String value) throws UnsupportedOperationException;
	
	/**
	 * Sets an attribute node. If the attribute already exists, it will be
	 * replaced by the given attribute.
	 * 
	 * @return The old attribute or <code>null</code> if the attribute did not
	 *         exist.
	 * 
	 * @throws UnsupportedOperationException
	 *             If the DOM node does not support attributes.
	 * @throws IllegalArgumentException
	 *             If the given node is not an attribute of this node.
	 */
	public DomAttribute setAttributeNode(DomAttribute attr) throws UnsupportedOperationException, IllegalArgumentException;
	
	// ==[ Navigation ]=========================================================
	
	/**
	 * Return the parent node of this node.
	 */
	public DomNode getParent();
	
	/**
	 * Return whether this node has any children.
	 */
	public boolean hasChildNodes();
	
	/**
	 * Returns a collection containing the children of this node.
	 */
	public Collection<DomNode> childNodes();
	
	/**
	 * Returns the first child of this node or <code>null</code> if this node
	 * has no children.
	 */
	public DomNode getFirstChild();
	
	/**
	 * Returns the last child of this node or <code>null</code> if this node has
	 * no children.
	 */
	public DomNode getLastChild();
	
	/**
	 * Return the next node on the same level as this node. Returns
	 * <code>null</code> if there is no next sibling.
	 */
	public DomNode getNextSibling();
	
	/**
	 * Return the previous node on the same level as this node. Returns
	 * <code>null</code> if there is no previous sibling.
	 */
	public DomNode getPrevSibling();
	
	// ==[ Tree modification ]==================================================
	
	/**
	 * Adds a node to the end of the list of children of this node.
	 * 
	 * @throws UnsupportedOperationException
	 *             If the DOM node does not support children.
	 */
	public void appendChild(DomNode child) throws UnsupportedOperationException;
	
	/**
	 * Insert a node into the list of children of this node. The node will be
	 * inserted before another given child node.
	 * 
	 * @throws UnsupportedOperationException
	 *             If the DOM node does not support children.
	 * @throws IllegalArgumentException
	 *             If the <code>before</code> node is not a child of this node.
	 */
	public void insertBefore(DomNode before, DomNode child) throws UnsupportedOperationException, IllegalArgumentException;
	
	/**
	 * Remove the given child node from the list of children.
	 * 
	 * @return Returns <code>true</code> if the child was removed. If the given
	 *         node is not a child of this node, <code>false</code> is returned.
	 * 
	 * @throws UnsupportedOperationException
	 *             If the DOM node does not support children.
	 */
	public boolean removeChild(DomNode child) throws UnsupportedOperationException;
	
	/**
	 * Replace a given child node with another node.
	 * 
	 * @return Returns <code>true</code> if the child was replaced. If the given
	 *         node is not a child of this node, <code>false</code> is returned.
	 * 
	 * @throws UnsupportedOperationException
	 *             If the DOM node does not support children.
	 */
	public boolean replaceChild(DomNode search, DomNode replace) throws UnsupportedOperationException;
	
	// ==[ Cloning ]============================================================
	
	/**
	 * Create a copy of this node that is not part of any DOM tree.
	 */
	public DomNode cloneNode();
	
	// =========================================================================
	
	//getElementsByTagName()
}
