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
import java.util.TreeSet;

import org.sweble.wikitext.engine.config.MagicWord;

import de.fau.cs.osr.ptk.common.ast.AstNode;

public abstract class MagicWordBase
		implements
			Serializable
{
	private static final long serialVersionUID = 1L;
	
	private final MagicWord magicWord;
	
	// =========================================================================
	
	public MagicWordBase(MagicWord magicWord)
	{
		super();
		this.magicWord = magicWord;
	}
	
	// =========================================================================
	
	public String getName()
	{
		return magicWord.getName();
	}
	
	public Boolean isCaseSensitive()
	{
		return magicWord.isCaseSensitive();
	}
	
	public TreeSet<String> getAliases()
	{
		return magicWord.getAliases();
	}
	
	public MagicWord getMagicWord()
	{
		return magicWord;
	}
	
	public abstract AstNode invoke(
			ExpansionFrame expansionFrame,
			org.sweble.wikitext.lazy.parser.MagicWord magicWord);
}
