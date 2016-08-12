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
package org.sweble.wom3.serialization;

import java.util.HashMap;
import java.util.Map;

public final class ScopeStack
{
	private Scope top = null;

	// =========================================================================

	public String getNsUriForPrefix(String prefix)
	{
		for (Scope scope = top; scope != null; scope = scope.parent)
		{
			String uri = scope.getNsUriForPrefix(prefix);
			if (uri != null)
				return uri;
		}
		return null;
	}

	public String getXmlns()
	{
		for (Scope scope = top; scope != null; scope = scope.parent)
		{
			String uri = scope.getXmlns();
			if (uri != null)
				return uri;
		}
		return null;
	}

	public Scope push()
	{
		top = new Scope(top);
		return top;
	}

	public void pop()
	{
		if (top == null)
			throw new InternalError();
		top = top.parent;
	}

	// =========================================================================

	public static final class Scope
	{
		private final Scope parent;

		private String xmlns = null;

		private final Map<String, String> prefixToNsUriMap =
				new HashMap<String, String>();

		public Scope(Scope parent)
		{
			this.parent = parent;
		}

		public Scope getParent()
		{
			return parent;
		}

		public String getNsUriForPrefix(String prefix)
		{
			return prefixToNsUriMap.get(prefix);
		}

		public void put(String prefix, String valueString)
		{
			String old = prefixToNsUriMap.get(prefix);
			if (old != null && !old.equals(valueString))
				throw new NamespaceException("Conflicting prefixes registered in element's scope");
			prefixToNsUriMap.put(prefix, valueString);
		}

		public String getXmlns()
		{
			return xmlns;
		}

		public void setXmlns(String xmlns)
		{
			if (this.xmlns != null && !this.xmlns.equals(xmlns))
				throw new NamespaceException(String.format(
						"Conflicting default namespace URI declarations in "
								+ "element's scope: '%s' vs. '%s'",
						this.xmlns,
						xmlns));
			this.xmlns = xmlns;
		}
	}
}
