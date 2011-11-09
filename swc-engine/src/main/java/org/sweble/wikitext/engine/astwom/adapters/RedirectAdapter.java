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

import org.sweble.wikitext.engine.astwom.AttributeManager;
import org.sweble.wikitext.engine.astwom.FullElement;
import org.sweble.wikitext.engine.astwom.Toolbox;
import org.sweble.wikitext.engine.wom.WomNodeType;
import org.sweble.wikitext.engine.wom.WomRedirect;
import org.sweble.wikitext.engine.wom.WomTitle;
import org.sweble.wikitext.lazy.preprocessor.Redirect;
import org.sweble.wikitext.lazy.utils.TextUtils;

public class RedirectAdapter
        extends
            FullElement
        implements
            WomRedirect
{
	private static final long serialVersionUID = 1L;
	
	private static final WomTitle EMPTY_TITLE = new EmptyTitleAdapter();
	
	@Getter(AccessLevel.PROTECTED)
	@Setter(AccessLevel.PROTECTED)
	private AttributeManager attribManager = AttributeManager.emptyManager();
	
	// =========================================================================
	
	public RedirectAdapter(String target)
	{
		super(new Redirect(target));
		checkValidTarget(target);
	}
	
	public RedirectAdapter(Redirect redirect)
	{
		super(redirect);
		
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
	
	@Override
	protected String checkAttributeValue(String name, String value) throws IllegalArgumentException, UnsupportedOperationException
	{
		if (name.equalsIgnoreCase("category"))
		{
			String checkedValue = checkValidTarget(value);
			setTargetInAst(checkedValue);
			return checkedValue;
		}
		else
		{
			throw new IllegalArgumentException("Attribute `" + name + "' not supported by this node");
		}
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
		page = checkValidTarget(page);
		
		// Fix AST
		setTargetInAst(page);
		
		// Fix WOM
		NativeOrXmlAttributeAdapter old = setAttributeUnchecked("target", page);
		
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
	
	private void setTargetInAst(String target)
	{
		Redirect astNode = getAstNode();
		astNode.setTarget(target);
		TextUtils.addRtData(
		        astNode,
		        TextUtils.joinRt("#REDIRECT[[", target, "]]"));
	}
	
	private String checkValidTarget(String target)
	{
		return Toolbox.checkValidTarget(target);
	}
}
