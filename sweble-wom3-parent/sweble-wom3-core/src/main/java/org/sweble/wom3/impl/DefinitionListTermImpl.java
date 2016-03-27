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

import org.sweble.wom3.Wom3DefinitionListDef;
import org.sweble.wom3.Wom3DefinitionListTerm;
import org.sweble.wom3.Wom3Node;

public class DefinitionListTermImpl
		extends
			BackboneWomElemWithUnivAttrs
		implements
			Wom3DefinitionListTerm
{
	private static final long serialVersionUID = 1L;

	private ArrayList<Wom3DefinitionListDef> defs = new ArrayList<Wom3DefinitionListDef>();

	// =========================================================================

	public DefinitionListTermImpl(DocumentImpl owner)
	{
		super(owner);
	}

	// =========================================================================

	@Override
	public String getWomName()
	{
		return "dt";
	}

	// =========================================================================

	@Override
	public int getDefNum()
	{
		return defs.size();
	}

	@Override
	public Collection<Wom3DefinitionListDef> getDefs()
	{
		return Collections.unmodifiableCollection(defs);
	}

	@Override
	public Wom3DefinitionListDef getDef(int index) throws IndexOutOfBoundsException
	{
		return defs.get(index);
	}

	@Override
	public Wom3DefinitionListDef replaceDef(int index, Wom3DefinitionListDef def) throws IndexOutOfBoundsException
	{
		Backbone parent = getParentNode();
		if (parent != null)
		{
			Wom3DefinitionListDef old = getDef(index);
			// Is expected to assertWritable();
			parent.replaceChild(def, old);
			return old;
		}
		else
		{
			assertWritableOnDocument();
			return defs.set(index, def);
		}
	}

	@Override
	public Wom3DefinitionListDef removeDef(int index) throws IndexOutOfBoundsException
	{
		Backbone parent = getParentNode();
		if (parent != null)
		{
			Wom3DefinitionListDef old = getDef(index);
			// Is expected to assertWritable();
			parent.removeChild(old);
			return old;
		}
		else
		{
			assertWritableOnDocument();
			return defs.remove(index);
		}
	}

	@Override
	public void appendDef(Wom3DefinitionListDef def)
	{
		Backbone parent = getParentNode();
		if (parent != null)
		{
			Wom3Node before = null;
			if (!defs.isEmpty())
			{
				Wom3DefinitionListDef lastDef = defs.get(defs.size() - 1);
				before = lastDef.getNextSibling();
			}
			else
			{
				before = this.getNextSibling();
			}

			if (before == null)
			{
				// Is expected to assertWritable();
				parent.appendChild(def);
			}
			else
			{
				// Is expected to assertWritable();
				parent.insertBefore(def, before);
			}
		}
		else
		{
			assertWritableOnDocument();
			defs.add(def);
		}
	}

	@Override
	public void insertDef(int beforeIndex, Wom3DefinitionListDef def) throws IndexOutOfBoundsException
	{
		Backbone parent = getParentNode();
		if (parent != null)
		{
			int size = defs.size();
			if (beforeIndex < 0 || beforeIndex > size)
				throw new IndexOutOfBoundsException();
			if (beforeIndex == size)
			{
				// Is expected to assertWritable();
				appendDef(def);
			}
			else
			{
				// Is expected to assertWritable();
				parent.insertBefore(def, defs.get(beforeIndex));
			}
		}
		else
		{
			assertWritableOnDocument();
			defs.add(beforeIndex, def);
		}
	}

	// =========================================================================

	protected void defRemoved(Wom3DefinitionListDef removed)
	{
		for (int i = 0; i < defs.size(); ++i)
		{
			if (defs.get(i) == removed)
			{
				defs.remove(i);
				return;
			}
		}
		throw new InternalError();
	}

	protected void defAdded(Backbone prev, Wom3DefinitionListDef added)
	{
		int i = 0;
		Backbone p = prev;
		while (true)
		{
			if (p == null)
			{
				defs.add(i, added);
				return;
			}
			else if (p instanceof Wom3DefinitionListTerm)
			{
				if (p != this)
					throw new InternalError();
				defs.add(i, added);
				return;
			}
			else if (p instanceof Wom3DefinitionListDef)
			{
				++i;
			}
			p = p.getPreviousSibling();
		}
	}

	protected void appendAll(Collection<Wom3DefinitionListDef> defs)
	{
		this.defs.addAll(defs);
	}

	protected void transfer(
			Backbone sFirst,
			Backbone sLast,
			DefinitionListTermImpl other)
	{
		Backbone d = sLast;
		int i = defs.size();
		while (true)
		{
			if (d instanceof Wom3DefinitionListDef)
			{
				// Sanity check
				if (defs.get(--i) != d)
					throw new InternalError();
			}
			if (d == sFirst)
				break;
			// Step over non defs
			d = d.getPreviousSibling();
		}
		// Sanity check
		if (d != sFirst)
			throw new InternalError();

		// Do it!
		other.appendAll(defs.subList(i, defs.size()));
		while (defs.size() > i)
			defs.remove(defs.size() - 1);
	}
}
