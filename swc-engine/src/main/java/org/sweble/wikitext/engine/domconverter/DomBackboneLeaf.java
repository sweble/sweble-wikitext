package org.sweble.wikitext.engine.domconverter;

import java.util.Collection;
import java.util.Collections;

import org.sweble.wikitext.engine.dom.DomNode;

public abstract class DomBackboneLeaf
        extends
            DomBackbone
{
	private static final long serialVersionUID = 1L;
	
	// =========================================================================
	
	public DomBackboneLeaf()
	{
	}
	
	/*
	public DomBackboneLeaf(DomBackbone parent, DomBackbone nextSibling, DomBackbone prevSibling)
	{
		super(parent, nextSibling, prevSibling);
	}
	*/
	
	// =========================================================================
	
	@Override
	public final boolean hasChildNodes()
	{
		return false;
	}
	
	@Override
	public final Collection<DomNode> childNodes()
	{
		return Collections.emptyList();
	}
	
	@Override
	public final DomNode getFirstChild()
	{
		return null;
	}
	
	@Override
	public final DomNode getLastChild()
	{
		return null;
	}
	
	@Override
	public final void appendChild(DomNode child) throws UnsupportedOperationException
	{
		throw new UnsupportedOperationException();
	}
	
	@Override
	public final void insertBefore(DomNode before, DomNode child) throws UnsupportedOperationException, IllegalArgumentException
	{
		throw new UnsupportedOperationException();
	}
	
	@Override
	public final boolean removeChild(DomNode child) throws UnsupportedOperationException
	{
		throw new UnsupportedOperationException();
	}
	
	@Override
	public final boolean replaceChild(DomNode search, DomNode replace) throws UnsupportedOperationException
	{
		throw new UnsupportedOperationException();
	}
}
