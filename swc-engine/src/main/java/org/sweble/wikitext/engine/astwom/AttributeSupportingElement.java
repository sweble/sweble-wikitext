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

import org.sweble.wikitext.engine.astwom.AttributeDescriptor.Normalization;
import org.sweble.wikitext.engine.astwom.adapters.NativeOrXmlAttributeAdapter;
import org.sweble.wikitext.engine.wom.WomAttribute;
import org.sweble.wikitext.parser.utils.XmlAttribute;
import org.sweble.wikitext.parser.utils.XmlCharRef;
import org.sweble.wikitext.parser.utils.XmlEntityRef;
import org.sweble.wikitext.parser.AstNodeTypes;

import de.fau.cs.osr.ptk.common.ast.AstNode;
import de.fau.cs.osr.ptk.common.ast.NodeList;
import de.fau.cs.osr.ptk.common.ast.Text;
import de.fau.cs.osr.utils.StringUtils;
import de.fau.cs.osr.utils.XmlGrammar;

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
	protected AttributeManagerBase getAttribManager()
	{
		return null;
	}
	
	protected void setAttribManager(AttributeManagerBase attribManager)
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
	/*
	 * @startuml AttributeSupportingElement-getAttribManagerOrFail
	 * [-> AttributeSupportingElement: getAttribManagerOrFail
	 * activate AttributeSupportingElement
	 * 
	 *   AttributeSupportingElement -> "? extends AttributeSupportingElement": getAttribManager
	 *   activate "? extends AttributeSupportingElement"
	 *     AttributeSupportingElement <-- "? extends AttributeSupportingElement": attribManager
	 *   deactivate "? extends AttributeSupportingElement"
	 * 
	 *   alt attribManager == null
	 *     [<-- AttributeSupportingElement: throw UnsupportedOperationException
	 *   end
	 * 
	 *   [<-- AttributeSupportingElement: attribManager
	 * deactivate AttributeSupportingElement
	 * @enduml
	 */
	protected final AttributeManagerBase getAttribManagerOrFail() throws UnsupportedOperationException
	{
		AttributeManagerBase attribManager = getAttribManager();
		if (attribManager == null)
		{
			doesNotSupportAttributes();
			return null;
		}
		return attribManager;
	}
	
	/*
	 * @startuml AttributeSupportingElement-getAttribManagerForModificationOrFail
	 * [-> AttributeSupportingElement: getAttribManagerForModificationOrFail
	 * activate AttributeSupportingElement
	 * 
	 *   AttributeSupportingElement -> AttributeSupportingElement: getAttribManagerOrFail
	 *   activate AttributeSupportingElement
	 * 
	 *     AttributeSupportingElement -> "? extends AttributeSupportingElement": getAttribManager
	 *     activate "? extends AttributeSupportingElement"
	 *       AttributeSupportingElement <-- "? extends AttributeSupportingElement": attribManager
	 *     deactivate "? extends AttributeSupportingElement"
	 * 
	 *     alt attribManager == null
	 *       [<-- AttributeSupportingElement: throw UnsupportedOperationException
	 *     end
	 * 
	 *     AttributeSupportingElement <-- AttributeSupportingElement: attribManager
	 *   deactivate AttributeSupportingElement
	 * 
	 *   alt is immutable empty manager
	 *     create AttributeManager
	 *     AttributeSupportingElement -> AttributeManager: new
	 *     AttributeSupportingElement -> "? extends AttributeSupportingElement": setAttribManager
	 *   end
	 * 
	 *   [<-- AttributeSupportingElement: attribManager
	 * deactivate AttributeSupportingElement
	 * @enduml
	 */
	protected final AttributeManagerBase getAttribManagerForModificationOrFail() throws UnsupportedOperationException
	{
		AttributeManagerBase attribManager = getAttribManagerOrFail();
		if (attribManager.isEmptyManager())
		{
			attribManager = AttributeManagerBase.createManager();
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
	
	/* 
	 * @startuml AttributeSupportingElement-getAstAttribContainerOrAddSupport
	 * [-> AttributeSupportingElement: getAstAttribContainerOrAddSupport
	 * activate AttributeSupportingElement
	 * 
	 *   AttributeSupportingElement -> "? extends AttributeSupportingElement": getAstAttribContainer
	 *   activate "? extends AttributeSupportingElement"
	 *     AttributeSupportingElement <-- "? extends AttributeSupportingElement": astAttribContainer
	 *   deactivate "? extends AttributeSupportingElement"
	 *   
	 *   alt attribContainer == null
	 *     AttributeSupportingElement -> "? extends AttributeSupportingElement": addAstAttribSupport
	 *     activate "? extends AttributeSupportingElement"
	 *       AttributeSupportingElement <-- "? extends AttributeSupportingElement": astAttribContainer
	 *     deactivate "? extends AttributeSupportingElement"
	 *   end
	 *   
	 *   [<-- AttributeSupportingElement: astAttribContainer
	 * deactivate AttributeSupportingElement
	 * @enduml
	 */
	protected final NodeList getAstAttribContainerOrAddSupport()
	{
		NodeList attribContainer = getAstAttribContainer();
		if (attribContainer == null)
			attribContainer = addAstAttribSupport();
		return attribContainer;
	}
	
	protected abstract AttributeDescriptor getAttributeDescriptor(String name);
	
	// should be protected
	/*
	 * @startuml AttributeSupportingElement-getAttributeDescriptorOrFail
	 * [-> AttributeSupportingElement: getAttributeDescriptorOrFail
	 * activate AttributeSupportingElement
	 * 
	 *   AttributeSupportingElement -> "? extends AttributeSupportingElement": getAttributeDescriptor
	 *   activate "? extends AttributeSupportingElement"
	 *     AttributeSupportingElement <-- "? extends AttributeSupportingElement": descriptor
	 *   deactivate "? extends AttributeSupportingElement"
	 *   
	 *   alt no descriptor available
	 * 	   [<-- AttributeSupportingElement: throw IllegalArgumentException
	 *   end
	 * 	 [<-- AttributeSupportingElement: descriptor
	 *   
	 * deactivate AttributeSupportingElement
	 * @enduml
	 */
	public AttributeDescriptor getAttributeDescriptorOrFail(String name)
	{
		AttributeDescriptor d = getAttributeDescriptor(name);
		if (d == null)
			throw new IllegalArgumentException("An attribute named `" + name + "' is not supported by this element!");
		
		return d;
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
		AttributeManagerBase attrs = getAttribManager();
		if (attrs == null)
			return Collections.emptyList();
		return attrs.getAttributes();
	}
	
	@Override
	public final String getAttribute(String name)
	{
		AttributeManagerBase attrs = getAttribManager();
		if (attrs == null)
			return null;
		return attrs.getAttribute(name);
	}
	
	@Override
	public final NativeOrXmlAttributeAdapter getAttributeNode(String name)
	{
		AttributeManagerBase attrs = getAttribManager();
		if (attrs == null)
			return null;
		return attrs.getAttributeNode(name);
	}
	
	@Override
	public final NativeOrXmlAttributeAdapter removeAttribute(String name)
	{
		return removeAttributeUnchecked(checkAttributeRemoval(name), name);
	}
	
	protected NativeOrXmlAttributeAdapter removeAttributeUnchecked(
			AttributeDescriptor descriptor,
			String name)
	{
		// Don't use getAstAttribContainerOrAddSupport()!
		// If the element doesn't have any attributes then the caller is
		// removing something that's not there and we should not create the
		// attribute manager.
		AttributeManagerBase attrs = getAttribManagerOrFail();
		return attrs.removeAttribute(descriptor, name, getAstAttribContainer());
	}
	
	@Override
	public final void removeAttributeNode(WomAttribute attr) throws IllegalArgumentException
	{
		removeAttributeNodeUnchecked(checkAttributeRemoval(attr.getName()), attr);
	}
	
	protected void removeAttributeNodeUnchecked(
			AttributeDescriptor descriptor,
			WomAttribute attr)
	{
		// Don't use getAstAttribContainerOrAddSupport()!
		// If the element doesn't have any attributes then the caller is
		// removing something that's not there and we should not create the
		// attribute manager.
		AttributeManagerBase attrs = getAttribManagerOrFail();
		attrs.removeAttributeNode(descriptor, attr, this, getAstAttribContainer());
	}
	
	private AttributeDescriptor checkAttributeRemoval(String name)
	{
		AttributeDescriptor d = getAttributeDescriptorOrFail(name);
		if (!d.isRemovable())
			throw new UnsupportedOperationException("Attribute `" + name + "' cannot be removed");
		
		return d;
	}
	
	/*
	 * @startuml AttributeSupportingElement-setAttribute
	 * [-> AttributeSupportingElement: setAttribute
	 * activate AttributeSupportingElement
	 *   
	 *   AttributeSupportingElement -> AttributeSupportingElement: getAttributeDescriptorOrFail
	 *   activate AttributeSupportingElement
	 *   
	 *     AttributeSupportingElement -> "? extends AttributeSupportingElement": getAttributeDescriptor
	 *     activate "? extends AttributeSupportingElement"
	 *       AttributeSupportingElement <-- "? extends AttributeSupportingElement": descriptor
	 *     deactivate "? extends AttributeSupportingElement"
	 *     
	 *     alt no descriptor available
	 * 	     [<-- AttributeSupportingElement: throw IllegalArgumentException
	 *     end
	 *     
	 * 	   AttributeSupportingElement <-- AttributeSupportingElement: descriptor
	 *   deactivate AttributeSupportingElement
	 *   
	 *   AttributeSupportingElement -> AttributeSupportingElement: setAttribute
	 *   activate AttributeSupportingElement
	 *   
	 *     AttributeSupportingElement -> AttributeDescriptor: verify
	 *     activate AttributeDescriptor
	 * 
	 *       alt invalid value
	 *         [<-- AttributeDescriptor: throw IllegalArgumentException
	 *       end
	 * 
	 *     AttributeSupportingElement <-- AttributeDescriptor: possibly altered value
	 *     deactivate AttributeDescriptor
	 * 
	 *     AttributeSupportingElement -> AttributeSupportingElement: getAttribManagerForModificationOrFail
	 *     activate AttributeSupportingElement
	 * 
	 *       AttributeSupportingElement -> AttributeSupportingElement: getAttribManagerOrFail
	 *       activate AttributeSupportingElement
	 * 
	 *         AttributeSupportingElement -> "? extends AttributeSupportingElement": getAttribManager
	 *         activate "? extends AttributeSupportingElement"
	 *           AttributeSupportingElement <-- "? extends AttributeSupportingElement": attribManager
	 *         deactivate "? extends AttributeSupportingElement"
	 * 
	 *         alt attribManager == null
	 *           [<-- AttributeSupportingElement: throw UnsupportedOperationException
	 *         end
	 * 
	 *         AttributeSupportingElement <-- AttributeSupportingElement: attribManager
	 *       deactivate AttributeSupportingElement
	 * 
	 *       alt is immutable empty manager
	 *         create AttributeManager
	 *         AttributeSupportingElement -> AttributeManager: new
	 *         AttributeSupportingElement -> "? extends AttributeSupportingElement": setAttribManager
	 *       end
	 * 
	 *       AttributeSupportingElement <-- AttributeSupportingElement: attribManager
	 *     deactivate AttributeSupportingElement
	 * 
	 *     AttributeSupportingElement -> AttributeSupportingElement: getAstAttribContainerOrAddSupport
	 *     activate AttributeSupportingElement
	 * 
	 *       AttributeSupportingElement -> "? extends AttributeSupportingElement": getAstAttribContainer
	 *       activate "? extends AttributeSupportingElement"
	 *         AttributeSupportingElement <-- "? extends AttributeSupportingElement": astAttribContainer
	 *       deactivate "? extends AttributeSupportingElement"
	 * 
	 *       alt attribContainer == null
	 *         AttributeSupportingElement -> "? extends AttributeSupportingElement": addAstAttribSupport
	 *         activate "? extends AttributeSupportingElement"
	 *           AttributeSupportingElement <-- "? extends AttributeSupportingElement": astAttribContainer
	 *         deactivate "? extends AttributeSupportingElement"
	 *       end
	 * 
	 *       AttributeSupportingElement <-- AttributeSupportingElement: astAttribContainer
	 *     deactivate AttributeSupportingElement
	 * 
	 *     AttributeSupportingElement -> AttributeManagerBase: setAttribute
	 *     activate AttributeManagerBase
	 *       alt failure
	 *         [<-- AttributeManagerBase: throw 
	 *       end
	 *       AttributeSupportingElement <-- AttributeManagerBase: oldAttrib
	 *     deactivate AttributeManagerBase
	 * 
	 * 	   AttributeSupportingElement <-- AttributeSupportingElement: oldAttrib
	 *   deactivate AttributeSupportingElement
	 *   
	 *   [<-- AttributeSupportingElement: oldAttrib
	 * deactivate AttributeSupportingElement
	 * @enduml
	 */
	@Override
	public final NativeOrXmlAttributeAdapter setAttribute(
			String name,
			String value)
	{
		AttributeDescriptor d = getAttributeDescriptorOrFail(name);
		return setAttribute(d, name, value);
	}
	
	/*
	 * @startuml AttributeSupportingElement-setAttribute-2
	 * [-> AttributeSupportingElement: setAttribute
	 * activate AttributeSupportingElement
	 * 
	 *   AttributeSupportingElement -> AttributeDescriptor: verify
	 *   activate AttributeDescriptor
	 *   
	 *     alt invalid value
	 * 	     [<-- AttributeDescriptor: throw IllegalArgumentException
	 *     end
	 *   
	 *   AttributeSupportingElement <-- AttributeDescriptor: possibly altered value
	 *   deactivate AttributeDescriptor
	 * 
	 *   AttributeSupportingElement -> AttributeSupportingElement: getAttribManagerForModificationOrFail
	 *   activate AttributeSupportingElement
	 *   
	 *     AttributeSupportingElement -> AttributeSupportingElement: getAttribManagerOrFail
	 *     activate AttributeSupportingElement
	 * 
	 *       AttributeSupportingElement -> "? extends AttributeSupportingElement": getAttribManager
	 *       activate "? extends AttributeSupportingElement"
	 *         AttributeSupportingElement <-- "? extends AttributeSupportingElement": attribManager
	 *       deactivate "? extends AttributeSupportingElement"
	 * 
	 *       alt attribManager == null
	 *         [<-- AttributeSupportingElement: throw UnsupportedOperationException
	 *       end
	 * 
	 *       AttributeSupportingElement <-- AttributeSupportingElement: attribManager
	 *     deactivate AttributeSupportingElement
	 * 
	 *     alt is immutable empty manager
	 *       create AttributeManager
	 *       AttributeSupportingElement -> AttributeManager: new
	 *       AttributeSupportingElement -> "? extends AttributeSupportingElement": setAttribManager
	 *     end
	 * 
	 *     AttributeSupportingElement <-- AttributeSupportingElement: attribManager
	 *   deactivate AttributeSupportingElement
	 * 
	 *   AttributeSupportingElement -> AttributeSupportingElement: getAstAttribContainerOrAddSupport
	 *   activate AttributeSupportingElement
	 * 
	 *     AttributeSupportingElement -> "? extends AttributeSupportingElement": getAstAttribContainer
	 *     activate "? extends AttributeSupportingElement"
	 *       AttributeSupportingElement <-- "? extends AttributeSupportingElement": astAttribContainer
	 *     deactivate "? extends AttributeSupportingElement"
	 *   
	 *     alt attribContainer == null
	 *       AttributeSupportingElement -> "? extends AttributeSupportingElement": addAstAttribSupport
	 *       activate "? extends AttributeSupportingElement"
	 *         AttributeSupportingElement <-- "? extends AttributeSupportingElement": astAttribContainer
	 *       deactivate "? extends AttributeSupportingElement"
	 *     end
	 *   
	 *     AttributeSupportingElement <-- AttributeSupportingElement: astAttribContainer
	 *   deactivate AttributeSupportingElement
	 * 
	 *   AttributeSupportingElement -> AttributeManagerBase: setAttribute
	 *   activate AttributeManagerBase
	 *     alt failure
	 * 	     [<-- AttributeManagerBase: throw 
	 *     end
	 *     AttributeSupportingElement <-- AttributeManagerBase: oldAttrib
	 *   deactivate AttributeManagerBase
	 * 
	 * [<-- AttributeSupportingElement: oldAttrib
	 * deactivate AttributeSupportingElement
	 * @enduml
	 */
	protected NativeOrXmlAttributeAdapter setAttribute(
			AttributeDescriptor descriptor,
			String name,
			String value)
	{
		String altered = descriptor.verify(this, value);
		AttributeManagerBase attrs = getAttribManagerForModificationOrFail();
		return attrs.setAttribute(descriptor, name, altered, this, getAstAttribContainerOrAddSupport());
	}
	
	protected NativeOrXmlAttributeAdapter setAttributeUnchecked(
			AttributeDescriptor descriptor,
			String name,
			String value)
	{
		AttributeManagerBase attrs = getAttribManagerForModificationOrFail();
		return attrs.setAttribute(descriptor, name, value, this, getAstAttribContainerOrAddSupport());
	}
	
	@Override
	public final NativeOrXmlAttributeAdapter setAttributeNode(WomAttribute attr) throws IllegalArgumentException
	{
		AttributeDescriptor d = getAttributeDescriptorOrFail(attr.getName());
		return setAttributeNode(d, attr);
	}
	
	protected NativeOrXmlAttributeAdapter setAttributeNode(
			AttributeDescriptor descriptor,
			WomAttribute attr)
	{
		String value = attr.getValue();
		String altered = descriptor.verify(this, value);
		if (altered != value)
			attr.setValue(altered);
		
		AttributeManagerBase attrs = getAttribManagerForModificationOrFail();
		return attrs.setAttributeNode(descriptor, attr, this, getAstAttribContainerOrAddSupport());
	}
	
	// =========================================================================
	
	protected NativeOrXmlAttributeAdapter setAttribute(
			AttributeDescriptor descriptor,
			String name,
			int value)
	{
		String altered = descriptor.verify(this, String.valueOf(value));
		AttributeManagerBase attrs = getAttribManagerForModificationOrFail();
		return attrs.setAttribute(descriptor, name, altered, this, getAstAttribContainerOrAddSupport());
	}
	
	protected int getIntAttribute(String name)
	{
		NativeOrXmlAttributeAdapter a = this.getAttributeNode(name);
		if (a == null)
			throw new InternalError("Undefined attribute: " + name);
		return a.getIntValue();
	}
	
	protected int getIntAttribute(String name, int default_)
	{
		NativeOrXmlAttributeAdapter a = this.getAttributeNode(name);
		if (a == null)
			return default_;
		return a.getIntValue();
	}
	
	// =========================================================================
	
	protected void addAttributes(NodeList xmlAttributes)
	{
		if (xmlAttributes == null)
			throw new NullPointerException();
		
		if (!xmlAttributes.isEmpty())
			addAttributes(xmlAttributes, getAttribManagerForModificationOrFail());
	}
	
	protected void addAttributes(
			NodeList xmlAttributes,
			AttributeManagerBase attribManager)
	{
		Iterator<AstNode> i = xmlAttributes.iterator();
		while (i.hasNext())
		{
			AstNode n = i.next();
			if (n.getNodeType() != AstNodeTypes.NT_XML_ATTRIBUTE)
				continue;
			
			XmlAttribute astAttr = (XmlAttribute) n;
			
			// MediaWiki cleans up HTML and normalizes attributes to be proper
			// XHTML attributes (lowercase).
			String name = astAttr.getName().toLowerCase();
			
			// If the given element does not allow for this attribute, we just 
			// hide it in the WOM. It can still be found in the AST.
			AttributeDescriptor descriptor = getAttributeDescriptor(name);
			if (descriptor == null)
				continue;
			
			// Don't import hidden attributes
			if (!descriptor.syncToAst())
				continue;
			
			// We only keep the lowercase name if the attribute is one of the
			// known attributes. There are no rules that say how the other
			// attribute names should be formatted.
			if (descriptor == GenericAttributeDescriptor.get())
				name = astAttr.getName();
			
			String value = normalize(
					descriptor.getNormalizationMode(),
					astAttr.getValue());
			
			// All known attributes (XHTML and WOM) only have lowercase values
			// => correction is easy ;)
			if (descriptor == GenericAttributeDescriptor.get())
				value = value.toLowerCase();
			
			try
			{
				descriptor.verify(this, value);
			}
			catch (IllegalArgumentException e)
			{
				// If the given attribute has an invalid value, we just 
				// hide it in the WOM. It can still be found and overwritten 
				// in the AST.
				continue;
			}
			
			NativeOrXmlAttributeAdapter attr =
					new NativeOrXmlAttributeAdapter(
							astAttr,
							name,
							value);
			
			getAttribManagerForModificationOrFail().setAttributeNode(
					descriptor,
					attr,
					this,
					null);
		}
	}
	
	// =========================================================================
	
	protected static String normalize(Normalization normalizationMode, NodeList value)
	{
		switch (normalizationMode)
		{
			case CDATA:
				return convertAstToStringAndNormalize(value);
			case NON_CDATA:
				return trimAndCollapse(convertAstToStringAndNormalize(value));
			case NONE:
				return convertAstToString(value);
			default:
				throw new AssertionError();
		}
	}
	
	private static String convertAstToString(NodeList value)
	{
		StringBuilder b = new StringBuilder();
		for (AstNode n : value)
		{
			switch (n.getNodeType())
			{
				case AstNode.NT_TEXT:
					b.append(((Text) n).getContent());
					break;
				case AstNodeTypes.NT_XML_CHAR_REF:
				{
					int cp = ((XmlCharRef) n).getCodePoint();
					if (!XmlGrammar.isChar(cp))
						//throw new IllegalArgumentException();
						StringUtils.hexCharRef(b, cp);
					else
						b.append(Character.toChars(cp));
					break;
				}
				case AstNodeTypes.NT_XML_ENTITY_REF:
				{
					String resolved = ((XmlEntityRef) n).getResolved();
					if (resolved == null)
						//throw new IllegalArgumentException();
						StringUtils.entityRef(b, ((XmlEntityRef) n).getName());
					else
						b.append(resolved);
					break;
				}
				default:
					throw new AssertionError("Unepxected node type: " + value.getNodeName());
			}
		}
		return b.toString();
	}
	
	private static String convertAstToStringAndNormalize(NodeList value)
	{
		StringBuilder b = new StringBuilder();
		for (AstNode n : value)
		{
			switch (n.getNodeType())
			{
				case AstNode.NT_TEXT:
					normalizeWhitespace(b, ((Text) n).getContent());
					break;
				case AstNodeTypes.NT_XML_CHAR_REF:
				{
					int cp = ((XmlCharRef) n).getCodePoint();
					if (!XmlGrammar.isChar(cp))
						//throw new IllegalArgumentException();
						StringUtils.hexCharRef(b, cp);
					else
						b.append(Character.toChars(cp));
					break;
				}
				case AstNodeTypes.NT_XML_ENTITY_REF:
				{
					String resolved = ((XmlEntityRef) n).getResolved();
					if (resolved == null)
						//throw new IllegalArgumentException();
						StringUtils.entityRef(b, ((XmlEntityRef) n).getName());
					else
						b.append(resolved);
					break;
				}
				default:
					throw new AssertionError("Unepxected node type: " + value.getNodeName());
			}
		}
		return b.toString();
	}
	
	private static void normalizeWhitespace(StringBuilder b, String text)
	{
		int len = text.length();
		for (int i = 0; i < len; ++i)
		{
			char ch = text.charAt(i);
			switch (ch)
			{
				case '\r': // U+000D
					++i;
					if (i >= len || text.charAt(i) != '\n')
						--i;
					b.append(' ');
					break;
				
				case '\t': // U+0009
				case '\n': // U+000A
				case ' ': //  U+0020
					b.append(' ');
					break;
				
				default:
					b.append(ch);
					break;
			}
		}
	}
	
	/**
	 * http://www.w3.org/TR/REC-xml/#AVNormalize
	 */
	private static String trimAndCollapse(String cdataNormalized)
	{
		String normalized = cdataNormalized;
		
		int from = 0;
		int length = normalized.length();
		
		while ((from < length) && (normalized.charAt(from) == ' '))
			++from;
		
		while ((from < length) && (normalized.charAt(length - 1) == ' '))
			--length;
		
		if (from >= length)
			return "";
		
		StringBuffer sb = new StringBuffer(length - from);
		
		while (from < length)
		{
			char ch = normalized.charAt(from);
			sb.append(ch);
			
			while (ch == ' ')
			{
				ch = normalized.charAt(from);
				if (ch != ' ')
				{
					sb.append(ch);
					break;
				}
				++from;
			}
			++from;
		}
		
		return sb.toString();
	}
	
	// Never tested:
	///**
	// * http://www.w3.org/TR/REC-xml/#AVNormalize
	// * http://www.w3.org/TR/REC-xml/#sec-line-ends
	// */
	//public static String normalizeCdata(XmlEntityResolver resolver, String value)
	//{
	//	int len = value.length();
	//	StringBuffer result = new StringBuffer(len);
	//	
	//	outmost: for (int i = 0; i < len; ++i)
	//	{
	//		char ch = value.charAt(i);
	//		switch (ch)
	//		{
	//			case '\r': // U+000D
	//				// translating both the two-character sequence #xD #xA and 
	//				// any #xD that is not followed by #xA to a single #xA 
	//				// character.
	//				if (++i < len)
	//				{
	//					ch = value.charAt(i);
	//					if (ch != '\n')
	//						--i;
	//				}
	//				// For a white space character (#x20, #xD, #xA, 
	//				// #x9), append a space character (#x20) to the 
	//				// normalized value.
	//				result.append(' ');
	//				break;
	//			
	//			case '\t': // U+0009
	//			case '\n': // U+000A
	//			case ' ': //  U+0020
	//				// For a white space character (#x20, #xD, #xA, 
	//				// #x9), append a space character (#x20) to the 
	//				// normalized value.
	//				result.append(' ');
	//				break;
	//			
	//			case '&':
	//			{
	//				/*
	//				 * [67]     Reference     ::= EntityRef | CharRef
	//				 */
	//				
	//				int j = i;
	//				ref: if (++j < len)
	//				{
	//					ch = value.charAt(j);
	//					switch (ch)
	//					{
	//						case '#':
	//						{
	//							// For a character reference, append the 
	//							// referenced character to the normalized value.
	//							
	//							/*
	//							 * [66]     CharRef       ::= '&#' [0-9]+ ';'
	//							 *                        |   '&#x' [0-9a-fA-F]+ ';'
	//							 */
	//							
	//							if (++j >= len)
	//								break ref;
	//							
	//							ch = value.charAt(j);
	//							
	//							int ref = 0;
	//							switch (ch)
	//							{
	//								case 'x':
	//									while (++j < len)
	//									{
	//										ch = value.charAt(j);
	//										if (ch >= '0' && ch <= '9')
	//										{
	//											ref = ref * 0x10 + (ch - '0');
	//										}
	//										else if (ch >= 'A' && ch <= 'F')
	//										{
	//											ref = ref * 0x10 + (ch - 'A' + 0xA);
	//										}
	//										else if (ch >= 'a' && ch <= 'f')
	//										{
	//											ref = ref * 0x10 + (ch - 'a' + 0xa);
	//										}
	//										else
	//											break;
	//									}
	//									
	//									if (j < i + 4 || ch != ';')
	//										break ref;
	//									
	//									break;
	//								
	//								default:
	//									while (true)
	//									{
	//										if (ch >= '0' && ch <= '9')
	//										{
	//											ref = ref * 10 + (ch - '0');
	//										}
	//										else
	//											break;
	//										
	//										if (++j >= len)
	//											break;
	//										
	//										ch = value.charAt(j);
	//									}
	//									
	//									if (j < i + 3 || ch != ';')
	//										break ref;
	//									
	//									break;
	//							}
	//							
	//							result.append((char) ref);
	//							
	//							i = j;
	//							continue outmost;
	//						}
	//						
	//						default:
	//						{
	//							// For an entity reference, recursively apply 
	//							// step 3 of this algorithm to the replacement 
	//							// text of the entity.
	//							
	//							/*
	//							 * [68]     EntityRef     ::= '&' Name ';'
	//							 * [4]      NameStartChar ::= ":" | [A-Z] | "_" | [a-z] | [#xC0-#xD6] | [#xD8-#xF6] | [#xF8-#x2FF] | [#x370-#x37D] | [#x37F-#x1FFF] | [#x200C-#x200D] | [#x2070-#x218F] | [#x2C00-#x2FEF] | [#x3001-#xD7FF] | [#xF900-#xFDCF] | [#xFDF0-#xFFFD] | [#x10000-#xEFFFF]
	//							 * [4a]     NameChar      ::= NameStartChar | "-" | "." | [0-9] | #xB7 | [#x0300-#x036F] | [#x203F-#x2040]
	//							 * [5]      Name          ::= NameStartChar (NameChar)*
	//							 */
	//							
	//							int k = j;
	//							if (!XmlGrammar.isNameStartChar(ch))
	//								break;
	//							
	//							int l = k;
	//							while (++l < len)
	//							{
	//								ch = value.charAt(l);
	//								if (!XmlGrammar.isNameChar(ch))
	//									break;
	//							}
	//							
	//							if (ch != ';')
	//								break ref;
	//							
	//							result.append(resolver.resolveXmlEntity(
	//									value.substring(k, l)));
	//							
	//							i = j;
	//							continue outmost;
	//						}
	//					}
	//				}
	//				
	//				// This is actually a syntax error ...
	//				result.append(ch);
	//				break;
	//			}
	//			
	//			default:
	//				// For another character, append the character to the 
	//				// normalized value.
	//				result.append(ch);
	//				break;
	//		}
	//	}
	//	
	//	return result.toString();
	//}
}
