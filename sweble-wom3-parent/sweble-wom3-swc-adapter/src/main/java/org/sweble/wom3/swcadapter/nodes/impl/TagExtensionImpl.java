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
package org.sweble.wom3.swcadapter.nodes.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.sweble.wom3.impl.AttributeDescriptor;
import org.sweble.wom3.impl.AttributeVerifiers;
import org.sweble.wom3.impl.Backbone;
import org.sweble.wom3.impl.ChildDescriptor;
import org.sweble.wom3.impl.DocumentImpl;
import org.sweble.wom3.impl.NativeAndStringValuePair;
import org.sweble.wom3.swcadapter.nodes.SwcAttr;
import org.sweble.wom3.swcadapter.nodes.SwcNode;
import org.sweble.wom3.swcadapter.nodes.SwcTagExtBody;
import org.sweble.wom3.swcadapter.nodes.SwcTagExtension;
import org.sweble.wom3.swcadapter.utils.StringConversionException;
import org.sweble.wom3.swcadapter.utils.SwcTextUtils;

public class TagExtensionImpl
		extends
			BackboneSwcElement
		implements
			SwcTagExtension
{
	private static final long serialVersionUID = 1L;

	private static final ChildDescriptor[] BODY_DESCRIPTOR = {
			childDesc(SwcNode.MWW_NS_URI, "attr", ChildDescriptor.MULTIPLE),
			childDesc(SwcNode.MWW_NS_URI, "body") };

	private Map<String, AttrImpl> argByName;

	private ArrayList<AttrImpl> argByIndex;

	private TagExtBodyImpl body;

	// =========================================================================

	public TagExtensionImpl(DocumentImpl owner)
	{
		super(owner);
	}

	// =========================================================================

	@Override
	public String getSwcName()
	{
		return "tagext";
	}

	// =========================================================================

	@Override
	protected void allowsInsertion(Backbone prev, Backbone child)
	{
		checkInsertion(prev, child, BODY_DESCRIPTOR);
	}

	@Override
	protected void allowsRemoval(Backbone child)
	{
		checkRemoval(child, BODY_DESCRIPTOR);
	}

	@Override
	protected void allowsReplacement(Backbone oldChild, Backbone newChild)
	{
		checkReplacement(oldChild, newChild, BODY_DESCRIPTOR);
	}

	@Override
	public void childInserted(Backbone prev, Backbone added)
	{
		if (added instanceof SwcTagExtBody)
		{
			this.body = (TagExtBodyImpl) added;
		}
		else if (added instanceof SwcAttr)
		{
			// TODO: More efficient implementation possible
			this.argByIndex = null;
			this.argByName = null;
		}
	}

	@Override
	public void childRemoved(Backbone prev, Backbone removed)
	{
		if (removed == this.body)
			this.body = null;
		if (removed instanceof SwcAttr)
		{
			// TODO: More efficient implementation possible
			this.argByIndex = null;
			this.argByName = null;
		}
	}

	// =========================================================================

	@Override
	public String getName()
	{
		return getStringAttr("name");
	}

	@Override
	public String setName(String name)
	{
		return setStringAttr(ATTR_DESC_NAME, "name", name);
	}

	// =========================================================================

	@Override
	public boolean hasBody()
	{
		return this.body != null;
	}

	@Override
	public TagExtBodyImpl getBody()
	{
		return this.body;
	}

	@Override
	public SwcTagExtBody setBody(SwcTagExtBody body)
	{
		return (SwcTagExtBody) replaceOrAppend(this.body, body, false);
	}

	@Override
	public SwcAttr getXmlAttribute(int index)
	{
		if (argByIndex == null)
		{
			argByIndex = new ArrayList<AttrImpl>();
			for (SwcAttr arg : getXmlAttributes())
				argByIndex.add((AttrImpl) arg);
		}
		return argByIndex.get(index);
	}

	@Override
	public SwcAttr getXmlAttribute(String name)
	{
		if (argByName == null)
		{
			argByName = new HashMap<String, AttrImpl>();
			for (SwcAttr arg : getXmlAttributes())
			{
				try
				{
					String resolvedName = SwcTextUtils.womToText(arg.getName()).trim();
					argByName.put(resolvedName, (AttrImpl) arg);
				}
				catch (StringConversionException e)
				{
					// We simply don't add un-resolved argument names to the index.
				}
			}
		}
		return argByName.get(name);
	}

	@Override
	public Collection<SwcAttr> getXmlAttributes()
	{
		// TODO: Use view instead of static excerpt!

		ArrayList<SwcAttr> args = new ArrayList<SwcAttr>();
		for (Backbone i = getFirstChild(); i != null; i = i.getNextSibling())
		{
			if (i instanceof SwcAttr)
				args.add((SwcAttr) i);
		}

		return Collections.unmodifiableCollection(args);
	}

	// =========================================================================

	protected static final AttributeDescriptor ATTR_DESC_NAME = new AttrDescName();

	@Override
	protected AttributeDescriptor getAttributeDescriptor(
			String namespaceURL,
			String localName,
			String qualifiedName)
	{
		return getAttrDescStrict(namespaceURL, localName, qualifiedName,
				"name", ATTR_DESC_NAME);
	}

	public static final class AttrDescName
			extends
				AttributeDescriptor
	{
		@Override
		public int getFlags()
		{
			return makeFlags(
					false /* removable */,
					false /* readOnly */,
					false /* customAction */,
					Normalization.NON_CDATA);
		}

		@Override
		public boolean verifyAndConvert(
				Backbone parent,
				NativeAndStringValuePair verified)
		{
			return AttributeVerifiers.XML_NAME.verifyAndConvert(parent, verified);
		}
	}
}
