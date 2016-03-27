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
package org.sweble.wom3;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;

import org.w3c.dom.DOMException;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.UserDataHandler;

/**
 * The parent interface of every node in the Wiki Object Model.
 */
public interface Wom3Node
		extends
			Cloneable,
			Serializable,
			Iterable<Wom3Node>,
			Node
{
	/**
	 * The version of the WOM standard represented by these classes.
	 */
	public static final String VERSION = "3.0";

	public static final String WOM_NS_URI = "http://sweble.org/schema/wom30";

	public static final String DEFAULT_WOM_NS_PREFIX = "wom";

	// =========================================================================
	// 
	// DOM Level 3 Node Interface Attributes
	// 
	// =========================================================================

	/**
	 * The name of this node, depending on its type; see the following table:
	 * 
	 * <pre>
	 * <table border="1" cellspacing="0" style="width: 60em;">
	 *   <tr><th>Interface</th><th>nodeName</th></tr>
	 *   <tr><td>Attr</td><td>same as Attr.name</td></tr>
	 *   <tr><td>CDATASection</td><td>#cdata-section</td></tr>
	 *   <tr><td>Comment</td><td>#comment</td></tr>
	 *   <tr><td>Document</td><td>#document</td></tr>
	 *   <tr><td>DocumentFragment</td><td>#document-fragment</td></tr>
	 *   <tr><td>DocumentType</td><td>same as DocumentType.name</td></tr>
	 *   <tr><td>Element</td><td>same as Element.tagName</td></tr>
	 *   <tr><td>Entity</td><td>entity name</td></tr>
	 *   <tr><td>EntityReference</td><td>name of entity referenced</td></tr>
	 *   <tr><td>Notation</td><td>notation name</td></tr>
	 *   <tr><td>ProcessingInstruction</td><td>same as ProcessingInstruction.target</td></tr>
	 *   <tr><td>Text</td><td>#text</td></tr>
	 * </table>
	 * </pre>
	 * 
	 * @since DOM Level 1
	 * @since WOM Version 3
	 */
	@Override
	public String getNodeName();

	/**
	 * The value of this node, depending on its type; see the following table.
	 * When it is defined to be null, setting it has no effect, including if the
	 * node is read-only.
	 * 
	 * <pre>
	 * <table border="1" cellspacing="0" style="width: 60em;">
	 *   <tr><th>Interface</th><th>nodeValue</th></tr>
	 *   <tr><td>Attr</td><td>same as Attr.value</td></tr>
	 *   <tr><td>CDATASection</td><td>same as CharacterData.data, the content of the CDATA Section</td></tr>
	 *   <tr><td>Comment</td><td>same as CharacterData.data, the content of the comment</td></tr>
	 *   <tr><td>Document</td><td>null</td></tr>
	 *   <tr><td>DocumentFragment</td><td>null</td></tr>
	 *   <tr><td>DocumentType</td><td>null</td></tr>
	 *   <tr><td>Element</td><td>null</td></tr>
	 *   <tr><td>Entity</td><td>null</td></tr>
	 *   <tr><td>EntityReference</td><td>null</td></tr>
	 *   <tr><td>Notation</td><td>null</td></tr>
	 *   <tr><td>ProcessingInstruction</td><td>same as ProcessingInstruction.data</td></tr>
	 *   <tr><td>Text</td><td>same as CharacterData.data, the content of the text node</td></tr>
	 * </table>
	 * </pre>
	 * 
	 * @throws DOMException
	 *             <dl>
	 *             <dt>DOMSTRING_SIZE_ERR</dt>
	 *             <dd>Raised when it would return more characters than fit in a
	 *             DOMString variable on the implementation platform.</dd>
	 *             </dl>
	 * 
	 * @since DOM Level 1
	 * @since WOM Version 3
	 */
	@Override
	public String getNodeValue() throws DOMException;

	/**
	 * The value of this node, depending on its type; see the following table.
	 * When it is defined to be null, setting it has no effect, including if the
	 * node is read-only.
	 * 
	 * <pre>
	 * <table border="1" cellspacing="0" style="width: 60em;">
	 *   <tr><th>Interface</th><th>nodeValue</th></tr>
	 *   <tr><td>Attr</td><td>same as Attr.value</td></tr>
	 *   <tr><td>CDATASection</td><td>same as CharacterData.data, the content of the CDATA Section</td></tr>
	 *   <tr><td>Comment</td><td>same as CharacterData.data, the content of the comment</td></tr>
	 *   <tr><td>Document</td><td>null</td></tr>
	 *   <tr><td>DocumentFragment</td><td>null</td></tr>
	 *   <tr><td>DocumentType</td><td>null</td></tr>
	 *   <tr><td>Element</td><td>null</td></tr>
	 *   <tr><td>Entity</td><td>null</td></tr>
	 *   <tr><td>EntityReference</td><td>null</td></tr>
	 *   <tr><td>Notation</td><td>null</td></tr>
	 *   <tr><td>ProcessingInstruction</td><td>same as ProcessingInstruction.data</td></tr>
	 *   <tr><td>Text</td><td>same as CharacterData.data, the content of the text node</td></tr>
	 * </table>
	 * </pre>
	 * 
	 * @throws DOMException
	 *             <dl>
	 *             <dt>NO_MODIFICATION_ALLOWED_ERR</dt>
	 *             <dd>Raised when the node is readonly and if it is not defined
	 *             to be null.</dd>
	 *             </dl>
	 * 
	 * @since DOM Level 1
	 * @since WOM Version 3
	 */
	@Override
	public void setNodeValue(String nodeValue) throws DOMException;

	/**
	 * A code representing the type of the underlying object.
	 * 
	 * @since DOM Level 1
	 * @since WOM Version 3
	 */
	@Override
	public short getNodeType();

	/**
	 * The Document object associated with this node. This is also the Document
	 * object used to create new nodes. When this node is a Document or a
	 * DocumentType which is not used with any Document yet, this is null.
	 * 
	 * @since DOM Level 1, modified in DOM Level 2
	 * @since WOM Version 3
	 */
	@Override
	public Wom3Document getOwnerDocument();

	/**
	 * The parent of this node. All nodes, except Attr, Document,
	 * DocumentFragment, Entity, and Notation may have a parent. However, if a
	 * node has just been created and not yet added to the tree, or if it has
	 * been removed from the tree, this is null.
	 * 
	 * <pre>
	 * <table border="1" cellspacing="0" style="width: 60em;">
	 *   <tr><th>Interface</th><th>parent</th></tr>
	 *   <tr><td>Attr</td><td>no</td></tr>
	 *   <tr><td>CDATASection</td><td><b>yes</b></td></tr>
	 *   <tr><td>Comment</td><td><b>yes</b></td></tr>
	 *   <tr><td>Document</td><td>no</td></tr>
	 *   <tr><td>DocumentFragment</td><td>no</td></tr>
	 *   <tr><td>DocumentType</td><td><b>yes</b></td></tr>
	 *   <tr><td>Element</td><td><b>yes</b></td></tr>
	 *   <tr><td>Entity</td><td>no</td></tr>
	 *   <tr><td>EntityReference</td><td><b>yes</b></td></tr>
	 *   <tr><td>Notation</td><td>no</td></tr>
	 *   <tr><td>ProcessingInstruction</td><td><b>yes</b></td></tr>
	 *   <tr><td>Text</td><td><b>yes</b></td></tr>
	 * </table>
	 * </pre>
	 * 
	 * @since DOM Level 1
	 * @since WOM Version 3
	 */
	@Override
	public Wom3Node getParentNode();

	/**
	 * The first child of this node. If there is no such node, this returns
	 * null.
	 * 
	 * @since DOM Level 1
	 * @since WOM Version 3
	 */
	@Override
	public Wom3Node getFirstChild();

	/**
	 * The last child of this node. If there is no such node, this returns null.
	 * 
	 * @since DOM Level 1
	 * @since WOM Version 3
	 */
	@Override
	public Wom3Node getLastChild();

	/**
	 * The node immediately following this node. If there is no such node, this
	 * returns null.
	 * 
	 * @since DOM Level 1
	 * @since WOM Version 3
	 */
	@Override
	public Wom3Node getPreviousSibling();

	/**
	 * The node immediately preceding this node. If there is no such node, this
	 * returns null.
	 * 
	 * @since DOM Level 1
	 * @since WOM Version 3
	 */
	@Override
	public Wom3Node getNextSibling();

	/**
	 * A NodeList that contains all children of this node. If there are no
	 * children, this is a NodeList containing no nodes.
	 * 
	 * @since DOM Level 1
	 * @since WOM Version 3
	 */
	@Override
	public NodeList getChildNodes();

	/**
	 * A NamedNodeMap containing the attributes of this node (if it is an
	 * Element) or null otherwise.
	 * 
	 * @since DOM Level 1
	 * @since WOM Version 3
	 */
	@Override
	public NamedNodeMap getAttributes();

	/**
	 * The namespace URI of this node, or null if it is unspecified (see XML
	 * Namespaces).
	 * 
	 * <p>
	 * This is not a computed value that is the result of a namespace lookup
	 * based on an examination of the namespace declarations in scope. It is
	 * merely the namespace URI given at creation time.
	 * 
	 * <p>
	 * For nodes of any type other than ELEMENT_NODE and ATTRIBUTE_NODE and
	 * nodes created with a DOM Level 1 method, such as
	 * Document.createElement(), this is always null.
	 * 
	 * <p>
	 * Note: Per the Namespaces in XML Specification [XML Namespaces] an
	 * attribute does not inherit its namespace from the element it is attached
	 * to. If an attribute is not explicitly given a namespace, it simply has no
	 * namespace.
	 * 
	 * @since DOM Level 2
	 * @since WOM Version 3
	 */
	@Override
	public String getNamespaceURI();

	/**
	 * The namespace prefix of this node, or null if it is unspecified.
	 * 
	 * <p>
	 * For nodes of any type other than ELEMENT_NODE and ATTRIBUTE_NODE and
	 * nodes created with a DOM Level 1 method, such as createElement from the
	 * Document interface, this is always null.
	 * 
	 * @since DOM Level 2
	 * @since WOM Version 3
	 */
	@Override
	public String getPrefix();

	/**
	 * The namespace prefix of this node. When it is defined to be null, setting
	 * it has no effect, including if the node is read-only.
	 * 
	 * <p>
	 * Note that setting this attribute, when permitted, changes the nodeName
	 * attribute, which holds the qualified name, as well as the tagName and
	 * name attributes of the Element and Attr interfaces, when applicable.
	 * 
	 * <p>
	 * Setting the prefix to null makes it unspecified, setting it to an empty
	 * string is implementation dependent.
	 * 
	 * <p>
	 * Note also that changing the prefix of an attribute that is known to have
	 * a default value, does not make a new attribute with the default value and
	 * the original prefix appear, since the namespaceURI and localName do not
	 * change.
	 * 
	 * @throws DOMException
	 *             <dl>
	 *             <dt>INVALID_CHARACTER_ERR</dt><dd>Raised if the specified
	 *             prefix contains an illegal character according to the XML
	 *             version in use specified in the Document.xmlVersion
	 *             attribute.</dd>
	 * 
	 *             <dt>NO_MODIFICATION_ALLOWED_ERR</dt><dd> Raised if this node
	 *             is readonly.</dd>
	 * 
	 *             <dt>NAMESPACE_ERR</dt><dd>Raised if the specified prefix is
	 *             malformed per the Namespaces in XML specification, if the
	 *             namespaceURI of this node is null, if the specified prefix is
	 *             "xml" and the namespaceURI of this node is different from
	 *             "http://www.w3.org/XML/1998/namespace", if this node is an
	 *             attribute and the specified prefix is "xmlns" and the
	 *             namespaceURI of this node is different from
	 *             "http://www.w3.org/2000/xmlns/", or if this node is an
	 *             attribute and the qualifiedName of this node is "xmlns" [XML
	 *             Namespaces].</dd>
	 *             </dl>
	 * 
	 * @since DOM Level 2
	 * @since WOM Version 3
	 */
	@Override
	public void setPrefix(String prefix) throws DOMException;

	/**
	 * Returns the local part of the qualified name of this node.
	 * 
	 * <p>
	 * For nodes of any type other than ELEMENT_NODE and ATTRIBUTE_NODE and
	 * nodes created with a DOM Level 1 method, such as
	 * Document.createElement(), this is always null.
	 * 
	 * @since DOM Level 2
	 * @since WOM Version 3
	 */
	public String getLocalName();

	/**
	 * The absolute base URI of this node or null if the implementation wasn't
	 * able to obtain an absolute URI. This value is computed as described in
	 * Base URIs. However, when the Document supports the feature "HTML" [DOM
	 * Level 2 HTML], the base URI is computed using first the value of the href
	 * attribute of the HTML BASE element if any, and the value of the
	 * documentURI attribute from the Document interface otherwise.
	 * 
	 * @since DOM Level 3
	 * @since WOM Version 3
	 */
	@Override
	public String getBaseURI();

	/**
	 * This attribute returns the text content of this node and its descendants.
	 * 
	 * <p>
	 * On getting, no serialization is performed, the returned string does not
	 * contain any markup. No whitespace normalization is performed and the
	 * returned string does not contain the white spaces in element content (see
	 * the attribute Text.isElementContentWhitespace). Similarly, on setting, no
	 * parsing is performed either, the input string is taken as pure textual
	 * content.
	 * 
	 * <p>
	 * The string returned is made of the text content of this node depending on
	 * its type, as defined below:
	 * 
	 * <pre>
	 * <table border="1" cellspacing="0" style="width: 60em;">
	 *   <tr>
	 *     <th>Node type</th>
	 *     <th>Content</th>
	 *   </tr>
	 *   <tr>
	 *     <td>ELEMENT_NODE, ATTRIBUTE_NODE, ENTITY_NODE, ENTITY_REFERENCE_NODE, DOCUMENT_FRAGMENT_NODE</td>
	 *     <td>concatenation of the textContent attribute value of every child node, excluding COMMENT_NODE and PROCESSING_INSTRUCTION_NODE nodes. This is the empty string if the node has no children.</td>
	 *   </tr>
	 *   <tr>
	 *     <td>TEXT_NODE, CDATA_SECTION_NODE, COMMENT_NODE, PROCESSING_INSTRUCTION_NODE</td>
	 *     <td>nodeValue</td>
	 *   </tr>
	 *   <tr>
	 *     <td>DOCUMENT_NODE, DOCUMENT_TYPE_NODE, NOTATION_NODE</td>
	 *     <td>null</td>
	 *   </tr>
	 * </table>
	 * </pre>
	 * 
	 * @throws DOMException
	 *             <dl>
	 *             <dt>DOMSTRING_SIZE_ERR</dt><dd>Raised when it would return
	 *             more characters than fit in a DOMString variable on the
	 *             implementation platform.</dd>
	 *             </dl>
	 * 
	 * @since DOM Level 3
	 * @since WOM Version 3
	 */
	@Override
	public String getTextContent() throws DOMException;

	/**
	 * Sets the text content of this node.
	 * 
	 * When it is defined to be null, setting it has no effect. On setting, any
	 * possible children this node may have are removed and, if it the new
	 * string is not empty or null, replaced by a single Text node containing
	 * the string this attribute is set to.
	 * 
	 * @throws DOMException
	 *             <dl>
	 *             <dt>NO_MODIFICATION_ALLOWED_ERR</dt>
	 *             <dd>Raised when the node is readonly.</dd>
	 *             </dl>
	 * 
	 * @since DOM Level 3
	 * @since WOM Version 3
	 */
	@Override
	public void setTextContent(String textContent) throws DOMException;

	// =========================================================================
	// 
	// DOM Level 3 Node Interface Operations
	// 
	// =========================================================================

	/**
	 * Returns whether this node (if it is an element) has any attributes.
	 * 
	 * @since DOM Level 2
	 * @since WOM Version 3
	 */
	@Override
	public boolean hasAttributes();

	/**
	 * Returns whether this node has any children.
	 * 
	 * @since DOM Level 1
	 * @since WOM Version 3
	 */
	@Override
	public boolean hasChildNodes();

	/**
	 * Inserts the node newChild before the existing child node refChild. If
	 * refChild is null, insert newChild at the end of the list of children.
	 * 
	 * <p>
	 * If newChild is a DocumentFragment object, all of its children are
	 * inserted, in the same order, before refChild. If the newChild is already
	 * in the tree, it is first removed.
	 * 
	 * <p>
	 * Note: Inserting a node before itself is implementation dependent.
	 * 
	 * @return The node being inserted.
	 * 
	 * @throws DOMException
	 *             <dl>
	 *             <dt>HIERARCHY_REQUEST_ERR</dt><dd>Raised if this node is of a
	 *             type that does not allow children of the type of the newChild
	 *             node, or if the node to insert is one of this node's
	 *             ancestors or this node itself, or if this node is of type
	 *             Document and the DOM application attempts to insert a second
	 *             DocumentType or Element node.</dd>
	 * 
	 *             <dt>WRONG_DOCUMENT_ERR</dt><dd>Raised if newChild was created
	 *             from a different document than the one that created this
	 *             node.</dd>
	 * 
	 *             <dt>NO_MODIFICATION_ALLOWED_ERR</dt><dd>Raised if this node
	 *             is readonly or if the parent of the node being inserted is
	 *             readonly.</dd>
	 * 
	 *             <dt>NOT_FOUND_ERR</dt><dd>Raised if refChild is not a child
	 *             of this node.</dd>
	 * 
	 *             <dt>NOT_SUPPORTED_ERR</dt><dd>If this node is of type
	 *             Document, this exception might be raised if the DOM
	 *             implementation doesn't support the insertion of a
	 *             DocumentType or Element node.</dd>
	 *             </dl>
	 * 
	 * @since DOM Level 1, modified in DOM Level 3
	 * @since WOM Version 3
	 */
	@Override
	public Wom3Node insertBefore(Node newChild, Node refChild) throws DOMException;

	/**
	 * Replaces the child node oldChild with newChild in the list of children,
	 * and returns the oldChild node.
	 * 
	 * <p>
	 * If newChild is a DocumentFragment object, oldChild is replaced by all of
	 * the DocumentFragment children, which are inserted in the same order. If
	 * the newChild is already in the tree, it is first removed.
	 * 
	 * <p>
	 * Note: Replacing a node with itself is implementation dependent.
	 * 
	 * @return The node replaced.
	 * 
	 * @throws DOMException
	 *             <dl>
	 *             <dt>HIERARCHY_REQUEST_ERR</dt><dd>Raised if this node is of a
	 *             type that does not allow children of the type of the newChild
	 *             node, or if the node to put in is one of this node's
	 *             ancestors or this node itself, or if this node is of type
	 *             Document and the result of the replacement operation would
	 *             add a second DocumentType or Element on the Document node.
	 *             </dd>
	 * 
	 *             <dt>WRONG_DOCUMENT_ERR</dt><dd>Raised if newChild was created
	 *             from a different document than the one that created this
	 *             node.</dd>
	 * 
	 *             <dt>NO_MODIFICATION_ALLOWED_ERR</dt><dd>Raised if this node
	 *             or the parent of the new node is readonly.</dd>
	 * 
	 *             <dt>NOT_FOUND_ERR</dt><dd>Raised if oldChild is not a child
	 *             of this node.</dd>
	 * 
	 *             <dt>NOT_SUPPORTED_ERR</dt><dd>if this node is of type
	 *             Document, this exception might be raised if the DOM
	 *             implementation doesn't support the replacement of the
	 *             DocumentType child or Element child.</dd>
	 *             </dl>
	 * 
	 * @since DOM Level 1, modified in DOM Level 3
	 * @since WOM Version 3
	 */
	@Override
	public Wom3Node replaceChild(Node newChild, Node oldChild) throws DOMException;

	/**
	 * Removes the child node indicated by oldChild from the list of children,
	 * and returns it.
	 * 
	 * @return The node removed.
	 * 
	 * @throws DOMException
	 *             <dl>
	 *             <dt>NO_MODIFICATION_ALLOWED_ERR</dt>
	 *             <dd>Raised if this node is readonly.</dd>
	 * 
	 *             <dt>NOT_FOUND_ERR</dt>
	 *             <dd>Raised if oldChild is not a child of this node.</dd>
	 * 
	 *             <dt>NOT_SUPPORTED_ERR</dt>
	 *             <dd>if this node is of type Document, this exception might be
	 *             raised if the DOM implementation doesn't support the removal
	 *             of the DocumentType child or the Element child.</dd>
	 *             </dl>
	 * 
	 * @since DOM Level 1, modified in DOM Level 3
	 * @since WOM Version 3
	 */
	@Override
	public Wom3Node removeChild(Node child) throws DOMException;

	/**
	 * Adds the node newChild to the end of the list of children of this node.
	 * If the newChild is already in the tree, it is first removed.
	 * 
	 * @return The node added.
	 * 
	 * @throws DOMException
	 *             <dl>
	 *             <dt>HIERARCHY_REQUEST_ERR</dt>
	 *             <dd>Raised if this node is of a type that does not allow
	 *             children of the type of the newChild node, or if the node to
	 *             append is one of this node's ancestors or this node itself,
	 *             or if this node is of type Document and the DOM application
	 *             attempts to append a second DocumentType or Element node.
	 * 
	 *             <dt>WRONG_DOCUMENT_ERR</dt>
	 *             <dd>Raised if newChild was created from a different document
	 *             than the one that created this node.
	 * 
	 *             <dt>NO_MODIFICATION_ALLOWED_ERR</dt>
	 *             <dd>Raised if this node is readonly or if the previous parent
	 *             of the node being inserted is readonly.
	 * 
	 *             <dt>NOT_SUPPORTED_ERR</dt>
	 *             <dd>if the newChild node is a child of the Document node,
	 *             this exception might be raised if the DOM implementation
	 *             doesn't support the removal of the DocumentType child or
	 *             Element child.
	 *             </dl>
	 * 
	 * @since DOM Level 1, modified in DOM Level 3
	 * @since WOM Version 3
	 */
	@Override
	public Wom3Node appendChild(Node child) throws DOMException;

	/**
	 * @since DOM Level 1, modified in DOM Level 3
	 * @since WOM Version 3
	 */
	@Override
	public void normalize();

	/**
	 * @since DOM Level 3
	 * @since WOM Version 3
	 */
	@Override
	public String lookupPrefix(String namespaceURI);

	/**
	 * @since DOM Level 3
	 * @since WOM Version 3
	 */
	@Override
	public String lookupNamespaceURI(String prefix);

	/**
	 * @since DOM Level 3
	 * @since WOM Version 3
	 */
	@Override
	public boolean isDefaultNamespace(String namespaceURI);

	/**
	 * @since DOM Level 3
	 * @since WOM Version 3
	 */
	@Override
	public boolean isSameNode(Node other);

	/**
	 * @since DOM Level 3
	 * @since WOM Version 3
	 */
	@Override
	public boolean isEqualNode(Node arg);

	/**
	 * @since DOM Level 3
	 * @since WOM Version 3
	 */
	@Override
	public short compareDocumentPosition(Node other) throws DOMException;

	/**
	 * @since DOM Level 2
	 * @since WOM Version 3
	 */
	@Override
	public boolean isSupported(String feature, String version);

	/**
	 * @since DOM Level 3
	 * @since WOM Version 3
	 */
	@Override
	public Object getFeature(String feature, String version);

	/**
	 * @since DOM Level 3
	 * @since WOM Version 3
	 */
	@Override
	public Object setUserData(String key, Object data, UserDataHandler handler);

	/**
	 * @since DOM Level 3
	 * @since WOM Version 3
	 */
	@Override
	public Object getUserData(String key);

	/**
	 * @since DOM Level 1
	 * @since WOM Version 3
	 */
	@Override
	public Wom3Node cloneNode(boolean deep);

	// =========================================================================
	// 
	// WOM Version 3 Extensions
	// 
	// =========================================================================

	/**
	 * Returns an immutable collection containing this node's children.
	 */
	public Collection<Wom3Node> getWomChildNodes();

	/**
	 * Returns an immutable collection of this node's attributes.
	 */
	public Collection<Wom3Attribute> getWomAttributes();

	/**
	 * Returns a read-only iterator for this node's attributes.
	 */
	public Iterator<Wom3Attribute> attributeIterator();

	//	/**
	//	 * Returns whether this node supports attributes.
	//	 */
	//	public boolean supportsAttributes();

	//	/**
	//	 * Returns whether this node supports children.
	//	 */
	//	public boolean supportsChildNodes();

	//	// Later overrides org.w3c.dom.Element
	//	public String getAttribute(String name);
	//	
	//	// Later overrides org.w3c.dom.Element
	//	public Wom3Attribute getAttributeNode(String name);
	//	
	//	// Later overrides org.w3c.dom.Element
	//	public void removeAttribute(String name) throws DOMException;
	//	
	//	// Later overrides org.w3c.dom.Element
	//	public Wom3Attribute removeAttributeNode(Attr attr) throws DOMException;
	//	
	//	// Later overrides org.w3c.dom.Element
	//	public void setAttribute(String name, String value) throws DOMException;
	//	
	//	// Later overrides org.w3c.dom.Element
	//	public Wom3Attribute setAttributeNode(Attr attr) throws DOMException;
}
