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

package org.sweble.wikitext.engine;

import java.io.Serializable;
import java.util.Map;

import org.sweble.wikitext.lazy.preprocessor.TagExtension;

import de.fau.cs.osr.ptk.common.ast.AstNode;
import de.fau.cs.osr.ptk.common.ast.NodeList;

public abstract class TagExtensionBase
        implements
            Serializable
{
	private static final long serialVersionUID = 1L;
	
	private final String name;
	
	// =========================================================================
	
	public TagExtensionBase(String name)
	{
		super();
		this.name = name;
	}
	
	// =========================================================================
	
	public String getName()
	{
		return name;
	}
	
	public abstract AstNode invoke(
	        ExpansionFrame preprocessorFrame,
	        TagExtension tagExtension,
	        Map<String, NodeList> attributes,
	        String body);
}
