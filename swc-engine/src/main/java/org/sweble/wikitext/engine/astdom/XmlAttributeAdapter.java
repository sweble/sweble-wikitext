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

import org.sweble.wikitext.engine.dom.DomAttribute;
import org.sweble.wikitext.engine.dom.DomNodeType;
import org.sweble.wikitext.lazy.ParserConfigInterface;
import org.sweble.wikitext.lazy.utils.StringConversionException;
import org.sweble.wikitext.lazy.utils.StringConverter;
import org.sweble.wikitext.lazy.utils.TextUtils;
import org.sweble.wikitext.lazy.utils.XmlAttribute;

import de.fau.cs.osr.ptk.common.ast.NodeList;
import de.fau.cs.osr.utils.FmtNotYetImplementedError;
import de.fau.cs.osr.utils.XmlGrammar;

public class XmlAttributeAdapter
        extends
            DomBackbone
        implements
            DomAttribute
{
	private static final long serialVersionUID = 1L;
	
	private final String value;
	
	// =========================================================================
	
	public XmlAttributeAdapter(ParserConfigInterface resolver, XmlAttribute astNode) throws NullPointerException
	{
		super(astNode);
		
		if (resolver == null || astNode == null)
			throw new NullPointerException();
		
		this.value = convertValue(resolver, astNode);
	}
	
	public XmlAttributeAdapter(String name, String value) throws NullPointerException, IllegalArgumentException
	{
		super(new XmlAttribute(
		        name,
		        convertValue(value),
		        true));
		
		if (name == null || value == null)
			throw new NullPointerException();
		
		if (!checkName(name))
			throw new IllegalArgumentException("Illegal attribute name.");
		
		this.value = value;
	}
	
	// =========================================================================
	
	@Override
	public String getName()
	{
		return getAstNode().getName();
	}
	
	// =========================================================================
	
	@Override
	public String getNodeName()
	{
		return "Attribute";
	}
	
	@Override
	public DomNodeType getNodeType()
	{
		return DomNodeType.ATTRIBUTE;
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
		return value;
	}
	
	@Override
	public void appendText(String text) throws UnsupportedOperationException
	{
		throw new FmtNotYetImplementedError();
	}
	
	@Override
	public String deleteText(int from, int length) throws UnsupportedOperationException, IndexOutOfBoundsException
	{
		throw new FmtNotYetImplementedError();
	}
	
	@Override
	public void insertText(int at, String text) throws UnsupportedOperationException, IndexOutOfBoundsException
	{
		throw new FmtNotYetImplementedError();
	}
	
	@Override
	public String replaceText(String text) throws UnsupportedOperationException
	{
		throw new FmtNotYetImplementedError();
	}
	
	@Override
	public String replaceText(int from, int length, String text) throws UnsupportedOperationException, IndexOutOfBoundsException
	{
		throw new FmtNotYetImplementedError();
	}
	
	// =========================================================================
	
	protected XmlAttribute getAstNode()
	{
		return (XmlAttribute) super.getAstNode();
	}
	
	@Override
	protected NodeList getAttributeContainer()
	{
		return null;
	}
	
	@Override
	protected NodeList getChildContainer()
	{
		return null;
	}
	
	private static boolean checkName(String name)
	{
		return XmlGrammar.xmlName().matcher(name).matches();
	}
	
	private static NodeList convertValue(String value)
	{
		return TextUtils.stringToAst(value);
	}
	
	private static String convertValue(ParserConfigInterface resolver, XmlAttribute astNode)
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
