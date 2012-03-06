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

import org.sweble.wikitext.engine.astwom.WomBackbone;
import org.sweble.wikitext.engine.wom.WomNodeType;
import org.sweble.wikitext.engine.wom.WomTitle;

import de.fau.cs.osr.ptk.common.ast.AstNode;
import de.fau.cs.osr.ptk.common.ast.NodeList;

public class EmptyTitleAdapter
		extends
			WomBackbone
		implements
			WomTitle
{
	private static final long serialVersionUID = 1L;
	
	// =========================================================================
	
	private static final WomTitle EMPTY_TITLE = new EmptyTitleAdapter();
	
	public static WomTitle get()
	{
		return EMPTY_TITLE;
	}
	
	// =========================================================================
	
	private EmptyTitleAdapter()
	{
		super(new NodeList());
	}
	
	// =========================================================================
	
	@Override
	protected AstNode setAstNode(AstNode astNode)
	{
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void unlink()
	{
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void link(
			WomBackbone parent,
			WomBackbone prevSibling,
			WomBackbone nextSibling)
	{
		throw new UnsupportedOperationException();
	}
	
	// =========================================================================
	
	@Override
	public String getNodeName()
	{
		return "title";
	}
	
	@Override
	public WomNodeType getNodeType()
	{
		return WomNodeType.ELEMENT;
	}
}
