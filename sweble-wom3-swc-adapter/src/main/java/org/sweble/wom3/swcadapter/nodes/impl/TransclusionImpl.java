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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.sweble.wom3.impl.AttributeDescriptor;
import org.sweble.wom3.impl.Backbone;
import org.sweble.wom3.impl.ChildDescriptor;
import org.sweble.wom3.impl.DocumentImpl;
import org.sweble.wom3.impl.SiblingCollectionBounds;
import org.sweble.wom3.impl.SiblingRangeCollection;
import org.sweble.wom3.swcadapter.nodes.SwcArg;
import org.sweble.wom3.swcadapter.nodes.SwcName;
import org.sweble.wom3.swcadapter.nodes.SwcTransclusion;
import org.sweble.wom3.swcadapter.utils.StringConversionException;
import org.sweble.wom3.swcadapter.utils.SwcTextUtils;

public class TransclusionImpl
		extends
			BackboneSwcElement
		implements
			SwcTransclusion
{
	private static final long serialVersionUID = 1L;
	
	private static final ChildDescriptor[] BODY_DESCRIPTOR = {
			childDesc(MWW_NS_URI, "name", ChildDescriptor.REQUIRED),
			childDesc(MWW_NS_URI, "arg", ChildDescriptor.MULTIPLE) };
	
	private NameImpl name;
	
	private SiblingRangeCollection<TransclusionImpl, ArgImpl> args;
	
	private Map<String, ArgImpl> argByName;
	
	private ArrayList<ArgImpl> argByIndex;
	
	// =========================================================================
	
	public TransclusionImpl(DocumentImpl owner)
	{
		super(owner);
		
		args = new SiblingRangeCollection<TransclusionImpl, ArgImpl>(
				this, new SiblingCollectionsBoundIml());
	}
	
	private final class SiblingCollectionsBoundIml
			implements
				SiblingCollectionBounds,
				Serializable
	{
		private static final long serialVersionUID = 1L;
		
		@Override
		public Backbone getPred()
		{
			return name;
		}
		
		@Override
		public Backbone getSucc()
		{
			return null;
		}
	}
	
	// =========================================================================
	
	@Override
	public String getSwcName()
	{
		return "transclusion";
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
		if (added instanceof SwcName)
			this.name = (NameImpl) added;
		if (added instanceof SwcArg)
		{
			// TODO: More efficient implementation possible
			this.argByIndex = null;
			this.argByName = null;
		}
	}
	
	@Override
	public void childRemoved(Backbone prev, Backbone removed)
	{
		if (removed == this.name)
			this.name = null;
		if (removed instanceof SwcArg)
		{
			// TODO: More efficient implementation possible
			this.argByIndex = null;
			this.argByName = null;
		}
	}
	
	// =========================================================================
	
	@Override
	public SwcName getName()
	{
		return this.name;
	}
	
	@Override
	public SwcName setName(SwcName name)
	{
		return (SwcName) replaceOrInsertBeforeOrAppend(
				this.name, getFirstChild(), name, true);
	}
	
	@Override
	public SwcArg getArgument(int index)
	{
		if (argByIndex == null)
		{
			argByIndex = new ArrayList<ArgImpl>();
			for (ArgImpl arg : args)
				argByIndex.add(arg);
		}
		return argByIndex.get(index);
	}
	
	@Override
	public SwcArg getArgument(String name)
	{
		if (argByName == null)
		{
			argByName = new HashMap<String, ArgImpl>();
			for (ArgImpl arg : args)
			{
				try
				{
					String resolvedName = SwcTextUtils.womToText(arg.getName()).trim();
					argByName.put(resolvedName, arg);
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
	public Collection<SwcArg> getArguments()
	{
		@SuppressWarnings({ "unchecked", "rawtypes" })
		List<SwcArg> uc =
				(List) Collections.unmodifiableList(args);
		return uc;
	}
	
	// =========================================================================
	
	@Override
	protected AttributeDescriptor getAttributeDescriptor(
			String namespaceUri,
			String localName,
			String qualifiedName)
	{
		return getAttrDescStrict(namespaceUri, localName, qualifiedName);
	}
}
