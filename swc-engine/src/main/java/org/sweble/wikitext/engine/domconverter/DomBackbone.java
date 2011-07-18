package org.sweble.wikitext.engine.domconverter;

import org.sweble.wikitext.engine.dom.DomNode;

public abstract class DomBackbone
        implements
            DomNode
{
	private static final long serialVersionUID = 1L;
	
	private DomBackbone parent;
	
	private DomBackbone prevSibling;
	
	private DomBackbone nextSibling;
	
	// =========================================================================
	
	public DomBackbone()
	{
	}
	
	// =========================================================================
	
	@Override
	public final DomBackbone getParent()
	{
		return parent;
	}
	
	@Override
	public final DomBackbone getPrevSibling()
	{
		return prevSibling;
	}
	
	@Override
	public final DomBackbone getNextSibling()
	{
		return nextSibling;
	}
	
	// =========================================================================
	
	@Override
	public final DomNode cloneNode()
	{
		try
		{
			return (DomNode) this.clone();
		}
		catch (CloneNotSupportedException e)
		{
			throw new AssertionError();
		}
	}
	
	@Override
	protected Object clone() throws CloneNotSupportedException
	{
		DomBackbone node = (DomBackbone) super.clone();
		node.parent = null;
		node.nextSibling = null;
		node.prevSibling = null;
		return node;
	}
	
	// =========================================================================
	
	protected void setParent(DomBackbone parent)
	{
		this.parent = parent;
	}
	
	protected void setPrevSibling(DomBackbone prevSibling)
	{
		this.prevSibling = prevSibling;
	}
	
	protected void setNextSibling(DomBackbone nextSibling)
	{
		this.nextSibling = nextSibling;
	}
	
	public boolean isLinked()
	{
		return parent != null;
	}
	
	public void unlink()
	{
		parent = null;
		
		if (prevSibling != null)
			prevSibling.nextSibling = nextSibling;
		if (nextSibling != null)
			nextSibling.prevSibling = prevSibling;
		
		prevSibling = null;
		nextSibling = null;
	}
	
	public void link(DomBackbone parent, DomBackbone prevSibling, DomBackbone nextSibling)
	{
		if (isLinked())
			throw new IllegalStateException(
			        "Node is still child of another DOM node.");
		
		this.parent = parent;
		
		this.prevSibling = prevSibling;
		if (prevSibling != null)
		{
			if (prevSibling.nextSibling != nextSibling)
				throw new IllegalStateException(
				        "DOM sibling chain inconsistent.");
			
			prevSibling.nextSibling = this;
		}
		
		this.nextSibling = nextSibling;
		if (nextSibling != null)
		{
			if (nextSibling.prevSibling != prevSibling)
				throw new IllegalStateException(
				        "DOM sibling chain inconsistent.");
			
			nextSibling.prevSibling = this;
		}
	}
}
