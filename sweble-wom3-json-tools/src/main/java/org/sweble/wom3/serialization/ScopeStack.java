/**
 * Copyright 2011 The Open Source Research Group,
 *                University of Erlangen-Nürnberg
 */
package org.sweble.wom3.serialization;

import java.util.HashMap;
import java.util.Map;

final class ScopeStack
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
	
	static final class Scope
	{
		final Scope parent;
		
		private String xmlns = null;
		
		private final Map<String, String> prefixToNsUriMap =
				new HashMap<String, String>();
		
		public Scope(Scope parent)
		{
			this.parent = parent;
		}
		
		private String getNsUriForPrefix(String prefix)
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
		
		private String getXmlns()
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
