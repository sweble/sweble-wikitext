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
package org.sweble.wom3.impl;

import java.net.URL;
import java.util.Map;

import org.joda.time.DateTime;
import org.sweble.wom3.Wom3Attribute;
import org.sweble.wom3.Wom3Clear;
import org.sweble.wom3.Wom3Color;
import org.sweble.wom3.Wom3HorizAlign;
import org.sweble.wom3.Wom3TableVAlign;
import org.sweble.wom3.Wom3ValueWithUnit;
import org.w3c.dom.DOMException;

public abstract class BackboneWomElement
		extends
			BackboneElement
{
	private static final long serialVersionUID = 1L;

	private String prefix;

	// =========================================================================

	public BackboneWomElement(DocumentImpl owner)
	{
		super(owner);
	}

	// =========================================================================

	/**
	 * The name of the WOM node.
	 */
	public abstract String getWomName();

	@Override
	public String getNodeName()
	{
		if (this.prefix == null)
			return getWomName();
		return this.prefix + ":" + getWomName();
	}

	@Override
	public String getPrefix()
	{
		return prefix;
	}

	@Override
	public String getLocalName()
	{
		return getWomName();
	}

	@Override
	public String getNamespaceURI()
	{
		return WOM_NS_URI;
	}

	// =========================================================================

	@Override
	public void setPrefix(String prefix) throws DOMException
	{
		assertWritableOnDocument();

		if ((prefix != null) && prefix.isEmpty())
			prefix = null;

		if (prefix != null)
		{
			Toolbox.checkValidXmlName(prefix);

			if (prefix.indexOf(':') >= 0)
				throw new DOMException(DOMException.NAMESPACE_ERR, "Prefix must not contain ':' character");

			if (prefix.equals("xml"))
				// Cannot have xml: prefix since it's namespaceURI will never be the XML namespace!
				throw new DOMException(DOMException.NAMESPACE_ERR, "WOM node cannot have \"xml\" prefix!");
		}

		this.prefix = prefix;
	}

	// =========================================================================

	/**
	 * If the attribute is not in the global attribute partition and is not
	 * called "xmlns", this method returns null, hence the name "...Strict".
	 * Attribute name comparisons are case-sensitive!
	 */
	protected static AttributeDescriptor getAttrDescStrict(
			String namespaceUri,
			String localName,
			String qualifiedName)
	{
		return getAttrDescStrict(namespaceUri, localName, qualifiedName, null);
	}

	/**
	 * Try to get the attribute descriptor from a map. If no attribute
	 * descriptor matches and the attribute is not in the global attribute
	 * partition and is not called "xmlns", this method returns null, hence the
	 * name "...Strict". Attribute name comparisons are case-sensitive!
	 */
	protected static AttributeDescriptor getAttrDescStrict(
			String namespaceUri,
			String localName,
			String qualifiedName,
			Map<String, AttributeDescriptor> nameMap)
	{
		if ((namespaceUri != null) && !namespaceUri.isEmpty())
			// We accept any attribute in the global attribute partition
			return CommonAttributeDescriptors.ATTR_DESC_GENERIC;

		if ((localName == null) || localName.equals(qualifiedName))
		{
			if (nameMap != null)
			{
				// There's no prefix, it's in the per-element-type parition
				AttributeDescriptor d = nameMap.get(qualifiedName);
				if (d != null)
					return d;
			}

			// We also always accept "xmlns" declarations.
			if ("xmlns".equals(qualifiedName))
				return CommonAttributeDescriptors.ATTR_DESC_GENERIC;

			return null;
		}
		else
		{
			// We have a prefix.
			// We accept any attribute in the global attribute partition
			// FIXME: This is probably not correct. A prefix could be bound
			// to the empty namespace, in which case the attribute would
			// be in the per-element-type partition.
			return CommonAttributeDescriptors.ATTR_DESC_GENERIC;
		}
	}

	/**
	 * Try to get the attribute descriptor from a map. If no attribute
	 * descriptor matches and the attribute is not in the global attribute
	 * partition and is not called "xmlns", this method returns null, hence the
	 * name "...Strict". Attribute name comparisons are case-sensitive!
	 */
	protected static AttributeDescriptor getAttrDescStrict(
			String namespaceUri,
			String localName,
			String qualifiedName,
			String attr1Name,
			AttributeDescriptor attr1Desc)
	{
		return getAttrDescStrict(
				namespaceUri, localName, qualifiedName,
				attr1Name, attr1Desc,
				null, null,
				null, null);
	}

	/**
	 * Try to get the attribute descriptor from a map. If no attribute
	 * descriptor matches and the attribute is not in the global attribute
	 * partition and is not called "xmlns", this method returns null, hence the
	 * name "...Strict". Attribute name comparisons are case-sensitive!
	 */
	protected static AttributeDescriptor getAttrDescStrict(
			String namespaceUri,
			String localName,
			String qualifiedName,
			String attr1Name,
			AttributeDescriptor attr1Desc,
			String attr2Name,
			AttributeDescriptor attr2Desc)
	{
		return getAttrDescStrict(
				namespaceUri, localName, qualifiedName,
				attr1Name, attr1Desc,
				attr2Name, attr2Desc,
				null, null);
	}

	/**
	 * Try to get the attribute descriptor from a map. If no attribute
	 * descriptor matches and the attribute is not in the global attribute
	 * partition and is not called "xmlns", this method returns null, hence the
	 * name "...Strict". Attribute name comparisons are case-sensitive!
	 */
	protected static AttributeDescriptor getAttrDescStrict(
			String namespaceUri,
			String localName,
			String qualifiedName,
			String attr1Name,
			AttributeDescriptor attr1Desc,
			String attr2Name,
			AttributeDescriptor attr2Desc,
			String attr3Name,
			AttributeDescriptor attr3Desc)
	{
		if ((namespaceUri != null) && !namespaceUri.isEmpty())
			// We accept any attribute in the global attribute partition
			return CommonAttributeDescriptors.ATTR_DESC_GENERIC;

		if ((localName == null) || localName.equals(qualifiedName))
		{
			if (attr1Name.equals(qualifiedName))
				return attr1Desc;
			if ((attr2Name != null) && attr2Name.equals(qualifiedName))
				return attr2Desc;
			if ((attr3Name != null) && attr3Name.equals(qualifiedName))
				return attr3Desc;

			// We also always accept "xmlns" declarations.
			if ("xmlns".equals(qualifiedName))
				return CommonAttributeDescriptors.ATTR_DESC_GENERIC;

			return null;
		}
		else
		{
			// We have a prefix.
			// We accept any attribute in the global attribute partition
			// FIXME: This is probably not correct. A prefix could be bound
			// to the empty namespace, in which case the attribute would
			// be in the per-element-type partition.
			return CommonAttributeDescriptors.ATTR_DESC_GENERIC;
		}
	}

	/**
	 * Try to get the attribute descriptor from a map. If no attribute
	 * descriptor matches and the attribute is not in the global attribute
	 * partition and is not called "xmlns", this method returns the generic
	 * attribute descriptor. Attribute name comparisons are case-sensitive!
	 */
	protected static AttributeDescriptor getAttrDesc(
			String namespaceUri,
			String localName,
			String qualifiedName,
			Map<String, AttributeDescriptor> nameMap)
	{
		if (((namespaceUri == null) || ((namespaceUri != null) && namespaceUri.isEmpty()))
				&& ((localName == null) || localName.equals(qualifiedName)))
		{
			// The attribute is in the per-element-type partition
			// FIXME: This is probably not correct. A prefix could be bound
			// to the empty namespace, in which case the attribute would
			// be in the per-element-type partition.
			AttributeDescriptor d = nameMap.get(qualifiedName.toLowerCase());
			if (d != null)
				return d;
		}

		return CommonAttributeDescriptors.ATTR_DESC_GENERIC;
	}

	/**
	 * Try to get the attribute descriptor from a map. If no attribute
	 * descriptor matches and the attribute is not in the global attribute
	 * partition and is not called "xmlns", this method returns the generic
	 * attribute descriptor. Attribute name comparisons are case-sensitive!
	 */
	protected static AttributeDescriptor getAttrDesc(
			String namespaceUri,
			String localName,
			String qualifiedName,
			String attr1Name,
			AttributeDescriptor attr1Desc)
	{
		if (((namespaceUri == null) || ((namespaceUri != null) && namespaceUri.isEmpty()))
				&& ((localName == null) || localName.equals(qualifiedName)))
		{
			// The attribute is in the per-element-type partition
			// FIXME: This is probably not correct. A prefix could be bound
			// to the empty namespace, in which case the attribute would
			// be in the per-element-type partition.
			if (attr1Name.equals(qualifiedName))
				return attr1Desc;
		}

		return CommonAttributeDescriptors.ATTR_DESC_GENERIC;
	}

	// =========================================================================

	public URL getUrlAttr(String name)
	{
		return (URL) getAttributeNativeData(name);
	}

	public URL setUrlAttr(AttributeDescriptor d, String name, URL source)
	{
		return setAttributeDirect(d, name, source);
	}

	public Wom3Clear getClearAttr(String name)
	{
		return (Wom3Clear) getAttributeNativeData(name);
	}

	public Wom3Clear setClearAttr(
			AttributeDescriptor d,
			String name,
			Wom3Clear clear)
	{
		return setAttributeDirect(d, name, clear);
	}

	public DateTime setDatetimeAttr(String name)
	{
		return (DateTime) getAttributeNativeData(name);
	}

	public DateTime setDatetimeAttr(
			AttributeDescriptor d,
			String name,
			DateTime timestamp)
	{
		return setAttributeDirect(d, name, timestamp);
	}

	public Wom3HorizAlign getAlignAttr(String name)
	{
		return (Wom3HorizAlign) getAttributeNativeData(name);
	}

	public Wom3HorizAlign setAlignAttr(
			AttributeDescriptor d,
			String name,
			Wom3HorizAlign align)
	{
		return setAttributeDirect(d, name, align);
	}

	public Wom3TableVAlign getTableVAlignAttr(String name)
	{
		return (Wom3TableVAlign) getAttributeNativeData(name);
	}

	public Wom3TableVAlign setTableVAlignAttr(
			AttributeDescriptor d,
			String name,
			Wom3TableVAlign valign)
	{
		return setAttributeDirect(d, name, valign);
	}

	public Wom3Color getColorAttr(String name)
	{
		return (Wom3Color) getAttributeNativeData(name);
	}

	public Wom3Color setColorAttr(
			AttributeDescriptor d,
			String name,
			Wom3Color color)
	{
		return setAttributeDirect(d, name, color);
	}

	// =========================================================================

	public String getStringAttr(String name)
	{
		// getAttribute() would return "" for non-existing attributes.
		// But getStringAttr() is called for our WOM specific getter methods
		// which must return null for non-existing attributes.
		//return getAttribute(name);

		Wom3Attribute attributeNode = getAttributeNode(name);
		if (attributeNode == null)
			return null;
		return attributeNode.getNodeValue();
	}

	public String setStringAttr(
			AttributeDescriptor d,
			String name,
			String value)
	{
		return setAttributeDirect(d, name, value);
	}

	public Integer getIntAttr(String name)
	{
		return (Integer) getAttributeNativeData(name);
	}

	public Integer setIntAttr(
			AttributeDescriptor d,
			String name,
			Integer value)
	{
		return setAttributeDirect(d, name, value);
	}

	public boolean getBoolAttr(String name)
	{
		return getAttributeNativeData(name) != null;
	}

	public boolean setBoolAttr(
			AttributeDescriptor d,
			String name,
			boolean value)
	{
		return setAttributeDirect(d, name, value) != null;
	}

	public Wom3ValueWithUnit getValueWithUnitAttr(String name)
	{
		return (Wom3ValueWithUnit) getAttributeNativeData(name);
	}

	public Wom3ValueWithUnit setValueWithUnitAttr(
			AttributeDescriptor d,
			String name,
			Wom3ValueWithUnit value)
	{
		return setAttributeDirect(d, name, value);
	}
}
