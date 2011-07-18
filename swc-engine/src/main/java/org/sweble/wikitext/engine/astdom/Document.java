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
package org.sweble.wikitext.engine.astdom;

import org.sweble.wikitext.engine.dom.DomDocument;
import org.sweble.wikitext.engine.dom.DomNodeType;

import de.fau.cs.osr.ptk.common.ast.AstNode;
import de.fau.cs.osr.ptk.common.ast.ContentNode;
import de.fau.cs.osr.ptk.common.ast.NodeList;

public class Document
        extends
            DomBackbone
        implements
            DomDocument
{
	private static final long serialVersionUID = 1L;
	
	private final String namespace;
	
	private final String path;
	
	private final String title;
	
	private String name;
	
	// =========================================================================
	
	public Document(AstNode astNode, String namespace, String path, String title)
	{
		super(astNode);
		
		if (title == null)
			throw new IllegalArgumentException("Argument `title' must not be null.");
		
		String name = "";
		if (namespace != null && !namespace.isEmpty())
		{
			name += namespace;
			name += ':';
		}
		if (path != null && !path.isEmpty())
		{
			name += path;
			if (!path.endsWith("/"))
				name += '/';
		}
		name += title;
		
		this.name = name;
		this.namespace = namespace;
		this.path = path;
		this.title = title;
	}
	
	// =========================================================================
	
	@Override
	public String getName()
	{
		return name;
	}
	
	@Override
	public String getTitle()
	{
		return title;
	}
	
	@Override
	public String getNamespace()
	{
		return namespace;
	}
	
	@Override
	public String getPath()
	{
		return path;
	}
	
	// =========================================================================
	
	@Override
	public String getNodeName()
	{
		return "article";
	}
	
	@Override
	public DomNodeType getNodeType()
	{
		return DomNodeType.DOCUMENT;
	}
	
	@Override
	protected NodeList getChildContainer()
	{
		return ((ContentNode) getAstNode()).getContent();
	}
	
	@Override
	protected NodeList getAttributeContainer()
	{
		return null;
	}
}
