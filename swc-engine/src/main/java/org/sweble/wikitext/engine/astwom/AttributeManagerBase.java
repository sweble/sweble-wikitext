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
import java.util.Date;
import java.util.Iterator;
import java.util.ListIterator;

import org.sweble.wikitext.engine.astwom.adapters.NativeOrXmlAttributeAdapter;
import org.sweble.wikitext.engine.wom.WomAttribute;
import org.sweble.wikitext.engine.wom.WomClear;
import org.sweble.wikitext.engine.wom.WomColor;
import org.sweble.wikitext.engine.wom.WomHorizAlign;
import org.sweble.wikitext.engine.wom.WomI18nDir;
import org.sweble.wikitext.engine.wom.WomUniversalAttributes;
import org.sweble.wikitext.engine.wom.WomValueWithUnit;
import org.sweble.wikitext.parser.AstNodeTypes;
import org.sweble.wikitext.parser.nodes.WtNode;
import org.sweble.wikitext.parser.nodes.WtNodeList;
import org.sweble.wikitext.parser.nodes.XmlAttribute;

public abstract class AttributeManagerBase
		implements
			WomUniversalAttributes
{
	private static final AttributeManagerBase emptyManager = new EmptyAttributeManager();
	
	public static AttributeManagerBase emptyManager()
	{
		return emptyManager;
	}
	
	public static AttributeManagerBase createManager()
	{
		return new AttributeManager();
	}
	
	// =========================================================================
	
	public abstract Collection<WomAttribute> getAttributes();
	
	public abstract String getAttribute(String name);
	
	public abstract NativeOrXmlAttributeAdapter getAttributeNode(String name);
	
	public abstract NativeOrXmlAttributeAdapter removeAttribute(
			AttributeDescriptor descriptor,
			String name,
			WtNodeList attrContainer);
	
	public abstract void removeAttributeNode(
			AttributeDescriptor descriptor,
			WomAttribute attr,
			WomBackbone parent,
			WtNodeList attrContainer) throws IllegalArgumentException;
	
	public abstract NativeOrXmlAttributeAdapter setAttribute(
			AttributeDescriptor descriptor,
			String name,
			String value,
			WomBackbone parent,
			WtNodeList attrContainer);
	
	public abstract NativeOrXmlAttributeAdapter setAttributeNode(
			AttributeDescriptor descriptor,
			WomAttribute attr,
			WomBackbone parent,
			WtNodeList attrContainer) throws IllegalArgumentException;
	
	// =========================================================================
	
	public abstract boolean isEmptyManager();
	
	// =========================================================================
	
	@Override
	public String getId()
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public String setId(String id) throws IllegalArgumentException
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public String getClasses()
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public String setClasses(String classes)
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public String getStyle()
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public String setStyle(String style)
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public String getTitle()
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public String setTitle(String title)
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	// =========================================================================
	
	@Override
	public WomI18nDir getDir()
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public WomI18nDir setDir(WomI18nDir dir)
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public String getLang()
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public String setLang(String lang)
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public String getXmlLang()
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public String setXmlLang(String lang)
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	// =========================================================================
	
	@Override
	public String getOnclick()
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public String setOnclick(String handler)
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public String getOndblclick()
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public String setOndblclick(String handler)
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public String getOnmousedown()
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public String setOnmousedown(String handler)
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public String getOnmouseup()
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public String setOnmouseup(String handler)
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public String getOnmouseover()
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public String setOnmouseover(String handler)
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public String getOnmousemove()
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public String setOnmousemove(String handler)
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public String getOnmouseout()
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public String setOnmouseout(String handler)
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public String getOnkeypress()
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public String setOnkeypress(String handler)
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public String getOnkeydown()
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public String setOnkeydown(String handler)
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public String getOnkeyup()
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public String setOnkeyup(String handler)
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	// =========================================================================
	
	// Used by HorizontalRuleAdapter, ParagraphAdapter
	public WomHorizAlign getAlign()
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	// Used by HorizontalRuleAdapter, ParagraphAdapter
	public WomHorizAlign setAlign(WomHorizAlign align)
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	// Used by HorizontalRuleAdapter
	public boolean isNoshade()
	{
		// TODO Auto-generated method stub
		return false;
	}
	
	// Used by HorizontalRuleAdapter
	public boolean setNoshade(boolean noshade)
	{
		// TODO Auto-generated method stub
		return false;
	}
	
	// Used by HorizontalRuleAdapter, FontAdapter
	public Integer getSize()
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	// Used by HorizontalRuleAdapter, FontAdapter
	public Integer setSize(Integer size)
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	// Used by HorizontalRuleAdapter
	public WomValueWithUnit getWidth()
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	// Used by HorizontalRuleAdapter
	public WomValueWithUnit setWidth(WomValueWithUnit width)
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	// Used by BlockquoteAdapter, DelAdapter, InsAdapter
	public String getCite()
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	// Used by BlockquoteAdapter, DelAdapter, InsAdapter
	public String setCite(String source)
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	// Used by BreakAdapter
	public WomClear getClear()
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	// Used by BreakAdapter
	public WomClear setClear(WomClear clear)
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	// Used by DelAdapter, InsAdapter
	public Date getDatetime()
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	// Used by DelAdapter, InsAdapter
	public Date setDatetime(Date timestamp)
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	// Used by FontAdapter
	public WomColor getColor()
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	// Used by FontAdapter
	public WomColor setColor(WomColor color)
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	// Used by FontAdapter
	public String getFace()
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	// Used by FontAdapter
	public String setFace(String face)
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	// =========================================================================
	
	public static final class EmptyAttributeManager
			extends
				AttributeManagerBase
	{
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
		public NativeOrXmlAttributeAdapter removeAttribute(
				AttributeDescriptor descriptor,
				String name,
				WtNodeList attrContainer)
		{
			// Doesn't fail if an attribute with the specified name doesn't
			// exist and therefore should also not fail if the node has no
			// attributes.
			return null;
		}
		
		@Override
		public void removeAttributeNode(
				AttributeDescriptor descriptor,
				WomAttribute attr,
				WomBackbone parent,
				WtNodeList attrContainer) throws IllegalArgumentException
		{
			// The node has no attributes yet someone passes an attribute he
			// claims to be of this node ...
			unsupported();
		}
		
		@Override
		public NativeOrXmlAttributeAdapter setAttribute(
				AttributeDescriptor descriptor,
				String name,
				String value,
				WomBackbone parent,
				WtNodeList attrContainer)
		{
			unsupported();
			return null;
		}
		
		@Override
		public NativeOrXmlAttributeAdapter setAttributeNode(
				AttributeDescriptor descriptor,
				WomAttribute attr,
				WomBackbone parent,
				WtNodeList attrContainer) throws IllegalArgumentException
		{
			unsupported();
			return null;
		}
		
		@Override
		public boolean isEmptyManager()
		{
			return true;
		}
		
		private static void unsupported()
		{
			throw new UnsupportedOperationException("Cannot modify an instance of EmptyAttributeManager!");
		}
	}
	
	// =========================================================================
	
	public static final class AttributeManager
			extends
				AttributeManagerBase
	{
		private NativeOrXmlAttributeAdapter firstAttr;
		
		// =====================================================================
		
		@Override
		public boolean isEmptyManager()
		{
			return false;
		}
		
		// =====================================================================
		
		@Override
		public Collection<WomAttribute> getAttributes()
		{
			return new SiblingCollection<WomAttribute>(firstAttr);
		}
		
		@Override
		public String getAttribute(String name)
		{
			final WomAttribute attributeNode = getAttributeNode(name);
			if (attributeNode == null)
				return null;
			return attributeNode.getValue();
		}
		
		@Override
		public NativeOrXmlAttributeAdapter getAttributeNode(String name)
		{
			if (name != null)
			{
				WomAttribute i = firstAttr;
				while (i != null)
				{
					if (i.getName().equals(name))
						return (NativeOrXmlAttributeAdapter) i;
					
					i = (WomAttribute) i.getNextSibling();
				}
			}
			return null;
		}
		
		/*
		 * @startuml AttributeManagerBase-removeAttribute.svg
		 * [-> AttributeManager: removeAttribute()
		 * activate AttributeManager
		 * 
		 *   alt name == null
		 *     [<-- AttributeManager: throw IllegalArgumentException
		 *   end
		 * 
		 *   AttributeManager --> AttributeManager: oldAttribute = getAttribute()
		 *   alt oldAttribute == null
		 *     [<-- AttributeManager: null
		 *   end
		 * 
		 *   AttributeManager -> AttributeManager: removeAttribute()
		 *   activate AttributeManager
		 * 
		 *     AttributeManager -> NativeOrXmlAttributeAdapter: attribute.unlink()
		 *   
		 *     AttributeManager -> AttributeDescriptor: syncToAst()
		 *     activate AttributeDescriptor
		 *       AttributeManager <-- AttributeDescriptor: syncToAst
		 *     deactivate AttributeDescriptor
		 *   
		 *     alt attrContainer != null && syncToAst
		 *       AttributeManager -> AttributeManager: removeAttributeFromAst()
		 *     end
		 * 
		 *     AttributeManager -> AttributeDescriptor: customAction()
		 * 
		 *   deactivate AttributeManager
		 *   
		 *   [<-- AttributeManager: oldAttribute
		 * deactivate AttributeManager
		 * @enduml
		 */
		@Override
		public NativeOrXmlAttributeAdapter removeAttribute(
				AttributeDescriptor descriptor,
				String name,
				WtNodeList attrContainer)
		{
			if (name == null)
				throw new IllegalArgumentException("Argument `name' is null.");
			
			NativeOrXmlAttributeAdapter remove = getAttributeNode(name);
			if (remove == null)
				return null;
			
			removeAttribute(descriptor, remove, attrContainer);
			
			return remove;
		}
		
		@Override
		public void removeAttributeNode(
				AttributeDescriptor descriptor,
				WomAttribute attr,
				WomBackbone parent,
				WtNodeList attrContainer) throws IllegalArgumentException
		{
			if (attr == null)
				throw new IllegalArgumentException("Argument `attr' is null.");
			
			if (attr.getParent() != parent)
				throw new IllegalArgumentException("Given attribute `attr' is not an attribute of this XML element.");
			
			removeAttribute(descriptor, (NativeOrXmlAttributeAdapter) attr, attrContainer);
		}
		
		/*
		 * @startuml AttributeManagerBase-setAttribute.svg
		 * [-> AttributeManager: setAttribute()
		 * activate AttributeManager
		 * 
		 *   alt name == null
		 *     [<-- AttributeManager: throw IllegalArgumentException
		 *   end
		 * 
		 *   alt value == null
		 *     AttributeManager -> AttributeManager: removeAttribute()
		 *     activate AttributeManager
		 *       AttributeManager <-- AttributeManager: oldAttribute
		 *     deactivate AttributeManager
		 *   else
		 *     create NativeOrXmlAttributeAdapter
		 *     AttributeManager -> NativeOrXmlAttributeAdapter: new
		 *     AttributeManager -> AttributeManager: setAttribute()
		 *     activate AttributeManager
		 *       AttributeManager -> AttributeManager: replaceAttribute()
		 *       activate AttributeManager
		 *         AttributeManager <-- AttributeManager: oldAttribute
		 *       deactivate AttributeManager
		 *       AttributeManager <-- AttributeManager: oldAttribute
		 *     deactivate AttributeManager
		 *   end
		 * 
		 *   [<-- AttributeManager: oldAttribute
		 * deactivate AttributeManager
		 * @enduml
		 */
		@Override
		public NativeOrXmlAttributeAdapter setAttribute(
				AttributeDescriptor descriptor,
				String name,
				String value,
				WomBackbone parent,
				WtNodeList attrContainer)
		{
			if (name == null)
				throw new IllegalArgumentException("Argument `name' and/or `value' is null.");
			
			if (value == null)
			{
				return removeAttribute(descriptor, name, attrContainer);
			}
			else
			{
				NativeOrXmlAttributeAdapter attr = new NativeOrXmlAttributeAdapter(name, value);
				
				return setAttributeNode(descriptor, attr, parent, attrContainer);
			}
		}
		
		/*
		 * @startuml AttributeManagerBase-setAttributeNode.svg
		 * [-> AttributeManager: setAttributeNode
		 * activate AttributeManager
		 * 
		 *   AttributeManager -> AttributeManager: replaceAttribute
		 * 
		 *   [<-- AttributeManager: oldAttribute
		 * deactivate AttributeManager
		 * @enduml
		 */
		@Override
		public NativeOrXmlAttributeAdapter setAttributeNode(
				AttributeDescriptor descriptor,
				WomAttribute attr,
				WomBackbone parent,
				WtNodeList attrContainer) throws IllegalArgumentException
		{
			if (attr == null)
				throw new IllegalArgumentException("Argument `attr' is null.");
			
			final NativeOrXmlAttributeAdapter newAttr =
					Toolbox.expectType(NativeOrXmlAttributeAdapter.class, attr, "attr");
			
			if (newAttr.isLinked())
				throw new IllegalStateException(
						"Given attribute `attr' is still attribute of another WOM node.");
			
			NativeOrXmlAttributeAdapter oldAttr = getAttributeNode(attr.getName());
			
			replaceAttribute(descriptor, oldAttr, newAttr, parent, attrContainer);
			
			return oldAttr;
		}
		
		// =====================================================================
		
		/*
		 * @startuml AttributeManagerBase-removeAttribute-2.svg
		 * [-> AttributeManager: removeAttribute()
		 * activate AttributeManager
		 * 
		 *   AttributeManager -> NativeOrXmlAttributeAdapter: unlink()
		 *   
		 *   AttributeManager -> AttributeDescriptor: syncToAst()
		 *   activate AttributeDescriptor
		 *     AttributeManager <-- AttributeDescriptor: syncToAst
		 *   deactivate AttributeDescriptor
		 *   
		 *   alt attrContainer != null && syncToAst
		 *     AttributeManager -> AttributeManager: removeAttributeFromAst()
		 *   end
		 * 
		 *   AttributeManager -> AttributeDescriptor: customAction(null)
		 * 
		 * deactivate AttributeManager
		 * @enduml
		 */
		private void removeAttribute(
				AttributeDescriptor descriptor,
				NativeOrXmlAttributeAdapter attribute,
				WtNodeList attrContainer)
		{
			WomBackbone parent = attribute.getParent();
			
			// remove from WOM
			if (attribute == firstAttr)
				firstAttr = (NativeOrXmlAttributeAdapter) attribute.getNextSibling();
			attribute.unlink();
			
			if (attrContainer != null && descriptor.syncToAst())
				removeAttributeFromAst(attribute, attrContainer);
			
			descriptor.customAction(parent, attribute, null);
		}
		
		private void removeAttributeFromAst(
				NativeOrXmlAttributeAdapter remove,
				WtNodeList attrContainer) throws AssertionError
		{
			// remove from AST
			// the ast can contain an attribute with the same name multiple times!
			Iterator<WtNode> i = attrContainer.iterator();
			while (i.hasNext())
			{
				WtNode node = i.next();
				// there could also be garbage nodes ...
				if (!node.isNodeType(AstNodeTypes.NT_XML_ATTRIBUTE))
					continue;
				
				XmlAttribute attr = (XmlAttribute) node;
				if (attr.getName().equalsIgnoreCase(remove.getName()))
				{
					i.remove();
					// the WOM will always refer to the last attribute
					if (attr == remove.getAstNode())
					{
						i = null;
						break;
					}
				}
			}
			if (i != null)
				throw new AssertionError("WOM and AST out of sync!");
		}
		
		/*
		 * @startuml AttributeManagerBase-replaceAttribute.svg
		 * [-> AttributeManager: replaceAttribute()
		 * activate AttributeManager
		 * 
		 *   alt oldAttr != null
		 *     AttributeManager -> NativeOrXmlAttributeAdapter: oldAttr.unlink()
		 *   end
		 *   
		 *   AttributeManager -> NativeOrXmlAttributeAdapter: newAttr.link()
		 *   
		 *   alt attrContainer != null && syncToAst
		 *     AttributeManager -> AttributeManager: replaceAttributeInAst()
		 *   else
		 *     alt newAttr.getAstNode() == null
		 *       AttributeManager -> NativeOrXmlAttributeAdapter: newAttr.detachAstNode()
		 *     end
		 *   end
		 * 
		 *   AttributeManager -> AttributeDescriptor: customAction(newAttr)
		 * 
		 * deactivate AttributeManager
		 * @enduml
		 */
		private void replaceAttribute(
				AttributeDescriptor descriptor,
				final NativeOrXmlAttributeAdapter oldAttr,
				final NativeOrXmlAttributeAdapter newAttr,
				WomBackbone parent,
				WtNodeList attrContainer)
		{
			// replace in WOM
			WomBackbone prev = null;
			WomBackbone next = null;
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
					prev = (WomBackbone) prev.getNextSibling();
			}
			
			newAttr.link(parent, prev, next);
			if (firstAttr == null || oldAttr == firstAttr)
				firstAttr = newAttr;
			
			if (attrContainer != null && descriptor.syncToAst())
			{
				replaceAttributeInAst(oldAttr, newAttr, attrContainer);
			}
			else
			{
				if (newAttr.getAstNode() == null)
					newAttr.detachAstNode();
			}
			
			descriptor.customAction(parent, oldAttr, newAttr);
		}
		
		private void replaceAttributeInAst(
				final NativeOrXmlAttributeAdapter oldAttr,
				final NativeOrXmlAttributeAdapter newAttr,
				WtNodeList attrContainer) throws AssertionError
		{
			if (newAttr.getAstNode() == null)
				newAttr.attachAstNode();
			
			// replace in AST
			// the ast can contain an attribute with the same name multiple times!
			
			XmlAttribute oldAstNode = null;
			if (oldAttr != null)
				oldAstNode = oldAttr.getAstNode();
			
			String name = newAttr.getName();
			XmlAttribute newAstNode = newAttr.getAstNode();
			
			boolean replaced = false;
			
			ListIterator<WtNode> i = attrContainer.listIterator();
			while (i.hasNext())
			{
				WtNode node = i.next();
				if (!node.isNodeType(AstNodeTypes.NT_XML_ATTRIBUTE))
					continue;
				
				XmlAttribute attr = (XmlAttribute) node;
				if (attr.getName().equalsIgnoreCase(name))
				{
					if (attr == oldAstNode)
					{
						i.set(newAstNode);
						replaced = true;
						
						Object rtd = oldAstNode.getAttribute("RTD");
						if (rtd != null)
							newAstNode.setAttribute("RTD", rtd);
						
						// The node the WOM refers to comes always last
						break;
					}
					else
					{
						// Remove all nodes with the same name and replace only the last
						i.remove();
					}
				}
			}
			
			if (!replaced)
			{
				if (oldAttr != null)
					throw new AssertionError();
				
				attrContainer.add(newAstNode);
			}
		}
	}
}
