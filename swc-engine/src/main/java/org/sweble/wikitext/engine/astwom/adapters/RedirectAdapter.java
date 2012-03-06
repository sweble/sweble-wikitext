/**
 * Copyright 2011 The Open Source Research Group,
 *                University of Erlangen-Nürnberg
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.sweble.wikitext.engine.astwom.adapters;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import org.sweble.wikitext.engine.astwom.AttributeDescriptor;
import org.sweble.wikitext.engine.astwom.AttributeManagerBase;
import org.sweble.wikitext.engine.astwom.AttributeSupportingElement;
import org.sweble.wikitext.engine.astwom.GenericAttributeDescriptor;
import org.sweble.wikitext.engine.astwom.Toolbox;
import org.sweble.wikitext.engine.astwom.UniversalAttributes;
import org.sweble.wikitext.engine.wom.WomNode;
import org.sweble.wikitext.engine.wom.WomNodeType;
import org.sweble.wikitext.engine.wom.WomRedirect;
import org.sweble.wikitext.engine.wom.WomTitle;
import org.sweble.wikitext.lazy.preprocessor.Redirect;
import org.sweble.wikitext.lazy.utils.TextUtils;

import de.fau.cs.osr.utils.Utils;

public class RedirectAdapter
		extends
			AttributeSupportingElement
		implements
			WomRedirect
{
	private static final long serialVersionUID = 1L;
	
	private static final WomTitle EMPTY_TITLE = new EmptyTitleAdapter();
	
	@Getter(AccessLevel.PROTECTED)
	@Setter(AccessLevel.PROTECTED)
	private AttributeManagerBase attribManager = AttributeManagerBase.emptyManager();
	
	// =========================================================================
	
	public RedirectAdapter(String target)
	{
		super(new Redirect(target));
		setTarget(target);
	}
	
	public RedirectAdapter(Redirect redirect)
	{
		super(redirect);
		setRedirectFromAst();
		if (redirect == null)
			throw new NullPointerException();
	}
	
	// =========================================================================
	
	@Override
	public String getNodeName()
	{
		return "redirect";
	}
	
	@Override
	public Redirect getAstNode()
	{
		return (Redirect) super.getAstNode();
	}
	
	@Override
	public WomNodeType getNodeType()
	{
		return WomNodeType.ELEMENT;
	}
	
	// =========================================================================
	
	@Override
	public String getTarget()
	{
		return getAttribute("target");
	}
	
	@Override
	public String setTarget(String page)
	{
		NativeOrXmlAttributeAdapter old = setAttributeUnchecked(
				Attributes.target,
				"target",
				page);
		
		return (old == null) ? null : old.getValue();
	}
	
	@Override
	public WomTitle getLinkTitle()
	{
		return EMPTY_TITLE;
	}
	
	@Override
	public String getLinkTarget()
	{
		return getTarget();
	}
	
	// =========================================================================
	
	private void setRedirectFromAst()
	{
		setAttributeUnchecked(
				Attributes.TARGET_FROM_AST,
				"target",
				getAstNode().getTarget());
	}
	
	private void setTargetInAst(String target)
	{
		Redirect astNode = getAstNode();
		astNode.setTarget(target);
		TextUtils.addRtData(
				astNode,
				TextUtils.joinRt("#REDIRECT[[", target, "]]"));
	}
	
	// =========================================================================
	
	@Override
	protected AttributeDescriptor getAttributeDescriptor(String name)
	{
		AttributeDescriptor d = Utils.fromString(Attributes.class, name);
		if (d != null && d != Attributes.TARGET_FROM_AST)
			return d;
		d = Utils.fromString(UniversalAttributes.class, name);
		if (d != null)
			return d;
		return GenericAttributeDescriptor.get();
	}
	
	private static enum Attributes implements AttributeDescriptor
	{
		target
		{
			@Override
			public String verify(WomNode parent, String value) throws IllegalArgumentException
			{
				Toolbox.checkValidTarget(value);
				return value;
			}
			
			@Override
			public void customAction(
					WomNode parent,
					NativeOrXmlAttributeAdapter oldAttr,
					NativeOrXmlAttributeAdapter newAttr)
			{
				RedirectAdapter redirect = (RedirectAdapter) parent;
				redirect.setTargetInAst(newAttr.getValue());
			}
		},
		
		TARGET_FROM_AST
		{
			@Override
			public String verify(WomNode parent, String value) throws IllegalArgumentException
			{
				return value;
			}
			
			@Override
			public void customAction(
					WomNode parent,
					NativeOrXmlAttributeAdapter oldAttr,
					NativeOrXmlAttributeAdapter newAttr)
			{
			}
		};
		
		// =====================================================================
		
		@Override
		public boolean isRemovable()
		{
			return false;
		}
		
		@Override
		public boolean syncToAst()
		{
			return false;
		}
		
		@Override
		public Normalization getNormalizationMode()
		{
			return Normalization.NONE;
		}
	}
}
