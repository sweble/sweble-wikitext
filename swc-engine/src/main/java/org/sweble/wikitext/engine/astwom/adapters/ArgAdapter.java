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

import org.sweble.wikitext.engine.astwom.AstToWomNodeFactory;
import org.sweble.wikitext.engine.astwom.AttributeDescriptor;
import org.sweble.wikitext.engine.astwom.CustomChildrenElement;
import org.sweble.wikitext.engine.astwom.Toolbox;
import org.sweble.wikitext.engine.astwom.WomBackbone;
import org.sweble.wikitext.engine.wom.WomArg;
import org.sweble.wikitext.engine.wom.WomName;
import org.sweble.wikitext.engine.wom.WomNode;
import org.sweble.wikitext.engine.wom.WomValue;
import org.sweble.wikitext.parser.nodes.TemplateArgument;
import org.sweble.wikitext.parser.nodes.WtNodeList;

public class ArgAdapter
		extends
			CustomChildrenElement
		implements
			WomArg
{
	private static final long serialVersionUID = 1L;
	
	private NameAdapter name;
	
	private ValueAdapter value;
	
	// =========================================================================
	
	public ArgAdapter()
	{
		super(new TemplateArgument());
		setArgValue(new ValueAdapter());
	}
	
	public ArgAdapter(WomValue value)
	{
		super(new TemplateArgument());
		setArgValue(value);
	}
	
	public ArgAdapter(WomName name, WomValue value)
	{
		super(new TemplateArgument());
		setName(name);
		setArgValue(value);
	}
	
	public ArgAdapter(
			AstToWomNodeFactory womNodeFactory,
			TemplateArgument astNode/*, WomName name, WomValue value*/)
	{
		super(astNode);
		
		if (astNode.getHasName())
		{
			this.name = new NameAdapter(womNodeFactory, astNode.getName());
			this.name.link(this, null, null);
		}
		
		this.value = new ValueAdapter(womNodeFactory, astNode.getValue());
		this.value.link(this, name, null);
	}
	
	// =========================================================================
	
	@Override
	public String getNodeName()
	{
		return "arg";
	}
	
	@Override
	public TemplateArgument getAstNode()
	{
		return (TemplateArgument) super.getAstNode();
	}
	
	@Override
	public WomNode getFirstChild()
	{
		return name == null ? value : name;
	}
	
	@Override
	public WomNode getLastChild()
	{
		return value;
	}
	
	@Override
	protected void replaceChildImpl(WomBackbone newChild, WomBackbone oldChild)
	{
		if (oldChild == name)
		{
			setName(Toolbox.expectType(WomName.class, newChild));
		}
		else if (oldChild == value)
		{
			setArgValue(Toolbox.expectType(WomValue.class, newChild));
		}
		else
		{
			// replaceChild should have made sure that the given oldChild IS 
			// a child of this node, and therefore it can only be name or value
			throw new InternalError();
		}
	}
	
	// =========================================================================
	
	@Override
	public WomName getName()
	{
		return name;
	}
	
	@Override
	public WomName setName(WomName name)
	{
		WomName old = this.name;
		
		Toolbox.expectType(NameAdapter.class, name);
		if (this.name != null)
		{
			this.name.unlink();
			getAstNode().setName(new WtNodeList());
			getAstNode().setHasName(false);
		}
		
		this.name = (NameAdapter) name;
		if (name != null)
		{
			this.name.link(this, null, this.value);
			getAstNode().setName(this.name.getAstNode());
			getAstNode().setHasName(true);
		}
		
		return old;
	}
	
	@Override
	public WomValue getArgValue()
	{
		return value;
	}
	
	@Override
	public WomValue setArgValue(WomValue value) throws NullPointerException
	{
		if (value == null)
			throw new NullPointerException("Cannot remove mandatory child <value> from <arg>");
		
		WomValue old = this.value;
		
		Toolbox.expectType(ValueAdapter.class, value);
		
		if (this.value != null)
			this.value.unlink();
		
		this.value = (ValueAdapter) value;
		this.value.link(this, this.name, null);
		getAstNode().setValue(this.value.getAstNode());
		
		return old;
	}
	
	// =========================================================================
	
	@Override
	protected AttributeDescriptor getAttributeDescriptor(String name)
	{
		return null;
	}
}
