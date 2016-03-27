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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.sweble.wom3.Wom3DefinitionList;
import org.sweble.wom3.Wom3DefinitionListDef;
import org.sweble.wom3.Wom3DefinitionListItem;
import org.sweble.wom3.Wom3DefinitionListTerm;
import org.sweble.wom3.Wom3Node;

public class DefinitionListImpl
		extends
			BackboneWomElemWithCoreAttrs
		implements
			Wom3DefinitionList
{
	private static final long serialVersionUID = 1L;

	private DefinitionListTermImpl implicitFirstDt;

	private ArrayList<Wom3DefinitionListTerm> terms = new ArrayList<Wom3DefinitionListTerm>();

	// =========================================================================

	public DefinitionListImpl(DocumentImpl owner)
	{
		super(owner);
	}

	// =========================================================================

	@Override
	public String getWomName()
	{
		return "dl";
	}

	// =========================================================================

	@Override
	public boolean isCompact()
	{
		return getBoolAttr("compact");
	}

	@Override
	public boolean setCompact(boolean compact)
	{
		return setBoolAttr(CommonAttributeDescriptors.ATTR_DESC_COMPACT, "compact", compact);
	}

	// =========================================================================

	@Override
	public int getTermNum()
	{
		return terms.size();
	}

	@Override
	public Collection<Wom3DefinitionListTerm> getTerms()
	{
		return Collections.unmodifiableCollection(terms);
	}

	@Override
	public Wom3DefinitionListTerm getTerm(int index) throws IndexOutOfBoundsException
	{
		return terms.get(index);
	}

	@Override
	public Wom3DefinitionListTerm replaceTerm(
			int index,
			Wom3DefinitionListTerm term) throws IndexOutOfBoundsException
	{
		assertWritableOnDocument();

		Wom3DefinitionListTerm old = getTerm(index);
		terms.set(index, term);
		replaceChildNoNotify(term, old);
		removeDefs(old);
		appendDefs(term);
		return old;
	}

	@Override
	public Wom3DefinitionListTerm removeTerm(int index) throws IndexOutOfBoundsException
	{
		assertWritableOnDocument();

		Wom3DefinitionListTerm old = terms.remove(index);
		removeChildNoNotify(old);
		removeDefs(old);
		return old;
	}

	@Override
	public void appendTerm(Wom3DefinitionListTerm term)
	{
		assertWritableOnDocument();

		terms.add(term);
		appendChildNoNotify(term);
		appendDefs(term);
	}

	@Override
	public void insertTerm(int beforeIndex, Wom3DefinitionListTerm term) throws IndexOutOfBoundsException
	{
		assertWritableOnDocument();

		if (beforeIndex < 0 || beforeIndex > terms.size())
			throw new IndexOutOfBoundsException();

		if (beforeIndex == terms.size())
		{
			appendTerm(term);
		}
		else
		{
			insertBeforeNoNotify(terms.get(beforeIndex), term);
		}

		terms.add(beforeIndex, term);
		appendDefs(term);
	}

	private void removeDefs(Wom3DefinitionListTerm removedTerm)
	{
		// The term itself keeps its children. We just unlink them from this 
		// <dl> element.
		for (Wom3DefinitionListDef dd : removedTerm.getDefs())
			removeChildNoNotify(dd);
	}

	private void appendDefs(Wom3DefinitionListTerm addedTerm)
	{
		Wom3Node before = addedTerm.getNextSibling();
		if (before != null)
		{
			for (Wom3DefinitionListDef dd : addedTerm.getDefs())
				insertBeforeNoNotify(before, dd);
		}
		else
		{
			for (Wom3DefinitionListDef dd : addedTerm.getDefs())
				appendChildNoNotify(dd);
		}
	}

	// =========================================================================

	@Override
	public int getItemNum()
	{
		int count = 0;
		for (Wom3DefinitionListTerm term : terms)
			count += 1 + term.getDefNum();
		return count;
	}

	@Override
	public Collection<Wom3DefinitionListItem> getItems()
	{
		ArrayList<Wom3DefinitionListItem> items =
				new ArrayList<Wom3DefinitionListItem>(terms.size() * 5);
		if (implicitFirstDt != null)
			items.addAll(implicitFirstDt.getDefs());
		for (Wom3DefinitionListTerm term : terms)
		{
			items.add(term);
			items.addAll(term.getDefs());
		}
		return Collections.unmodifiableCollection(items);
	}

	@Override
	public Wom3DefinitionListItem getItem(int index) throws IndexOutOfBoundsException
	{
		Wom3DefinitionListItem item = getItemOrNullAtSize(index);
		if (item == null)
			throw new IndexOutOfBoundsException();
		return item;
	}

	@Override
	public Wom3DefinitionListItem replaceItem(
			int index,
			Wom3DefinitionListItem item) throws IndexOutOfBoundsException
	{
		Wom3DefinitionListItem old = getItem(index);
		if (old.getClass() != item.getClass())
			throw new UnsupportedOperationException("Can only replace item with another item of the same type!");

		replaceChild(item, old);
		return old;
	}

	@Override
	public Wom3DefinitionListItem removeItem(int index) throws IndexOutOfBoundsException
	{
		Wom3DefinitionListItem old = getItem(index);
		removeChild(old);
		return old;
	}

	@Override
	public void appendItem(Wom3DefinitionListItem item)
	{
		appendChild(item);
	}

	@Override
	public void insertItem(int beforeIndex, Wom3DefinitionListItem item) throws IndexOutOfBoundsException
	{
		Wom3DefinitionListItem before = getItemOrNullAtSize(beforeIndex);
		if (before == null)
		{
			appendChild(item);
		}
		else
		{
			insertBefore(item, before);
		}
	}

	private Wom3DefinitionListItem getItemOrNullAtSize(int index)
	{
		int count = 0;
		if (implicitFirstDt != null)
		{
			int sub = index;
			int subSize = implicitFirstDt.getDefNum();
			if (sub < subSize)
				return implicitFirstDt.getDef(sub);
			count += subSize;
		}
		for (Wom3DefinitionListTerm term : terms)
		{
			if (index == count)
				return term;
			++count;
			int sub = index - count;
			int subSize = term.getDefNum();
			if (sub < subSize)
				return term.getDef(sub);
			count += subSize;
		}
		if (index == count)
			return null;
		throw new IndexOutOfBoundsException();
	}

	// =========================================================================

	@Override
	public void childInserted(Backbone prev, Backbone added)
	{
		if (added instanceof Wom3DefinitionListDef)
		{
			Backbone p = searchTermForDef(prev);
			if (prev == null)
				prev = implicitFirstDt;
			((DefinitionListTermImpl) p).defAdded(prev, (Wom3DefinitionListDef) added);
		}
		else if (added instanceof Wom3DefinitionListTerm)
		{
			DefinitionListTermImpl addedTerm = (DefinitionListTermImpl) added;

			int i = (prev == null) ? 0 : indexOfTerm(prev) + 1;
			DefinitionListTermImpl stealFrom = (DefinitionListTermImpl)
					((i > 0) ? terms.get(i - 1) : implicitFirstDt);

			// If the term was added in between the defs of another term, we 
			// have to steal the other term's defs which now are located behind 
			// the inserted term
			Backbone sFirst = null;
			Backbone sLast = addedTerm;
			if (stealFrom != null && !(addedTerm.getNextSibling() instanceof Wom3DefinitionListTerm))
			{
				// Find range of defs to steal
				while (true)
				{
					Backbone n = sLast.getNextSibling();
					if (n instanceof Wom3DefinitionListTerm || n == null)
						break;
					sLast = n;
				}
				sFirst = addedTerm.getNextSibling();
			}

			// Insert term into term array and add the term's defs directly 
			// after the term
			terms.add(i, addedTerm);
			appendDefs(addedTerm);

			if (sLast != addedTerm)
			{
				stealFrom.transfer(sFirst, sLast, addedTerm);
				if (implicitFirstDt != null && implicitFirstDt.getDefNum() == 0)
					implicitFirstDt = null;
			}
		}
	}

	@Override
	public void childRemoved(Backbone prev, Backbone removed)
	{
		if (removed instanceof Wom3DefinitionListDef)
		{
			Backbone p = searchTermForDef(prev);
			((DefinitionListTermImpl) p).defRemoved((Wom3DefinitionListDef) removed);
		}
		else if (removed instanceof Wom3DefinitionListTerm)
		{
			int i = (prev == null) ? 0 : indexOfTerm(prev) + 1;
			terms.remove(i);
			removeDefs((Wom3DefinitionListTerm) removed);
		}
	}

	private Backbone searchTermForDef(Backbone prev)
	{
		// Search backwards for the term to which the added definition will belong
		Backbone p = prev;
		while (p != null && !(p instanceof Wom3DefinitionListTerm))
			p = p.getPreviousSibling();
		// There's no term in front of the inserted definition
		if (p == null)
			p = getImplicitFirstDt();
		return p;
	}

	private Backbone getImplicitFirstDt()
	{
		if (implicitFirstDt == null)
			implicitFirstDt = (DefinitionListTermImpl)
					getOwnerDocument().createElementNS(Wom3Node.WOM_NS_URI, "dt");
		return implicitFirstDt;
	}

	private int indexOfTerm(Wom3Node node)
	{
		int i = -1;
		Wom3Node child = getFirstChild();
		while (true)
		{
			if (child instanceof Wom3DefinitionListTerm)
				++i;
			if (child == node)
				return i;
			child = child.getNextSibling();
		}
	}

	// =========================================================================

	private static final Map<String, AttributeDescriptor> NAME_MAP = new HashMap<String, AttributeDescriptor>();

	static
	{
		NAME_MAP.putAll(UniversalAttributes.getNameMap());
		NAME_MAP.put("compact", CommonAttributeDescriptors.ATTR_DESC_COMPACT);
	}

	@Override
	protected AttributeDescriptor getAttributeDescriptor(
			String namespaceUri,
			String localName,
			String qualifiedName)
	{
		return getAttrDesc(namespaceUri, localName, qualifiedName, NAME_MAP);
	}
}
