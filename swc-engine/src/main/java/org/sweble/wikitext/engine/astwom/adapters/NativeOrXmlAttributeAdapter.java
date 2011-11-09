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

import org.sweble.wikitext.engine.astwom.WomBackbone;
import org.sweble.wikitext.engine.astwom.Toolbox;
import org.sweble.wikitext.engine.astwom.WomNodeFactory;
import org.sweble.wikitext.engine.wom.WomAttribute;
import org.sweble.wikitext.engine.wom.WomNodeType;
import org.sweble.wikitext.lazy.utils.StringConversionException;
import org.sweble.wikitext.lazy.utils.StringConverter;
import org.sweble.wikitext.lazy.utils.TextUtils;
import org.sweble.wikitext.lazy.utils.XmlAttribute;
import org.sweble.wikitext.lazy.utils.XmlEntityResolver;

import de.fau.cs.osr.ptk.common.ast.AstNode;
import de.fau.cs.osr.ptk.common.ast.NodeList;
import de.fau.cs.osr.utils.XmlGrammar;

public class NativeOrXmlAttributeAdapter
        extends
            WomBackbone
        implements
            WomAttribute
{
	private static final long serialVersionUID = 1L;
	
	private String name;
	
	private String value;
	
	// =========================================================================
	
	/**
	 * Create a WOM attribute node from an AST attribute node.
	 * 
	 * @param resolver
	 *            Used to resolve XML entity references.
	 * @param astNode
	 *            The AST node to create a WOM attribute from.
	 * @throws NullPointerException
	 *             Thrown if any argument is <code>null</code>.
	 */
	public NativeOrXmlAttributeAdapter(WomNodeFactory nodeFactory, XmlAttribute astNode) throws NullPointerException
	{
		super(astNode);
		
		if (nodeFactory == null || astNode == null)
			throw new NullPointerException();
		
		this.name = getAstNode().getName();
		this.value = convertValue(nodeFactory, astNode);
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
		
		if (name == null || value == null)
			throw new NullPointerException();
		
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
	
	// =========================================================================
	
	@Override
	public String getName()
	{
		return name;
	}
	
	@Override
	public String setName(String name) throws IllegalArgumentException, NullPointerException
	{
		if (name == null)
			throw new NullPointerException();
		
		if (!checkName(name))
			throw new IllegalArgumentException("Illegal attribute name.");
		
		if (getParent() != null && getParent().getAttribute(name) != null)
			throw new IllegalArgumentException("Attribute with this name " +
			        "already exists for the corresponding element!");
		
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
	public boolean attachAstNode()
	{
		if (getAstNode() != null)
			return false;
		
		super.setAstNode(
		        Toolbox.addXmlAttrRtData(
		                new XmlAttribute(
		                        name,
		                        convertValue(value),
		                        true)));
		
		return true;
	}
	
	public AstNode detachAstNode()
	{
		return super.setAstNode(null);
	}
	
	// =========================================================================
	
	public XmlAttribute getAstNode()
	{
		return (XmlAttribute) super.getAstNode();
	}
	
	private static boolean checkName(String name)
	{
		return XmlGrammar.xmlName().matcher(name).matches();
	}
	
	private static NodeList convertValue(String value)
	{
		return TextUtils.stringToAst(value);
	}
	
	private static String convertValue(XmlEntityResolver resolver, XmlAttribute astNode)
	{
		try
		{
			int opt = StringConverter.RESOLVE_CHAR_REF |
			        StringConverter.RESOLVE_ENTITY_REF |
			        StringConverter.FAIL_ON_IGNORED |
			        StringConverter.FAIL_ON_XML_COMMENTS;
			
			return StringConverter.convert(astNode.getValue(), resolver, opt);
		}
		catch (StringConversionException e)
		{
			// If the parser is working correctly this should not happen...
			throw new AssertionError();
		}
	}
}
