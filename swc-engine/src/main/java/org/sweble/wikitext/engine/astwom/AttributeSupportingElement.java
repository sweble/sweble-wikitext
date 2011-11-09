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

import org.sweble.wikitext.engine.astwom.adapters.NativeOrXmlAttributeAdapter;
import org.sweble.wikitext.engine.wom.WomAttribute;

import de.fau.cs.osr.ptk.common.ast.AstNode;
import de.fau.cs.osr.ptk.common.ast.NodeList;

public abstract class AttributeSupportingElement
        extends
            WomBackbone
{
	private static final long serialVersionUID = 1L;
	
	// =========================================================================
	
	public AttributeSupportingElement(AstNode astNode)
	{
		super(astNode);
	}
	
	// =========================================================================
	
	/**
	 * Get the attribute manager. The variable holding this object has to be
	 * placed in the concrete implementation of this class in order to allow for
	 * automatic generation of the needed delegate methods using Lombok. Once
	 * Lombok supports the generation of delegate methods from a method, this
	 * variable could be moved back into this class.
	 * 
	 * This method is used by attribute getters which won't fail when called on
	 * an element that doesn't support attributes.
	 * 
	 * @return The attribute manager or <code>null</code> if the element never
	 *         supports attributes in any AST incarnation.
	 */
	protected AttributeManager getAttribManager()
	{
		return null;
	}
	
	protected void setAttribManager(AttributeManager attribManager)
	{
		throw new InternalError();
	}
	
	/**
	 * This method is similar to getAttribManager() but fails with an exception
	 * if the element doesn't support attributes. It is used by attribute
	 * modifying methods.
	 * 
	 * @return The attribute manager.
	 * @throws UnsupportedOperationException
	 *             Thrown if the element never supports attributes in any AST
	 *             incarnation.
	 */
	protected final AttributeManager getAttribManagerOrFail() throws UnsupportedOperationException
	{
		AttributeManager attribManager = getAttribManager();
		if (attribManager == null)
		{
			doesNotSupportAttributes();
			return null;
		}
		return attribManager;
	}
	
	protected final AttributeManager getAttribManagerForModificationOrFail() throws UnsupportedOperationException
	{
		AttributeManager attribManager = getAttribManagerOrFail();
		if (attribManager.isEmptyManager())
		{
			attribManager = AttributeManager.createManager();
			setAttribManager(attribManager);
		}
		return attribManager;
	}
	
	/**
	 * Returns the AST attribute container node. If the AST element that backs
	 * this element doesn't support attributes, <code>null</code> is returned.
	 * 
	 * @return The AST attribute container node or <code>null</code> if the AST
	 *         element doesn't support attributes.
	 */
	protected NodeList getAstAttribContainer()
	{
		return null;
	}
	
	/**
	 * If AST node that backs this element doesn't support attributes this
	 * method can be called to convert the underlying AST node into an
	 * equivalent node that does support attributes. For example the italic
	 * ticks <code>''</code> do not support attributes and could be converted to
	 * the corresponding HTML element <code>&lt;i></code>. If such a conversion
	 * is not possible a <code>null</code> is returned.
	 * 
	 * @return The AST attribute container node of the new, attribute supporting
	 *         AST node.
	 * @throws UnsupportedOperationException
	 *             Thrown if no conversion is possible.
	 */
	protected NodeList addAstAttribSupport()
	{
		return null;
	}
	
	protected final NodeList getAstAttribContainerOrAddSupport()
	{
		NodeList attribContainer = getAstAttribContainer();
		if (attribContainer == null)
			attribContainer = addAstAttribSupport();
		return attribContainer;
	}
	
	/**
	 * Give the implementing class a chance to check and possibly modify the new
	 * value of the attribute.
	 * 
	 * The default behavior is to accept all values and return the value
	 * unaltered.
	 * 
	 * @param name
	 *            The name of the attribute whose value gets changed.
	 * @param value
	 *            The new value to check or <code>null</code> if the value shall
	 *            be removed.
	 * @param object
	 * @return The (possibly modified) value to set.
	 * @throws IllegalArgumentException
	 *             Thrown if the specified value is illegal.
	 * @throws UnsupportedOperationException
	 *             Thrown if <code>null</code> was passed as value but removal
	 *             of the attribute is not allowed or if the value of the
	 *             specified attribute cannot be modified at all.
	 */
	protected String checkAttributeValue(String name, String value) throws IllegalArgumentException, UnsupportedOperationException
	{
		return value;
	}
	
	// =========================================================================
	
	@Override
	public final boolean supportsAttributes()
	{
		return getAttribManager() != null;
	}
	
	@Override
	public final Collection<WomAttribute> getAttributes()
	{
		AttributeManager attrs = getAttribManager();
		if (attrs == null)
			return Collections.emptyList();
		return attrs.getAttributes();
	}
	
	@Override
	public final String getAttribute(String name)
	{
		AttributeManager attrs = getAttribManager();
		if (attrs == null)
			return null;
		return attrs.getAttribute(name);
	}
	
	@Override
	public final NativeOrXmlAttributeAdapter getAttributeNode(String name)
	{
		AttributeManager attrs = getAttribManager();
		if (attrs == null)
			return null;
		return attrs.getAttributeNode(name);
	}
	
	@Override
	public final NativeOrXmlAttributeAdapter removeAttribute(String name)
	{
		checkAttributeValue(name, null);
		return removeAttributeUnchecked(name);
	}
	
	protected NativeOrXmlAttributeAdapter removeAttributeUnchecked(String name)
	{
		// Don't use getAstAttribContainerOrAddSupport()!
		// If the element doesn't have any attributes then the caller is
		// removing something that's not there and we should not create the
		// attribute manager.
		AttributeManager attrs = getAttribManagerOrFail();
		return attrs.removeAttribute(name, getAstAttribContainer());
	}
	
	@Override
	public final void removeAttributeNode(WomAttribute attr) throws IllegalArgumentException
	{
		checkAttributeValue(attr.getName(), null);
		removeAttributeNodeUnchecked(attr);
	}
	
	protected void removeAttributeNodeUnchecked(WomAttribute attr)
	{
		// Don't use getAstAttribContainerOrAddSupport()!
		// If the element doesn't have any attributes then the caller is
		// removing something that's not there and we should not create the
		// attribute manager.
		AttributeManager attrs = getAttribManagerOrFail();
		attrs.removeAttributeNode(attr, this, getAstAttribContainer());
	}
	
	@Override
	public final NativeOrXmlAttributeAdapter setAttribute(String name, String value)
	{
		return setAttributeUnchecked(name, checkAttributeValue(name, value));
	}
	
	protected NativeOrXmlAttributeAdapter setAttributeUnchecked(String name, String value)
	{
		AttributeManager attrs = getAttribManagerForModificationOrFail();
		return attrs.setAttribute(name, value, this, getAstAttribContainerOrAddSupport());
	}
	
	@Override
	public final NativeOrXmlAttributeAdapter setAttributeNode(WomAttribute attr) throws IllegalArgumentException
	{
		String value = attr.getValue();
		String altered = checkAttributeValue(attr.getName(), value);
		if (altered != value)
			attr.setValue(value);
		
		return setAttributeNodeUnchecked(attr);
	}
	
	protected NativeOrXmlAttributeAdapter setAttributeNodeUnchecked(WomAttribute attr)
	{
		AttributeManager attrs = getAttribManagerForModificationOrFail();
		return attrs.setAttributeNode(attr, this, getAstAttribContainerOrAddSupport());
	}
}
