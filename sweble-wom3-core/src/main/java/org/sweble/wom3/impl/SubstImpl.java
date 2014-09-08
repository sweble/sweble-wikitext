/**
 * Copyright 2011 The Open Source Research Group,
 *                University of Erlangen-Nürnberg
 */
package org.sweble.wom3.impl;

import org.sweble.wom3.Wom3For;
import org.sweble.wom3.Wom3Node;
import org.sweble.wom3.Wom3Repl;
import org.sweble.wom3.Wom3Subst;

public class SubstImpl
		extends
			BackboneContainer
		implements
			Wom3Subst
{
	private static final long serialVersionUID = 1L;
	
	private static final ChildDescriptor[] BODY_DESCRIPTOR = {
			childDesc("repl", ChildDescriptor.REQUIRED),
			childDesc("for", ChildDescriptor.REQUIRED) };
	
	private Wom3Repl repl;
	
	private Wom3For for_;
	
	// =========================================================================
	
	public SubstImpl(DocumentImpl owner)
	{
		super(owner);
	}
	
	// =========================================================================
	
	@Override
	public String getWomName()
	{
		return "subst";
	}
	
	// =========================================================================
	
	@Override
	public String getDisplacementId()
	{
		return getStringAttr("did");
	}
	
	@Override
	public String setDisplacementId(String did)
	{
		return setStringAttr(Attributes.DID, "did", did);
	}
	
	// =========================================================================
	
	@Override
	public Wom3Repl getRepl()
	{
		return repl;
	}
	
	@Override
	public Wom3Repl setRepl(Wom3Repl repl)
	{
		return (Wom3Repl) replaceOrInsertBeforeOrAppend(
				this.repl, this.for_, repl, true);
	}
	
	@Override
	public Wom3For getFor()
	{
		return for_;
	}
	
	@Override
	public Wom3For setFor(Wom3For for_) throws NullPointerException
	{
		return (Wom3For) replaceOrAppend(this.for_, for_, true);
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
		if (added instanceof Wom3Repl)
			this.repl = (Wom3Repl) added;
		else if (added instanceof Wom3For)
			this.for_ = (Wom3For) added;
	}
	
	// =========================================================================
	
	@Override
	protected AttributeDescriptor getAttributeDescriptor(
			String namespaceUri,
			String localName,
			String qualifiedName)
	{
		return getAttrDescStrict(namespaceUri, localName, qualifiedName,
				"did", Attributes.DID);
	}
	
	private static enum Attributes implements AttributeDescriptor
	{
		DID
		{
			@Override
			public boolean verifyAndConvert(
					Backbone parent,
					NativeAndStringValuePair verified)
			{
				return AttributeVerifiers.ID.verifyAndConvert(parent, verified);
			}
		};
		
		// =====================================================================
		
		@Override
		public boolean isRemovable()
		{
			return true;
		}
		
		@Override
		public Normalization getNormalizationMode()
		{
			return Normalization.NON_CDATA;
		}
		
		@Override
		public void customAction(
				Wom3Node parent,
				AttributeBase oldAttr,
				AttributeBase newAttr)
		{
		}
	}
}
