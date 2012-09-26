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
package org.sweble.wikitext.engine.astwom.adapters;

import org.sweble.wikitext.engine.astwom.AttributeDescriptor;
import org.sweble.wikitext.engine.astwom.AttributeSupportingElement;
import org.sweble.wikitext.engine.astwom.Toolbox;
import org.sweble.wikitext.engine.astwom.WomBackbone;
import org.sweble.wikitext.engine.wom.WomAttribute;
import org.sweble.wikitext.engine.wom.WomNodeType;
import org.sweble.wikitext.parser.nodes.XmlAttribute;
import org.sweble.wikitext.parser.utils.TextUtils;

import de.fau.cs.osr.ptk.common.ast.AstNode;
import de.fau.cs.osr.ptk.common.ast.NodeList;

/**
 * Attributes in the WOM can map to different things in the AST. For one, native
 * attributes are attributes of non-HTML elements. For example Wikitext links
 * have attributes like "target". In this case NativeOrXmlAttributeAdapter does
 * not have an AST node as counterpart. Instead the value will be set on the
 * link node directly.
 * 
 * HTML elements like &lt;b> (bold) on the other hand have the XmlAttribute AST
 * node as counterpart for the NativeOrXmlAttributeAdapter WOM node. In this
 * case the bold node (which "owns" the attribute) need not know about a value
 * or name change of one of its attributes. The NativeOrXmlAttributeAdapter
 * class can access its AST counterpart node directly and apply changes.
 * 
 * Finally, HTML elements like &lt;b> (bold) can have a native representation
 * (three ticks in the case of the bold element). These native analogs can not
 * carry attributes. Therefore, if an attribute is set on a native element, the
 * native element must be converted to its HTML analog. If a HTML element with
 * attributes were converted to its native analog, all attributes would have to
 * be dropped.
 * 
 * FIXME: NativeOrXmlAttributeAdapter does not yet inform the attribute
 * container of changes (be it name or value). This is especially problematic
 * for native attributes which do not have an AST counterpart but must be set on
 * the container AST node explicitly by the container. Right now changes are
 * stored in the WOM attribute but not in the container element. Thus the
 * changes are lost in the AST if the attribute does not have an AST attribute
 * as counterpart.
 * 
 */
public class NativeOrXmlAttributeAdapter
		extends
			WomBackbone
		implements
			WomAttribute
{
	private static final long serialVersionUID = 1L;
	
	private String name;
	
	/**
	 * This value must always be normalized.
	 */
	private String value;
	
	// =========================================================================
	
	/**
	 * Create a WOM attribute node from an AST attribute node. The attribute
	 * will be normalized according to the normalization mode reported by the
	 * descriptor.
	 * 
	 * @param astNode
	 *            The AST node to create a WOM attribute from.
	 * @param normalizedName
	 *            The normalized name of the attribute. Normalization only
	 *            applies to known attributes (XHTML and WOM). Names of other
	 *            attributes are not normalized and correspond to the original
	 *            in the AST.
	 * @param normalizedValue
	 *            The normalized value of the attribute. Normalization only
	 *            applies to known attributes (XHTML and WOM). Values of other
	 *            attributes are not normalized and correspond to the original
	 *            in the AST.
	 * @throws NullPointerException
	 *             Thrown if any argument is <code>null</code>.
	 */
	public NativeOrXmlAttributeAdapter(
			XmlAttribute astNode,
			String normalizedName,
			String normalizedValue) throws NullPointerException
	{
		super(astNode);
		
		if (astNode == null || normalizedName == null || normalizedValue == null)
			throw new NullPointerException();
		
		this.name = normalizedName;
		this.value = normalizedValue;
	}
	
	/**
	 * Create a WOM attribute from name and value.
	 * 
	 * A WOM attribute created this way will not have an AST node associated. To
	 * associate an AST node with a WOM attribute either use the respective
	 * constructor or call attachAstNode() after instantiation.
	 * 
	 * @param name
	 *            The name of the attribute. The name has to be a valid XML
	 *            name.
	 * @param value
	 *            The value associated with the attribute.
	 * @throws NullPointerException
	 *             Thrown if any argument is <code>null</code>.
	 * @throws IllegalArgumentException
	 *             Thrown if the specified name is not a valid XML name.
	 */
	public NativeOrXmlAttributeAdapter(String name, String value) throws NullPointerException, IllegalArgumentException
	{
		super(null);
		
		setName(name);
		setValue(value);
	}
	
	// =========================================================================
	
	@Override
	public String getNodeName()
	{
		return "Attribute";
	}
	
	@Override
	public WomNodeType getNodeType()
	{
		return WomNodeType.ATTRIBUTE;
	}
	
	public XmlAttribute getAstNode()
	{
		return (XmlAttribute) super.getAstNode();
	}
	
	// =========================================================================
	
	@Override
	public String getName()
	{
		return name;
	}
	
	@Override
	public String setName(String name) throws IllegalArgumentException, NullPointerException
	{
		Toolbox.checkValidXmlName(name);
		
		AttributeSupportingElement parent =
				(AttributeSupportingElement) getParent();
		
		if (parent != null)
		{
			if (parent.getAttribute(name) != null)
				throw new IllegalArgumentException("Attribute with this name " +
						"already exists for the corresponding element!");
			
			// Check if attribute name is allowed on our parent node.
			parent.getAttributeDescriptorOrFail(name);
		}
		
		String old = getName();
		
		this.name = name;
		if (getAstNode() != null)
			getAstNode().setName(name);
		
		return old;
	}
	
	@Override
	public String getValue()
	{
		return value;
	}
	
	@Override
	public String setValue(String value)
	{
		if (value == null)
			throw new NullPointerException();
		
		AttributeSupportingElement parent =
				(AttributeSupportingElement) getParent();
		
		if (parent != null)
		{
			// Check if attribute name is allowed on our parent node.
			AttributeDescriptor descriptor =
					parent.getAttributeDescriptorOrFail(name);
			
			descriptor.verify(parent, value);
		}
		
		String old = getValue();
		
		this.value = value;
		if (getAstNode() != null)
			getAstNode().setValue(convertValue(value));
		
		return old;
	}
	
	// =========================================================================
	
	/**
	 * Attach an AST attribute to this WOM attribute node.
	 * 
	 * @return <code>True</code> if an AST node was attached, <code>false</code>
	 *         if an AST node is already attached and no operation was
	 *         performed.
	 */
	// should be protected
	public boolean attachAstNode()
	{
		if (getAstNode() != null)
			return false;
		
		super.setAstNode(
				Toolbox.addRtData(
						new XmlAttribute(
								name,
								convertValue(value),
								true)));
		
		return true;
	}
	
	// should be protected
	public AstNode detachAstNode()
	{
		return super.setAstNode(null);
	}
	
	private static NodeList convertValue(String value)
	{
		return TextUtils.stringToAst(value, true);
	}
	
	// =========================================================================
	
	public int getIntValue()
	{
		return Integer.valueOf(getValue());
	}
	
	public int getIntValue(int default_)
	{
		try
		{
			return Integer.valueOf(getValue());
		}
		catch (NumberFormatException e)
		{
			return default_;
		}
	}
}
