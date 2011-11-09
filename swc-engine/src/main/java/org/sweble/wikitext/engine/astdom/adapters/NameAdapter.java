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
package org.sweble.wikitext.engine.astdom.adapters;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import org.sweble.wikitext.engine.astdom.ChildManager;
import org.sweble.wikitext.engine.astdom.FullElement;
import org.sweble.wikitext.engine.astdom.WomNodeFactory;
import org.sweble.wikitext.engine.wom.WomName;
import org.sweble.wikitext.engine.wom.WomNodeType;

import de.fau.cs.osr.ptk.common.ast.NodeList;

public class NameAdapter
        extends
            FullElement
        implements
            WomName
{
	private static final long serialVersionUID = 1L;
	
	@Getter(AccessLevel.PROTECTED)
	@Setter(AccessLevel.PROTECTED)
	private ChildManager childManager = ChildManager.emptyManager();
	
	// =========================================================================
	
	public NameAdapter()
	{
		super(new NodeList());
	}
	
	public NameAdapter(WomNodeFactory womNodeFactory, NodeList name)
	{
		super(name);
		
		if (name == null)
			throw new NullPointerException();
		
		if (!name.isEmpty())
			super.addContent(womNodeFactory, name, getChildManagerForModificationOrFail());
	}
	
	// =========================================================================
	
	@Override
	public String getNodeName()
	{
		return "name";
	}
	
	@Override
	public NodeList getAstNode()
	{
		return (NodeList) super.getAstNode();
	}
	
	@Override
	public WomNodeType getNodeType()
	{
		return WomNodeType.ELEMENT;
	}
	
	// =========================================================================
	
	@Override
	public NodeList getAstChildContainer()
	{
		return getAstNode();
	}
}
